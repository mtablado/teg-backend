package com.eg.tracker.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Service;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.DriverPosition;
import com.eg.tracker.domain.DriverStatusType;
import com.eg.tracker.domain.UserType;
import com.eg.tracker.repository.ReactiveDriverRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class TrafficServiceImpl implements TrafficService {

	@Autowired
	private ReactiveDriverRepository driverRepository;

	@Autowired
	private DriverService driverService;


	@Autowired
	@Qualifier("trafficMapContainer")
	private MessageListenerContainer mlc;


	@Override
	public Flux<DriverPosition> getTraffic() {

		// TODO check if this concatenation needs to be delegated to UI.

		// Get the last stored positions
		Flux<DriverPosition> initial = this.driverRepository.findAllByType(UserType.DRIVER)
			.map(d -> {
				log.debug("Traffic from initial flux:" + d);
				return new DriverPosition(d);
			});

		// Register to new movements.
		Flux<DriverPosition> drivers = Flux.create(emitter -> {
			this.mlc.setupMessageListener((MessageListener) m -> {
				DriverPosition d = (DriverPosition) SerializationUtils.deserialize(m.getBody());
				log.debug("Traffic from queue flux:" + d);
				emitter.next(d);
			});
	        emitter.onRequest(v -> {
	            this.mlc.start();
	        });
	        emitter.onDispose(() -> {
	            this.mlc.stop();
	        });
		});

		// Concat fluxes to ensure the initials get overridden by moves (if any)
		return Flux.concat(initial, drivers);
	}

	@Override
	@Scheduled(cron = "0 0/5 7-22 * * *")
	public void processDriversStatus() {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime movementBoundary = now.minusMinutes(10);
		LocalDateTime offBoundary = now.minusHours(4);

		Flux<Driver> drivers = this.driverService.findRxDrivers();
		drivers.subscribe(driver -> {
			if (null != driver.getLastPosition()) {

				Date date = driver.getLastPosition().getTime();
				LocalDateTime lastMove = date.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDateTime();

				if (lastMove.isAfter(movementBoundary)) {
					// last 15 minutes, moving.
					driver.setStatus(DriverStatusType.MOVING);
				} else if (lastMove.isBefore(offBoundary)) {
					// no news since 4 ours, off
					driver.setStatus(DriverStatusType.OFF);
				} else {
					// between off and 15 minutes, stationary
					driver.setStatus(DriverStatusType.STATIONARY);
				}
			} else {
				driver.setStatus(DriverStatusType.UNKOWN);
			}
			this.driverService.saveDriver(driver);
		});
	}

}
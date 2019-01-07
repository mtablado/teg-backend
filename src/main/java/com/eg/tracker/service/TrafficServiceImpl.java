package com.eg.tracker.service;

import java.time.Duration;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Service;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.DriverPosition;
import com.eg.tracker.domain.UserType;
import com.eg.tracker.repository.ReactiveDriverRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class TrafficServiceImpl implements TrafficService {

	private Flux<Driver> drivers;

	@Autowired
	private ReactiveDriverRepository driverRepository;

	@Autowired
	private MessageListenerContainer mlc;

	public Flux<Driver> _getTraffic() {
		Flux<Driver> drivers = this.driverRepository.findAllByType(UserType.DRIVER);
		Flux<Driver> interval = Flux.interval(Duration.ofSeconds(1))
				.zipWith(drivers, (i, item) -> item);

		return interval;

	}

	@Override
	public Flux<DriverPosition> getTraffic() {

		// TODO check if this concatenation needs to be delegated to UI.

		// Get the last stored positions
		Flux<DriverPosition> initial = this.driverRepository.findAllByType(UserType.DRIVER)
			.map(d -> new DriverPosition(d));

		// Register to new movements.
		Flux<DriverPosition> drivers = Flux.create(emitter -> {
			this.mlc.setupMessageListener((MessageListener) m -> {
				DriverPosition d = (DriverPosition) SerializationUtils.deserialize(m.getBody());
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

	public Flux<Driver> __getTraffic() {
		return Flux.create(fluxSink -> {
			while (!fluxSink.isCancelled()) {
				this.driverRepository.findAllByType(UserType.DRIVER).subscribe(driver -> {
						fluxSink.next(driver);
					});
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			log.info("Traffic flux cancelled by client");
		});
	}

}

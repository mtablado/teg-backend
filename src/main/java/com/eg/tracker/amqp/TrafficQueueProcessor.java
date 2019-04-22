package com.eg.tracker.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Component;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.DriverPosition;
import com.eg.tracker.domain.Position;
import com.eg.tracker.service.DriverService;

import lombok.extern.slf4j.Slf4j;

/**
 * Traffic processor for database updating.
 */
@Slf4j
@Component
@Qualifier("trafficQueueProcessor")
public class TrafficQueueProcessor implements MessageListener {

	@Autowired
	private DriverService driverService;

	@Override
	public void onMessage(Message message) {
		DriverPosition d = (DriverPosition) SerializationUtils.deserialize(message.getBody());
		log.debug("updating {} position", d.getId());
		Driver driver = this.driverService.getDriver(d.getId());
		Position p = new Position();
		p.setLatitude(d.getLatitude());
		p.setLongitude(d.getLongitude());
		p.setTime(d.getTime());
		this.driverService.setLastPosition(driver, p);
	}


}

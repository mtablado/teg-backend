package com.eg.tracker.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eg.tracker.conf.AMQPConfig;
import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.DriverPosition;
import com.eg.tracker.domain.Position;
import com.eg.tracker.domain.User;
import com.eg.tracker.service.DriverService;

@RestController
@RequestMapping("/private/api/v1")
public class TrackerController {

	@Autowired
	private DriverService service;

	@Autowired
    private RabbitTemplate rabbitTemplate;

	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping
	@RequestMapping(value="/position", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void trackPosition(@RequestBody Position position) throws Exception {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Driver driver = this.service.getDriver(currentUser.getId());
		driver = this.service.setLastPosition(driver, position);
		this.emit(driver, driver.getLastPosition());
	}

	private void emit(Driver driver, Position p) {
		DriverPosition dp = new DriverPosition();
		dp.setId(driver.getId());
		dp.setLatitude(p.getLatitude());
		dp.setLongitude(p.getLongitude());
		dp.setTime(p.getTime());
		dp.setName(driver.getName());
		dp.setPlate(driver.getPlate());

		byte[] obj = SerializationUtils.serialize(dp);
		this.rabbitTemplate.convertAndSend(AMQPConfig.topicExchangeName, "teg.traffic", obj);
	}

}

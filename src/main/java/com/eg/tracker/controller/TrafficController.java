package com.eg.tracker.controller;

import java.time.Duration;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eg.tracker.conf.AMQPConfig;
import com.eg.tracker.domain.DriverPosition;
import com.eg.tracker.domain.Position;
import com.eg.tracker.service.TrafficService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/private/api/v1")
public class TrafficController {

	@Autowired
	TrafficService service;

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	@Qualifier("trafficExchange")
	private Exchange trafficExchange;

	@GetMapping(value = "/stream/traffic", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<DriverPosition> getTraffic() {
		return this.service.getTraffic();
	}

	@GetMapping(value = "/traffic", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<List<DriverPosition>> getSyncTraffic() {

		return this.service.getTraffic()
			.take(50)
			.take(Duration.ofMillis(500))
			.collectList();

	}

	@PostMapping(value = "/drivers/register-position")
	public Mono<ResponseEntity<?>> trackPosition(@RequestBody Position position) throws Exception {

		return Mono.fromCallable(() -> {
			this.amqpTemplate.convertAndSend(
				this.trafficExchange.getName(), AMQPConfig.trafficPositionKey(), position);
			return ResponseEntity.accepted().build();
		});
	}
}

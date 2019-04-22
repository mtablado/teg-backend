package com.eg.tracker.service;

import com.eg.tracker.domain.DriverPosition;

import reactor.core.publisher.Flux;

public interface TrafficService {
	Flux<DriverPosition> getTraffic();

	void processDriversStatus();
}

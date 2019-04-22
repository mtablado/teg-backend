package com.eg.tracker.service;

import java.util.Date;

import com.eg.tracker.domain.DriverPosition;
import com.eg.tracker.domain.DriverStatusType;

import reactor.core.publisher.Flux;

public interface TrafficService {
	Flux<DriverPosition> getTraffic();

	void processDriversStatus();

	DriverStatusType getDriverStatusFromDate(Date date);
}

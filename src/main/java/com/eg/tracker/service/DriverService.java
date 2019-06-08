package com.eg.tracker.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.Position;

import reactor.core.publisher.Flux;

public interface DriverService {

	Driver getDriver(String id);

	/**
	 * List active drivers
	 * @return List active drivers
	 */
	List<Driver> findDrivers();

	/**
	 * List all drivers including disabled ones.
	 * @return All drivers including disabled ones.
	 */
	List<Driver> findAllDrivers();

	Flux<Driver> findRxDrivers();

	Flux<Driver> findAllRxDrivers();

	@PreAuthorize("hasRole('ADMIN')")
	Driver addDriver(Driver driver);

	@PreAuthorize("hasRole('ADMIN')")
	Driver saveDriver(Driver driver);

	Driver setLastPosition(Driver driver, Position position);

}

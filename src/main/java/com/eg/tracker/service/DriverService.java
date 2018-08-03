package com.eg.tracker.service;

import org.springframework.security.access.prepost.PreAuthorize;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.Position;

public interface DriverService {

	Driver getDriver(String id);

	@PreAuthorize("hasRole('ADMIN')")
	Driver addDriver(Driver driver);

	Driver setLastPosition(Driver driver, Position position);
}

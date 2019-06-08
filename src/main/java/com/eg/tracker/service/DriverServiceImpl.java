package com.eg.tracker.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.Position;
import com.eg.tracker.domain.UserType;
import com.eg.tracker.exception.NotFoundException;
import com.eg.tracker.repository.DriverRepository;
import com.eg.tracker.repository.ReactiveDriverRepository;

import reactor.core.publisher.Flux;

@Service
public class DriverServiceImpl implements DriverService {

	@Autowired
	private DriverRepository repository;

	@Autowired
	private ReactiveDriverRepository reactiveRepository;

	@Override
	public Driver getDriver(String id) {
		return this.repository.findById(id).orElseThrow(() -> new NotFoundException());
	}

	@Override
	public List<Driver> findDrivers() {
		return this.repository.findAllByTypeAndLastPositionNotNullAndEnabled(UserType.DRIVER, true);
	}

	@Override
	public List<Driver> findAllDrivers() {
		return this.repository.findByType(UserType.DRIVER);
	}

	@Override
	public Flux<Driver> findRxDrivers() {
		return this.reactiveRepository.findAllByTypeAndLastPositionNotNullAndEnabled(UserType.DRIVER, true);
	}

	@Override
	public Flux<Driver> findAllRxDrivers() {
		return this.reactiveRepository.findAllByType(UserType.DRIVER);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Driver addDriver(Driver driver) {
		driver.setEnabled(true);
		driver.setLocked(false);
		return this.repository.save(driver);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Driver saveDriver(Driver driver) {
		driver.setEnabled(true);
		driver.setLocked(false);
		return this.repository.save(driver);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Driver setLastPosition(Driver driver, Position position) {
		position.setTime(Calendar.getInstance().getTime());
		driver.setLastPosition(position);
		return this.repository.save(driver);
	}

}

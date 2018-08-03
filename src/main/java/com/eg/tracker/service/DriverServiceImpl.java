package com.eg.tracker.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.Position;
import com.eg.tracker.exception.NotFoundException;
import com.eg.tracker.repository.DriverRepository;

@Service
public class DriverServiceImpl implements DriverService {

	@Autowired
	private DriverRepository repository;

	@Override
	public Driver getDriver(String id) {
		return this.repository.findById(id).orElseThrow(() -> new NotFoundException());
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
	public Driver setLastPosition(Driver driver, Position position) {
		position.setTime(Calendar.getInstance().getTime());
		driver.setLastPosition(position);
		return this.repository.save(driver);
	}
}

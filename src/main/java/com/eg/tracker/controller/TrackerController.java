package com.eg.tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.domain.Position;
import com.eg.tracker.domain.User;
import com.eg.tracker.repository.DriverRepository;
import com.eg.tracker.service.DriverService;

@RestController
@RequestMapping("/private/api/v1")
public class TrackerController {

	@Autowired
	private DriverRepository repository;

	@Autowired
	private DriverService service;

	private void init() {
		// save a couple of customers
//		Driver driver = new Driver();
//		driver.setLastName("Esponja");
//		driver.setName("Bob");
//		this.repository.save(driver);
	}

	@RequestMapping(value="/user/{id}/last-position", method = RequestMethod.GET)
	public String getLastPosition(@PathVariable("id") String id) throws Exception {
		return "Hellow user:" + id;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/drivers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Driver> getDrivers() throws Exception {
//		this.init();
		return this.repository.findAll();
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/drivers/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Driver getDriver(@PathVariable("id") String id) throws Exception {
		return this.service.getDriver(id);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping
	@RequestMapping(value="/drivers/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Driver addDriver(@RequestBody Driver driver) throws Exception {
		return this.service.addDriver(driver);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping
	@RequestMapping(value="/position", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void trackPosition(@RequestBody Position position) throws Exception {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Driver driver = this.service.getDriver(currentUser.getId());
		this.service.setLastPosition(driver, position);
	}

}

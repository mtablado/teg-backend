package com.eg.tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eg.tracker.domain.Driver;
import com.eg.tracker.service.DriverService;

@RestController
@RequestMapping("/private/api/v1")
public class DriversController {

	@Autowired
	private DriverService service;

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping
	@RequestMapping(value="/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Driver> getDrivers(
			@RequestParam(value = "all", required = false) boolean all) throws Exception {
		if (all) {
			return this.service.findAllDrivers();
		}
		return this.service.findDrivers();
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping
	@RequestMapping(value="/drivers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Driver getDriver(@PathVariable("id") String id) throws Exception {
		return this.service.getDriver(id);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping
	@RequestMapping(value="/drivers/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Driver addDriver(@RequestBody Driver driver) throws Exception {
		if (driver.getId() != null) {
			Driver d = this.service.getDriver(driver.getId());
			d.setName(driver.getName());
			d.setLastname(driver.getLastname());
			d.setPlate(driver.getPlate());
			return this.service.saveDriver(d);
		} else {
			return this.service.addDriver(driver);
		}
	}

}

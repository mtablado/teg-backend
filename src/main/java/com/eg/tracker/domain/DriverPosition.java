package com.eg.tracker.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class DriverPosition implements Serializable {

	private static final long serialVersionUID = -3042436797411994497L;

	public DriverPosition() {}

	public DriverPosition(Driver driver) {
		this.id = driver.getId();
		this.name = driver.getName();
		this.plate = driver.getPlate();
		Position p = driver.getLastPosition();
		if (null != p) {
			this.latitude = p.getLatitude();
			this.longitude = p.getLongitude();
			this.time = p.getTime();
		}
	}

	private String id;
	private String name;
	private String plate;
	private String latitude;
	private String longitude;
	private Date time;

}

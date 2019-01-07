package com.eg.tracker.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Document(collection="user")
@Data
@EqualsAndHashCode(callSuper = true)
public class Driver extends User {

	private static final long serialVersionUID = 7154642670294036780L;

	private Position lastPosition;
	private String plate;

	public Driver() {
		this.type = UserType.DRIVER;
	}

}

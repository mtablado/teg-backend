package com.eg.tracker.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Position implements Serializable {

	private static final long serialVersionUID = 2399236466764794569L;
	private String latitude;
	private String longitude;
	private Date time;
}

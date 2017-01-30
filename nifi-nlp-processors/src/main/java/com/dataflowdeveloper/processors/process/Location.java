package com.dataflowdeveloper.processors.process;

import java.io.Serializable;

/**
 * 
 * @author tspann
 *
 */
public class Location implements Serializable {

	private static final long serialVersionUID = -813050143597962280L;
	private String location = null;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Location [location=");
		builder.append(location);
		builder.append("]");
		return builder.toString();
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @param location
	 */
	public Location(String location) {
		super();
		this.location = location;
	}

	/**
	 * 
	 */
	public Location() {
		super();
	}
	
	
}

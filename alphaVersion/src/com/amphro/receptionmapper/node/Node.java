package com.amphro.receptionmapper.node;

import com.amphro.receptionmapper.phone.NetworkType;

public class Node {
	/**
	 * Do not change these values. They are what the server uses. 
	 * Ideally we would pull the names via a service from 
	 * Receptionmapper.com. We can add that later. 
	 */
	public final static String LATITUDE = "latitude";
	public final static String LONGITUDE = "longitude";
	public final static String CARRIER = "carrier";
	public final static String NETWORK_TYPE = "network";
	public final static String SIGNAL_STRENGTH = "signalstrength";
	public final static String PHONE = "phone";
	public final static String MANUFACTURER = "manufac";
	
	private double latitude;
	private double longitude;
	private String provider;
	private Integer signalStrength;
	private NetworkType type;
	
	public Node(double latitude, double longitude, String provider,
			NetworkType type) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.provider = provider;
		this.type = type;
	}
	
	public Node(double latitude, double longitude, String provider,
			Integer signalStrength, NetworkType type) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.provider = provider;
		this.signalStrength = signalStrength;
		this.type = type;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public Integer getSignalStrength() {
		return signalStrength;
	}
	public void setSignalStrength(Integer signalStrength) {
		this.signalStrength = signalStrength;
	}
	public NetworkType getType() {
		return type;
	}
	public void setType(NetworkType type) {
		this.type = type;
	}
}

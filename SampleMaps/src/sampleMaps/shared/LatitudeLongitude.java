package sampleMaps.shared;

import java.io.Serializable;

public class LatitudeLongitude implements Serializable {
	private static final long serialVersionUID = 1L;
	private double _latitude;
	private double _longitude;
	
	private LatitudeLongitude() {
		
	}
	
	//TODO: fix this to be correct in terms of lat first then long
	public LatitudeLongitude(double latitude, double longitude) {
		_latitude = latitude;
		_longitude = longitude;
	}
	
	public double getLatitude() {
		return _latitude;
	}
	
	public double getLongitude() {
		return _longitude;
	}

	@Override
	public String toString() {
		return "(" + _latitude + ", " + _longitude + ")";
	}
}

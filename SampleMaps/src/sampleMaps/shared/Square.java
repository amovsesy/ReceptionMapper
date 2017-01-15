package sampleMaps.shared;

import java.io.Serializable;

public class Square implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LatitudeLongitude _northWest;
	private LatitudeLongitude _northEast;
	private LatitudeLongitude _southEast;
	private LatitudeLongitude _southWest;
	
	public Square() {
		
	}
	
	public void setNorthWest(LatitudeLongitude northWest) {
		_northWest = northWest;
	}
	
	public void setNorthEast(LatitudeLongitude northEast) {
		_northEast = northEast;
	}
	
	public void setSouthEast(LatitudeLongitude southEast) {
		_southEast = southEast;
	}
	
	public void setSouthWest(LatitudeLongitude southWest) {
		_southWest = southWest;
	}
	
	public LatitudeLongitude getNorthWest() {
		return _northWest;
	}
	
	public LatitudeLongitude getNorthEast() {
		return _northEast;
	}
	
	public LatitudeLongitude getSouthEast() {
		return _southEast;
	}
	
	public LatitudeLongitude getSouthWest() {
		return _southWest;
	}

	@Override
	public String toString() {
		String ret = "";
		ret += "northWest = " + _northWest.toString();
		ret += " northEast = " + _northEast.toString();
		ret += " southEast = " + _southEast.toString();
		ret += " southWest = " + _southWest.toString();
		return ret;
	}
	
	
}

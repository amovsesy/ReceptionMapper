package sampleMaps.shared;

import java.io.Serializable;

public class ReceptionGridPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	private Double _receptionAverage;
	private Double _networkAverage;
	private String _carrier;
	private int _grid_x;
	private int _grid_y;
	private Square _points;
	
	private ReceptionGridPoint() {
		
	}

	public ReceptionGridPoint(Double receptionAverage, Double networkAverage, Square points, String carrier) {
		_receptionAverage = receptionAverage;
		_networkAverage = networkAverage;
		_carrier = carrier;
		_points = points;
	}

	public Double getReceptionAverage() {
		return _receptionAverage;
	}

	public Double getNetworkAverage() {
		return _networkAverage;
	}

	public String getCarrier() {
		return _carrier;
	}
	
	public Square getPoints() {
		return _points;
	}

	@Override
	public String toString() {
		return "reception = " + _receptionAverage + " network = "
				+ _networkAverage + " carrier = " + _carrier + " (x, y) = (" +
				_grid_x + ", " + _grid_y + ")" + "(points) " + _points.toString();
	}
	
	public int getX() {
		return _grid_x;
	}
	
	public int getY() {
		return _grid_y;
	}
	
	public void setX(int x) {
		_grid_x = x;
	}
	
	public void setY(int y) {
		_grid_y = y;
	}
}

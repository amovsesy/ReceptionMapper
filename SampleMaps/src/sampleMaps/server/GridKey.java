package sampleMaps.server;

public class GridKey {
	private Integer _x;
	private Integer _y;
	
	public GridKey(int x, int y) {
		_x = x;
		_y = y;
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}

	@Override
	public int hashCode() {
		return (int)(0.5 * (_x + _y) * (_x + _y + 1) + _y);
	}

	@Override
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode();
	}
}

package com.amphro.receptionmapper.reports.shared;

import java.io.Serializable;
//import com.google.gwt.user.client.rpc.IsSerializable;

public class Grid implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String GRID_ID = "id";
	public static final String GRID_X = "gssx";
	public static final String GRID_Y = "gssy";
	public static final String GRID_AVGREC = "avgrec";
	public static final String GRID_NUMREC = "numrec";
	public static final String GRID_CARRIER = "carrier";
	public static final String GRID_AVGNET = "avgnet";
	public static final String GRID_NUMNET = "numnet";
	public static final int MIN_LEVEL = 1;
	public static final int MAX_LEVEL = 5;
	
	private int id = -1;
	private int level = -1;
	private int gridX = -1;
	private int gridY = -1;
	private float avgrec = 0.0f;
	private int numrec = -1;
	private int carrier = -1;
	private float avgnet = 0.0f;
	private int numnet = -1;
	
	public Grid() {}
	
	public Grid(int id, int level, int gridX, int gridY, float avgrec, int numrec,
			int carrier, float avgnet, int numnet) 
	{
		this.id = id;
		this.level = level;
		this.gridX = gridX;
		this.gridY = gridY;
		this.avgrec = avgrec;
		this.numrec = numrec;
		this.carrier = carrier;
		this.avgnet = avgnet;
		this.numnet = numnet;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getGridX() {
		return gridX;
	}
	
	public void setGridX(int gridX) {
		this.gridX = gridX;
	}
	
	public int getGridY() {
		return gridY;
	}
	
	public void setGridY(int gridY) {
		this.gridY = gridY;
	}
	
	public float getAvgrec() {
		return avgrec;
	}
	
	public void setAvgrec(float avgrec) {
		this.avgrec = avgrec;
	}
	
	public int getNumrec() {
		return numrec;
	}
	
	public void setNumrec(int numrec) {
		this.numrec = numrec;
	}
	
	public int getCarrier() {
		return carrier;
	}
	
	public void setCarrier(int carrier) {
		this.carrier = carrier;
	}
	
	public float getAvgnet() {
		return avgnet;
	}
	
	public void setAvgnet(float avgnet) {
		this.avgnet = avgnet;
	}

	public void setNumnet(int numnet) {
		this.numnet = numnet;
	}

	public int getNumnet() {
		return numnet;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
}

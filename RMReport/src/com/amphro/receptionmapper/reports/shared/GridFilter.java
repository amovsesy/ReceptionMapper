package com.amphro.receptionmapper.reports.shared;

import java.util.ArrayList;

public class GridFilter {
	private int level, id, minGridX, maxGridX, minGridY, maxGridY, 
		minNumRec, maxNumRec, minNumNet, maxNumNet;
	private float minAvgNet, maxAvgNet, minAvgRec, maxAvgRec;
	private ArrayList<Integer> carriers;
	
	public GridFilter() {
		carriers = new ArrayList<Integer>();
		reset();
	}
	
	void reset() {
		level = id = minGridX = maxGridX = minGridY = maxGridY
			= minNumRec = maxNumRec = minNumNet = maxNumNet = -1;
		minAvgNet = maxAvgNet = minAvgRec = maxAvgRec = Float.MIN_VALUE;
		carriers.clear();
	}
	
	public String toString() {
		String result = "";
		if(id > 0)
			return Grid.GRID_ID + " = " + id; //no need to do other wheres
		if(minGridX > -1 && level >= 1)
			result += " AND " + Grid.GRID_X(level) + " >= " + minGridX;
		if(maxGridX > -1 && level >= 1)
			result += " AND " + Grid.GRID_X(level) + " <= " + maxGridX;
		if(minGridY > -1 && level >= 1)
			result += " AND " + Grid.GRID_Y(level) + " >= " + minGridY;
		if(maxGridY > -1 && level >= 1)
			result += " AND " + Grid.GRID_Y(level) + " <= " + maxGridY;
		if(minNumRec > -1)
			result += " AND " + Grid.GRID_NUMREC + " >= " + minNumRec;
		if(maxNumRec > -1)
			result += " AND " + Grid.GRID_NUMREC + " <= " + maxNumRec;
		if(minNumNet > -1)
			result += " AND " + Grid.GRID_NUMNET + " >= " + minNumNet;
		if(maxNumNet > -1)
			result += " AND " + Grid.GRID_NUMNET + " <= " + maxNumNet;
		if(minAvgNet >= 0.0f)
			result += " AND " + Grid.GRID_AVGNET + " <= " + minAvgNet;
		if(maxAvgNet >= 0.0f)
			result += " AND " + Grid.GRID_AVGNET + " <= " + maxAvgNet;
		if(minAvgRec >= 0.0f) 
			result += " AND " + Grid.GRID_AVGREC + " <= " + minAvgRec;
		if(maxAvgRec >= 0.0f)
			result += " AND " + Grid.GRID_AVGREC + " <= " + maxAvgRec;
		if(carriers.size() > 0) {
			result += " AND (";
			for(int i = 0; i < carriers.size(); i++) {
				result += Grid.GRID_CARRIER + " = " + carriers.get(i) + " OR ";
			}
			result = result.substring(0, result.length() - 4) + ")";
		}
		
		if(result.length() > 0)
			result = result.substring(4); //remove initial "AND"
		return result;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinGridX() {
		return minGridX;
	}

	public void setMinGridX(int minGridX) {
		this.minGridX = minGridX;
	}

	public int getMaxGridX() {
		return maxGridX;
	}

	public void setMaxGridX(int maxGridX) {
		this.maxGridX = maxGridX;
	}

	public int getMinGridY() {
		return minGridY;
	}

	public void setMinGridY(int minGridY) {
		this.minGridY = minGridY;
	}

	public int getMaxGridY() {
		return maxGridY;
	}

	public void setMaxGridY(int maxGridY) {
		this.maxGridY = maxGridY;
	}

	public int getMinNumRec() {
		return minNumRec;
	}

	public void setMinNumRec(int minNumRec) {
		this.minNumRec = minNumRec;
	}

	public int getMaxNumRec() {
		return maxNumRec;
	}

	public void setMaxNumRec(int maxNumRec) {
		this.maxNumRec = maxNumRec;
	}

	public int getMinNumNet() {
		return minNumNet;
	}

	public void setMinNumNet(int minNumNet) {
		this.minNumNet = minNumNet;
	}

	public int getMaxNumNet() {
		return maxNumNet;
	}

	public void setMaxNumNet(int maxNumNet) {
		this.maxNumNet = maxNumNet;
	}

	public float getMinAvgNet() {
		return minAvgNet;
	}

	public void setMinAvgNet(float minAvgNet) {
		this.minAvgNet = minAvgNet;
	}

	public float getMaxAvgNet() {
		return maxAvgNet;
	}

	public void setMaxAvgNet(float maxAvgNet) {
		this.maxAvgNet = maxAvgNet;
	}

	public float getMinAvgRec() {
		return minAvgRec;
	}

	public void setMinAvgRec(float minAvgRec) {
		this.minAvgRec = minAvgRec;
	}

	public float getMaxAvgRec() {
		return maxAvgRec;
	}

	public void setMaxAvgRec(float maxAvgRec) {
		this.maxAvgRec = maxAvgRec;
	}

	public ArrayList<Integer> getCarriers() {
		return carriers;
	}

	public void setCarriers(ArrayList<Integer> carriers) {
		this.carriers = carriers;
	}
}
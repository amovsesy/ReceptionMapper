package com.amphro.receptionmapper.reports.shared;

import java.sql.Date;
import java.util.ArrayList;

/**
 * To be appended to an SQL query on Nodes as the WHERE clause
 */
public class NodeFilter {
	private int minLat, maxLat, minLong, maxLong;
	private ArrayList<Integer> clients;
	private ArrayList<Integer> networks;
	private Date minTime, maxTime;
	private int minSignalStrength, maxSignalStrength;
	
	public NodeFilter() {
		clients = new ArrayList<Integer>();
		networks = new ArrayList<Integer>();
		reset();
	}
	
	public void reset() {
		minLat = maxLat = minLong = maxLong = minSignalStrength = maxSignalStrength = -1;
		minTime = maxTime = null;
		clients.clear();
		networks.clear();
	}
	
	public String toString() {
		String result = "";
		if(minLat > -1)
			result += " AND " + Node.NODE_LATITUDE + " >= " + minLat + " ";
		if(maxLat > -1)
			result += " AND " + Node.NODE_LATITUDE + " <= " + maxLat + " ";
		if(minLong > -1)
			result += " AND " + Node.NODE_LONGITUDE + " >= " + minLong + " ";
		if(maxLong > -1)
			result += " AND " + Node.NODE_LONGITUDE + " >= " + maxLong + " ";
		if(minTime != null) {
			result += " AND " + Node.NODE_TIME + " >= '" + minTime + "'";
		} else if (maxTime != null) {
			result += " AND " + Node.NODE_TIME + " <= '" + maxTime + "'";
		}
		if(minSignalStrength > -1) {
			result += " AND " + Node.NODE_SIGNAL_STRENGTH + " >= " + minSignalStrength;
		}
		if(maxSignalStrength > -1) {
			result += " AND " + Node.NODE_SIGNAL_STRENGTH + " >= " + maxSignalStrength;
		}
		if(clients.size() > 0) {
			result += " AND (";
			for(int i = 0; i < clients.size(); i++) {
				result += Node.NODE_CLIENT + " = " + clients.get(i) + " OR ";
			}
			result = result.substring(0, result.length() - 4) + ")";
		}
		if(networks.size() > 0) {
			result += " AND (";
			for(int i = 0; i < networks.size(); i++) {
				result += Node.NODE_NETWORK + " = " + networks.get(i) + " OR ";
			}
			result = result.substring(0, result.length() - 4) + ")";
		}
		
		if(result.length() > 0)
			result = result.substring(4); //remove initial "AND"
		return result;
	}

	public ArrayList<Integer> getClients() {
		return clients;
	}

	public void setClients(ArrayList<Integer> clients) {
		this.clients = clients;
	}

	public ArrayList<Integer> getNetworks() {
		return networks;
	}

	public void setNetworks(ArrayList<Integer> networks) {
		this.networks = networks;
	}

	public int getMinSignalStrength() {
		return minSignalStrength;
	}

	public void setMinSignalStrength(int minSignalStrength) {
		this.minSignalStrength = minSignalStrength;
	}

	public int getMaxSignalStrength() {
		return maxSignalStrength;
	}

	public void setMaxSignalStrength(int maxSignalStrength) {
		this.maxSignalStrength = maxSignalStrength;
	}

	public int getMinLat() {
		return minLat;
	}

	public int getMaxLat() {
		return maxLat;
	}

	public int getMinLong() {
		return minLong;
	}

	public int getMaxLong() {
		return maxLong;
	}

	public Date getMinTime() {
		return minTime;
	}

	public Date getMaxTime() {
		return maxTime;
	}

	public void setMinLat(int minLat) {
		this.minLat = minLat;
	}

	public void setMaxLat(int maxLat) {
		this.maxLat = maxLat;
	}

	public void setMinLong(int minLong) {
		this.minLong = minLong;
	}

	public void setMaxLong(int maxLong) {
		this.maxLong = maxLong;
	}

	public void setMinTime(Date minTime) {
		this.minTime = minTime;
	}

	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}
}

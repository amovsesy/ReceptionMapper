package com.amphro.receptionmapper.reports.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Node implements IsSerializable {
	/* Node Column Names */
	public final static String NODE_ID="id";
	public final static String NODE_LATITUDE="latitude";
	public final static String NODE_LONGITUDE="longitude";
	public final static String NODE_CLIENT="client";
	public final static String NODE_NETWORK="network";
	public final static String NODE_SIGNAL_STRENGTH="signal-strength";
	public final static String NODE_TIME="time";
	
	private int mId;
	private float mLatitude;
	private float mLongitude;
	private int mClient;
	private String mNetwork;
	private int mSignal;
	private Date mTime;
	private String mAddress;
	private String mCity;
	private String mCountry;
	
	public Node(){}
	
	public Node(int id, float latitude, float longitude, int client, String network, int signalStrength, Date time) {
		mId = id;
		mLatitude = latitude;
		mLongitude = longitude;
		mClient = client;
		mNetwork = network;
		mSignal = signalStrength;
		mTime = time;
	}

	public int getId() {
		return mId;
	}
	
	public float getLatitude() {
		return mLatitude;
	}

	public float getLongitude() {
		return mLongitude;
	}

	public int getClient() {
		return mClient;
	}

	public String getNetwork() {
		return mNetwork;
	}

	public int getSignalStrength() {
		return mSignal;
	}

	public Date getTime() {
		return mTime;
	}
	
	public void setAddress(String address) {
		this.mAddress = address;
	}
	
	public String getAddress() {
		return mAddress;
	}
	
	public void setCity(String city) {
		this.mCity = city;
	}
	
	public String getCIty() {
		return mCity;
	}
	
	public void setCountry(String country) {
		this.mCountry = country;
	}
	
	public String getCountry() {
		return mCountry;
	}

	@Override
	public String toString() {
		return 
			NODE_LATITUDE+"="+mLatitude+"; <br />"+
			NODE_LONGITUDE+"="+mLongitude+"; <br />"+
			NODE_CLIENT+"="+mClient+"; <br />"+
			NODE_NETWORK+"="+mNetwork+"; <br />"+
			NODE_SIGNAL_STRENGTH+"="+mSignal+"; <br />"+
			NODE_TIME+"="+mTime+";<br />"+
			(mAddress != null ? " "+mAddress+";" : "");
	}
}

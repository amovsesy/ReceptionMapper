package com.amphro.receptionmapper.reports.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Node implements IsSerializable {
	/* Node Column Names */
	public final static String NODE_LATITUDE="latitude";
	public final static String NODE_LONGITUDE="longitude";
	public final static String NODE_CLIENT="client";
	public final static String NODE_NETWORK="network";
	public final static String NODE_SIGNAL_STRENGTH="signal-strength";
	public final static String NODE_TIME="time";
	
	private float mLatitude;
	private float mLongitude;
	private int mClient;
	private String mNetwork;
	private int mSignal;
	private Date mTime;
	
	public Node(){}
	
	public Node(float latitude, float longitude, int client, String network, int signalStrength, Date time) {
		mLatitude = latitude;
		mLongitude = longitude;
		mClient = client;
		mNetwork = network;
		mSignal = signalStrength;
		mTime = time;
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

	@Override
	public String toString() {
		return 
			NODE_LATITUDE+"="+mLatitude+"; "+
			NODE_LONGITUDE+"="+mLongitude+"; "+
			NODE_CLIENT+"="+mClient+"; "+
			NODE_NETWORK+"="+mNetwork+"; "+
			NODE_SIGNAL_STRENGTH+"="+mSignal+"; "+
			NODE_TIME+"="+mTime+";";
	}
}

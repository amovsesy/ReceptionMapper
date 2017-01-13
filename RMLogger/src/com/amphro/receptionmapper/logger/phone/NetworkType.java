package com.amphro.receptionmapper.logger.phone;

import android.telephony.TelephonyManager;

public enum NetworkType {
	xRTT(TelephonyManager.NETWORK_TYPE_1xRTT, -1),
	CDMA(TelephonyManager.NETWORK_TYPE_CDMA, 0),
	EDGE(TelephonyManager.NETWORK_TYPE_EDGE, 1),
	EVDO_0(TelephonyManager.NETWORK_TYPE_EVDO_0, 3),
	EVDO_A(TelephonyManager.NETWORK_TYPE_EVDO_A, 5),
	GPRS(TelephonyManager.NETWORK_TYPE_GPRS, 5),
	HSDPA(TelephonyManager.NETWORK_TYPE_HSDPA, 5),
	HSPA(TelephonyManager.NETWORK_TYPE_HSPA, 5),
	HSUPA(TelephonyManager.NETWORK_TYPE_HSUPA, 5),
	UMTS(TelephonyManager.NETWORK_TYPE_UMTS, 5),
	UNKNOWN(TelephonyManager.NETWORK_TYPE_UNKNOWN, 5);
	
	private int mNetworkStrength;
	private int mTMNetworkInt;
	
	NetworkType(int tmNetworkInt, int strength) {
		mTMNetworkInt = tmNetworkInt;
		mNetworkStrength = strength;
	}
	
	public int getStrength() {
		return mNetworkStrength;
	}
	
	public int getTMNetworkInt() {
		return mTMNetworkInt;
	}
}

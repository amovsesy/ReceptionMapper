package com.amphro.receptionmapper.phone;

import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.Context;

/**
 * A class that provides the data of the phone:
 *  1. current provider
 *  2. current network type
 *  3. phone model
 *  4. manufacturer
 *  
 * @author Aleksandr Movsesyan
 *
 */
public class PhoneData {
	private TelephonyManager tm;
	
	/*
	 * Sets up the telephony manager with the given context to get all the
	 *   needed information.
	 */
	public PhoneData(Context t, PhoneStateListener psl){
		tm = (TelephonyManager)t.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(psl, PhoneStateListener.LISTEN_SIGNAL_STRENGTH|
				PhoneStateListener.LISTEN_SERVICE_STATE);
	}
	
	public String getCurrentProvider() {
		/*
		 * Found two ways to get the provider, gets both and
		 *   then returns the one that is not null. If both
		 *   null then returns null.
		 */
		String networkOp = tm.getNetworkOperatorName();
		Log.d("provider", networkOp);
		
		String simOp = tm.getSimOperatorName();
		Log.d("provider", simOp);
		
		String res = (networkOp != null)?
				networkOp.toUpperCase():
					simOp.toUpperCase();
		
		return (res == null || res.equals("")) ? "UNKNOWN" : res;
	}

	/*
	 * Returns the network type after converting it from what
	 *   is returned by getNetworkType to an locally created
	 *   network type.
	 */
	public NetworkType getNetworkType() {
		int netType = tm.getNetworkType();
		NetworkType ret = NetworkType.NTNONE;
		
		switch(netType){
			case TelephonyManager.NETWORK_TYPE_EDGE:
				ret = NetworkType.NTEDGE;
				break;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				ret = NetworkType.NT2G;
				break;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				ret = NetworkType.NT3G;
				break;
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				ret = NetworkType.NTUKN;
				break;
		}
		
		return ret;
	}
	
	/*
	 * Returns the phone's model string from Build.MODEL
	 */
	public String getPhone(){
		Log.d("model", Build.MODEL);
		return Build.MODEL.toUpperCase();
	}
	
	/*
	 * Returns the phone's manufacturer from Build.MANUFACTURER
	 */
	public String getManufacturer(){
		Log.d("manufacturer", Build.MANUFACTURER);
		return Build.MANUFACTURER.toUpperCase();
	}
}

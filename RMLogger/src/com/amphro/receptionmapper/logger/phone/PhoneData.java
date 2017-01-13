package com.amphro.receptionmapper.logger.phone;

import android.content.Context;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.amphro.receptionmapper.logger.Logger;

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
	/** The tag for logging debug messages */
	private final static String TAG = "PhoneDate";
	
	private TelephonyManager mManager;
	private PhoneStateListener mListener;
	
	private Integer mSignalStrength;
	private Integer mState;
	
	private Integer mGsm;
	private Integer mGsmError;
	private Integer mCdma;
	private Integer mCdmaError;
	private Integer mEvdo;
	private Integer mEvdoError;
	
	/*
	 * Sets up the telephony manager with the given context to get all the
	 *   needed information.
	 */
	public PhoneData(Context t){
		mManager = (TelephonyManager)t.getSystemService(Context.TELEPHONY_SERVICE);
		
		mListener = new ReceptionMapperPhoneListener();
		mSignalStrength = null;
		mState = null;
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ECLAIR_MR1){
			mManager.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTH|
					PhoneStateListener.LISTEN_SERVICE_STATE);
		} else {
			mManager.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS|
					PhoneStateListener.LISTEN_SERVICE_STATE);
		}
	}
	
	public void destroyPhoneData() {
		mManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
	}
	
	public String getCurrentProvider() {
		/*
		 * Found two ways to get the provider, gets both and
		 *   then returns the one that is not null. If both
		 *   null then returns null.
		 */
		String networkOp = mManager.getNetworkOperatorName();
		Log.d("provider", networkOp);
		
		String simOp = mManager.getSimOperatorName();
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
		int netType = mManager.getNetworkType();

		switch(netType){
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return NetworkType.xRTT;
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return NetworkType.CDMA;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return NetworkType.EVDO_0;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return NetworkType.EVDO_A;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return NetworkType.GPRS;
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return NetworkType.HSDPA;
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return NetworkType.HSPA;
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return NetworkType.HSUPA;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return NetworkType.UMTS;
			default:
				return NetworkType.UNKNOWN;
		}
	}
	
	public int getNetworkTypeInt() {
		return mManager.getNetworkType();
	}
	
	public String getPhoneType() {
		if (mManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA)
			return "CDMA";
		else if (mManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM)
			return "GSM";
		else
			return "NONE";
	}
	
	public String getDeviceID() {
		return mManager.getDeviceId();
	}
	
	/*
	 * Returns the phone's model string from Build.MODEL
	 */
	public String getPhone(){
		Log.d("model", Build.MODEL);
		return Build.MODEL.toUpperCase();
	}
	
	public Integer getSignalStrength(){
		return mSignalStrength;
	}
	
	public Integer getCdma() {
		return mCdma;
	}
	
	public Integer getCdmaError() {
		return mCdmaError;
	}
	
	public Integer getGsm() {
		return mGsm;
	}
	
	public Integer getGsmError() {
		return mGsmError;
	}
	public Integer getEvdo() {
		return mEvdo;
	}
	
	public Integer getEvdoError() {
		return mEvdoError;
	}
	
	public boolean hasSim(){
		return (mManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE);
	}
	
	/*
	 * Returns the phone's manufacturer from Build.MANUFACTURER
	 */
	public String getManufacturer(){
		Log.d("manufacturer", Build.MANUFACTURER);
		return Build.MANUFACTURER.toUpperCase();
	}
	
	/**
	 * Listens for changed in the phone state
	 */
	private class ReceptionMapperPhoneListener extends PhoneStateListener
	{
		@Override
		public void onServiceStateChanged(ServiceState serviceState) {
			super.onServiceStateChanged(serviceState);
			
			mState = serviceState.getState();
			log("The service state changed to " + mState);
		}

		@Override
		public void onSignalStrengthChanged(int asu) {
			super.onSignalStrengthChanged(asu);
			
			log("The signal strength changed to " + asu + " from " + mSignalStrength);
			mSignalStrength = asu;
		}

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			
			log("The signal strength changed to " + signalStrength.getGsmSignalStrength() + " from " + mSignalStrength);
			mSignalStrength = signalStrength.getGsmSignalStrength();
			mGsm = signalStrength.getGsmSignalStrength();
			mGsmError = signalStrength.getGsmBitErrorRate();
			mCdma = signalStrength.getCdmaDbm();
			mCdmaError = signalStrength.getCdmaEcio();
			mEvdo = signalStrength.getEvdoDbm();
			mEvdoError = signalStrength.getEvdoEcio();
			
		}
	}
	
	/**
	 * Log a debug message using the PhoneData tag
	 * @param message The message to display
	 */
	private void log(String message) {
		Logger.log(TAG, message);
	}
}

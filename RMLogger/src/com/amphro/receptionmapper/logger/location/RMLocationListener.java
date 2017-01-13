package com.amphro.receptionmapper.logger.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.amphro.receptionmapper.logger.Logger;

/**
 * Listens for a location change from the android phone.
 * 
 * @author Thomas Dvornik
 * 
 */
public class RMLocationListener implements LocationListener {
	/* The tag for logging debug messages */
	private final static String DEBUG_TAG = "RM Location Listener";
	
	private RMLocationManager mManager;
	
	public RMLocationListener(RMLocationManager manager) {
		super();
		mManager = manager;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		log("Got location " + location.toString());
		if (location != null){
			mManager.updateLocation(location);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		log(provider + " disabled");
		if (mManager.isAndroidLocation()) {
			mManager.requestAndroidUpdates();
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		if (mManager.isAndroidLocation()) {
			log(provider + " enabled");
			mManager.requestAndroidUpdates();	
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		log(provider + " changed to " + status);
	}
	
	/**
	 * Log a debug message using the ReceptionMapperService tag
	 * @param message The message to display
	 */
	private void log(String message) {
		Logger.log(DEBUG_TAG, message);
	}
}
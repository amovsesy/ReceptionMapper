package com.amphro.receptionmapper.logger.location;

import android.content.Context;
import android.location.Location;

import com.amphro.receptionmapper.logger.Logger;
import com.skyhookwireless.wps.IPLocation;
import com.skyhookwireless.wps.IPLocationCallback;
import com.skyhookwireless.wps.WPSAuthentication;
import com.skyhookwireless.wps.WPSContinuation;
import com.skyhookwireless.wps.WPSLocation;
import com.skyhookwireless.wps.WPSLocationCallback;
import com.skyhookwireless.wps.WPSPeriodicLocationCallback;
import com.skyhookwireless.wps.WPSReturnCode;
import com.skyhookwireless.wps.XPS;

/**
 * RMSkyhookListener listens for location updates from Skyhook and sends them to
 * the location manager.
 * 
 * @author Thomas Dvornik
 * 
 */
public class RMSkyhookListener implements IPLocationCallback,
		WPSLocationCallback, WPSPeriodicLocationCallback {
	/* The tag for logging debug messages */
	private final static String DEBUG_TAG = "RM Skyhook Callback";

	/* Skyhook authentication information */
	private final String USERNAME = "tdvornik";
	private final String DOMAIN = "Amphro";
	private final String PROVIDER = "Skyhook";

	/* The number of consecutive skyhook error before starting a provider */
	public final static int SKYHOOK_ERROR_THRESHOLD = 10;

	/* Service state */
	private int mFailedConnections = 0;

	/* Skyhook location */
	private XPS mXps;
	private WPSAuthentication mAuth;
	private RMLocationManager mManager;

	public RMSkyhookListener(RMLocationManager manager, Context context) {
		mManager = manager;
		/*
		 * Setup the Skyhook call back and service
		 */
		mXps = new XPS(context);
		mAuth = new WPSAuthentication(USERNAME, DOMAIN);
		startXPSCallbacks();
	}

	/**
	 * Has Skyhook produced an error and can't continue. Skyhook can provide an
	 * error quite often, so this method will only return true if a enough
	 * errors happen in a row gets above the threshold.
	 * 
	 * @return True if Skyhook as errored out, false otherwise
	 */
	public boolean isSkyhookError() {
		return mFailedConnections > SKYHOOK_ERROR_THRESHOLD;
	}

	@Override
	public void handleIPLocation(IPLocation arg0) {
		log("Location from ip from skyhook " + arg0.toString());
	}

	@Override
	public WPSContinuation handleWPSPeriodicLocation(WPSLocation location) {
		if (location != null) {
			//log("Location from skyhook " + location.toString());
			if (mManager != null) {
				mManager.updateLocation(toLoc(location));
			}
			mFailedConnections = 0;
		} else {
			log("WPS loc is null, logging as error");
			mFailedConnections++;
		}
		return WPSContinuation.WPS_CONTINUE;
	}

	private boolean mStop = false;
	
	@Override
	public void done() {
		log("done");
		if (!mStop) {
		new Thread() {
			public void run() {
				startXPSCallbacks();
			}
		}.start();
		}
	}

	@Override
	public WPSContinuation handleError(WPSReturnCode arg0) {
		log("WPS ERROR " + arg0.toString());
		if (++mFailedConnections >= SKYHOOK_ERROR_THRESHOLD) {
			mFailedConnections = 0;
			log("Requesting android location updates.");
			if (mManager != null) {
				mManager.requestAndroidUpdates();
			}
		}
		return WPSContinuation.WPS_CONTINUE;
	}

	@Override
	public void handleWPSLocation(WPSLocation arg0) {
		log("Location from WPS from skyhook " + arg0.toString());
	}

	/**
	 * Get periodic updates from Skyhook
	 */
	private void startXPSCallbacks() {
		log("Start skyhook " + mXps.toString() + " " + mAuth.toString());
		if (mXps != null) {
			mXps.getXPSLocation(mAuth, RMLocationManager.TIME_SEC_INTERVAL, 30,this);
		}
	}

	public void stopCallbacks() {
		mStop = true;
		mXps = null;
		mAuth = null;
		mManager = null;
	}
	
	/**
	 * Set the internal location. Used by WPS callbacks.
	 * 
	 * @param mLocation
	 */
	private Location toLoc(WPSLocation l) {
		return createLoc(l.getLongitude(), l.getLatitude(), l.getAltitude());
	}

	/**
	 * Create a new location
	 * 
	 * @param longitude
	 * @param latitude
	 * @param altitude
	 * @return The created location with provider Skyhook
	 */
	private Location createLoc(double longitude, double latitude,
			double altitude) {
		Location loc = new Location(PROVIDER);
		loc.setLongitude(longitude);
		loc.setLatitude(latitude);
		loc.setAltitude(altitude);
		return loc;
	}

	/**
	 * Log a debug message using the ReceptionMapperService tag
	 * 
	 * @param message
	 *            The message to display
	 */
	private void log(String message) {
		Logger.log(DEBUG_TAG, message);
	}
}

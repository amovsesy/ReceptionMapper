package com.amphro.receptionmapper.logger.location;

import java.util.Date;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.amphro.receptionmapper.logger.Logger;

/**
 * A location manager that receives location updates from skyhook and android.
 * Android location updates will only happen if skyhook errors out.
 * 
 * @author Thomas Dvornik
 * 
 */
public class RMLocationManager {
	/* Time in millisec */
	private final static int MILLISEC_CONST = 1000;
	public final static int TIME_SEC_INTERVAL = 3;
	private final static int TIME_MSEC_INTERVAL = TIME_SEC_INTERVAL
			* MILLISEC_CONST;

	/*
	 * Set to an hour. Millsec constant * number of seconds in a minute * number
	 * of minutes in an hour.
	 */
	public final static int TIME_FOR_LOCATION_TO_GET_OUTDATED = MILLISEC_CONST * 60 * 60;
	
	/* Distance in meters */
	public final static long DISTANCE_INTERVAL = 100;

	/* The tag for logging debug messages */
	private final static String DEBUG_TAG = "Location Manager";

	/* Location providers */
	private LocationManager mLocationManager;
	private RMSkyhookListener mCallback;
	private RMLocationListener mLocationListener;

	private Location mLocation;
	private Date mLastUpdated, mLastRetrieved;

	public RMLocationManager(Context context) {
		mLocationManager = (LocationManager) (context
				.getSystemService(Context.LOCATION_SERVICE));
		mLocationListener = new RMLocationListener(this);

		mCallback = new RMSkyhookListener(this, context);
		
		mLastRetrieved = new Date();
		log("Set up skyhook listner at " + mLastRetrieved.toGMTString());
	}

	/**
	 * Is the location manager getting, or should be getting, locations
	 * from the Android OS
	 * @return
	 */
	public boolean isAndroidLocation() {
		return mCallback.isSkyhookError();
	}

	/**
	 * Update the location to a current location
	 * 
	 * @param location The new location
	 */
	public void updateLocation(Location location) {
		mLocation = location;
		mLastUpdated = new Date();
	}

	/**
	 * Get the current location.
	 * 
	 * @return Gets the current location, which may be old if there have been
	 *         updates recently.
	 */
	public Location getLocation() {
		mLastRetrieved = new Date();
		//log("Location retreived at " + mLastRetrieved.toGMTString());
		return mLocation;
	}

	/**
	 * Is there a location. The location will be null if just initialized and
	 * received no location updates
	 * 
	 * @return True if there is a location, false otherwise.
	 */
	public boolean hasLocation() {
		return mLocation != null;
	}

	/**
	 * Has the location updated since the last location retrieval. i.e.
	 * <code>getLocation()</code> has been called.
	 * @return True if the location has been updated since last retrieved, false
	 *         otherwise.
	 */
	public boolean hasNewLocation() {
		//log("The last updated location was at " + mLastUpdated.toGMTString());
		return hasLocation() && mLastUpdated.after(mLastRetrieved);
	}

	/**
	 * Is the location old? The location is considered old if it hasn't
	 * been updated in an hour.
	 * @return True if the location hasn't been updated in an hour, 
	 * false otherwise. 
	 */
	public boolean isOldLocation() {
		return mLastUpdated.before(new Date(mLastUpdated.getTime()
				- TIME_FOR_LOCATION_TO_GET_OUTDATED));
	}

	/**
	 * Request updates from all enabled providers. If there is no enabled
	 * providers, the best provider will start
	 */
	public void requestAndroidUpdates() {
		String providers = "";
		for (String provider : mLocationManager.getProviders(true)) {
			requstAndroidUpdates(provider);
			providers += provider + " ";
		}
		if (providers.length() == 0) {
			providers = mLocationManager.getBestProvider(getProviderCriteria(),
					false);
			requstAndroidUpdates(providers);
		}

		log("Starting the following providers: " + providers);
	}

	/**
	 * Get the criteria for a provider
	 * 
	 * @return The Criteria that is best for this service
	 */
	private Criteria getProviderCriteria() {
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		c.setAltitudeRequired(false);
		c.setBearingRequired(false);
		c.setPowerRequirement(Criteria.POWER_LOW);
		c.setSpeedRequired(false);
		return c;
	}

	/**
	 * Have the location manager request location updates from the provider.
	 * 
	 * @param provider
	 */
	private void requstAndroidUpdates(String provider) {
		log("Requestiong updates for " + provider);
		mLocationManager.requestLocationUpdates(provider, TIME_MSEC_INTERVAL,
				DISTANCE_INTERVAL, mLocationListener);
	}
	
	public void destroyManager() {
		if (mLocationManager != null)
			mLocationManager.removeUpdates(mLocationListener);
		if (mCallback != null)
			mCallback.stopCallbacks();
		mCallback = null;
		mLocationListener = null;
		mLocationManager = null;
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

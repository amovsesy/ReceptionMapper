package com.amphro.receptionmapper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.util.Log;

import com.amphro.receptionmapper.node.Node;
import com.amphro.receptionmapper.node.NodeProvider;
import com.amphro.receptionmapper.phone.PhoneData;
import com.skyhookwireless.wps.IPLocation;
import com.skyhookwireless.wps.IPLocationCallback;
import com.skyhookwireless.wps.WPSAuthentication;
import com.skyhookwireless.wps.WPSContinuation;
import com.skyhookwireless.wps.WPSLocation;
import com.skyhookwireless.wps.WPSLocationCallback;
import com.skyhookwireless.wps.WPSPeriodicLocationCallback;
import com.skyhookwireless.wps.WPSReturnCode;
import com.skyhookwireless.wps.XPS;

public class ReceptionMapperService extends Service {
	/** Time in millisec */
	public final static int MILLISEC_CONST = 1000;
	public final static int TIME_SEC_INTERVAL = 5;
	public final static int TIME_MSEC_INTERVAL = TIME_SEC_INTERVAL * MILLISEC_CONST;
	
	/** Distance in meters */
	public final static long DISTANCE_INTERVAL = 100;
	
	/** The tag for logging debug messages */
	public final static String TAG = "RECEPTIONMAPPERSERVICE";
	
	/** The number of consecutive skyhook error before starting a provider */
	public final static int SKYHOOK_ERROR_THRESHOLD = 10;
	
	public final static int NOTIFICATION_ID = 1;
	
	/** Skyhook authentication information */
	public final String USERNAME = "tdvornik";
	public final String DOMAIN = "Amphro";
	public final String PROVIDER = "Skyhook";
	
	/** Skyhook location */
	private XPS mXps;
	private WPSAuthentication mAuth;
	private RMSkyhookCallback mCallback;
	
	/** Android location */
	private LocationManager mLocMan;
	private RMLocationListener mLocationListener;
	
	/** Service Uploader */
	private NodeProvider mNodeProvider;
	
	/** Phone data */
	private PhoneData mPhoneData;
	private PhoneStateListener mPhoneStateListener;
	private String mPhone;
	private String mManufacturer;
	private int mSignalStrength;
	private int mServState;
	
	/** Notification Manager */
	private NotificationManager mNotificationManager;
	
	/** Service state */
	private int mFailedConnections;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mFailedConnections = 0;
		mSignalStrength = -1;
		
		mLocMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mLocationListener = new RMLocationListener();
		mNodeProvider = new NodeProvider(this);
		
		setUpNotification();
		setUpPhoneData(this);
		
		setUpSkyhook();
		startXPSCallbacks();
	}
	
	@Override
	public void onDestroy() {
		mNodeProvider.closeDB();
		mNotificationManager.cancel(NOTIFICATION_ID);
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	/**
	 * Setup the skyhook callback and service
	 */
	private void setUpSkyhook() {
		mCallback = new RMSkyhookCallback();
		mXps = new XPS(this);
		mAuth = new WPSAuthentication(USERNAME, DOMAIN);
	}
	
	/**
	 * Set up the phone data and listener
	 * @param currentContext
	 */
	private void setUpPhoneData(final Context currentContext) {	
		mPhoneStateListener = new RMPhoneStateListener();
		mPhoneData = new PhoneData(this, mPhoneStateListener);
		mPhone = mPhoneData.getPhone();
		mManufacturer = mPhoneData.getManufacturer();
		mServState = ServiceState.STATE_IN_SERVICE;
	}

	/** 
	 * Set up the notification that the service is running
	 */
	public void setUpNotification(){
		log("Starting notification");
		mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, getString(R.string.app_name), System.currentTimeMillis());
		String expandedTitle = getString(R.string.notificationExpandedTitle);
		String expandedText = getString(R.string.notificationExpandedText);
		Intent i = new Intent(this, ReceptionMapperService.class);
		PendingIntent launchIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
		notification.setLatestEventInfo(getApplicationContext(), expandedTitle, expandedText, launchIntent);
		mNotificationManager.notify(NOTIFICATION_ID,notification);
	}

	/**
	 * Get periodic updates from skyhool
	 */
	private void startXPSCallbacks() {
		log("Start skyhook "+mXps.toString()+" "+mAuth.toString());
		mXps.getXPSLocation(mAuth, TIME_SEC_INTERVAL, 30, mCallback);
	}
	
	/**
	 * Upload data to ReceptionMapper.com
	 * @param location
	 */
	public void uploadLocation(Location location) {
		if (location != null && 
				mSignalStrength != -1 &&
				location.getLatitude() != 0 && 
				location.getLongitude() != 0) {
			ContentValues cv = new ContentValues();
			cv.put(Node.LATITUDE, location.getLatitude());
			cv.put(Node.LONGITUDE, location.getLongitude());
			cv.put(Node.NETWORK_TYPE, mPhoneData.getNetworkType().toString());
			cv.put(Node.CARRIER, mPhoneData.getCurrentProvider());
			cv.put(Node.SIGNAL_STRENGTH, mSignalStrength);
			cv.put(Node.PHONE, mPhone);
			cv.put(Node.MANUFACTURER, mManufacturer);
			mNodeProvider.insert(null, cv);
		}
	}

	/**
	 * Start the best location provider for this service
	 */
	private void startAProvider() {
		String provider = mLocMan.getBestProvider(getProviderCriteria(), false);
		requstUpdates(provider);
		log("Starting service " + provider);
	}
	
	/**
	 * Get the criteria for a provider
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
	 * Request updates from all enabled providers. If there is 
	 * no enabled providers, the best provider will start
	 */
	private void requestUpdates() {
		int providers = 0;
		for (String provider : mLocMan.getProviders(true)) {
			requstUpdates(provider);
			providers++;
		}
		if (providers == 0) {
			startAProvider();
		} else {
			log("Starting " + providers + "providers");
		}
	}
	
	/**
	 * Have the location manager request location updates
	 * from the provider. 
	 * @param provider
	 */
	private void requstUpdates(String provider) {
		log("Requestiong updates for " + provider);
		mLocMan.requestLocationUpdates(provider, TIME_MSEC_INTERVAL, DISTANCE_INTERVAL, mLocationListener);
	}
	
	/**
	 * Set the internal location. Used by WPS callbacks.
	 * @param mLocation
	 */
	protected Location toLoc(WPSLocation l) {
		return createLoc(l.getLongitude(), l.getLatitude(), l.getAltitude());
	}
	
	/**
	 * Set the internal location. Used by IP callbacks.
	 * @param mLocation
	 */
	protected Location toLoc(IPLocation l) {
		return createLoc(l.getLongitude(), l.getLatitude(), l.getAltitude());
	}
	
	/**
	 * Create a new location
	 * @param longitude
	 * @param latitude
	 * @param altitude
	 * @return The created location with provider Skyhook
	 */
	private Location createLoc(double longitude, double latitude, double altitude) {
		Location loc = new Location(PROVIDER);
		loc.setLongitude(longitude);
		loc.setLatitude(latitude);
		loc.setAltitude(altitude);
		return loc;
	}
	
	/**
	 * Log a debug message using the ReceptionMapperService tag
	 * @param message The message to display
	 */
	private void log(String message) {
		Log.d(TAG, message);
	}
	
	/**
	 * Listens for changed in the phone state
	 */
	private class RMPhoneStateListener extends PhoneStateListener {
		@Override
		public void onServiceStateChanged(ServiceState serviceState) {
			super.onServiceStateChanged(serviceState);
			
			mServState = serviceState.getState();
			log("The service state changed to " + mServState);
		}

		@Override
		public void onSignalStrengthChanged(int asu) {
			super.onSignalStrengthChanged(asu);
			if(asu < 0 || mServState == ServiceState.STATE_OUT_OF_SERVICE){
				mSignalStrength = 0;
			} else{
				mSignalStrength = asu;
			}		
			log("The signal strength changed from " + asu + " to " + mSignalStrength);
		}
	}
	
	/**
	 * Listens for a location change from android
	 */
	private class RMLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			log("Got location " + location.toString());
			if (location != null){
				uploadLocation(location);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			log(provider + " disabled");
			requestUpdates();
		}

		@Override
		public void onProviderEnabled(String provider) {
			log(provider + " enabled");
			requstUpdates(provider);	
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			log(provider + " changed to " + status);
		}
	}
	
	/**
	 * Skyhook callbacks for location updates
	 */
	private class RMSkyhookCallback implements IPLocationCallback, WPSLocationCallback, WPSPeriodicLocationCallback {
		@Override
		public void handleIPLocation(IPLocation arg0) {
			log("Location from ip from skyhook "+arg0.toString());
		}
		
		@Override
		public WPSContinuation handleWPSPeriodicLocation(WPSLocation location) {
			if (location != null) {
				log("Location from skyhook " + location.toString());
				uploadLocation(toLoc(location));
				mFailedConnections = 0;
			} else {
				log("WPS loc is null, logging as error");
				mFailedConnections++;
			}
			return WPSContinuation.WPS_CONTINUE;
		}

		@Override
		public void done() {
			log("done");
			new Thread() {
				public void run() {
	                startXPSCallbacks();
	            }
			}.start();
		}
		
		@Override
		public WPSContinuation handleError(WPSReturnCode arg0) {
			log("WPS ERROR " + arg0.toString());
			if (++mFailedConnections >= SKYHOOK_ERROR_THRESHOLD) {
				mFailedConnections = 0;
				requestUpdates();
			}
			return WPSContinuation.WPS_CONTINUE;
		}

		@Override
		public void handleWPSLocation(WPSLocation arg0) {
			log("Location from WPS from skyhook "+arg0.toString());
		}
	}
}

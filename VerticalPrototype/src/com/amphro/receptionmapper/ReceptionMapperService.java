package com.amphro.receptionmapper;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import android.telephony.ServiceState;

import com.skyhookwireless.wps.IPLocation;
import com.skyhookwireless.wps.IPLocationCallback;
import com.skyhookwireless.wps.WPS;
import com.skyhookwireless.wps.WPSAuthentication;
import com.skyhookwireless.wps.WPSContinuation;
import com.skyhookwireless.wps.WPSLocation;
import com.skyhookwireless.wps.WPSLocationCallback;
import com.skyhookwireless.wps.WPSReturnCode;
import com.skyhookwireless.wps.WPSStreetAddressLookup;

public class ReceptionMapperService extends Service {
	private WPS wps;
	private WPSAuthentication auth;
	private IPLocationCallback callback;
	private WPSLocationCallback callback2;
	private String rm = "ReceptionMapperService";
	private double longitude = 0;
	private double latitude = 0;
	private int signalStrength = 1000;
	
	@Override
	public void onCreate() {
		super.onCreate();
		initSckhookService();
		
		// Call the location function with callback
		Log.d(rm, auth.getUsername() + " " + auth.getRealm());
		wps.getIPLocation(auth, WPSStreetAddressLookup.WPS_NO_STREET_ADDRESS_LOOKUP, callback);
		wps.getLocation(auth, WPSStreetAddressLookup.WPS_NO_STREET_ADDRESS_LOOKUP, callback2);
		
		try{
			String url = "http://www.receptionmapper.com/respond.php";
			URL u = new URL(url);
			InputStream i = u.openStream();
			Scanner s = new Scanner(i);
			s.useDelimiter("\n");
			
			String output = "";
			while(s.hasNext()){
				output += s.next();
			}
			
			TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			int netType = tm.getNetworkType();
			
			PhoneStateListener pl = new PhoneStateListener(){
				@Override
				public void onSignalStrengthChanged(int asu) {
					signalStrength = asu;
					Log.d(rm, "state changed = " + signalStrength);
					super.onSignalStrengthChanged(asu);
				}

				@Override
				public void onCallForwardingIndicatorChanged(boolean cfi) {
					// TODO Auto-generated method stub
					Log.d(rm, "call forward");
					super.onCallForwardingIndicatorChanged(cfi);
				}

				@Override
				public void onCallStateChanged(int state, String incomingNumber) {
					// TODO Auto-generated method stub
					Log.d(rm, "call state");
					super.onCallStateChanged(state, incomingNumber);
				}

				@Override
				public void onCellLocationChanged(CellLocation location) {
					// TODO Auto-generated method stub
					Log.d(rm, "cell location");
					super.onCellLocationChanged(location);
				}

				@Override
				public void onDataActivity(int direction) {
					// TODO Auto-generated method stub
					Log.d(rm, "data activity");
					super.onDataActivity(direction);
				}

				@Override
				public void onDataConnectionStateChanged(int state) {
					// TODO Auto-generated method stub
					Log.d(rm, "data connection " + state);
					super.onDataConnectionStateChanged(state);
				}

				@Override
				public void onMessageWaitingIndicatorChanged(boolean mwi) {
					// TODO Auto-generated method stub
					Log.d(rm, "message waiting");
					super.onMessageWaitingIndicatorChanged(mwi);
				}

				@Override
				public void onServiceStateChanged(ServiceState serviceState) {
					// TODO Auto-generated method stub
					Log.d(rm, "service state changed = " + serviceState.getState());
					signalStrength = serviceState.getState();
					super.onServiceStateChanged(serviceState);
				}
			};
			
			tm.listen(pl, PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
			tm.listen(pl, PhoneStateListener.LISTEN_DATA_ACTIVITY);
			tm.listen(pl, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
			tm.listen(pl, PhoneStateListener.LISTEN_SERVICE_STATE);
			tm.listen(pl, PhoneStateListener.LISTEN_CALL_STATE);
			
			output += "\nLongitude = " + longitude + "\nLatitude = " + latitude;
			output += "\nNetwork Type: " + netType + "\nSignal Strenght: " + signalStrength;
			Context c = getApplicationContext();
			Toast.makeText(c, output, Toast.LENGTH_LONG).show();
			Log.d(rm, output);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		initSckhookService();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	private void initSckhookService() {
		// Create the authentication object
		// myAndroidContext must be a Context instance
		if (wps == null) {
			wps = new WPS(this); 
		}
		if (auth == null) {
			auth = new WPSAuthentication("tdvornik", "Amphro");
		}

		// Callback object
		if (callback == null) {
			callback = new IPLocationCallback(){
				// What the application should do after it's done
				public void done() {
					// after done() returns, you can make more WPS calls.
				}
				
				// What the application should do if an error occurs
				public WPSContinuation handleError(WPSReturnCode error) {
					//handleWPSError(error); // you'll implement handleWPSError()
				
					// To retry the location call on error use WPS_CONTINUE,
					// otherwise return WPS_STOP
					Log.d(rm + "IPError", error.toString());
					return WPSContinuation.WPS_STOP;
				}

				public void handleIPLocation(IPLocation arg0) {
					// TODO Auto-generated method stub
					String l = "";
					Log.d(rm + "IPLocation", arg0.toString());
					longitude = arg0.getLongitude();
					latitude = arg0.getLatitude();
				}
			};

			// Callback object
			if (callback2 == null) {
				callback2 = new WPSLocationCallback(){
					// What the application should do after it's done
					public void done() {
						// after done() returns, you can make more WPS calls.
					}
					
					// What the application should do if an error occurs
					public WPSContinuation handleError(WPSReturnCode error) {
						//handleWPSError(error); // you'll implement handleWPSError()
					
						// To retry the location call on error use WPS_CONTINUE,
						// otherwise return WPS_STOP
						Log.d(rm + "WPSError", error.toString());
						return WPSContinuation.WPS_STOP;
					}
					
					// Implements the actions using the location object
					public void handleWPSLocation(WPSLocation location) {
						// you'll implement printLocation()
						//printLocation(location.getLatitude(), location.getLongitude());
						String l = "";
						Log.d(rm + "WPSLocation", location.toString());
						latitude = location.getLatitude();
						longitude = location.getLongitude();
					}
				};
			}
		}
	}

}

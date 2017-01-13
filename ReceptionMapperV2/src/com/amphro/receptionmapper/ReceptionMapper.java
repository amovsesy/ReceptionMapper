package com.amphro.receptionmapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.amphro.receptionmapper.dialog.DisplayOptionsDialog;
import com.amphro.receptionmapper.dialog.LocationDialog;
import com.amphro.receptionmapper.dialog.NetworkProviderDialog;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class ReceptionMapper extends Activity {
	private static final int MAX_IMAGE_DOWNLOAD_ATTEMPTS = 5;
	private static final String CONFIRM_TEXT = "Ok";
	
	private MapView mapView;
	private ScrollImageView mapimageView;
	private ProgressDialog progress;
	private SharedPreferences preferences;
	private String cachedMap;
	private String paramCache;
	private List<String> carriers;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagescrollview);
        
        if (!isAppExpired()) {
	        try {
            	TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
            	if(tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT){
            		startService(new Intent(this, ReceptionMapperService.class));
            	}
            } 
	        catch (Exception e) { 
	        	/* Recover from this exception, just don't start the service */ 
	        }
	        preferences = this.getPreferences(MODE_PRIVATE);
	        carriers = new ArrayList<String>();
	        
	    	FrameLayout fl = (FrameLayout)findViewById(R.id.frame);
			mapimageView = new ScrollImageView(this, carriers);
			mapimageView.setCarriers(carriers);
			fl.addView(mapimageView);
			
			if(!preferences.contains("CITY")){
	        	setupLocationDialog();
	        } else {
	        	setMap(preferences.getString("CITY", ""));
	        }
	        
	        if(!preferences.contains("GRID")){
	        	setupDisplayDialog();
	        }
	        
	        if(!preferences.contains("ATT")){
	        	setupNetworkDialog();
	        }
        }
	}

	/** 
	 * Checks if the app is expired. 
	 * This app expires 3/20/2010
	 * @return true if app espired, false otherwise
	 */
	private boolean isAppExpired() {
		if (new Date().after(new GregorianCalendar(2010,2,20).getTime())) {
			alertUser("Beta App Expired", "This is a beta version of the app.  Please download the full version once on the market.", true);
            /* Should never return because the app will exit */
            return true;
		}
		return false;
	}
	
	/**
	 * Allert the user that something happend
	 * @param title of the alert
	 * @param message of the alert
	 * @param exitApp Should the app exit from this alert?
	 */
	private void alertUser(String title, String message, final boolean exitApp) {
		AlertDialog.Builder ad = new AlertDialog.Builder(ReceptionMapper.this);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton(CONFIRM_TEXT, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (exitApp) {
					finish();
				}
			}
		}).show();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(preferences.getBoolean("OPTIONSDIALOG", false)){
			setupDisplayDialog();
		} else if (preferences.getBoolean("NETWORKDIALOG", false)){
			setupNetworkDialog();
		} else if (preferences.getBoolean("LOCATIONDIALOG", false)){
			setupLocationDialog();
		}
	}

	/**
	 * Sets the ScrollImageView's map if the mapName isn't null and isn't cached
	 * @param mapName Name of the map to load
	 */
	public void setMap(String mapName) {
		if (mapName == null || mapName.equals("") || (cachedMap != null && cachedMap.equals(cachedMap))) {
			return;
		}
		String mapUrl;
		Bitmap map = null;
		
		try {
			mapUrl = "http://maps.google.com/maps/api/staticmap?center="+encode(mapName)+"&zoom=13&mobile=true&size=2000x2000&sensor=false";
			map = getRemoteImage(new URL(mapUrl));
			
			if(map == null){
				alertUser("Load Error", "Failed to load the map. Check internet connectivity, then reload the map.", false);
			} else {
				cachedMap = mapName;
				setMapOverlay(mapName);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		mapimageView.setImage(map);
		mapimageView.postInvalidate();
	}
	
	/**
	 * Sets the ScrollImageView's mapOverlay if the mapName isn't null
	 * Need to see if options are cached
	 * @param mapName Name of the map to load
	 */
	public void setMapOverlay(String mapName) {
		if (mapName == null || mapName.equals("")) {
			return;
		}
		String params = buildOverlayParams(mapName);
		if (paramCache != null && paramCache.equals(params)) {
			return;
		}
		
		if (params == null) {
			mapimageView.setOverlayImage(null);
			mapimageView.postInvalidate();
			return;
		}
		
		Bitmap overlays = null;
		String overlayUrl = "http://www.receptionmapper.com/services/createOverlay.php?image=";
		try {
			overlayUrl += params;
			overlays = getRemoteImage(new URL(overlayUrl));

			if(overlays == null){
				alertUser("Load Error", "Failed to load reception overlays. Check internet connectivity then reload the data you wanted.", false);
				paramCache = null;
			} else {
				paramCache = params;
				
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		mapimageView.setOverlayImage(overlays);
		mapimageView.postInvalidate();
	}
	
	/**
	 * Get the overlay params as a string in http request format
	 * @param mapName name of the map for the params to get
	 * @return The params as a string in http request format
	 */
	public String buildOverlayParams(String mapName) {
		String overlayParams = "";
		String display = preferences.getString("DISPLAY", "NONE");
		String network = preferences.getString("NETWORK", "NONE");
		
		if (display.equals("reception")) {
			overlayParams += "recep";
		} else {
			if (network.equals("3G")) {
				overlayParams += "3g";
			} else if (network.equals("2G")) {
				overlayParams += "2g";
			} else if (network.equals("edge")) {
				overlayParams += "edge";
			} else if (network.equals("all")) { 
				overlayParams += "all";
			} else {
				return null;
			}
		}
		overlayParams += "&carriers=";
		carriers.clear();
		if (preferences.getBoolean("ATT", false)) {
			overlayParams +="att";	
			carriers.add("att");
		}
		if (preferences.getBoolean("SPRINT", false)) {
			overlayParams += overlayParams.endsWith("=") ? "" : ",";
			overlayParams += "sprint";
			carriers.add("sprint");
		}
		if (preferences.getBoolean("TMOBILE", false)) {
			overlayParams += overlayParams.endsWith("=") ? "" : ",";
			overlayParams += "tmobile";
			carriers.add("tmobile");
		}
		if (preferences.getBoolean("VERIZON", false)) {
			overlayParams += overlayParams.endsWith("=") ? "" : ",";
			overlayParams += "verizon";
			carriers.add("verizon");
		}
		
		if (overlayParams.endsWith("=")) {
			return null;
		}
		
		overlayParams += "&grid=";
		if (preferences.getBoolean("GRID", false)) {
			overlayParams += "true";
		} else {
			overlayParams += "false";
		}
		return overlayParams;
	}
	

	/**
	 * Wrapper to encode a encode a string so it can 
	 * be used in a url
	 * @param arg the string to encode
	 * @return the encoded string
	 * @throws UnsupportedEncodingException
	 */
	private String encode(String arg) throws UnsupportedEncodingException {
		return URLEncoder.encode(arg, "UTF-8");
	}
	
	/**
	 * Create a bitmap from a URL. Will try MAX_IMAGE_DOWNLOAD_ATTEMPTS
	 * before giving up and returning null
	 * @param aURL of the image
	 * @return the created bitmap
	 */
	public Bitmap getRemoteImage(URL aURL) {
		try {
			Bitmap bm = null;
			int tries = 0;
			while (tries++ < MAX_IMAGE_DOWNLOAD_ATTEMPTS && bm == null) {
				URLConnection conn = aURL.openConnection();
				conn.connect();
				BufferedInputStream bis = new BufferedInputStream(conn
						.getInputStream());
				bm = BitmapFactory.decodeStream(bis);
				bis.close();
			}
			return bm;
		} catch (IOException e) {
			Log.d("DEBUGTAG", "Oh noooz an error...");
		}
		return null;
	}

	/**
	 * Move the map view to a specific location from an address
	 * 
	 * @param address
	 * @return true if the address was found and the map view was moved to that
	 *         location, false otherwise.
	 */
	protected boolean gotoLocation(String address) {
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		GeoPoint p;

		try {
			List<Address> addresses = geoCoder.getFromLocationName(address, 5);

			if (addresses.size() > 0) {
				p = new GeoPoint((int) (addresses.get(0).getLatitude() * 1E6),
						(int) (addresses.get(0).getLongitude() * 1E6));
				mapView.getController().animateTo(p);
				mapView.invalidate();
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Menu.FIRST, Menu.NONE, "Load Map");
		menu.add(0, Menu.FIRST+1, Menu.NONE, "Network Providers");
		menu.add(0, Menu.FIRST+2, Menu.NONE, "Display Options");

		return true;
	}
	
	public void setupProgress(){
		progress = new ProgressDialog(this);
        progress.setTitle("");
        progress.setMessage("Updating. Please wait....");
	}
	
	public void setupNetworkDialog(){
		preferences.edit().putBoolean("NETWORKDIALOG", true).commit();
		setupProgress();
		NetworkProviderDialog networkDialog = new NetworkProviderDialog(this, progress, preferences);
		networkDialog.show();
	}
	
	public void setupLocationDialog(){
		preferences.edit().putBoolean("LOCATIONDIALOG", true).commit();
		setupProgress();
		LocationDialog locationDialog = new LocationDialog(this, progress, preferences);
		locationDialog.show();
	}
	
	public void setupDisplayDialog(){
		preferences.edit().putBoolean("OPTIONSDIALOG", true).commit();
		setupProgress();
		DisplayOptionsDialog displayDialog = new DisplayOptionsDialog(this, progress, preferences);
		displayDialog.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case Menu.FIRST:
				setupLocationDialog();
				return true;
			case Menu.FIRST+1:
	    		setupNetworkDialog();
	    		return true;
	    	case Menu.FIRST+2:
	    		setupDisplayDialog();
	    		return true;
	    }
		return false;
	}
}
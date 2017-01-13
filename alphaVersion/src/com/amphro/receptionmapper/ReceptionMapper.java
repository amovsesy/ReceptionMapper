package com.amphro.receptionmapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.amphro.receptionmapper.dialog.DisplayOptionsDialog;
import com.amphro.receptionmapper.dialog.LocationDialog;
import com.amphro.receptionmapper.dialog.NetworkProviderDialog;
import com.amphro.receptionmapper.node.NodeProvider;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ReceptionMapper extends MapActivity {
	MapView mapView;
	ImageView imageView;
	List<Overlay> mapOverlays;
	Drawable drawable;
	ReceptionMapOverlay overlay;
	NodeProvider nodeProvider;
	ProgressDialog progress;
	SharedPreferences preferences;
	FrameLayout frame;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagescrollview);
        
        preferences = this.getPreferences(MODE_PRIVATE);
        
		startService(new Intent(this, ReceptionMapperService.class));

		// mapView = (MapView) findViewById(R.id.mapview);
		// mapView.setBuiltInZoomControls(true);
        frame = (FrameLayout)findViewById(R.id.frame);
		imageView = new ImageView(this);
		imageView.setAlpha(255);
		
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
		
//		setMap("San Luis Obispo, CA");
		// ((ImageView)findViewById(R.id.imagerecep)).setAlpha(50);
		/*
		 * mapOverlays = mapView.getOverlays(); drawable =
		 * this.getResources().getDrawable(R.drawable.recep); nodeProvider = new
		 * NodeProvider(this);
		 * 
		 * HashMap<String, Float> grids = nodeProvider.getGrids(0, 0, 0, 0, 0);
		 * Log.d("RECEPTIONMAPPER GRIDS", grids == null ? "IS NULL" :
		 * "NOT NULL"); overlay = new ReceptionMapOverlay(grids);
		 * mapOverlays.add(overlay); LocationManager lm =
		 * (LocationManager)getSystemService(Context.LOCATION_SERVICE); Criteria
		 * c = new Criteria(); c.setAltitudeRequired(false);
		 * c.setBearingRequired(false);
		 * c.setPowerRequirement(Criteria.NO_REQUIREMENT);
		 * c.setSpeedRequired(false); Location l =
		 * lm.getLastKnownLocation(lm.getBestProvider(c, true));
		 * 
		 * GeoPoint point; if (l != null){ point = new
		 * GeoPoint((int)(l.getLatitude() * 1e6),(int)(l.getLongitude() * 1e6));
		 * 
		 * mapView.getController().animateTo(point);
		 * mapView.getController().setZoom(14);
		 * 
		 * }
		 */
		// TODO: Add an search bar on the top of the map view so someone can
		// enter an address
		// gotoLocation("empire state building");
	}

	public void setMap(String mapName) {
		if (mapName == null || mapName.equals("")) {
			return;
		}
		frame.removeAllViews();
		String url;
		try {
			url = "http://maps.google.com/maps/api/staticmap?center="+encode(mapName)+"&zoom=13&mobile=true&size=2000x2000&sensor=false";
			imageView.setImageBitmap(getRemoteImage(new URL(url)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		frame.addView(imageView);
		String display = preferences.getString("DISPLAY", "NONE");
		String network = preferences.getString("NETWORK", "NONE");
		if (preferences.getBoolean("ATT", false)) {
			if (display.equals("reception")) {
				addOverlay(R.drawable.redarea);
			} else {
				if (network.equals("3G")) {
					addOverlay(R.drawable.red3g);
				} else if (network.equals("2G")) {
					addOverlay(R.drawable.red2g);
				} else if (network.equals("edge")) {
					addOverlay(R.drawable.rededge);
				} else {
					addOverlay(R.drawable.red3g);
					addOverlay(R.drawable.red2g);
					addOverlay(R.drawable.rededge);
				}
			}
		}
		if (preferences.getBoolean("SPRINT", false)) {
			if (display.equals("reception")) {
				addOverlay(R.drawable.greenarea);
			} else {
				if (network.equals("3G")) {
					addOverlay(R.drawable.green3g);
				} else if (network.equals("2G")) {
					addOverlay(R.drawable.green2g);
				} else if (network.equals("edge")) {
					addOverlay(R.drawable.greenedge);
				} else {
					addOverlay(R.drawable.green3g);
					addOverlay(R.drawable.green2g);
					addOverlay(R.drawable.greenedge);
				}
			}
		}
		if (preferences.getBoolean("TMOBILE", false)) {
			if (display.equals("reception")) {
				addOverlay(R.drawable.yellowarea);
			} else {
				if (network.equals("3G")) {
					addOverlay(R.drawable.yellow3g);
				} else if (network.equals("2G")) {
					addOverlay(R.drawable.yellow2g);
				} else if (network.equals("edge")) {
					addOverlay(R.drawable.yellowedge);
				} else {
					addOverlay(R.drawable.yellow3g);
					addOverlay(R.drawable.yellow2g);
					addOverlay(R.drawable.yellowedge);
				}
			}
		}
		if (preferences.getBoolean("VERIZON", false)) {
			if (display.equals("reception")) {
				addOverlay(R.drawable.bluearea);
			} else {
				if (network.equals("3G")) {
					addOverlay(R.drawable.blue3g);
				} else if (network.equals("2G")) {
					addOverlay(R.drawable.blue2g);
				} else if (network.equals("edge")) {
					addOverlay(R.drawable.blueedge);
				} else {
					addOverlay(R.drawable.blue3g);
					addOverlay(R.drawable.blue2g);
					addOverlay(R.drawable.blueedge);
				}
			}
		}
		
		if (preferences.getBoolean("GRID", false)) {
			addOverlay(R.drawable.grid);
		}
		
		imageView.postInvalidate();
	}
	
	public void addOverlay(int drawableID) {
		ImageView iv = new ImageView(this);
		iv.setImageResource(drawableID);
		frame.addView(iv);
	}

	private String encode(String arg) throws UnsupportedEncodingException {
		return URLEncoder.encode(arg, "UTF-8");
	}
	
	public Bitmap getRemoteImage(URL aURL) {
		try {
			URLConnection conn = aURL.openConnection();
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn
					.getInputStream());
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			return bm;
		} catch (IOException e) {
			Log.d("DEBUGTAG", "Oh noooz an error...");
		}
		return null;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
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
			String add = "";
			// TODO: add logic so if multiple address are found we ask them what
			// one they meant.
			// There might also be something out there that does this for us
			// already.
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
		/*
		 * SubMenu netProvider = menu.addSubMenu(0, Menu.FIRST, Menu.NONE,
		 * "Network Privider"); MenuInflater inf = new MenuInflater(this);
		 * inf.inflate(R.menu.networkprovider, netProvider);
		 * 
		 * SubMenu displayOptions = menu.addSubMenu(0, Menu.FIRST+1, Menu.NONE,
		 * "Display Options"); inf.inflate(R.menu.displayoptions,
		 * displayOptions);
		 * 
		 * displayOptions.add(1, Menu.FIRST, Menu.NONE, "3G")
		 * .setChecked(true).setOnMenuItemClickListener(this);
		 * displayOptions.add(1, Menu.FIRST+1, Menu.NONE, "2G")
		 * .setOnMenuItemClickListener(this); displayOptions.add(1,
		 * Menu.FIRST+2, Menu.NONE, "Edge") .setOnMenuItemClickListener(this);
		 * displayOptions.setGroupCheckable(1, true, true);
		 */

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
		setupProgress();
		NetworkProviderDialog networkDialog = new NetworkProviderDialog(this, progress, preferences);
		networkDialog.show();
	}
	
	public void setupLocationDialog(){
		setupProgress();
		LocationDialog locationDialog = new LocationDialog(this, progress, preferences);
		locationDialog.show();
	}
	
	public void setupDisplayDialog(){
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
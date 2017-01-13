package com.amphro.receptionmapper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ReceptionMapper extends MapActivity {
	MapView mapView;
	
	List<Overlay> mapOverlays;
	Drawable drawable;
	ReceptionMapOverlay itemizedOverlay;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startService(new Intent(this, ReceptionMapperService.class));
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.recep);

        ShapeDrawable sd = new ShapeDrawable(new OvalShape());
        sd.getPaint().setColor(0xff74AC23);
        
        sd.getShape().resize(500, 800);
        
        itemizedOverlay = new ReceptionMapOverlay(sd);
        
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setPowerRequirement(Criteria.NO_REQUIREMENT);
        c.setSpeedRequired(false);
        Location l = lm.getLastKnownLocation(lm.getBestProvider(c, true));
        
        GeoPoint point; 
        if (l != null){
        	point = new GeoPoint((int)(l.getLatitude() * 1e6),(int)(l.getLongitude() * 1e6));
        	OverlayItem overlayitem = new OverlayItem(point, "", "");
        
        	mapView.getController().animateTo(point);
        	mapView.getController().setZoom(14);
        
        	itemizedOverlay.addOverlay(overlayitem);
        	mapOverlays.add(itemizedOverlay);
        }
    	//TODO: Add an search bar on the top of the map view so someone can enter an address
        //gotoLocation("empire state building");
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Move the map view to a specific location from an address
	 * @param address
	 * @return true if the address was found and the map view was 
	 * 	moved to that location, false otherwise.
	 */
	protected boolean gotoLocation(String address) {
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		GeoPoint p;
		
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address, 5);
            String add = "";
            //TODO: add logic so if multiple address are found we ask them what one they meant.
            // There might also be something out there that does this for us already.
            if (addresses.size() > 0) {
                p = new GeoPoint(
                        (int) (addresses.get(0).getLatitude() * 1E6), 
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
}
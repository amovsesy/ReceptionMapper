package com.amphro.receptionmapper;

import java.util.HashMap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.amphro.receptionmapper.location.Location;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class ReceptionMapOverlay extends Overlay {
	private HashMap<String, Float> mGrids;
	
	public ReceptionMapOverlay(HashMap<String, Float> grids) {
		super();
		mGrids = grids;
	}
	
	public void setGrids(HashMap<String, Float> grids) {
		mGrids = grids;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		Projection p = mapView.getProjection();
		Paint paint = new Paint();
		paint.setAlpha(50);
		paint.setColor(Color.RED);
		Float avgRec;
		GeoPoint gp;
		
		Log.d("RECEPTIONMAPPER DRAW MAP", String.valueOf(mGrids.size()));
		int right = mapView.getRight();
		int bottom = mapView.getBottom();
		Log.d("RECEPTIONMAPPER DRAW MAP", String.valueOf(mGrids.size()) + " " + String.valueOf(right) + " " + String.valueOf(bottom));
		int x;
		int y;
		
		for (int i = 0; i < right; i++) {
			for (int j = 0; j < bottom; j++) {
				gp = p.fromPixels(i, j);
				x = Location.toGSSDB5x(gp.getLongitudeE6()/1000000.00);
				y = Location.toGSSDB5y(gp.getLatitudeE6()/1000000.00);
				avgRec = mGrids.get(Location.makeKey(x, y));
				if (avgRec != null && avgRec > 0) {
					Log.d("RECEPTIONMAPPER DRAW MAP", String.valueOf(avgRec));
					paint.setAlpha(avgRec.intValue()*10);
					canvas.drawPoint(i, j, paint);
				}
			}
			Log.d("RECEPTIONMAPPER DRAW MAP", String.valueOf(i));
		}	
		Log.d("RECEPTIONMAPPER DRAW MAP", "DONE");
	}

}

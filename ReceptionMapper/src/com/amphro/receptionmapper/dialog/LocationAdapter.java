/**
 * 
 */
package com.amphro.receptionmapper.dialog;

import java.util.ArrayList;

import com.amphro.receptionmapper.ReceptionMapper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Aleks
 *
 */
public class LocationAdapter extends BaseAdapter implements ListAdapter, OnItemClickListener {
	private ArrayList<String> maps;
	private Context currentContext;
	private ProgressDialog progress;
	private LocationDialog currentDialog;
	private SharedPreferences preferences;
	
	public LocationAdapter(Context context, ArrayList<String> maps, ProgressDialog prog, LocationDialog loc, SharedPreferences prefs) {
		this.maps = maps;
		this.currentContext = context;
		this.progress = prog;
		this.currentDialog = loc;
		this.preferences = prefs;
	}
	
	@Override
	public int getCount() {
		return maps.size();
	}

	@Override
	public Object getItem(int position) {
		return maps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = new TextView(currentContext);
			((TextView)convertView).setText(this.maps.get(position));
			((TextView)convertView).setTextSize(20f);
		} else {
			((TextView)convertView).setText(this.maps.get(position));
			((TextView)convertView).setTextSize(20f);
		}
		
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		progress.show();
		currentDialog.dismiss();
		((ReceptionMapper)currentContext).setMap(maps.get(arg2));
		preferences.edit().putString("CITY", maps.get(arg2)).commit();
//		progress.dismiss();
//		do stuff
	}
}

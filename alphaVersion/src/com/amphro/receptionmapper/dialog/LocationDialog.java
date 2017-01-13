/**
 * 
 */
package com.amphro.receptionmapper.dialog;

import java.util.ArrayList;

import com.amphro.receptionmapper.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

/**
 * @author Aleks
 *
 */
public class LocationDialog extends Dialog implements TextWatcher {
	private ProgressDialog progress;
	private SharedPreferences preferences;
	private ListView mapList;
	private EditText search;
	private LocationAdapter cityListAdapter;
	private ArrayList<String> maps;
	private ArrayList<String> allMaps;
	
	public LocationDialog(Context context, ProgressDialog pd, SharedPreferences prefs) {
		super(context);
		this.progress = pd;
		this.preferences = prefs;
		maps = new ArrayList<String>();
		allMaps = new ArrayList<String>();
		
		maps.add("Los Angeles, CA");
		maps.add("San Francisco, CA");
		maps.add("San Luis Obispo, CA");
		
		allMaps.add("Los Angeles, CA");
		allMaps.add("San Francisco, CA");
		allMaps.add("San Luis Obispo, CA");
		
		cityListAdapter = new LocationAdapter(context, maps, progress, this, preferences);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.maps);
		setTitle("Click on a Location");
		
		mapList = (ListView) findViewById(R.id.mapsList);
		search = (EditText) findViewById(R.id.searchBar);
		
		mapList.setAdapter(cityListAdapter);
		mapList.setOnItemClickListener(cityListAdapter);
		
		search.addTextChangedListener(this);
		search.setText(preferences.getString("SAVEDTEXT", ""));
		
		filterMaps(search.getText().toString().toUpperCase());
	}
	
	public void filterMaps(String curText){
		maps.clear();
		
		
		for(int i=0; i < allMaps.size(); i++){
			if(allMaps.get(i).toUpperCase().startsWith(curText)){
				maps.add(allMaps.get(i));
			}
		}
		
		cityListAdapter.notifyDataSetChanged();
	}

	@Override
	public void afterTextChanged(Editable s) {
		filterMaps(s.toString().toUpperCase());
	}

	@Override
	public void dismiss() {
		preferences.edit().putString("SAVEDTEXT", search.getText().toString()).commit();
		super.dismiss();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
}

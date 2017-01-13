package com.amphro.receptionmapper.dialog;

import com.amphro.receptionmapper.R;
import com.amphro.receptionmapper.ReceptionMapper;
import com.amphro.receptionmapper.R.id;
import com.amphro.receptionmapper.R.layout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class DisplayOptionsDialog extends Dialog {
	private ProgressDialog progress;
	private SharedPreferences preferences;
	private ReceptionMapper context;
	
	public DisplayOptionsDialog(Context context, ProgressDialog pd, SharedPreferences prefs) {
		super(context);
		this.progress = pd;
		this.preferences = prefs;
		this.context = (ReceptionMapper) context;
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.displayoptionsmenu);
		setTitle("Display Options");
		
		final Spinner display = (Spinner) findViewById(R.id.whatDispalyDropDown);
		final TextView networkText = (TextView) findViewById(R.id.networks);
		final Spinner network = (Spinner) findViewById(R.id.networksDropDown);
		
		CheckBox markers = (CheckBox) findViewById(R.id.showMarkers);
		CheckBox area = (CheckBox) findViewById(R.id.showArea);
		
		markers.setChecked(preferences.getBoolean("MARKERS", false));
		area.setChecked(preferences.getBoolean("GRID", false));
		
		String disp = preferences.getString("DISPLAY", "");
		String net = preferences.getString("NETWORK", "");
		
		if(disp.equals("network")){
			display.setSelection(1);
		} else {
			display.setSelection(0);
		}
		
		if(net.equals("2G")){
			network.setSelection(1);
		} else if (net.equals("edge")){
			network.setSelection(2);
		} else if (net.equals("all")){
			network.setSelection(3);
		} else {
			network.setSelection(0);
		}
		
		display.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(display.getItemAtPosition(arg2).equals("Cell Reception")){
					network.setVisibility(View.INVISIBLE);
					networkText.setVisibility(View.INVISIBLE);
				} else {
					network.setVisibility(View.VISIBLE);
					networkText.setVisibility(View.VISIBLE);	
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		Button save = (Button) findViewById(R.id.saveOptions);
		Button cancel = (Button) findViewById(R.id.cancelOptions);
		
		save.setOnClickListener(new SaveOnClickListener());
		cancel.setOnClickListener(new CancelOnClickListener());
	}

	private class SaveOnClickListener implements android.view.View.OnClickListener{
		@Override
		public void onClick(View arg0) {
			CheckBox markers = (CheckBox) findViewById(R.id.showMarkers);
			CheckBox area = (CheckBox) findViewById(R.id.showArea);
			Spinner whatToDisplay = (Spinner) findViewById(R.id.whatDispalyDropDown);
			Spinner networksSpinner = (Spinner) findViewById(R.id.networksDropDown);
			
			DisplayOptionsDialog.this.dismiss();
//			progress.show();
			
			String network = "";
			
			if(networksSpinner.getSelectedItem().equals("3G Network")){
				network += "3G";
			} else if (networksSpinner.getSelectedItem().equals("2G Network")){
				network += "2G";
			} else if (networksSpinner.getSelectedItem().equals("Edge")){
				network += "edge";
			} else if (networksSpinner.getSelectedItem().equals("All")){
				network += "all";
			}
			
			String display = "";
			
			if(whatToDisplay.getSelectedItem().equals("Cell Reception")){
				display += "reception";
			} else if (whatToDisplay.getSelectedItem().equals("Network Type")){
				display += "network";
			}
			
			Editor edit = preferences.edit();
			edit.putBoolean("GRID", area.isChecked());
			edit.putBoolean("MARKERS", markers.isChecked());
			edit.putString("NETWORK", network);
			edit.putString("DISPLAY", display);
			edit.commit();
			
			context.setMap(preferences.getString("CITY", ""));
		}
	}
	
	private class CancelOnClickListener implements android.view.View.OnClickListener{
		@Override
		public void onClick(View arg0) {
			DisplayOptionsDialog.this.dismiss();
			
			if(!preferences.contains("MARKERS")){
				Editor edit = preferences.edit();
				edit.putBoolean("GRID", false);
				edit.putBoolean("MARKERS", false);
				edit.putString("NETWORK", "3G");
				edit.putString("DISPLAY", "reception");
				edit.commit();
			}
		}
	}
}

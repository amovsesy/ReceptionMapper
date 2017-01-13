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
import android.widget.Button;
import android.widget.CheckBox;

public class NetworkProviderDialog extends Dialog {
	private ProgressDialog progress;
	private SharedPreferences preferences;
	private ReceptionMapper context;
	
	public NetworkProviderDialog(Context context, ProgressDialog pd, SharedPreferences prefs) {
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
		
		setContentView(R.layout.networkprovidersmenu);
		setTitle("");
		
		Button save = (Button) findViewById(R.id.save);
		Button cancel = (Button) findViewById(R.id.cancel);
		
		CheckBox att = (CheckBox) findViewById(R.id.att);
		CheckBox sprint = (CheckBox) findViewById(R.id.sprint);
		CheckBox tmobile = (CheckBox) findViewById(R.id.tmobile);
		CheckBox verizon = (CheckBox) findViewById(R.id.verizon);
		
		att.setChecked(preferences.getBoolean("ATT", false));
		sprint.setChecked(preferences.getBoolean("SPRINT", false));
		tmobile.setChecked(preferences.getBoolean("TMOBILE", false));
		verizon.setChecked(preferences.getBoolean("VERIZON", false));
		
		save.setOnClickListener(new SaveOnClickListener());
		cancel.setOnClickListener(new CancelOnClickListener());
	}

	private class SaveOnClickListener implements android.view.View.OnClickListener{
		@Override
		public void onClick(View arg0) {
			CheckBox att = (CheckBox) findViewById(R.id.att);
			CheckBox sprint = (CheckBox) findViewById(R.id.sprint);
			CheckBox tmobile = (CheckBox) findViewById(R.id.tmobile);
			CheckBox verizon = (CheckBox) findViewById(R.id.verizon);
			
			NetworkProviderDialog.this.dismiss();
//			progress.show();
			
			Editor edit = preferences.edit();
			edit.putBoolean("ATT", att.isChecked());
			edit.putBoolean("SPRINT", sprint.isChecked());
			edit.putBoolean("TMOBILE", tmobile.isChecked());
			edit.putBoolean("VERIZON", verizon.isChecked());
			edit.commit();
			
			context.setMap(preferences.getString("CITY", ""));
		}
	}
	
	private class CancelOnClickListener implements android.view.View.OnClickListener{
		@Override
		public void onClick(View arg0) {
			NetworkProviderDialog.this.dismiss();
			
			if(!preferences.contains("ATT")){
				Editor edit = preferences.edit();
				edit.putBoolean("ATT", false);
				edit.putBoolean("SPRINT", false);
				edit.putBoolean("TMOBILE", false);
				edit.putBoolean("VERIZON", false);
				edit.commit();
			}
		}
	}
}

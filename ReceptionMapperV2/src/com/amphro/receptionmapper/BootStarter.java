package com.amphro.receptionmapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootStarter extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("RECEPTIONMAPPER STARTER", "Starting the reception mapper service");
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.amphro.receptionmapper.ReceptionMapperService");
		context.startService(serviceIntent);
	}
}
package com.amphro.receptionmapper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class RM extends Activity
{
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    try
    {
      TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
      
      if(tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT)
      {
        //this is where you want to start the service
      }
      
      //this is where you have the rest of the app
    }
    catch (Exception e)
    {

    }
  }
}

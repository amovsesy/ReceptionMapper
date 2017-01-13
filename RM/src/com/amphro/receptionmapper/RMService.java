package com.amphro.receptionmapper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RMService extends Service
{ 
  @Override
  public void onCreate()
  {
    // TODO Auto-generated method stub
    super.onCreate();
  }

  @Override
  public void onDestroy()
  {
    // TODO Auto-generated method stub
    super.onDestroy();
  }

  @Override
  public void onLowMemory()
  {
    // TODO Auto-generated method stub
    super.onLowMemory();
  }

  @Override
  public void onStart(Intent intent, int startId)
  {
    // TODO Auto-generated method stub
    super.onStart(intent, startId);
  }

  @Override
  public IBinder onBind(Intent intent)
  {
    return null;
  }
}

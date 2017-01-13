package com.amphro.receptionmapper;

import com.skyhookwireless.wps.IPLocation;
import com.skyhookwireless.wps.IPLocationCallback;
import com.skyhookwireless.wps.WPSContinuation;
import com.skyhookwireless.wps.WPSLocation;
import com.skyhookwireless.wps.WPSLocationCallback;
import com.skyhookwireless.wps.WPSPeriodicLocationCallback;
import com.skyhookwireless.wps.WPSReturnCode;

public class RMSkyhookCallback implements IPLocationCallback, WPSLocationCallback, WPSPeriodicLocationCallback
{

  @Override
  public void handleIPLocation(IPLocation arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void done()
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public WPSContinuation handleError(WPSReturnCode arg0)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void handleWPSLocation(WPSLocation arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public WPSContinuation handleWPSPeriodicLocation(WPSLocation arg0)
  {
    // TODO Auto-generated method stub
    return null;
  }

}

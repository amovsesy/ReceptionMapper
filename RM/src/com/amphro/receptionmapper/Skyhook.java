package com.amphro.receptionmapper;

import android.app.Service;
import android.location.Location;

import com.skyhookwireless.wps.IPLocation;
import com.skyhookwireless.wps.IPLocationCallback;
import com.skyhookwireless.wps.WPSAuthentication;
import com.skyhookwireless.wps.WPSContinuation;
import com.skyhookwireless.wps.WPSLocation;
import com.skyhookwireless.wps.WPSLocationCallback;
import com.skyhookwireless.wps.WPSPeriodicLocationCallback;
import com.skyhookwireless.wps.WPSReturnCode;
import com.skyhookwireless.wps.XPS;

public class Skyhook
{
  /** Time in millisec */
  public final static int MILLISEC_CONST = 1000;
  public final static int TIME_SEC_INTERVAL = 60*30;
  public final static int TIME_MSEC_INTERVAL = TIME_SEC_INTERVAL * MILLISEC_CONST;
  
  /** Skyhook authentication information */
  public final String USERNAME = "tdvornik";
  public final String DOMAIN = "Amphro";
  public final String PROVIDER = "Skyhook";
  
  /** The number of consecutive skyhook error before starting a provider */
  public final static int SKYHOOK_ERROR_THRESHOLD = 10;
  
  /** Skyhook location */
  private XPS _xps;
  private WPSAuthentication _wpsAuth;
  private RMSkyhookCallback _rmSkyhookCallback;
  
  private int _failedConnections;

  /**
   * Setup the skyhook callback and service
   */
  public <T extends Service> void setUpSkyhook(T service)
  {
    _rmSkyhookCallback = new RMSkyhookCallback();
    _xps = new XPS(service);
    _wpsAuth = new WPSAuthentication(USERNAME, DOMAIN);
    _failedConnections = 0;
  }
  
  public void startXPSCallbacks()
  {
    Util.log("Start skyhook "+ _xps.toString()+" "+ _wpsAuth.toString());
    _xps.getXPSLocation(_wpsAuth, TIME_SEC_INTERVAL, 30, _rmSkyhookCallback);
  }
  
  /**
   * Set the internal location. Used by WPS callbacks.
   * 
   * @param mLocation
   */
  protected Location toLoc(WPSLocation l)
  {
    return createLoc(l.getLongitude(), l.getLatitude(), l.getAltitude());
  }
  
  /**
   * Set the internal location. Used by IP callbacks.
   * 
   * @param mLocation
   */
  protected Location toLoc(IPLocation l)
  {
    return createLoc(l.getLongitude(), l.getLatitude(), l.getAltitude());
  }
  
  /**
   * Create a new location
   * 
   * @param longitude
   * @param latitude
   * @param altitude
   * @return The created location with provider Skyhook
   */
  private Location createLoc(double longitude, double latitude, double altitude)
  {
    Location loc = new Location(PROVIDER);
    loc.setLongitude(longitude);
    loc.setLatitude(latitude);
    loc.setAltitude(altitude);
    return loc;
  }
  
  /**
   * Skyhook callbacks for location updates
   */
  private class RMSkyhookCallback implements IPLocationCallback, WPSLocationCallback, WPSPeriodicLocationCallback
  {
    @Override
    public void handleIPLocation(IPLocation arg0)
    {
      Util.log("Location from ip from skyhook " + arg0.toString());
    }

    @Override
    public WPSContinuation handleWPSPeriodicLocation(WPSLocation location)
    {
      if (location != null)
      {
        Util.log("Location from skyhook " + location.toString());
        // This is where we used to upload the data to the server
        //uploadLocation(toLoc(location));
        _failedConnections = 0;
      }
      else
      {
        Util.log("WPS loc is null, logging as error");
        _failedConnections++;
      }
      return WPSContinuation.WPS_CONTINUE;
    }

    @Override
    public void done()
    {
      Util.log("done");
      new Thread()
      {
        public void run()
        {
          startXPSCallbacks();
        }
      }.start();
    }

    @Override
    public WPSContinuation handleError(WPSReturnCode arg0)
    {
      Util.log("WPS ERROR " + arg0.toString());
      if (++_failedConnections >= SKYHOOK_ERROR_THRESHOLD)
      {
        _failedConnections = 0;
        requestUpdates();
      }
      return WPSContinuation.WPS_CONTINUE;
    }

    @Override
    public void handleWPSLocation(WPSLocation arg0)
    {
      Util.log("Location from WPS from skyhook " + arg0.toString());
    }
  }
}

package com.amphro.receptionmapper;

import android.content.Context;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

/**
 * A class that provides the data of the phone
 *  
 * @author Aleksandr Movsesyan
 *
 */
public class PhoneData
{
  private TelephonyManager _telelphonyManager;
  private PhoneStateListener _phoneStateListener;
  
  private int _signalStrength;
  private int _serviceState;
  
  public PhoneData(Context context)
  {
    _phoneStateListener = new RMPhoneStateListener();
    
    _telelphonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    _telelphonyManager.listen(_phoneStateListener, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE|PhoneStateListener.LISTEN_SERVICE_STATE|PhoneStateListener.LISTEN_SIGNAL_STRENGTH|PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    
    _signalStrength = -1;
  }
  
  public String getCurrentProvider()
  {
    /*
     * Found two ways to get the provider, gets both and then returns the one that is not
     * null. If both null then returns null.
     */
    String networkOp = _telelphonyManager.getNetworkOperatorName();
    Util.log("provider from network op = " + networkOp);

    String simOp = _telelphonyManager.getSimOperatorName();
    Util.log("provider from sim operator = " + simOp);

    String currentProvider = !Util.isEmpy(networkOp) ? networkOp.toUpperCase() : simOp.toUpperCase();

    return Util.isEmpy(currentProvider) ? "UNKNOWN" : currentProvider;
  }
  
  /**
   * Returns the network type after converting it from what
   *   is returned by getNetworkType to an locally created
   *   network type.
   */
  public NetworkType getNetworkType()
  {
    int netType = _telelphonyManager.getNetworkType();
    
    switch(netType)
    {
      case TelephonyManager.NETWORK_TYPE_EDGE:
        return NetworkType.NTEDGE;
      case TelephonyManager.NETWORK_TYPE_GPRS:
        return NetworkType.NT2G;
      case TelephonyManager.NETWORK_TYPE_UMTS:
        return NetworkType.NT3G;
      case TelephonyManager.NETWORK_TYPE_UNKNOWN:
        return NetworkType.NTUKN;
    }
    
    return NetworkType.NTNONE;
  }
  
  /**
   * Returns the phone model from Build.MODEL
   */
  public String getPhone()
  {
    Util.log("model = " + Build.MODEL.toUpperCase());
    return Build.MODEL.toUpperCase();
  }
  
  /**
   * Returns the phone's manufacturer from Build.MANUFACTURER
   */
  public String getManufacturer()
  {
    Util.log("manufacturer = " + Build.MANUFACTURER);
    return Build.MANUFACTURER.toUpperCase();
  }
  
  public boolean hasSim()
  {
    return _telelphonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT;
  }
  
  private class RMPhoneStateListener extends PhoneStateListener
  {
    @Override
    public void onDataConnectionStateChanged(int state, int networkType)
    {
      // TODO Auto-generated method stub
      super.onDataConnectionStateChanged(state, networkType);
    }

    @Override
    public void onDataConnectionStateChanged(int state)
    {
      // TODO Auto-generated method stub
      super.onDataConnectionStateChanged(state);
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState)
    {
      super.onServiceStateChanged(serviceState);
      
      Util.log("Service state changed from " + _serviceState + " to " + serviceState.getState());
     
      _serviceState = serviceState.getState();
    }

    @Override
    public void onSignalStrengthChanged(int asu)
    {
      super.onSignalStrengthChanged(asu);
      
      Util.log("The signal strength changed from " + _signalStrength + " to " + asu);
      
      _signalStrength = (asu < 0 || _serviceState == ServiceState.STATE_OUT_OF_SERVICE) ? 0 : asu;
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength)
    {
      super.onSignalStrengthsChanged(signalStrength);
      
      Util.log("1 New method signal strength has changed from " + _signalStrength + " to " + signalStrength.getCdmaDbm());
      Util.log("2 New method signal strength has changed from " + _signalStrength + " to " + signalStrength.getCdmaEcio());
      Util.log("3 New method signal strength has changed from " + _signalStrength + " to " + signalStrength.getEvdoDbm());
      Util.log("4 New method signal strength has changed from " + _signalStrength + " to " + signalStrength.getEvdoEcio());
      Util.log("5 New method signal strength has changed from " + _signalStrength + " to " + signalStrength.getEvdoSnr());
      Util.log("6 New method signal strength has changed from " + _signalStrength + " to " + signalStrength.getGsmBitErrorRate());      
      Util.log("7 New method signal strength has changed from " + _signalStrength + " to " + signalStrength.getGsmSignalStrength());

      _signalStrength = (signalStrength.getGsmSignalStrength() == 99 || _serviceState == ServiceState.STATE_OUT_OF_SERVICE) ? 0 : signalStrength.getGsmSignalStrength();
    }
    
  }
}

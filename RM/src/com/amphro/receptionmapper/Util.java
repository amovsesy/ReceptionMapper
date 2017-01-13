package com.amphro.receptionmapper;

import android.util.Log;

public class Util
{
  /** The tag for logging debug messages */
  private final static String TAG = "RECEPTIONMAPPERSERVICE";
  
  public static boolean isEmpy(String s)
  {
    return s == null || s.equals("");
  }
  
  /**
   * Log a debug message using the ReceptionMapperService tag
   * @param message The message to display
   */
  public static void log(String message)
  {
    Log.d(TAG, message);
  }
}

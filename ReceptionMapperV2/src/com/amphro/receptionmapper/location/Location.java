package com.amphro.receptionmapper.location;

public class Location {
	public static final int INTERVAL = 15;
	public static final int LAT_RADIUS = 90;
	public static final int LON_RADIUS = 180;
	
	public static int toDeg(double val) {
	   return (int)Math.floor(val);
	}
	public static int toMin(double val) {
	   return (int)Math.floor(toMinF(val));
	}
	public static double toMinF(double val) {
	   return (Math.abs(val) - (Math.floor(Math.abs(val)))) * 60;
	}
	public static int toSec(double val) {
	   return (int)Math.floor(toSecF(val));
	}
	public static double toSecF(double val) {
	   return (toMinF(val) - toMin(val)) * 60;
	}

	public static int toGSS1x(double lon) {
	   return (int)Math.floor((toDeg(lon)+180)/15);
	}
	public static int toGSS1y(double lat) {
	   return (int)Math.floor((toDeg(lat)+90)/15);
	}
	public static int toGSS2x(double lon) {
	   return (toDeg(lon)+180)%15;
	}
	public static int toGSS2y(double lat) {
	   return (toDeg(lat)+90)%15;
	}
	public static int toGSS3x(double val) {
	   return toMin(val);
	}
	public static int toGSS3y(double val) {
	   return toMin(val);
	}
	public static int toGSS4x(double val) {
	   return toSec(val);
	}
	public static int toGSS4y(double val) {
	   return toSec(val);
	}
	public static int toGSS5x(double val) {
	   return (int)Math.floor((toSecF(val)-toSec(val)) * 10);
	}
	public static int toGSS5y(double val) {
	   return (int)Math.floor((toSecF(val)-toSec(val)) * 10);
	}

	public static String toGSS1(double lat, double lon) {
	   return String.format("%02d", toGSS1x(lon))+String.format("%02d", toGSS1y(lat));
	}
	public static String toGSS2(double lat, double lon) {
	   return String.format("%02d", toGSS2x(lon))+String.format("%02d", toGSS2y(lat));
	}
	public static String toGSS3(double lat, double lon) {
	   return String.format("%02d", toMin(lon))+String.format("%02d", toMin(lat));
	}
	public static String toGSS4(double lat, double lon) {
	   return String.format("%02d", toSec(lon))+String.format("%02d", toSec(lat));
	}
	public static String toGSS5(double lat, double lon) {
	   return String.format("%02d", toGSS5x(lon))+String.format("%02d", toGSS5y(lat));
	}

	public static String toGSS(double lat, double lon) {
	   return toGSS1(lat, lon)+toGSS2(lat, lon)+toGSS3(lat, lon)+toGSS4(lat, lon)+toGSS5(lat, lon);
	}

	public static int toGSSDB1x(double lon) {
	  return toGSS1x(lon);
	}
	public static int toGSSDB1y(double lat) {
	  return toGSS1y(lat);
	}
	public static int toGSSDB2x(double lon) {
	  return toGSS1x(lon)*15+toGSS2x(lon);
	}
	public static int toGSSDB2y(double lat) {
	  return toGSS1y(lat)*15+toGSS2y(lat);
	}
	public static int toGSSDB3x(double lon) {
	  return toGSS1x(lon)*15*60+toGSS2x(lon)*60+toGSS3x(lon);
	}
	public static int toGSSDB3y(double lat) {
	  return toGSS1y(lat)*15*60+toGSS2y(lat)*60+toGSS3y(lat);
	}
	public static int toGSSDB4x(double lon) {
	  return
	    toGSS1x(lon)*15*60*60+toGSS2x(lon)*60*60+toGSS3x(lon)*60+toGSS4x(lon);
	}
	public static int toGSSDB4y(double lat) {
	  return
	    toGSS1y(lat)*15*60*60+toGSS2y(lat)*60*60+toGSS3y(lat)*60+toGSS4y(lat);
	}
	public static int toGSSDB5x(double lon) {
	  return
	    toGSS1x(lon)*15*60*60*10+toGSS2x(lon)*60*60*10+toGSS3x(lon)*60*10+toGSS4x(lon)*10+toGSS5x(lon);
	}
	public static int toGSSDB5y(double lat) {
	  return toGSS1y(lat)*12*60*60*10+toGSS2y(lat)*60*60*10+toGSS3y(lat)*60*10+toGSS4y(lat)*10+toGSS5y(lat);
	}
	public static String makeKey(int x, int y) {
		return String.format("%010d", x) + String.format("%010d", y);
	}
}

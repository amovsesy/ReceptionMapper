<?php

class Grid {
	public static function toDeg($val) {
	   return floor($val);
	}
	public static function toMin($val) {
	   return floor(Grid::toMinF($val));
	}
	public static function toMinF($val) {
	   return (abs($val) - (floor(abs($val)))) * 60;
	}
	public static function toSec($val) {
	   return floor(Grid::toSecF($val));
	}
	public static function toSecF($val) {
	   return (Grid::toMinF($val) - Grid::toMin($val)) * 60;
	}

	public static function toGSS1x($lon) {
	   return floor((Grid::toDeg($lon)+180)/15);
	}
	public static function toGSS1y($lat) {
	   return floor((Grid::toDeg($lat)+90)/15);
	}
	public static function toGSS2x($lon) {
	   return (Grid::toDeg($lon)+180)%15;
	}
	public static function toGSS2y($lat) {
	   return (Grid::toDeg($lat)+90)%15;
	}
	public static function toGSS3x($val) {
	   return Grid::toMin($val);
	}
	public static function toGSS3y($val) {
	   return Grid::toMin($val);
	}
	public static function toGSS4x($val) {
	   return Grid::toSec($val);
	}
	public static function toGSS4y($val) {
	   return Grid::toSec($val);
	}
	public static function toGSS5x($val) {
	   return floor((Grid::toSecF($val)-Grid::toSec($val)) * 10);
	}
	public static function toGSS5y($val) {
	   return floor((Grid::toSecF($val)-Grid::toSec($val)) * 10);
	}
/*
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
*/
	public static function toGSSDB1x($lon) {
	  return Grid::toGSS1x($lon);
	}
	public static function toGSSDB1y($lat) {
	  return Grid::toGSS1y($lat);
	}
	public static function toGSSDB2x($lon) {
	  return Grid::toGSS1x($lon)*15+Grid::toGSS2x($lon);
	}
	public static function toGSSDB2y($lat) {
	  return Grid::toGSS1y($lat)*15+Grid::toGSS2y($lat);
	}
	public static function toGSSDB3x($lon) {
	  return Grid::toGSS1x($lon)*15*60+Grid::toGSS2x($lon)*60+Grid::toGSS3x($lon);
	}
	public static function toGSSDB3y($lat) {
	  return Grid::toGSS1y($lat)*15*60+Grid::toGSS2y($lat)*60+Grid::toGSS3y($lat);
	}
	public static function toGSSDB4x($lon) {
	  return
	    Grid::toGSS1x($lon)*15*60*60+Grid::toGSS2x($lon)*60*60+Grid::toGSS3x($lon)*60+Grid::toGSS4x($lon);
	}
	public static function toGSSDB4y($lat) {
	  return
	    Grid::toGSS1y($lat)*15*60*60+Grid::toGSS2y($lat)*60*60+Grid::toGSS3y($lat)*60+Grid::toGSS4y($lat);
	}
	public static function toGSSDB5x($lon) {
	  return
	    Grid::toGSS1x($lon)*15*60*60*10+Grid::toGSS2x($lon)*60*60*10+Grid::toGSS3x($lon)*60*10+Grid::toGSS4x($lon)*10+Grid::toGSS5x($lon);
	}
	public static function toGSSDB5y($lat) {
	  return
      Grid::toGSS1y($lat)*12*60*60*10+Grid::toGSS2y($lat)*60*60*10+Grid::toGSS3y($lat)*60*10+Grid::toGSS4y($lat)*10+Grid::toGSS5y($lat);
	}
  /*
	public static function makeKey(int x, int y) {
		return String.format("%010d", x) + String.format("%010d", y);
	}
  */
}

?>

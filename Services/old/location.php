<?php

function toDeg($val) {
   return floor($val);
}
function toMin($val) {
   return floor(toMinF($val));
}
function toMinF($val) {
   return (abs($val) - (floor(abs($val)))) * 60;
}
function toSec($val) {
   return floor(toSecF($val));
}
function toSecF($val) {
   return (toMinF($val) - toMin($val)) * 60;
}

function toGSS1x($lon) {
   return floor((toDeg($lon)+180)/15);
}
function toGSS1y($lat) {
   return floor((toDeg($lat)+90)/15);
}
function toGSS2x($lon) {
   return (toDeg($lon)+180)%15;
}
function toGSS2y($lat) {
   return (toDeg($lat)+90)%15;
}
function toGSS3x($val) {
   return toMin($val);
}
function toGSS3y($val) {
   return toMin($val);
}
function toGSS4x($val) {
   return toSec($val);
}
function toGSS4y($val) {
   return toSec($val);
}
function toGSS5x($val) {
   return floor((toSecF($val)-toSec($val)) * 10);
}
function toGSS5y($val) {
   return floor((toSecF($val)-toSec($val)) * 10);
}

function toGSS1($lat, $lon) {
   return itos(toGSS1x($lon)).itos(toGSS1y($lat));
}
function toGSS2($lat, $lon) {
   return itos(toGSS2x($lon)).itos(toGSS2y($lat));
}
function toGSS3($lat, $lon) {
   return itos(toMin($lon)).itos(toMin($lat));
}
function toGSS4($lat, $lon) {
   return itos(toSec($lon)).itos(toSec($lat));
}
function toGSS5($lat, $lon) {
   return itos(toGSS5x($lon)).itos(toGSS5y($lat));
}

function toGSS($lat, $lon) {
   return toGSS1($lat, $lon).toGSS2($lat, $lon).toGSS3($lat, $lon).toGSS4($lat, $lon).toGSS5($lat, $lon);
}

function toGSSDB1x($lon) {
  return toGSS1x($lon);
}
function toGSSDB1y($lat) {
  return toGSS1y($lat);
}
function toGSSDB2x($lon) {
  return toGSS1x($lon)*15+toGSS2x($lon);
}
function toGSSDB2y($lat) {
  return toGSS1y($lat)*15+toGSS2y($lat);
}
function toGSSDB3x($lon) {
  return toGSS1x($lon)*15*60+toGSS2x($lon)*60+toGSS3x($lon);
}
function toGSSDB3y($lat) {
  return toGSS1y($lat)*15*60+toGSS2y($lat)*60+toGSS3y($lat);
}
function toGSSDB4x($lon) {
  return
    toGSS1x($lon)*15*60*60+toGSS2x($lon)*60*60+toGSS3x($lon)*60+toGSS4x($lon);
}
function toGSSDB4y($lat) {
  return
    toGSS1y($lat)*15*60*60+toGSS2y($lat)*60*60+toGSS3y($lat)*60+toGSS4y($lat);
}
function toGSSDB5x($lon) {
  return
    toGSS1x($lon)*15*60*60*10+toGSS2x($lon)*60*60*10+toGSS3x($lon)*60*10+toGSS4x($lon)*10+toGSS5x($lon);
}
function toGSSDB5y($lat) {
  return
    toGSS1y($lat)*12*60*60*10+toGSS2y($lat)*60*60*10+toGSS3y($lat)*60*10+toGSS4y($lat)*10+toGSS5y($lat);
}
function itos($val) {
  if ($val < 10) {
     return '0'.$val;
  } else {
     return $val;
  }
}

/*
$lat = $_GET['lat'];
$lon = $_GET['lon'];

echo toGSSDB5x(-120.713400) ." to ". toGSSDB5x(-120.604992) ."<br />"; 
echo toGSSDB5y(35.326700) ." to ". toGSSDB5y(35.23840) ."<br />"; 
*/
/*
$lat = $_GET['lat'];
$lon = $_GET['lon'];

$dlat=toDeg($inlat);
$dlon=toDeg($inlon);

$mlat=toMin($inlat);
$mlon=toMin($inlon);

$slat=toSec($inlat);
$slon=toSec($inlon);

echo "lat = " . $dlat . " lon = " . $dlon . "<br>";
echo "lat = " . $mlat . " lon = " . $mlon . "<br>";
echo "lat = " . $slat . " lon = " . $slon . "<br>";

$g1 = toGSS1($inlat, $inlon);
echo "gss1x = " . toGSS1x($inlon) . " - ";
echo "gss1y = " . toGSS1y($inlat) . " - ";
echo "gss1 = " . $g1 . "<br>";

$g2 = toGSS2($inlat, $inlon);
echo "gss2x = " . toGSS2x($inlon) . " - ";
echo "gss2y = " . toGSS2y($inlat) . " - ";
echo "gss2 = " . $g2 . "<br>";

$g3 = toGSS3($inlat, $inlon);
echo "gss3x = " . toGSS3x($inlon) . " - ";
echo "gss3y = " . toGSS3y($inlat) . " - ";
echo "gss3 = " . $g3 . "<br>";

$g4 = toGSS4($inlat, $inlon);
echo "gss4x = " . toGSS4x($inlon) . " - ";
echo "gss4y = " . toGSS4y($inlat) . " - ";
echo "gss4 = " . $g4 . "<br>";

$g5 = toGSS5($inlat, $inlon);
echo "gss5x = " . toGSS5x($inlon) . " - ";
echo "gss5y = " . toGSS5y($inlat) . " - ";
echo "gss5 = " . $g5 . "<br>";

echo "gss = " . toGSS($inlat, $inlon) . "<br>";
*/

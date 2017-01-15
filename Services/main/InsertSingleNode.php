<?php 

require_once("ReceptionMapperDatabase.php");

function isValidParam($arg, $msg) {
   if (!isset($arg) || $arg =="") {
      die("Invalid argument: " . $msg);
   }
}

$database = new ReceptionMapperDatabase();

$lat=$_GET['latitude'];
isValidParam($lat, "Latitude");

$lon=$_GET['longitude'];
isValidParam($lon, "Longitude");

$phone=$_GET['phone'];
isValidParam($phone, "Phone");

$manufacturer=$_GET['manufacturer'];
isValidParam($manufacturer, "Manufacturer");

$carrier=$_GET['carrier'];
isValidParam($carrier, "Carrier");

$user=$_GET['user'];
isValidParam($user, "User");

$phonetype=$_GET['phonetype'];
isValidParam($phonetype, "Phone Type");
$deviceid=$_GET['deviceid'];
isValidParam($deviceid, "deviceid");
$networkint=$_GET['networkint'];
isValidParam($networkint, "Network Int");

$network = 0;
$network=$_GET['network'];

$signalstrength = 0;
$signalstrength=$_GET['signalstrength'];
$gsm = null;
$gsm=$_GET['gsm'];
$gsmerror = null;
$gsmerror=$_GET['gsmerror'];
$cdma = null;
$cdma=$_GET['cdma'];
$cdmaerror = null;
$cdmaerror=$_GET['cdmaerror'];
$evdo = null;
$evdo=$_GET['evdo'];
$evdoerror = null;
$evdoerror=$_GET['evdoerror'];

$query="INSERT INTO Nodes (";
$query .=
"`latitude`,`longitude`,`network`,`signal-strength`,`carrier`,`phone`,`manufacturer`,`username`,`gsm`,`gsmerror`,`cdma`,`cdmaerror`,`evdo`,`evdoerror`,`phonetype`,`deviceid`,`networkint`) VALUES ('";
$query .= $lat . "','";
$query .= $lon . "','";
$query .= $network . "','";
$query .= $signalstrength . "','";
$query .= $carrier . "','";
$query .= $phone . "','";
$query .= $manufacturer . "','";
$query .= $user . "','";
$query .= $gsm . "','";
$query .= $gsmerror . "','";
$query .= $cdma . "','";
$query .= $cdmaerror . "','";
$query .= $evdo . "','";
$query .= $evdoerror . "','";
$query .= $phonetype . "','";
$query .= $deviceid . "','";
$query .= $networkint . "')";

$result=mysql_query($query);
echo mysql_error();
echo "SUCCEEDED";
mysql_close();
?>


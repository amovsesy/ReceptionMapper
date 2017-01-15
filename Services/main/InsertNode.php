<?php 

require_once("ReceptionMapperDatabase.php");

function isValidParam($arg, $msg) {
   if (!isset($arg) || $arg =="") {
      die("Invalid argument: " . $msg);
   }
}

$database = new ReceptionMapperDatabase();

$list = json_decode($_GET['list']);
$nodes = $list->{'nodes'};

foreach ($nodes as $name=>$node) {
	foreach ($node as $key=>$value) {
		if($key == "latitude") {
			$lat=$value;
			isValidParam($lat, "Latitude");
		} else if($key == "longitude"){
			$lon=$value;
			isValidParam($lon, "Longitude");
		} else if($key == "phone"){
			$phone=$value;
			isValidParam($phone, "Phone");
		} else if($key == "manufacturer"){
			$manufacturer=$value;
			isValidParam($manufacturer, "Manufacturer");
		} else if($key == "carrier"){
			$carrier=$value;
			isValidParam($carrier, "Carrier");
		} else if($key == "user"){
			$user=$value;
			isValidParam($user, "User");
		} else if($key == "phonetype"){
			$phonetype=$value;
			isValidParam($phonetype, "Phone Type");
		} else if($key == "deviceid"){
			$deviceid=$value;
			isValidParam($deviceid, "deviceid");
		} else if($key == "networkint"){
			$networkint=$value;
			isValidParam($networkint, "Network Int");
		} else if($key == "network"){
			$network = 0;
			$network=$value;
		} else if($key == "signalstrength"){
			$signalstrength = 0;
			$signalstrength=$value;
		} else if($key == "gsm"){
			$gsm = null;
			$gsm=$value;
		} else if($key == "gsmerror"){
			$gsmerror = null;
			$gsmerror=$value;
		} else if($key == "cdma"){
			$cdma = null;
			$cdma=$value;
		} else if($key == "cdmaerror"){
			$cdmaerror = null;
			$cdmaerror=$value;
		} else if($key == "evdo"){
			$evdo = null;
			$evdo=$value;
		} else if($key == "evdoerror"){
			$evdoerror = null;
			$evdoerror=$value;
		}
	}
	
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
}

mysql_close();
?>


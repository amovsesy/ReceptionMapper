<?php

//include "rmDatabaseTM.php";
/*
function isValidParam($arg, $msg) {
  if (!isset($arg) || $arg =="") {
    die("Invalid argument: " . $msg);
  }
}
*/

function getManufacturerID($manufacturer){
  $query="SELECT id from Manufacturer where name='";
  $query .= $manufacturer . "'";
  $result=mysql_query($query);
  $num=mysql_numrows($result);
  $id=-1;

  if($num == 0){
    $query="SELECT max(id) from Manufacturer";
    $result=mysql_query($query);
    $num=mysql_result($result,0,"max(id)");
    $id = $num + 1;

    $query="INSERT INTO Manufacturer values(";
    $query .= $id . ", '";
    $query .= $manufacturer . "')";

    if(mysql_query($query)){
      echo "inserting into manufacturer success <br />";
    } else {
      echo "inserting into manufacturer died in a fire <br />";
    }
  } else {
    $id=mysql_result($result,0,"id");
  }

  mysql_free_result($result);
  return $id;
}

function getPhoneID($phone, $manufacturer){
  $manufacid = getManufacturerID($manufacturer);

  $query="SELECT id from Phone WHERE name='";
  $query .= $phone . "'";

  $result=mysql_query($query);
  $num=mysql_numrows($result);

  $id = -1;

  if(!($num == 0)) {
    $id=mysql_result($result,0,"id");
  } else {
    $query="SELECT max(id) from Phone";
    $result=mysql_query($query);
    $num=mysql_result($result,0,"max(id)");
    $id = $num + 1;

    $query="INSERT INTO Phone values(";
    $query .= $id . ", '";
    $query .= $phone . "', ";
    $query .= $manufacid . ")";

    if(mysql_query($query)){
      echo "inserting into phones success <br />";
    } else {
      echo "inserting into phones died in a fire <br />";
    }
  }

  mysql_free_result($result);
  return $id;
}

function mapCarrier($carrier){
  $ret = $carrier;

  if(strpos($carrier, "ATT") || strpos($carrier, "AT&T")){
    $ret = "AT&T";
  } else if (strpos($carrier, "VERIZON")){
    $ret = "VERIZON";
  } else if (strpos($carrier, "SPRINT")){
    $ret = "SPRINT";
  } else if (strpos($carrier, "T") && strpos($carrier, "MOBILE")){
    $ret = "T-MOBILE";
  }

  return $ret;
}

function getCarrierID($carier){
  $carrier = mapCarrier($carier);

  $query="SELECT id from Carrier WHERE name='";
  $query .= $carrier . "'";

  $result=mysql_query($query);
  $num=mysql_numrows($result);

  $id = -1;

  if(!($num == 0)) {
    $id=mysql_result($result,0,"id");
  } else {
    $query="SELECT max(id) from Carrier";
    $result=mysql_query($query);
    $num=mysql_result($result,0,"max(id)");
    $id = $num + 1;

    $query="INSERT INTO Carrier values(";
    $query .= $id . ", '";
    $query .= $carrier . "')";

    if(mysql_query($query)){
      echo "inserting into carrier success <br />";
    } else {
      echo "inserting into carrier died in a fire <br />";
    }
  }

  mysql_free_result($result);
  return $id;
}

function getClientID($phone, $manufacturer, $carrier){
  $carrierID = getCarrierID($carrier);
  $phoneID = getPhoneID($phone, $manufacturer);

  $query="SELECT id from Client WHERE phone=";
  $query .= $phoneID . " AND carrier=";
  $query .= $carrierID;

  $result=mysql_query($query);
  $num=mysql_numrows($result);

  $id = -1;

  if(!($num == 0)) {
    $id=mysql_result($result,0,"id");
  } else {
    $query="SELECT max(id) from Client";
    $result=mysql_query($query);
    $num=mysql_result($result,0,"max(id)");
    $id = $num + 1;

    $query="INSERT INTO Client values(";
    $query .= $id . ", '";
    $query .= $phoneID . "', ";
    $query .= $carrierID . ")";

    if(mysql_query($query)){
      echo "inserting into client success <br />";
    } else {
      echo "inserting into client died in a fire <br />";
    }
  }

  mysql_free_result($result);
  return $id;
}

function mapNetwork($network) {
  switch($network){
    case -1:
      return "NTUKN";
    case 0:
      return "NTNONE";
    case 1:
      return "NTEDGE";
    case 3:
      return "NT2G";
    case 5:
      return "NT3G";
  }
}

/*
$phone=$_GET['phone'];
isValidParam($phone, "Phone");

$manufac=$_GET['manufacturer'];
isValidParam($manufac, "Manufacturer");

$debug=$_GET['debug'];
isValidParam($debug, "Debug");

$carrier=$_GET['carrier'];
isValidParam($carrier, "Carrier");

openDB($debug);

$clientID=getClientID($phone, $manufac, $carrier);

echo $clientID;

mysql_close();
*/
?>

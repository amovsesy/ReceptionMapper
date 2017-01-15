<?php 

include "Database.php";
//include "client.php";
//include "location.php";

class Node {

}

function isValidParam($arg, $msg) {
   if (!isset($arg) || $arg =="") {
      die("Invalid argument: " . $msg);
   }
}

function createGrid($level, $x, $y, $carrier) {
    $query="SELECT max(id) from GridLevel".$level." ";
    $result=mysql_query($query);
    $num=mysql_result($result,0,"max(id)");
    $id = $num + 1;

    $query="INSERT INTO GridLevel".$level." VALUES (";
    $query .= $id . ", ";
    $query .= $x . ", ";
    $query .= $y . ", ";
    $query .= "0, 0, '".$carrier."', ";
    $query .= "0, 0)";
    if(!mysql_query($query)){
      die("inserting into grid level ".$level." died in a fire");
    }
    return $id;
}

function toArray($id, $avgrec, $numrec, $avgnet, $numnet) {
   return array("id" => $id, "avgrec" => $avgrec, "numrec" => $numrec, "avgnet"
       => $avgnet, "numnet" => $numnet);
}

function selectGridRow($x, $y, $level, $carrier) {
  $query="SELECT id, avgrec, numrec, avgnet, numnet from GridLevel".$level." where ";
  $query .= "gss".$level."x=".$x." and ";
  $query .= "gss".$level."y=".$y." and ";
  $query .= "carrier='".$carrier."'";
  return mysql_query($query);
}

function getGridLevel($x, $y, $level, $carrier, $network) {
  $array;
  $results=selectGridRow($x, $y, $level, $carrier);
  $num=mysql_numrows($results);
  if($num == 0){
    $id = createGrid($level, $x, $y, $carrier, $network);
    $array = toArray($id, 0, 0, 0, 0);
  } else {
    $id=mysql_result($results,0,"id");
    $avgrec=mysql_result($results,0,"avgrec");
    $numrec=mysql_result($results,0,"numrec");
    $avgnet=mysql_result($results,0,"avgnet");
    $numnet=mysql_result($results,0,"numnet");
    $array = toArray($id, $avgrec, $numrec, $avgnet, $numnet);
  }
  mysql_free_result($results);
  return $array;
}

function getGrids($lon, $lat, $carrier, $network){
  $id=-1;
  $grids = array();
  
  $grids["level1"] = getGridLevel(toGSSDB1x($lon), toGSSDB1y($lat), 1, $carrier, $network);
  $grids["level2"] = getGridLevel(toGSSDB2x($lon), toGSSDB2y($lat), 2, $carrier, $network);
  $grids["level3"] = getGridLevel(toGSSDB3x($lon), toGSSDB3y($lat), 3, $carrier, $network);
  $grids["level4"] = getGridLevel(toGSSDB4x($lon), toGSSDB4y($lat), 4, $carrier, $network);
  $grids["level5"] = getGridLevel(toGSSDB5x($lon), toGSSDB5y($lat), 5, $carrier, $network);
  return $grids;
}

function avg($new, $avg, $num) {
  return ($avg * $num + $new) / ($num + 1);
}

function updateGrid($id, $rec, $num, $level, $avgnet, $numnet) {
  $query="UPDATE GridLevel".$level." SET avgrec=".$rec.",
    numrec=".$num.", avgnet=".$avgnet.", numnet=".$numnet." where ";
  $query .= "id=".$id;
  if (!mysql_query($query)) 
    die("updated gridlevel".$level." ".$id." failed");
}

$lat=$_GET['latitude'];
isValidParam($lat, "Latitude");

$lon=$_GET['longitude'];
isValidParam($lon, "Longitude");

$manufac=$_GET['manufac'];
isValidParam($manufac, "Manufac");

$phone=$_GET['phone'];
isValidParam($phone, "Phone");

$carrier=$_GET['carrier'];
isValidParam($carrier, "Carrier");

$network = 0;
$network=$_GET['network'];

$signalstrength = 0;
$signalstrength=$_GET['signalstrength'];

//echo toGSS($lat, $lon)."<br>";

openDB(true);

$grids=getGrids($lon, $lat, getCarrierID($carrier), $network);
$level = 1;
foreach ($grids as $key => $l) {
  $avgrec = $l["avgrec"];
  $numrec = $l["numrec"];
  $avgnet = $l["avgnet"];
  $numnet = $l["numnet"];
  updateGrid($l["id"], avg($signalstrength, $avgrec, $numrec), $numrec + 1, $level,
      avg($network, $avgnet, $numnet), $numnet + 1);
  $level++;
}

$clientID=getClientID($phone, $manufac, $carrier);
$query="INSERT INTO Nodes (";
$query .= "`latitude`,`longitude`,`client`,`network`,`signal-strength`) VALUES ('";
$query .= $lat . "','";
$query .= $lon . "',";
$query .= $clientID . ",'";
$query .= $network . "','";
$query .= $signalstrength . "')";

$result=mysql_query($query);
echo "SUCCEEDED";
mysql_close();

?>

<?php 
include "rmDatabaseTM.php";
include "location.php";
openDB(true);

$ullat=$_GET['ullat'];
$ullon=$_GET['ullon'];
$brlat=$_GET['brlat'];
$brlon=$_GET['brlon'];
$level=$_GET['level'];

$ullx=0;
$ully=0;
$brlx=0;
$brly=0;

$innerQuery="";
if ($level == 1) {
  $ullx=toGSSDB1x($ullon);
  $ully=toGSSDB1y($ullat);
  $brlx=toGSSDB1x($brlon);
  $brly=toGSSDB1y($brlat);
} else if ($level == 2) {
  $ullx=toGSSDB2x($ullon);
  $ully=toGSSDB2y($ullat);
  $brlx=toGSSDB2x($brlon);
  $brly=toGSSDB2y($brlat);
} else if ($level == 3) {
  $ullx=toGSSDB3x($ullon);
  $ully=toGSSDB3y($ullat);
  $brlx=toGSSDB3x($brlon);
  $brly=toGSSDB3y($brlat);
} else if ($level == 4) {
  $ullx=toGSSDB4x($ullon);
  $ully=toGSSDB4y($ullat);
  $brlx=toGSSDB4x($brlon);
  $brly=toGSSDB4y($brlat);
} else if ($level == 5) {
  $ullx=toGSSDB5x($ullon);
  $ully=toGSSDB5y($ullat);
  $brlx=toGSSDB5x($brlon);
  $brly=toGSSDB5y($brlat);
}

function getSelectQuery($level, $ullx, $ully, $brlx, $brly) {
return "SELECT * FROM GridLevel".$level." WHERE gss".$level."x BETWEEN
    ".$ullx." AND ".$brlx." AND gss".$level."y BETWEEN ".$ully." AND ".$brly;
}
$query .= getSelectQuery($level, $ullx, $ully, $brlx, $brly);

$result=mysql_query($query);

$num=mysql_numrows($result);
mysql_close();

$i=0;
while ($i < $num) {
  echo mysql_result($result,$i,"gss".$level."x");
  echo ",";
  echo mysql_result($result,$i,"gss".$level."y");
  echo ",";
  echo mysql_result($result,$i,"avgrec");
  echo "\n";
  $i++;
}
?>



<?php 
include 'rmDatabaseTM.php';

$querySelect="SELECT * FROM testNodes";
$result=mysql_query($querySelect);

$num=mysql_numrows($result);
mysql_close();


echo "<b>Test Nodes Output</b><br><br>";

$i=$num - 1;
while ($i >= 0) {

$id=mysql_result($result,$i,"id");
$latitude=mysql_result($result,$i,"latitude");
$longitude=mysql_result($result,$i,"longitude");
$provider=mysql_result($result,$i,"provider");
$network=mysql_result($result,$i,"network");
$signalstrength=mysql_result($result,$i,"signal-strength");
$model=mysql_result($result,$i,"model");
$manufacturer=mysql_result($result,$i,"manufacturer");
$time=mysql_result($result,$i,"time");

echo "<b>$id</b><br>";
echo "Latitude: <b>$latitude</b>   -   Longitude: <b>$longitude</b><br>";
echo "Provider <b>$provider</b> on <b>$network</b> network with strength <b>$signalstrength</b>";
echo "<br>Uploaded at: <b>$time</b> from a <b>$model</b> made by <b>$manufacturer</b><br><hr><br>";

$i--;
}
?>



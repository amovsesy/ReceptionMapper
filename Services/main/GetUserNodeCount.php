<?php 
require_once("ReceptionMapperDatabase.php");

$database = new ReceptionMapperDatabase();

$username=$_GET['username'];
 
$count = mysql_fetch_assoc($database->getNodeCount(array('username'=>"'".$username."'")));

echo $count["COUNT(*)"];
?>

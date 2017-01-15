<?php 

function openDB($debug) {
  //Fill in this information
  $server="";
  $user="";
  $password="";
  $database=$debug?"receptionmappertest":"receptionmapper";
  mysql_connect($server,$user,$password);
  @mysql_select_db($database) or die("Unable to select database");
}
?>

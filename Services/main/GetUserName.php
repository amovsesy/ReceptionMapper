<?php 
require_once("ReceptionMapperDatabase.php");

$database = new ReceptionMapperDatabase();

$phoneid=$_GET['phoneid'];
 
$rows =  $database->selectUsers(array('phoneID'=>"'".$phoneid."'"));

echo $rows[0]['username'];
?>

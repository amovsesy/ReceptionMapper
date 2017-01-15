<?php 
//This is just for testing. Will be remove when all unit test are complete
//
require_once("ReceptionMapperDatabase.php");

try {
$db = new ReceptionMapperDatabase();
$result = $db->query("select * from User");
while($row = mysql_fetch_array($result))
    {
        echo $row['username'] . " " . $row['phoneID'];
          echo "<br />";
            }
} catch(DatabaseException $e) {
echo $e;
}
?>

<?php

require_once("Grid.php");
require_once("ReceptionMapperDatabase.php");

$database = new ReceptionMapperDatabase();

function networkToInt($netname) {
  //2g
  if (strcasecmp('gpsr', $netname) == 0) {
    return 3;
  }
  //edge
  else if (strcasecmp('edge', $netname) == 0) {
    return 1;
  }
  //3g
  else if (strcasecmp('utms', $netname) == 0) {
    return 5;
  }
  //3g
  else if (strcasecmp('hsdpa', $netname) == 0) {
    return 5;
  }
  //3g
  else if (strcasecmp('hsupa', $netname) == 0) {
    return 5;
  }
  //3g
  else if (strcasecmp('hspa', $netname) == 0) {
    return 5;
  }
  //???
  else if (strcasecmp('cdma', $netname) == 0) {
    return 5;
  }
  //???
  else if (strcasecmp('evdo_0', $netname) == 0) {
    return 3;
  }
  //???
  else if (strcasecmp('evdo_A', $netname) == 0) {
    return 5;
  }
  else {
    return 0;
  }
}
function getRec($node) {
  $gsm = $node['gsm'];
  $cdma = $node['cdma'];
  $evdo = $node['evdo'];
  
  if ($gsm != 99) {
    return 100*($gsm/31); 
  }
  else {
    return 100*((($cdma+$evdo)/2+120)/70); 
  }
}
function convertEnterNodeInGridLevel($database, $level, $x, $y, $carID, $node) {
  $grid = $database->selectGSS($level, array('gss'.$level.'x' => $x,'gss'.$level.'y' => $y, 'carrier' => $carID));
  //echo $level . "\n";
  if (empty($grid)) {
    $database->insertGSS($level, array(
         'gss'.$level.'x' => $x, 
         'gss'.$level.'y' => $y, 
         'avgrec' => getRec($node),
         'numuploads' => 1,
         'carrier' => $carID,
         'network' => networkToInt($node['network'])));
  } else {
    $numup = $grid[0]['numuploads'] + 1;
    
    if ($numup > 300) {
      $numup = 100;
    }
    //echo "Reception: ".getRec($node)."\n";
    //echo "Total Num: ".$numup."\n\n";
    $database->updateGSS($level, array(
         'gss'.$level.'x' => $x, 
         'gss'.$level.'y' => $y, 
         'carrier' => $carID), array(
         'avgrec' => (getRec($node)*($numup-1) + $grid[0]['avgrec'])/($numup),
         'numuploads' => $numup,
         'network' => (networkToInt($node['network'])*($numup-1) + $grid[0]['network'])/($numup)));
  }
}
$i = 0;
foreach($database->selectNodes(array()) as $node) {
  if ($i++ % 1000 == 0) {
    echo $i.' - ';
  }

  if (!empty($node['carrier'])) {
    $lon = $node['longitude'];
    $lat = $node['latitude'];

    /* Get the carrier id if it exist, else create it */
    $carID = $database->selectCarriers(array("name" => "'".$node['carrier']."'"));

    if (empty($carID)) {
      //echo "inserting ". $node['carrier'] ."\n";
      $carID = $database->insertCarrier(array("name" => "'".$node['carrier']."'"));
      $carID = $database->selectCarriers(array("name" => "'".$node['carrier']."'"));
    }

    $carID = $carID[0]['id'];

    /*****/

    $GSS1x =  Grid::toGSSDB1x($lon);
    $GSS1y =  Grid::toGSSDB1y($lat);
    convertEnterNodeInGridLevel($database, 1, $GSS1x, $GSS1y, $carID, $node);
    $GSS2x =  Grid::toGSSDB2x($lon);
    $GSS2y =  Grid::toGSSDB2y($lat);
    convertEnterNodeInGridLevel($database, 2, $GSS2x, $GSS2y, $carID, $node);
    $GSS3x =  Grid::toGSSDB3x($lon);
    $GSS3y =  Grid::toGSSDB3y($lat);
    convertEnterNodeInGridLevel($database, 3, $GSS3x, $GSS3y, $carID, $node);
    $GSS4x =  Grid::toGSSDB4x($lon);
    $GSS4y =  Grid::toGSSDB4y($lat);
    convertEnterNodeInGridLevel($database, 4, $GSS4x, $GSS4y, $carID, $node);
    $GSS5x =  Grid::toGSSDB5x($lon);
    $GSS5y =  Grid::toGSSDB5y($lat);
    convertEnterNodeInGridLevel($database, 5, $GSS5x, $GSS5y, $carID, $node);

/*      
$grid = $database->selectGSS(1, array('gss1x' => $GSS1x, 'gss1y' => $GSS1y, 'carrier' => $carID));

if (empty($grid)) {
   $database->insertGSS(1, array(
         'gss1x' => $GSS1x, 
         'gss1y' => $GSS1y, 
         'avgrec' => getRec($node),
         'numuploads' => 1,
         'carrier' => $carID,
         'network' => networkToInt($node['network'])));
} else {
   $numup = $grid[0]['numuploads'] + 1;

   //echo "Reception: ".getRec($node)."\n";
   //echo "Total Num: ".$numup."\n\n";
   $database->updateGSS(1, array(
         'gss1x' => $GSS1x, 
         'gss1y' => $GSS1y, 
         'carrier' => $carID), array(
         'avgrec' => (getRec($node)*$numup + $grid[0]['avgrec'])/($numup),
         'numuploads' => $numup,
         'network' => (networkToInt($node['network'])*$numup + $grid[0]['network'])/($numup)));
}
*/
/*

echo "GSS 1x: " . Grid::toGSSDB1x($lon) . "\n";
echo "GSS 2x: " . Grid::toGSSDB2x($lon) . "\n";
echo "GSS 3x: " . Grid::toGSSDB3x($lon) . "\n";
echo "GSS 4x: " . Grid::toGSSDB4x($lon) . "\n";
echo "GSS 5x: " . Grid::toGSSDB5x($lon) . "\n\n";

echo "GSS 1y: " . Grid::toGSSDB1y($lat) . "\n";
echo "GSS 2y: " . Grid::toGSSDB2y($lat) . "\n";
echo "GSS 3y: " . Grid::toGSSDB3y($lat) . "\n";
echo "GSS 4y: " . Grid::toGSSDB4y($lat) . "\n";
echo "GSS 5y: " . Grid::toGSSDB5y($lat) . "\n\n";
*/
}
}
?>

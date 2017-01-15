<?php
$image=$_GET['image'];
$carriers=$_GET['carriers'];
$hasGrid=$_GET['grid'];

$list = spliti(",",$carriers);
$images = array();
$j = 0;

if($hasGrid == "true"){
  $images[$j++] = "../maps/grid/grid.png";
}

for($i=0;$i<count($list);$i++){
  if($image=="all"){
    $images[$j++] = "../maps/".$list[$i]."/2g.png";
    $images[$j++] = "../maps/".$list[$i]."/3g.png";
    $images[$j++] = "../maps/".$list[$i]."/edge.png";
  }else{
    $images[$j++] = "../maps/".$list[$i]."/".$image.".png";
  }
}

if(count($images)==1){
  $dest = imagecreatefrompng($images[0]);
  imagealphablending($dest, true); // setting alpha blending on
  imagesavealpha($dest, true);
  header("Content-type: image/png");
  imagepng($dest);
  imagedestroy($dest);
}else{
  $dest = imagecreatefrompng($images[0]);
  imagealphablending($dest, true); // setting alpha blending on
  imagesavealpha($dest, true);

  for($i=1; $i < count($images); $i++){
    $src = imagecreatefrompng($images[$i]);
    imagealphablending($src, true); // setting alpha blending on
    imagesavealpha($src, true);
    imagecopy($dest, $src, 0, 0, 0, 0, 640, 640);
    imagedestroy($src);
  }

  header("Content-type: image/png");
  imagepng($dest);
  imagedestroy($dest);
}
?>

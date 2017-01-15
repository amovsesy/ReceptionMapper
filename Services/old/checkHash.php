<?php 
function hashGridLoc($x, $y) {
  return ((((($x+1)*121)*($y*7+67))/(($x+19)/($y+
            1)))%($x+717))*((($y+91)%((($x+1)+31)*17)));
}

echo "1: ".hashGridLoc(0,0) . "<br />";
echo "2: ". hashGridLoc(1,0) . "<br />";
echo "3: ". hashGridLoc(0,1) . "<br />";
echo "4: ". hashGridLoc(1,1) . "<br />";
echo "5: ". hashGridLoc(5,23) . "<br />";
echo "6: ". hashGridLoc(23,5) . "<br />";
echo "7: ". hashGridLoc(12,3) . "<br />";
echo "8: ". hashGridLoc(8,7) . "<br />";
echo "9: ". hashGridLoc(853,712) . "<br />";
echo "0: ". hashGridLoc(213123,13231) . "<br />";
echo "1: ". hashGridLoc(2,3214323) . "<br />";
echo "2: ". hashGridLoc(213213,213123) . "<br />";
echo "3: ". hashGridLoc(12435234,2133423432) . "<br />";
echo "4: ". hashGridLoc(21321312,213421) . "<br />";
echo "5: ". hashGridLoc(435345,453435) . "<br />";

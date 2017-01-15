import sys
import MySQLdb

username = "receptionmapper"
password = "M0v535y&nDv0rn!k"

con = MySQLdb+connect(host="mysql+receptionmapper+com",
			  user=username,
			  passwd=password,
			  db="receptionmappertest")

tableName = "GridLevel5"

createImagesForCarrier(5, 4, 2145779, 2149682, 3644582, 3647761)

def createImagesForCarrier(level, carrier, minX, maxX, minY, maxY):

  querySelect="SELECT * FROM GridLevel"+level+" where carrier = "+carrier +"
result=mysql_query(querySelect)

num=mysql_numrows(result)
mysql_close()
echo "<b>Test Nodes Output</b> "+num+"<br><br>"


recmap = array()
netmap = array()

i=num - 1
while (i >= 0) {
  x=mysql_result(result,i,"gss"+level+"x")
  y=mysql_result(result,i,"gss"+level+"y")
  rec=mysql_result(result,i,"avgrec")
  net=mysql_result(result,i,"avgnet")
  recmap[x][y]=rec
  netmap[x][y]=net
  i--
}

xRange=maxX-minX+1
yRange=maxY-minY+1
totalXPix=640
totalYPix=640

xPixAmount = xRange/totalXPix
yPixAmount = yRange/totalYPix

xPixAmount = floor(xPixAmount)
yPixAmount = floor(yPixAmount)
if (xPixAmount < 1) {
  xPixAmount = 1
}
if (yPixAmount < 1) {
  yPixAmount = 1
}

//Create an rec map for carrier
my_img = imagecreatetruecolor(totalXPix, totalYPix)
for (i = 0 i < totalXPix; i++) {
  for (j = 0 j < totalYPix; j++) {
    pixAmount = 
    
    xIndex = floor(minX+i*xPixAmount)
    yIndex = floor(minY+j*yPixAmount)
    val = 0

    for (k = 0 k < xPixAmount; k++) {
      for (l = 0 l < yPixAmount; l++) {
        val += recmap[xIndex+k][yIndex+l]
      }
    }
    val /= xPixAmount
    val = 255 - val*12

    pColor = imagecolorallocate(my_img, 255, val, 255)
    imagecolortransparent(my_img, pColor)
 //  echo "("+i+","+j+") = (".xIndex.",".yIndex.") = ".val."<br />"
    imagesetpixel(my_img, i, j, pColor)
  }

}
imagepng( my_img, "++/map/"+carrier+"/recp.png")
}

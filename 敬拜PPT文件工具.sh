#!/bin/sh

echo "---------------------------------------------------"
echo ""
echo "        一个用于自动制作主日崇拜PPT的小工具"
echo "                马克约瑟弟兄出品"
echo ""
echo "---------------------------------------------------"

workDir=$0
cd ${workDir%/*}
chmod u+w .
chmod u+x ./lib/jre/Contents/Home/bin/java
./lib/jre/Contents/Home/bin/java -DconfigFile=config/worship-ppt.properties -Dfile.encoding=UTF-8 -jar ./lib/worship-ppt.jar

echo "辛苦了 φ(゜▽゜*)?"
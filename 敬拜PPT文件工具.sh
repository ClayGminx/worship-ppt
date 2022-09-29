#!/bin/sh

echo "---------------------------------------------------"
echo ""
echo "        一个用于自动制作主日崇拜PPT的小工具"
echo "                马克约瑟弟兄出品"
echo ""
echo "---------------------------------------------------"

./jre/Contents/Home/bin/java -DconfigFile=配置/worship-ppt.properties -Dfile.encoding=UTF-8 -jar worship-ppt.jar

echo "辛苦了 φ(゜▽゜*)?"
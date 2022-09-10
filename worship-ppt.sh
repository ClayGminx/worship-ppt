#!/bin/sh

echo "---------------------------------------------------"
echo ""
echo "        一个用于自动制作主日崇拜PPT的小工具"
echo "                马克约瑟弟兄出品"
echo ""
echo "---------------------------------------------------"

echo ""
echo "正检查升级，请稍等..."
./jre/Contents/Home/bin/java -Dfile.encoding=UTF-8 -Drunning.scene=升级 -jar worship-ppt.jar

read -p "请输入带有敬拜数据的ini文件的路径：" file_path

./jre/Contents/Home/bin/java -Dfile.encoding=UTF-8 -Drunning.scene=PPT -jar worship-ppt.jar $file_path

echo "辛苦了 φ(゜▽゜*)?"
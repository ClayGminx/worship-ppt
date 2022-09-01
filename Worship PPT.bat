@ECHO OFF

REM 窗口
title Worship PPT Toolkit
color 0F

REM 介绍信息
echo ---------------------------------------
echo   一个用于自动制作主日崇拜PPT的小工具
echo           马克约瑟弟兄出品
echo ---------------------------------------

REM 先检查升级
echo 正检查升级，请稍等...
"C:\Program Files (x86)\Java\jre1.8.0_341\bin\java.exe" -jar -Drunning.scene=升级 worship-ppt.jar

REM PPT
set /p file_path=请输入带有敬拜数据的ini文件的路径：
"C:\Program Files (x86)\Java\jre1.8.0_341\bin\java.exe" -jar -Drunning.scene=PPT worship-ppt.jar "%file_path%"

echo 辛苦了 φ(゜▽゜*)? & pause>nul

@ECHO OFF

REM 窗口
title Worship PPT Toolkit
color 0F

REM 介绍信息
echo ---------------------------------------------------
echo.
echo         一个用于自动制作主日崇拜PPT的小工具
echo                  马克约瑟弟兄出品
echo.
echo ---------------------------------------------------
echo.

REM 先检查升级
echo 正检查升级，请稍等...
"jre\bin\java.exe" -Dfile.encoding=GBK -Drunning.scene=升级 -jar worship-ppt.jar

REM PPT
set /p file_path=请输入带有敬拜数据的ini文件的路径：
"jre\bin\java.exe" -Dfile.encoding=GBK -Drunning.scene=PPT -jar worship-ppt.jar "%file_path%"

echo 辛苦了 φ(゜▽゜*)? & pause>nul

@ECHO OFF

REM ����
title Worship PPT Toolkit
color 0F

REM ������Ϣ
echo ---------------------------------------------------
echo.
echo         һ�������Զ��������ճ��PPT��С����
echo                  ���Լɪ���ֳ�Ʒ
echo.
echo ---------------------------------------------------
echo.

REM �ȼ������
echo ��������������Ե�...
"jre\bin\java.exe" -Dfile.encoding=GBK -Drunning.scene=���� -jar worship-ppt.jar

REM PPT
set /p file_path=��������о������ݵ�ini�ļ���·����
"jre\bin\java.exe" -Dfile.encoding=GBK -Drunning.scene=PPT -jar worship-ppt.jar "%file_path%"

echo ������ ��(�b���b*)? & pause>nul

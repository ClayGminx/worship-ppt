@ECHO OFF

REM ����
title Worship PPT Toolkit
color 0F

REM ������Ϣ
echo ---------------------------------------
echo   һ�������Զ��������ճ��PPT��С����
echo           ���Լɪ���ֳ�Ʒ
echo ---------------------------------------

REM �ȼ������
echo ��������������Ե�...
"C:\Program Files (x86)\Java\jre1.8.0_341\bin\java.exe" -jar -Drunning.scene=���� worship-ppt.jar

REM PPT
set /p file_path=��������о������ݵ�ini�ļ���·����
"C:\Program Files (x86)\Java\jre1.8.0_341\bin\java.exe" -jar -Drunning.scene=PPT worship-ppt.jar "%file_path%"

echo ������ ��(�b���b*)? & pause>nul

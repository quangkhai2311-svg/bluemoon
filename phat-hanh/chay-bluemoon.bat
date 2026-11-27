@echo off
chcp 65001 >nul
cd /d "%~dp0"
where java >nul 2>&1 || (echo Chua cai Java. Cai JDK 21 tro len: https://adoptium.net & pause & exit /b 1)
echo Dang chay BlueMoon... Mo trinh duyet tai http://localhost:8080
echo Dong cua so nay de tat phan mem.
java -jar bluemoon.jar %*
pause

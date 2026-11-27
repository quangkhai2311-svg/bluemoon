@echo off
chcp 65001 >nul
cd /d "%~dp0"
where java >nul 2>&1 || (echo Chua cai Java. Cai JDK 21 tro len: https://adoptium.net & pause & exit /b 1)
if "%DB_USER%"=="" set DB_USER=root
set /p DB_PASSWORD=Mat khau MySQL cua %DB_USER%: 
java -jar bluemoon.jar --spring.profiles.active=mysql %*
pause

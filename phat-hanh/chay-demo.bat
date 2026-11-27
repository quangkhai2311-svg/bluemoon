@echo off
chcp 65001 >nul
cd /d "%~dp0"
where java >nul 2>&1 || (echo Chua cai Java. Cai JDK 21 tro len: https://adoptium.net & pause & exit /b 1)
echo Ban DEMO: du lieu gia lap luu rieng trong thu muc data\demo
echo Tai khoan demo: admin / admin12345, canbo / xem / thuquy mat khau matkhau123
java -jar bluemoon.jar --spring.profiles.active=demo %*
pause

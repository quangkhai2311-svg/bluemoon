#!/bin/sh
# Đóng gói bản phát hành: bluemoon-<phiên bản>.zip gồm jar, các file chạy trên Windows và hướng dẫn cài đặt.
set -e
cd "$(dirname "$0")/.."
mvn -q -B clean package
PB=$(mvn -q -B help:evaluate -Dexpression=project.version -DforceStdout)
OUT=target/bluemoon-$PB
mkdir -p "$OUT"
cp "target/bluemoon-$PB.jar" "$OUT/bluemoon.jar"
cp phat-hanh/*.bat HUONG_DAN_CAI_DAT.md "$OUT/"
(cd target && zip -qr "bluemoon-$PB.zip" "bluemoon-$PB")
echo "target/bluemoon-$PB.zip"

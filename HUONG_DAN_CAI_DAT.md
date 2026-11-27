# Hướng dẫn cài đặt BlueMoon (Windows)

## 1. Cài Java

BlueMoon cần **Java 21 trở lên**. Tải bản JDK 21 (Windows x64, file `.msi`) tại https://adoptium.net và cài với các lựa chọn mặc định.
Kiểm tra: mở Command Prompt, gõ `java -version`, thấy dòng `version "21...` trở lên là được.

## 2. Giải nén bản phát hành

Giải nén `bluemoon-<phiên bản>.zip` vào một thư mục cố định, ví dụ `D:\BlueMoon`. Thư mục gồm:

| File | Dùng để |
|---|---|
| `bluemoon.jar` | Phần mềm |
| `chay-bluemoon.bat` | Chạy bản dùng thật, dữ liệu lưu trong thư mục `data` cạnh file jar |
| `chay-demo.bat` | Chạy bản demo có sẵn dữ liệu giả lập, lưu riêng trong `data\demo` |
| `chay-mysql.bat` | Chạy với CSDL MySQL (mục 4) |

## 3. Chạy

Nhấp đúp `chay-bluemoon.bat` (hoặc `chay-demo.bat`), đợi cửa sổ đen hiện dòng `Started BlueMoonApplication`, rồi mở trình duyệt tại **http://localhost:8080**.
Đóng cửa sổ đen để tắt phần mềm.

Tài khoản quản trị tạo ở lần chạy đầu: `admin` / `admin12345`. **Đăng nhập rồi tạo tài khoản cho từng thành viên BQT; không dùng chung tài khoản admin.**
Muốn đặt mật khẩu admin khác ngay từ đầu: trước lần chạy đầu, chạy lệnh `setx BLUEMOON_ADMIN_PASSWORD "mật-khẩu-mới"` rồi mở lại cửa sổ.

Bản demo có thêm các tài khoản `canbo` (cán bộ cư dân), `xem` (chỉ xem), `thuquy` (thủ quỹ), mật khẩu `matkhau123`.

## 4. Dùng MySQL (khuyến nghị khi dùng thật)

1. Cài MySQL Server 8 trở lên, ghi nhớ mật khẩu tài khoản `root` (hoặc tạo tài khoản riêng).
2. Nhấp đúp `chay-mysql.bat`, nhập mật khẩu MySQL khi được hỏi. CSDL `bluemoon` được tạo tự động.
3. Khác máy hoặc khác cổng: đặt biến `DB_HOST`, `DB_PORT`, `DB_USER` trước khi chạy, ví dụ `set DB_PORT=3307`.

## 5. Sao lưu

- Dùng H2 (mặc định): tắt phần mềm, chép nguyên thư mục `data` sang nơi khác.
- Dùng MySQL: `mysqldump -u root -p bluemoon > bluemoon-ngay.sql`.

## 6. Lỗi thường gặp

| Hiện tượng | Cách xử lý |
|---|---|
| `Chua cai Java` hoặc `'java' is not recognized` | Cài Java (mục 1), mở lại cửa sổ |
| `Port 8080 was already in use` | Đã có một BlueMoon đang chạy; đóng cửa sổ cũ. Hoặc chạy `java -jar bluemoon.jar --server.port=8081` |
| Quên mật khẩu admin | Quản trị viên khác đăng nhập và tạo tài khoản mới; nếu chỉ có một admin, liên hệ nhóm phát triển |
| `Access denied for user` khi dùng MySQL | Sai mật khẩu MySQL; chạy lại và nhập đúng |

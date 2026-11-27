# BlueMoon – Phần mềm quản lý và thu phí chung cư

Bài tập lớn môn IT3180 – Nhập môn Công nghệ phần mềm, ĐHBK Hà Nội.

Phiên bản hiện tại: **Sprint 1 (16/11 – 27/11/2026)**: đăng nhập, tài khoản và phân quyền, căn hộ, hộ, nhân khẩu, chuyển đến, chuyển đi.

## Công nghệ

- Java 21, Spring Boot 4 (Web MVC, Security, Data JPA), Thymeleaf
- CSDL: H2 lưu ra file khi chạy thử, MySQL khi triển khai
- Kiểm thử: JUnit 5, Spring Security Test (MockMvc); GitHub Actions chạy `mvn test` mỗi lần push và mỗi Pull Request

## Chạy thử

Cần JDK 21 trở lên và Maven 3.9 trở lên (hoặc mở project bằng IntelliJ IDEA).

```bash
mvn spring-boot:run
```

Mở http://localhost:8080. Lần chạy đầu, hệ thống tự tạo tài khoản quản trị `admin`, mật khẩu `admin12345`.
Đặt biến môi trường `BLUEMOON_ADMIN_PASSWORD` trước lần chạy đầu để dùng mật khẩu khác. Dữ liệu chạy thử nằm trong thư mục `data/`; xóa thư mục này để làm lại từ đầu.

### Chạy bản demo (có dữ liệu giả lập)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

Nạp 12 căn hộ, 6 hộ, 14 nhân khẩu và các tài khoản `canbo` (cán bộ cư dân), `xem` (chỉ xem), `thuquy` (thủ quỹ), mật khẩu `matkhau123`. CSDL demo lưu riêng ở `data/demo`.

### Cài trên máy Windows của BQT

Xem [HUONG_DAN_CAI_DAT.md](HUONG_DAN_CAI_DAT.md). Gói cài đặt (jar và các file `.bat`) được tạo bằng `phat-hanh/dong-goi.sh` và đính kèm ở mục Releases của kho.

### Chạy với MySQL

```bash
DB_USER=root DB_PASSWORD=... mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

CSDL `bluemoon` được tạo tự động nếu chưa có (cấu hình trong `application-mysql.properties`).

### Kiểm thử

```bash
mvn test
```

## Vai trò và quyền

| Vai trò | Xem cư dân | Sửa cư dân | Quản trị tài khoản |
|---|---|---|---|
| Quản trị viên | ✓ | ✓ | ✓ |
| Cán bộ cư dân | ✓ | ✓ | |
| Chỉ xem | ✓ | | |
| Thủ quỹ | (quyền thu phí bổ sung ở Sprint 2) | | |

## User Story trong Sprint 1 và test tương ứng

| User Story | Chức năng | Test |
|---|---|---|
| US-001 | Danh mục căn hộ kèm diện tích | `CuDanTest.US001_CanHo` |
| US-002 | Thêm hộ gắn căn hộ và người đại diện | `CuDanTest.US002_Ho` |
| US-004 | Thêm nhân khẩu vào hộ | `CuDanTest.US004_NhanKhau` |
| US-005 | Ghi chuyển đến, chuyển đi, lịch sử biến động | `CuDanTest.US005_BienDong` |
| US-016 | Đăng nhập | `BaoMatTest.US016_DangNhap` |
| US-017 | Đăng ký tài khoản, phân quyền, khóa tài khoản | `BaoMatTest.US017_TaiKhoanPhanQuyen` |

Mỗi test được đặt tên theo tiêu chí chấp nhận, ví dụ `AC2_tuChoiMaTrung`. Ngoài AC còn có test bổ sung: ngày biến động không được trước biến động gần nhất, trang báo không có quyền, dữ liệu demo nạp được.
Riêng US-001 AC3 ("kỳ đã lập không đổi") được kiểm thử đầy đủ ở Sprint 2, khi có kỳ thu (US-009).

## Cấu trúc mã nguồn

```
src/main/java/vn/edu/hust/bluemoon/
  cauhinh/   cấu hình bảo mật, đồng hồ hệ thống
  chung/     kiểm tra dữ liệu, lỗi nghiệp vụ, trang chung
  cudan/     căn hộ, hộ, nhân khẩu, biến động (E1)
  taikhoan/  tài khoản, vai trò, quyền, đăng nhập (E5)
src/main/resources/templates/   giao diện Thymeleaf
src/test/java/...               test theo AC
```

## Quy ước làm việc

- Mỗi task làm trên một nhánh riêng `sprint1/<mã-task>-<mô-tả-ngắn>`, mở Pull Request vào `main`; cần ít nhất một người review.
- Commit message ghi mã User Story, ví dụ `US-004: kiểm tra ngày sinh không tồn tại`.
- Definition of Done: code đã review, test của các AC chạy qua, giao diện kiểm tra trên trình duyệt, cập nhật trạng thái task trong Sprint Backlog.

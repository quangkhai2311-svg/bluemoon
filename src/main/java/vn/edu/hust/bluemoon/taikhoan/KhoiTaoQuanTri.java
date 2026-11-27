package vn.edu.hust.bluemoon.taikhoan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Lần chạy đầu (chưa có tài khoản nào) tạo một tài khoản quản trị để đăng nhập và cấp tài khoản cho người khác. */
@Component
@Order(1)
public class KhoiTaoQuanTri implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(KhoiTaoQuanTri.class);

    private final TaiKhoanRepository taiKhoanRepo;
    private final PasswordEncoder encoder;
    private final String tenDangNhap;
    private final String matKhau;

    public KhoiTaoQuanTri(TaiKhoanRepository taiKhoanRepo, PasswordEncoder encoder,
                          @Value("${bluemoon.quan-tri.ten-dang-nhap}") String tenDangNhap,
                          @Value("${bluemoon.quan-tri.mat-khau}") String matKhau) {
        this.taiKhoanRepo = taiKhoanRepo;
        this.encoder = encoder;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (taiKhoanRepo.count() > 0) return;
        taiKhoanRepo.save(new TaiKhoan(tenDangNhap, encoder.encode(matKhau), "Quản trị viên", VaiTro.QUAN_TRI));
        log.warn("Đã tạo tài khoản quản trị '{}'. Hãy đổi mật khẩu mặc định trước khi bàn giao.", tenDangNhap);
    }
}

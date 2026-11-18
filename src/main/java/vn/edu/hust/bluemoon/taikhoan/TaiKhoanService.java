package vn.edu.hust.bluemoon.taikhoan;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hust.bluemoon.chung.KhongTimThayException;
import vn.edu.hust.bluemoon.chung.KiemTra;
import vn.edu.hust.bluemoon.chung.NghiepVuException;

/** US-017: quản trị viên đăng ký tài khoản, gán vai trò, khóa và mở khóa. */
@Service
@Transactional
public class TaiKhoanService {

    static final int DO_DAI_MAT_KHAU_TOI_THIEU = 8;

    private final TaiKhoanRepository taiKhoanRepo;
    private final PasswordEncoder encoder;

    public TaiKhoanService(TaiKhoanRepository taiKhoanRepo, PasswordEncoder encoder) {
        this.taiKhoanRepo = taiKhoanRepo;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<TaiKhoan> danhSach() {
        return taiKhoanRepo.findAllByOrderByTenDangNhapAsc();
    }

    public TaiKhoan dangKy(TaiKhoanForm f) {
        String ten = KiemTra.chuanHoa(f.getTenDangNhap());
        KiemTra kt = new KiemTra()
                .batBuoc("tenDangNhap", ten, "tên đăng nhập")
                .batBuoc("hoTen", KiemTra.chuanHoa(f.getHoTen()), "họ tên")
                .batBuoc("matKhau", f.getMatKhau(), "mật khẩu")
                .phaiChon("vaiTro", f.getVaiTro(), "vai trò");
        if (!kt.coLoi("tenDangNhap")) {
            kt.neu(!ten.matches("[A-Za-z0-9._-]{3,50}"), "tenDangNhap", "Tên đăng nhập 3–50 ký tự, chỉ gồm chữ không dấu, số, dấu . _ -")
              .neu(taiKhoanRepo.existsByTenDangNhapIgnoreCase(ten), "tenDangNhap", "Tên đăng nhập đã tồn tại");
        }
        if (!kt.coLoi("matKhau")) {
            kt.neu(f.getMatKhau().length() < DO_DAI_MAT_KHAU_TOI_THIEU, "matKhau",
                    "Mật khẩu phải có ít nhất " + DO_DAI_MAT_KHAU_TOI_THIEU + " ký tự");
        }
        kt.nemNeuCoLoi();
        return taiKhoanRepo.save(new TaiKhoan(ten.toLowerCase(), encoder.encode(f.getMatKhau()), f.getHoTen().trim(), f.getVaiTro()));
    }

    public void doiVaiTro(Long id, VaiTro vaiTro, String nguoiThucHien) {
        TaiKhoan tk = lay(id);
        if (vaiTro == null) throw new NghiepVuException("vaiTro", "Chưa chọn vai trò");
        if (tk.getTenDangNhap().equalsIgnoreCase(nguoiThucHien) && vaiTro != VaiTro.QUAN_TRI) {
            throw new NghiepVuException("vaiTro", "Không thể tự bỏ quyền quản trị của chính mình");
        }
        tk.doiVaiTro(vaiTro);
    }

    public void khoa(Long id, String nguoiThucHien) {
        TaiKhoan tk = lay(id);
        if (tk.getTenDangNhap().equalsIgnoreCase(nguoiThucHien)) {
            throw new NghiepVuException("hoatDong", "Không thể tự khóa tài khoản đang dùng");
        }
        tk.datHoatDong(false);
    }

    public void moKhoa(Long id) {
        lay(id).datHoatDong(true);
    }

    private TaiKhoan lay(Long id) {
        return taiKhoanRepo.findById(id).orElseThrow(() -> new KhongTimThayException("Không tìm thấy tài khoản"));
    }
}

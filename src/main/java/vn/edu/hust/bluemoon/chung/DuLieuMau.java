package vn.edu.hust.bluemoon.chung;

import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hust.bluemoon.cudan.*;
import vn.edu.hust.bluemoon.taikhoan.TaiKhoanForm;
import vn.edu.hust.bluemoon.taikhoan.TaiKhoanRepository;
import vn.edu.hust.bluemoon.taikhoan.TaiKhoanService;
import vn.edu.hust.bluemoon.taikhoan.VaiTro;

/**
 * Dữ liệu giả lập cho buổi demo (profile "demo"). Chỉ nạp khi CSDL chưa có căn hộ nào.
 * Tên người, số điện thoại đều là giả; thay bằng danh mục thật của BQT khi có.
 */
@Component
@Order(2)
@ConditionalOnProperty(name = "bluemoon.du-lieu-mau", havingValue = "true")
public class DuLieuMau implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DuLieuMau.class);
    static final String MAT_KHAU_DEMO = "matkhau123";

    private final CanHoRepository canHoRepo;
    private final CanHoService canHoService;
    private final HoService hoService;
    private final NhanKhauService nhanKhauService;
    private final TaiKhoanService taiKhoanService;
    private final TaiKhoanRepository taiKhoanRepo;

    public DuLieuMau(CanHoRepository canHoRepo, CanHoService canHoService, HoService hoService,
                     NhanKhauService nhanKhauService, TaiKhoanService taiKhoanService, TaiKhoanRepository taiKhoanRepo) {
        this.canHoRepo = canHoRepo;
        this.canHoService = canHoService;
        this.hoService = hoService;
        this.nhanKhauService = nhanKhauService;
        this.taiKhoanService = taiKhoanService;
        this.taiKhoanRepo = taiKhoanRepo;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (canHoRepo.count() > 0) return;

        taiKhoan("canbo", "Lê Thị Cúc", VaiTro.CAN_BO_CU_DAN);
        taiKhoan("xem", "Phạm Văn Dũng", VaiTro.CHI_XEM);
        taiKhoan("thuquy", "Hoàng Thị Em", VaiTro.THU_QUY);

        // tầng 2–4, mỗi tầng 4 căn; căn 01 và 04 ở góc nên rộng hơn
        String[] dienTich = {"92.5", "68.0", "68.0", "85.3"};
        for (int tang = 2; tang <= 4; tang++) {
            for (int so = 1; so <= 4; so++) {
                CanHoForm f = new CanHoForm();
                f.setMaCanHo("A%d%02d".formatted(tang, so));
                f.setTang(tang);
                f.setDienTich(new BigDecimal(dienTich[so - 1]));
                canHoService.tao(f);
            }
        }

        record NguoiMau(String ma, String hoTen, String ngaySinh, GioiTinh gt, String quanHe) {}
        record HoMau(String ma, String canHo, String sdt, List<NguoiMau> nguoi) {}
        List<HoMau> dsHo = List.of(
            new HoMau("H001", "A201", "0912 000 001", List.of(
                new NguoiMau("NK001", "Nguyễn Văn An", "1978-03-12", GioiTinh.NAM, "Chủ hộ"),
                new NguoiMau("NK002", "Trần Thị Bình", "1981-07-25", GioiTinh.NU, "Vợ"),
                new NguoiMau("NK003", "Nguyễn Minh Châu", "2008-11-02", GioiTinh.NU, "Con"))),
            new HoMau("H002", "A202", "0912 000 002", List.of(
                new NguoiMau("NK004", "Đỗ Quang Huy", "1990-01-30", GioiTinh.NAM, "Chủ hộ"),
                new NguoiMau("NK005", "Vũ Thị Lan", "1992-05-18", GioiTinh.NU, "Vợ"))),
            new HoMau("H003", "A204", "0912 000 003", List.of(
                new NguoiMau("NK006", "Bùi Thị Hạnh", "1965-09-09", GioiTinh.NU, "Chủ hộ"),
                new NguoiMau("NK007", "Ngô Văn Khoa", "1993-12-21", GioiTinh.NAM, "Con"),
                new NguoiMau("NK008", "Ngô Gia Bảo", "2019-04-14", GioiTinh.NAM, "Cháu"))),
            new HoMau("H004", "A301", "0912 000 004", List.of(
                new NguoiMau("NK009", "Phan Đức Long", "1985-06-06", GioiTinh.NAM, "Chủ hộ"))),
            new HoMau("H005", "A303", "0912 000 005", List.of(
                new NguoiMau("NK010", "Lý Thu Trang", "1988-08-08", GioiTinh.NU, "Chủ hộ"),
                new NguoiMau("NK011", "Đinh Văn Tuấn", "1986-02-17", GioiTinh.NAM, "Chồng"))),
            new HoMau("H006", "A402", "0912 000 006", List.of(
                new NguoiMau("NK012", "Trịnh Văn Phúc", "1972-10-01", GioiTinh.NAM, "Chủ hộ"),
                new NguoiMau("NK013", "Cao Thị Mai", "1975-03-03", GioiTinh.NU, "Vợ"))));

        Long idCanHo;
        for (HoMau h : dsHo) {
            idCanHo = canHoRepo.findAll().stream().filter(c -> c.getMaCanHo().equals(h.canHo())).findFirst().orElseThrow().getId();
            HoForm hf = new HoForm();
            hf.setMaHo(h.ma());
            hf.setCanHoId(idCanHo);
            hf.setNguoiDaiDien(h.nguoi().getFirst().hoTen());
            hf.setSoDienThoai(h.sdt());
            Ho ho = hoService.tao(hf);
            for (NguoiMau n : h.nguoi()) {
                NhanKhauForm nf = new NhanKhauForm();
                nf.setMaNhanKhau(n.ma());
                nf.setHoTen(n.hoTen());
                nf.setNgaySinh(n.ngaySinh());
                nf.setGioiTinh(n.gt());
                nf.setQuanHeChuHo(n.quanHe());
                nhanKhauService.them(ho.getId(), nf, "admin");
            }
        }

        // vài biến động để trang lịch sử có dữ liệu: con hộ H003 chuyển đi, một người chuyển đến hộ H004.
        // Nhân khẩu vừa thêm mang ngày hôm nay nên biến động cũng phải từ hôm nay trở đi.
        String homNay = java.time.LocalDate.now().toString();
        Ho h3 = hoService.danhSach().stream().filter(h -> h.getMaHo().equals("H003")).findFirst().orElseThrow();
        NhanKhau khoa = nhanKhauService.dangO(h3.getId()).stream().filter(n -> n.getMaNhanKhau().equals("NK007")).findFirst().orElseThrow();
        ChuyenDiForm di = new ChuyenDiForm();
        di.setNgayChuyenDi(homNay);
        di.setNoiChuyenDen("Phường Dịch Vọng, Cầu Giấy");
        nhanKhauService.chuyenDi(h3.getId(), khoa.getId(), di, "canbo");

        Ho h4 = hoService.danhSach().stream().filter(h -> h.getMaHo().equals("H004")).findFirst().orElseThrow();
        NhanKhauForm den = new NhanKhauForm();
        den.setMaNhanKhau("NK014");
        den.setHoTen("Phan Thị Ngọc");
        den.setNgaySinh("1987-12-12");
        den.setGioiTinh(GioiTinh.NU);
        den.setQuanHeChuHo("Vợ");
        den.setNgayChuyenDen(homNay);
        den.setNoiChuyenTu("TP Hải Phòng");
        nhanKhauService.chuyenDen(h4.getId(), den, "canbo");

        log.info("Đã nạp dữ liệu mẫu: {} căn hộ, {} hộ, tài khoản demo canbo / xem / thuquy (mật khẩu {}).",
                canHoRepo.count(), dsHo.size(), MAT_KHAU_DEMO);
    }

    private void taiKhoan(String ten, String hoTen, VaiTro vaiTro) {
        if (taiKhoanRepo.existsByTenDangNhapIgnoreCase(ten)) return;
        TaiKhoanForm f = new TaiKhoanForm();
        f.setTenDangNhap(ten);
        f.setHoTen(hoTen);
        f.setMatKhau(MAT_KHAU_DEMO);
        f.setVaiTro(vaiTro);
        taiKhoanService.dangKy(f);
    }
}

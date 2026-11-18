package vn.edu.hust.bluemoon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hust.bluemoon.chung.NghiepVuException;
import vn.edu.hust.bluemoon.cudan.*;

/** Kiểm thử theo tiêu chí chấp nhận (AC) của US-001, US-002, US-004, US-005. */
@SpringBootTest
@Transactional
class CuDanTest {

    @Autowired CanHoService canHoService;
    @Autowired HoService hoService;
    @Autowired NhanKhauService nhanKhauService;

    static CanHoForm canHo(String ma, String dienTich) {
        CanHoForm f = new CanHoForm();
        f.setMaCanHo(ma);
        f.setTang(12);
        f.setDienTich(dienTich == null ? null : new BigDecimal(dienTich));
        return f;
    }

    static HoForm ho(String ma, Long canHoId, String daiDien) {
        HoForm f = new HoForm();
        f.setMaHo(ma);
        f.setCanHoId(canHoId);
        f.setNguoiDaiDien(daiDien);
        return f;
    }

    static NhanKhauForm nhanKhau(String ma, String hoTen, String ngaySinh) {
        NhanKhauForm f = new NhanKhauForm();
        f.setMaNhanKhau(ma);
        f.setHoTen(hoTen);
        f.setNgaySinh(ngaySinh);
        f.setGioiTinh(GioiTinh.NAM);
        return f;
    }

    static ChuyenDiForm chuyenDi(String ngay) {
        ChuyenDiForm f = new ChuyenDiForm();
        f.setNgayChuyenDi(ngay);
        f.setNoiChuyenDen("Quận Cầu Giấy");
        return f;
    }

    Ho taoHo(String maCanHo, String maHo) {
        CanHo c = canHoService.tao(canHo(maCanHo, "75.5"));
        return hoService.tao(ho(maHo, c.getId(), "Nguyễn Văn An"));
    }

    @Nested
    class US001_CanHo {

        @Test
        void AC1_luuCanHoMoiVoiDienTichDuong() {
            CanHo c = canHoService.tao(canHo("a1203", "75.5"));
            assertThat(canHoService.danhSach()).extracting(CanHo::getMaCanHo).contains("A1203");
            assertThat(c.getDienTich()).isEqualByComparingTo("75.5");
        }

        @Test
        void AC2_tuChoiMaTrung() {
            canHoService.tao(canHo("A1203", "75.5"));
            assertThatThrownBy(() -> canHoService.tao(canHo("a1203", "60")))
                    .isInstanceOf(NghiepVuException.class)
                    .satisfies(e -> assertThat(((NghiepVuException) e).getLoi()).containsKey("maCanHo"));
        }

        @Test
        void AC2_tuChoiDienTichKhongDuong() {
            assertThatThrownBy(() -> canHoService.tao(canHo("A1204", "0"))).isInstanceOf(NghiepVuException.class);
            assertThatThrownBy(() -> canHoService.tao(canHo("A1205", "-10"))).isInstanceOf(NghiepVuException.class);
            assertThat(canHoService.danhSach()).isEmpty();
        }

        @Test
        void AC3_suaDienTich() {
            CanHo c = canHoService.tao(canHo("A1203", "75.5"));
            canHoService.capNhat(c.getId(), canHo("KHAC", "80"));
            CanHo sau = canHoService.lay(c.getId());
            assertThat(sau.getDienTich()).isEqualByComparingTo("80");
            assertThat(sau.getMaCanHo()).isEqualTo("A1203");
            // Phần "kỳ đã lập không đổi" kiểm thử ở Sprint 2 (US-009), khi có kỳ thu.
        }
    }

    @Nested
    class US002_Ho {

        @Test
        void AC1_taoHoGanCanHo() {
            Ho h = taoHo("A1203", "h001");
            assertThat(h.getMaHo()).isEqualTo("H001");
            assertThat(h.getCanHo().getMaCanHo()).isEqualTo("A1203");
            assertThat(h.getNguoiDaiDien()).isEqualTo("Nguyễn Văn An");
        }

        @Test
        void AC2_tuChoiMaHoTrung() {
            taoHo("A1203", "H001");
            CanHo c2 = canHoService.tao(canHo("A1204", "60"));
            assertThatThrownBy(() -> hoService.tao(ho("h001", c2.getId(), "Trần Thị Bình")))
                    .isInstanceOf(NghiepVuException.class)
                    .hasMessageContaining("Mã hộ đã tồn tại");
        }

        @Test
        void AC3_chiRaTungTruongThieu() {
            assertThatThrownBy(() -> hoService.tao(ho(" ", null, "")))
                    .isInstanceOf(NghiepVuException.class)
                    .satisfies(e -> assertThat(((NghiepVuException) e).getLoi())
                            .containsOnlyKeys("maHo", "canHoId", "nguoiDaiDien"));
        }

        @Test
        void tuChoiCanHoDaCoHo() {
            Ho h = taoHo("A1203", "H001");
            assertThatThrownBy(() -> hoService.tao(ho("H002", h.getCanHo().getId(), "Trần Thị Bình")))
                    .isInstanceOf(NghiepVuException.class)
                    .hasMessageContaining("đã có hộ");
        }
    }

    @Nested
    class US004_NhanKhau {

        @Test
        void AC1_themNhanKhauVaoHo() {
            Ho h = taoHo("A1203", "H001");
            nhanKhauService.them(h.getId(), nhanKhau("NK001", "Nguyễn Văn An", "1980-05-20"), "canbo");
            assertThat(nhanKhauService.dangO(h.getId())).extracting(NhanKhau::getHoTen).containsExactly("Nguyễn Văn An");
        }

        @Test
        void AC2_tuChoiThieuHoTen() {
            Ho h = taoHo("A1203", "H001");
            assertThatThrownBy(() -> nhanKhauService.them(h.getId(), nhanKhau("NK001", " ", "1980-05-20"), "canbo"))
                    .isInstanceOf(NghiepVuException.class).hasMessageContaining("họ tên");
        }

        @Test
        void AC2_tuChoiNgaySinhTuongLai() {
            Ho h = taoHo("A1203", "H001");
            String mai = LocalDate.now().plusDays(1).toString();
            assertThatThrownBy(() -> nhanKhauService.them(h.getId(), nhanKhau("NK001", "Bé Na", mai), "canbo"))
                    .isInstanceOf(NghiepVuException.class).hasMessageContaining("tương lai");
        }

        @Test
        void AC2_tuChoiNgaySinhKhongTonTai() {
            Ho h = taoHo("A1203", "H001");
            assertThatThrownBy(() -> nhanKhauService.them(h.getId(), nhanKhau("NK001", "Bé Na", "2023-02-30"), "canbo"))
                    .isInstanceOf(NghiepVuException.class).hasMessageContaining("không tồn tại");
            assertThatThrownBy(() -> nhanKhauService.them(h.getId(), nhanKhau("NK001", "Bé Na", "31/04/2020"), "canbo"))
                    .isInstanceOf(NghiepVuException.class).hasMessageContaining("không tồn tại");
        }

        @Test
        void AC3_tuChoiMaNhanKhauTrung() {
            Ho h = taoHo("A1203", "H001");
            nhanKhauService.them(h.getId(), nhanKhau("NK001", "Nguyễn Văn An", "1980-05-20"), "canbo");
            assertThatThrownBy(() -> nhanKhauService.them(h.getId(), nhanKhau("nk001", "Trần Văn Bảo", "1990-01-01"), "canbo"))
                    .isInstanceOf(NghiepVuException.class).hasMessageContaining("Mã nhân khẩu đã tồn tại");
        }
    }

    @Nested
    class US005_BienDong {

        @Test
        void AC1_chuyenDiThiKhongConTrongDanhSachNhungGiuLichSu() {
            Ho h = taoHo("A1203", "H001");
            NhanKhau nk = nhanKhauService.them(h.getId(), nhanKhau("NK001", "Nguyễn Văn An", "1980-05-20"), "canbo");
            nhanKhauService.chuyenDi(h.getId(), nk.getId(), chuyenDi("2026-11-20"), "canbo");
            assertThat(nhanKhauService.dangO(h.getId())).isEmpty();
            assertThat(nhanKhauService.lichSu(h.getId())).extracting(BienDong::getLoai)
                    .containsExactly(LoaiBienDong.CHUYEN_DI, LoaiBienDong.THEM_MOI);
        }

        @Test
        void AC2_chuyenDenThiCoTrongDanhSachHo() {
            Ho h = taoHo("A1203", "H001");
            NhanKhauForm f = nhanKhau("NK002", "Trần Thị Bình", "1985-03-15");
            f.setNgayChuyenDen("2026-11-18");
            f.setNoiChuyenTu("Hải Phòng");
            nhanKhauService.chuyenDen(h.getId(), f, "canbo");
            assertThat(nhanKhauService.dangO(h.getId())).extracting(NhanKhau::getMaNhanKhau).containsExactly("NK002");
        }

        @Test
        void AC2_nguoiDaChuyenDiCoTheChuyenDenHoKhac() {
            Ho h1 = taoHo("A1203", "H001");
            Ho h2 = taoHo("A1204", "H002");
            NhanKhau nk = nhanKhauService.them(h1.getId(), nhanKhau("NK001", "Nguyễn Văn An", "1980-05-20"), "canbo");
            NhanKhauForm f = nhanKhau("NK001", "Nguyễn Văn An", "1980-05-20");
            f.setNgayChuyenDen("2026-11-20");
            assertThatThrownBy(() -> nhanKhauService.chuyenDen(h2.getId(), f, "canbo"))
                    .isInstanceOf(NghiepVuException.class).hasMessageContaining("đang ở hộ H001");
            nhanKhauService.chuyenDi(h1.getId(), nk.getId(), chuyenDi("2026-11-20"), "canbo");
            nhanKhauService.chuyenDen(h2.getId(), f, "canbo");
            assertThat(nhanKhauService.dangO(h2.getId())).extracting(NhanKhau::getMaNhanKhau).containsExactly("NK001");
            assertThat(nhanKhauService.lichSu(h1.getId())).hasSize(2);
        }

        @Test
        void AC3_lichSuCoLoaiNgayHieuLucNguoiGhi() {
            Ho h = taoHo("A1203", "H001");
            NhanKhau nk = nhanKhauService.them(h.getId(), nhanKhau("NK001", "Nguyễn Văn An", "1980-05-20"), "canbo1");
            nhanKhauService.chuyenDi(h.getId(), nk.getId(), chuyenDi("2026-11-25"), "canbo2");
            BienDong moiNhat = nhanKhauService.lichSu(h.getId()).getFirst();
            assertThat(moiNhat.getLoai()).isEqualTo(LoaiBienDong.CHUYEN_DI);
            assertThat(moiNhat.getNgayHieuLuc()).isEqualTo(LocalDate.of(2026, 11, 25));
            assertThat(moiNhat.getNguoiGhi()).isEqualTo("canbo2");
        }

        @Test
        void tuChoiNgayChuyenDiKhongTonTai() {
            Ho h = taoHo("A1203", "H001");
            NhanKhau nk = nhanKhauService.them(h.getId(), nhanKhau("NK001", "Nguyễn Văn An", "1980-05-20"), "canbo");
            assertThatThrownBy(() -> nhanKhauService.chuyenDi(h.getId(), nk.getId(), chuyenDi("2026-02-30"), "canbo"))
                    .isInstanceOf(NghiepVuException.class);
            assertThat(nhanKhauService.dangO(h.getId())).hasSize(1);
        }
    }
}

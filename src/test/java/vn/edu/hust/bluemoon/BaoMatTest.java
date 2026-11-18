package vn.edu.hust.bluemoon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hust.bluemoon.taikhoan.TaiKhoan;
import vn.edu.hust.bluemoon.taikhoan.TaiKhoanForm;
import vn.edu.hust.bluemoon.taikhoan.TaiKhoanRepository;
import vn.edu.hust.bluemoon.taikhoan.TaiKhoanService;
import vn.edu.hust.bluemoon.taikhoan.VaiTro;

/** Kiểm thử theo AC của US-016 (đăng nhập) và US-017 (đăng ký tài khoản, phân quyền). */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BaoMatTest {

    @Autowired MockMvc mvc;
    @Autowired TaiKhoanService taiKhoanService;
    @Autowired TaiKhoanRepository taiKhoanRepo;
    @Autowired vn.edu.hust.bluemoon.cudan.CanHoRepository canHoRepo;
    @Autowired vn.edu.hust.bluemoon.cudan.HoRepository hoRepo;

    static final RequestPostProcessor QUAN_TRI = user("admin").authorities(() -> "QUAN_TRI_TAI_KHOAN",
            () -> "XEM_CU_DAN", () -> "SUA_CU_DAN");

    TaiKhoan taoTaiKhoan(String ten, VaiTro vaiTro) {
        TaiKhoanForm f = new TaiKhoanForm();
        f.setTenDangNhap(ten);
        f.setHoTen("Người dùng " + ten);
        f.setMatKhau("matkhau123");
        f.setVaiTro(vaiTro);
        return taiKhoanService.dangKy(f);
    }

    @Nested
    class US016_DangNhap {

        @Test
        void AC1_dungMatKhauThiVaoDuoc() throws Exception {
            mvc.perform(formLogin("/dang-nhap").userParameter("tenDangNhap").passwordParam("matKhau")
                            .user("admin").password("admin12345"))
                    .andExpect(authenticated().withUsername("admin"))
                    .andExpect(redirectedUrl("/"));
        }

        @Test
        void AC2_saiMatKhauHaySaiTenDeuCungMotThongBao() throws Exception {
            mvc.perform(formLogin("/dang-nhap").userParameter("tenDangNhap").passwordParam("matKhau")
                            .user("admin").password("sai-mat-khau"))
                    .andExpect(unauthenticated()).andExpect(redirectedUrl("/dang-nhap?loi"));
            mvc.perform(formLogin("/dang-nhap").userParameter("tenDangNhap").passwordParam("matKhau")
                            .user("khong-ton-tai").password("admin12345"))
                    .andExpect(unauthenticated()).andExpect(redirectedUrl("/dang-nhap?loi"));
        }

        @Test
        void AC3_chuaDangNhapThiChuyenVeTrangDangNhap() throws Exception {
            for (String url : new String[]{"/", "/can-ho", "/ho", "/ho/them", "/tai-khoan"}) {
                mvc.perform(get(url)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dang-nhap"));
            }
        }
    }

    @Nested
    class US017_TaiKhoanPhanQuyen {

        @Test
        void AC1_dangKyRoiDangNhapVoiQuyenCuaVaiTro() throws Exception {
            mvc.perform(post("/tai-khoan").with(QUAN_TRI).with(csrf())
                            .param("tenDangNhap", "canbo1").param("hoTen", "Lê Văn Cường")
                            .param("matKhau", "matkhau123").param("vaiTro", "CAN_BO_CU_DAN"))
                    .andExpect(redirectedUrl("/tai-khoan"));
            mvc.perform(formLogin("/dang-nhap").userParameter("tenDangNhap").passwordParam("matKhau")
                            .user("canbo1").password("matkhau123"))
                    .andExpect(authenticated().withAuthentication(a -> assertThat(a.getAuthorities())
                            .extracting(GrantedAuthority::getAuthority)
                            .contains("XEM_CU_DAN", "SUA_CU_DAN")
                            .doesNotContain("QUAN_TRI_TAI_KHOAN")));
        }

        @Test
        void AC1_tuChoiTenDangNhapTrung() throws Exception {
            mvc.perform(post("/tai-khoan").with(QUAN_TRI).with(csrf())
                            .param("tenDangNhap", "ADMIN").param("hoTen", "Trùng").param("matKhau", "matkhau123")
                            .param("vaiTro", "CHI_XEM"))
                    .andExpect(status().isOk());
            assertThat(taiKhoanRepo.count()).isEqualTo(1);
        }

        @Test
        void AC2_chiXemThiKhongSuaDuoc() throws Exception {
            taoTaiKhoan("xem1", VaiTro.CHI_XEM);
            mvc.perform(get("/can-ho").with(user("xem1").authorities(() -> "XEM_CU_DAN"))).andExpect(status().isOk());
            mvc.perform(post("/can-ho").with(user("xem1").authorities(() -> "XEM_CU_DAN")).with(csrf())
                            .param("maCanHo", "A1203").param("dienTich", "75"))
                    .andExpect(status().isForbidden());
            mvc.perform(get("/can-ho/them").with(user("xem1").authorities(() -> "XEM_CU_DAN")))
                    .andExpect(status().isOk()); // trang form xem được nhưng lưu bị từ chối ở trên
        }

        @Test
        void AC3_khongCoQuyenXemCuDanThiBiTuChoi() throws Exception {
            RequestPostProcessor thuQuy = user("thuquy1").authorities(() -> "ROLE_THU_QUY");
            mvc.perform(get("/ho").with(thuQuy)).andExpect(status().isForbidden());
            mvc.perform(get("/can-ho").with(thuQuy)).andExpect(status().isForbidden());
            mvc.perform(get("/tai-khoan").with(thuQuy)).andExpect(status().isForbidden());
        }

        @Test
        void AC4_biKhoaThiKhongDangNhapDuoc() throws Exception {
            TaiKhoan tk = taoTaiKhoan("canbo2", VaiTro.CAN_BO_CU_DAN);
            mvc.perform(post("/tai-khoan/{id}/khoa", tk.getId()).with(QUAN_TRI).with(csrf()))
                    .andExpect(redirectedUrl("/tai-khoan"));
            mvc.perform(formLogin("/dang-nhap").userParameter("tenDangNhap").passwordParam("matKhau")
                            .user("canbo2").password("matkhau123"))
                    .andExpect(unauthenticated()).andExpect(redirectedUrl("/dang-nhap?loi"));
        }

        @Test
        void khongTheTuKhoaChinhMinh() throws Exception {
            TaiKhoan admin = taiKhoanRepo.findByTenDangNhapIgnoreCase("admin").orElseThrow();
            mvc.perform(post("/tai-khoan/{id}/khoa", admin.getId()).with(QUAN_TRI).with(csrf()));
            assertThat(taiKhoanRepo.findById(admin.getId()).orElseThrow().isHoatDong()).isTrue();
        }
    }

    @Test
    void cacTrangHienThiDuocVoiQuanTri() throws Exception {
        for (String url : new String[]{"/", "/can-ho", "/can-ho/them", "/ho", "/ho/them", "/tai-khoan"}) {
            mvc.perform(get(url).with(QUAN_TRI)).andExpect(status().isOk());
        }
        mvc.perform(get("/dang-nhap")).andExpect(status().isOk());
    }

    @Test
    void trangChiTietHoHienThiDuocKhiFormCoLoi() throws Exception {
        mvc.perform(post("/can-ho").with(QUAN_TRI).with(csrf()).param("maCanHo", "A1203").param("dienTich", "75.5"));
        mvc.perform(post("/ho").with(QUAN_TRI).with(csrf()).param("maHo", "H001").param("canHoId",
                String.valueOf(canHoRepo.findAll().getFirst().getId())).param("nguoiDaiDien", "Nguyễn Văn An"));
        Long hoId = hoRepo.findAll().getFirst().getId();
        mvc.perform(get("/ho/{id}", hoId).with(QUAN_TRI))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("id=\"them-maNhanKhau\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("id=\"den-maNhanKhau\"")));
        mvc.perform(post("/ho/{id}/nhan-khau", hoId).with(QUAN_TRI).with(csrf())
                        .param("maNhanKhau", "NK001").param("hoTen", "").param("ngaySinh", "2023-02-30"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Chưa nhập họ tên")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Ngày sinh không tồn tại")));
    }
}

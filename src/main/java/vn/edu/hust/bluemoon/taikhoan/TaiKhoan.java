package vn.edu.hust.bluemoon.taikhoan;

import jakarta.persistence.*;

@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_dang_nhap", nullable = false, unique = true, length = 50)
    private String tenDangNhap;

    /** Mật khẩu đã băm (BCrypt), không lưu mật khẩu gốc. */
    @Column(name = "mat_khau", nullable = false, length = 100)
    private String matKhau;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Enumerated(EnumType.STRING)
    @Column(name = "vai_tro", nullable = false, length = 20)
    private VaiTro vaiTro;

    @Column(name = "hoat_dong", nullable = false)
    private boolean hoatDong = true;

    protected TaiKhoan() {
    }

    public TaiKhoan(String tenDangNhap, String matKhau, String hoTen, VaiTro vaiTro) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
    }

    public Long getId() { return id; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public String getHoTen() { return hoTen; }
    public VaiTro getVaiTro() { return vaiTro; }
    public boolean isHoatDong() { return hoatDong; }

    void doiVaiTro(VaiTro vaiTro) { this.vaiTro = vaiTro; }
    void datHoatDong(boolean hoatDong) { this.hoatDong = hoatDong; }
}

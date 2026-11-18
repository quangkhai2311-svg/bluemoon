package vn.edu.hust.bluemoon.cudan;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "nhan_khau")
public class NhanKhau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_nhan_khau", nullable = false, unique = true, length = 20)
    private String maNhanKhau;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "ngay_sinh", nullable = false)
    private LocalDate ngaySinh;

    @Enumerated(EnumType.STRING)
    @Column(name = "gioi_tinh", length = 10)
    private GioiTinh gioiTinh;

    @Column(name = "so_cccd", length = 20)
    private String soCccd;

    @Column(name = "quan_he_chu_ho", length = 50)
    private String quanHeChuHo;

    /** Hộ hiện tại; khi đã chuyển đi thì là hộ cuối cùng người đó ở. */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "ho_id", nullable = false)
    private Ho ho;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, length = 20)
    private TrangThaiCuTru trangThai;

    protected NhanKhau() {
    }

    public NhanKhau(String maNhanKhau, String hoTen, LocalDate ngaySinh, GioiTinh gioiTinh,
                    String soCccd, String quanHeChuHo, Ho ho) {
        this.maNhanKhau = maNhanKhau;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soCccd = soCccd;
        this.quanHeChuHo = quanHeChuHo;
        this.ho = ho;
        this.trangThai = TrangThaiCuTru.DANG_O;
    }

    public Long getId() { return id; }
    public String getMaNhanKhau() { return maNhanKhau; }
    public String getHoTen() { return hoTen; }
    public LocalDate getNgaySinh() { return ngaySinh; }
    public GioiTinh getGioiTinh() { return gioiTinh; }
    public String getSoCccd() { return soCccd; }
    public String getQuanHeChuHo() { return quanHeChuHo; }
    public Ho getHo() { return ho; }
    public TrangThaiCuTru getTrangThai() { return trangThai; }

    void chuyenDen(Ho hoMoi, String quanHeChuHo) {
        this.ho = hoMoi;
        this.quanHeChuHo = quanHeChuHo;
        this.trangThai = TrangThaiCuTru.DANG_O;
    }

    void chuyenDi() {
        this.trangThai = TrangThaiCuTru.DA_CHUYEN_DI;
    }
}

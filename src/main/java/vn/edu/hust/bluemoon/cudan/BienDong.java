package vn.edu.hust.bluemoon.cudan;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Một lần thay đổi cư trú của nhân khẩu; chỉ thêm, không sửa, không xóa để giữ lịch sử. */
@Entity
@Table(name = "bien_dong")
public class BienDong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "nhan_khau_id", nullable = false)
    private NhanKhau nhanKhau;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "ho_id", nullable = false)
    private Ho ho;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoaiBienDong loai;

    @Column(name = "ngay_hieu_luc", nullable = false)
    private LocalDate ngayHieuLuc;

    /** Nơi chuyển từ (chuyển đến) hoặc nơi chuyển tới (chuyển đi). */
    @Column(name = "dia_diem", length = 255)
    private String diaDiem;

    @Column(name = "nguoi_ghi", nullable = false, length = 50)
    private String nguoiGhi;

    @Column(name = "thoi_diem_ghi", nullable = false)
    private LocalDateTime thoiDiemGhi;

    protected BienDong() {
    }

    public BienDong(NhanKhau nhanKhau, Ho ho, LoaiBienDong loai, LocalDate ngayHieuLuc,
                    String diaDiem, String nguoiGhi, LocalDateTime thoiDiemGhi) {
        this.nhanKhau = nhanKhau;
        this.ho = ho;
        this.loai = loai;
        this.ngayHieuLuc = ngayHieuLuc;
        this.diaDiem = diaDiem;
        this.nguoiGhi = nguoiGhi;
        this.thoiDiemGhi = thoiDiemGhi;
    }

    public Long getId() { return id; }
    public NhanKhau getNhanKhau() { return nhanKhau; }
    public Ho getHo() { return ho; }
    public LoaiBienDong getLoai() { return loai; }
    public LocalDate getNgayHieuLuc() { return ngayHieuLuc; }
    public String getDiaDiem() { return diaDiem; }
    public String getNguoiGhi() { return nguoiGhi; }
    public LocalDateTime getThoiDiemGhi() { return thoiDiemGhi; }
}

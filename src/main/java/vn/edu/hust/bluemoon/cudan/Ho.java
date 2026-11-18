package vn.edu.hust.bluemoon.cudan;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ho")
public class Ho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_ho", nullable = false, unique = true, length = 20)
    private String maHo;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "can_ho_id", nullable = false)
    private CanHo canHo;

    @Column(name = "nguoi_dai_dien", nullable = false, length = 100)
    private String nguoiDaiDien;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "ngay_lap", nullable = false)
    private LocalDate ngayLap;

    protected Ho() {
    }

    public Ho(String maHo, CanHo canHo, String nguoiDaiDien, String soDienThoai, LocalDate ngayLap) {
        this.maHo = maHo;
        this.canHo = canHo;
        this.nguoiDaiDien = nguoiDaiDien;
        this.soDienThoai = soDienThoai;
        this.ngayLap = ngayLap;
    }

    public Long getId() { return id; }
    public String getMaHo() { return maHo; }
    public CanHo getCanHo() { return canHo; }
    public String getNguoiDaiDien() { return nguoiDaiDien; }
    public String getSoDienThoai() { return soDienThoai; }
    public LocalDate getNgayLap() { return ngayLap; }
}

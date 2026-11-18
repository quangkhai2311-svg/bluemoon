package vn.edu.hust.bluemoon.cudan;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "can_ho")
public class CanHo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_can_ho", nullable = false, unique = true, length = 20)
    private String maCanHo;

    private Integer tang;

    /** Diện tích (m²), căn cứ tính phí dịch vụ, phí quản lý. */
    @Column(name = "dien_tich", nullable = false, precision = 8, scale = 2)
    private BigDecimal dienTich;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    protected CanHo() {
    }

    public CanHo(String maCanHo, Integer tang, BigDecimal dienTich, String ghiChu) {
        this.maCanHo = maCanHo;
        this.tang = tang;
        this.dienTich = dienTich;
        this.ghiChu = ghiChu;
    }

    public Long getId() { return id; }
    public String getMaCanHo() { return maCanHo; }
    public Integer getTang() { return tang; }
    public BigDecimal getDienTich() { return dienTich; }
    public String getGhiChu() { return ghiChu; }

    void capNhat(Integer tang, BigDecimal dienTich, String ghiChu) {
        this.tang = tang;
        this.dienTich = dienTich;
        this.ghiChu = ghiChu;
    }
}

package vn.edu.hust.bluemoon.cudan;

import java.math.BigDecimal;

public class CanHoForm {

    private String maCanHo;
    private Integer tang;
    private BigDecimal dienTich;
    private String ghiChu;

    public static CanHoForm tu(CanHo c) {
        CanHoForm f = new CanHoForm();
        f.maCanHo = c.getMaCanHo();
        f.tang = c.getTang();
        f.dienTich = c.getDienTich();
        f.ghiChu = c.getGhiChu();
        return f;
    }

    public String getMaCanHo() { return maCanHo; }
    public void setMaCanHo(String maCanHo) { this.maCanHo = maCanHo; }
    public Integer getTang() { return tang; }
    public void setTang(Integer tang) { this.tang = tang; }
    public BigDecimal getDienTich() { return dienTich; }
    public void setDienTich(BigDecimal dienTich) { this.dienTich = dienTich; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}

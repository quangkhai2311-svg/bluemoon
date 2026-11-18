package vn.edu.hust.bluemoon.cudan;

/** Dùng cho cả "thêm nhân khẩu" (US-004) và "chuyển đến" (US-005). */
public class NhanKhauForm {

    private String maNhanKhau;
    private String hoTen;
    private String ngaySinh;
    private GioiTinh gioiTinh;
    private String soCccd;
    private String quanHeChuHo;
    /** Chỉ dùng khi chuyển đến. */
    private String ngayChuyenDen;
    private String noiChuyenTu;

    public String getMaNhanKhau() { return maNhanKhau; }
    public void setMaNhanKhau(String maNhanKhau) { this.maNhanKhau = maNhanKhau; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }
    public GioiTinh getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(GioiTinh gioiTinh) { this.gioiTinh = gioiTinh; }
    public String getSoCccd() { return soCccd; }
    public void setSoCccd(String soCccd) { this.soCccd = soCccd; }
    public String getQuanHeChuHo() { return quanHeChuHo; }
    public void setQuanHeChuHo(String quanHeChuHo) { this.quanHeChuHo = quanHeChuHo; }
    public String getNgayChuyenDen() { return ngayChuyenDen; }
    public void setNgayChuyenDen(String ngayChuyenDen) { this.ngayChuyenDen = ngayChuyenDen; }
    public String getNoiChuyenTu() { return noiChuyenTu; }
    public void setNoiChuyenTu(String noiChuyenTu) { this.noiChuyenTu = noiChuyenTu; }
}

package vn.edu.hust.bluemoon.cudan;

public enum TrangThaiCuTru {
    DANG_O("Đang ở"), DA_CHUYEN_DI("Đã chuyển đi");

    private final String ten;

    TrangThaiCuTru(String ten) { this.ten = ten; }

    public String getTen() { return ten; }
}

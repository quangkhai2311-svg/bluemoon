package vn.edu.hust.bluemoon.cudan;

public enum GioiTinh {
    NAM("Nam"), NU("Nữ");

    private final String ten;

    GioiTinh(String ten) { this.ten = ten; }

    public String getTen() { return ten; }
}

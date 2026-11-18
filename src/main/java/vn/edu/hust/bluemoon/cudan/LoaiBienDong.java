package vn.edu.hust.bluemoon.cudan;

public enum LoaiBienDong {
    THEM_MOI("Thêm nhân khẩu"), CHUYEN_DEN("Chuyển đến"), CHUYEN_DI("Chuyển đi");

    private final String ten;

    LoaiBienDong(String ten) { this.ten = ten; }

    public String getTen() { return ten; }
}

package vn.edu.hust.bluemoon.chung;

import java.util.LinkedHashMap;
import java.util.Map;

/** Gom các lỗi nhập liệu của một form rồi báo một lần, để người dùng thấy hết trường sai. */
public final class KiemTra {

    private final Map<String, String> loi = new LinkedHashMap<>();

    public KiemTra batBuoc(String truong, String giaTri, String tenHienThi) {
        if (giaTri == null || giaTri.isBlank()) loi.putIfAbsent(truong, "Chưa nhập " + tenHienThi);
        return this;
    }

    /** Dùng cho ô chọn (căn hộ, vai trò...). */
    public KiemTra phaiChon(String truong, Object giaTri, String tenHienThi) {
        if (giaTri == null) loi.putIfAbsent(truong, "Chưa chọn " + tenHienThi);
        return this;
    }

    public KiemTra batBuoc(String truong, Object giaTri, String tenHienThi) {
        if (giaTri == null) loi.putIfAbsent(truong, "Chưa nhập " + tenHienThi);
        return this;
    }

    public KiemTra neu(boolean viPham, String truong, String thongBao) {
        if (viPham) loi.putIfAbsent(truong, thongBao);
        return this;
    }

    public boolean coLoi(String truong) {
        return loi.containsKey(truong);
    }

    public void nemNeuCoLoi() {
        if (!loi.isEmpty()) throw new NghiepVuException(loi);
    }

    /** Bỏ khoảng trắng hai đầu; chuỗi rỗng coi như không nhập. */
    public static String chuanHoa(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}

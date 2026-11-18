package vn.edu.hust.bluemoon.chung;

import java.util.LinkedHashMap;
import java.util.Map;

/** Vi phạm quy tắc nghiệp vụ; mỗi lỗi gắn với tên trường trên form để hiển thị đúng chỗ. */
public class NghiepVuException extends RuntimeException {

    private final Map<String, String> loi;

    public NghiepVuException(Map<String, String> loi) {
        super(String.join("; ", loi.values()));
        this.loi = Map.copyOf(new LinkedHashMap<>(loi));
    }

    public NghiepVuException(String truong, String thongBao) {
        this(Map.of(truong, thongBao));
    }

    public Map<String, String> getLoi() {
        return loi;
    }
}

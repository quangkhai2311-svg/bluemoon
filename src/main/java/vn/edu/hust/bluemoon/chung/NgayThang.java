package vn.edu.hust.bluemoon.chung;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

/** Đọc ngày theo kiểu nghiêm ngặt: 30/02 hay 31/04 bị coi là ngày không tồn tại. */
public final class NgayThang {

    public static final DateTimeFormatter HIEN_THI = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final List<DateTimeFormatter> DINH_DANG = List.of(
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT));

    private NgayThang() {
    }

    /** Trả về null nếu chuỗi rỗng hoặc không phải ngày hợp lệ. */
    public static LocalDate doc(String s) {
        if (s == null || s.isBlank()) return null;
        for (DateTimeFormatter f : DINH_DANG) {
            try {
                return LocalDate.parse(s.trim(), f);
            } catch (DateTimeParseException ignored) {
                // thử định dạng tiếp theo
            }
        }
        return null;
    }
}

package vn.edu.hust.bluemoon.taikhoan;

import java.util.EnumSet;
import java.util.Set;

/** Vai trò gán cho tài khoản; mỗi vai trò là một tập quyền. Sprint 2 bổ sung quyền thu phí cho thủ quỹ. */
public enum VaiTro {
    QUAN_TRI("Quản trị viên", EnumSet.allOf(Quyen.class)),
    CAN_BO_CU_DAN("Cán bộ cư dân", EnumSet.of(Quyen.XEM_CU_DAN, Quyen.SUA_CU_DAN)),
    THU_QUY("Thủ quỹ", EnumSet.noneOf(Quyen.class)),
    CHI_XEM("Chỉ xem", EnumSet.of(Quyen.XEM_CU_DAN));

    private final String ten;
    private final Set<Quyen> quyen;

    VaiTro(String ten, Set<Quyen> quyen) {
        this.ten = ten;
        this.quyen = quyen;
    }

    public String getTen() { return ten; }
    public Set<Quyen> getQuyen() { return quyen; }
}

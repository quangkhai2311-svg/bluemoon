package vn.edu.hust.bluemoon.chung;

import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;

public final class LoiForm {

    private LoiForm() {
    }

    /** Chuyển lỗi nghiệp vụ thành lỗi của từng trường để form hiển thị cạnh ô nhập. */
    public static void ganVao(BindingResult br, NghiepVuException e) {
        e.getLoi().forEach((truong, thongBao) -> {
            if (br.getTarget() != null && BeanUtils.getPropertyDescriptor(br.getTarget().getClass(), truong) != null) {
                br.rejectValue(truong, "nghiepVu", thongBao);
            } else {
                br.reject("nghiepVu", thongBao);
            }
        });
    }
}

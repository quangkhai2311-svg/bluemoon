package vn.edu.hust.bluemoon.chung;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class TrangChungController {

    @GetMapping("/")
    public String trangChu() {
        return "trang-chu";
    }

    @GetMapping("/dang-nhap")
    public String dangNhap() {
        return "dang-nhap";
    }

    /** Spring Security chuyển tiếp (forward) yêu cầu bị từ chối tới đây với nguyên phương thức GET hoặc POST. */
    @RequestMapping("/khong-co-quyen")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String khongCoQuyen() {
        return "khong-co-quyen";
    }
}

package vn.edu.hust.bluemoon.chung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/khong-co-quyen")
    public String khongCoQuyen() {
        return "khong-co-quyen";
    }
}

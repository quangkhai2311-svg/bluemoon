package vn.edu.hust.bluemoon.taikhoan;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hust.bluemoon.chung.LoiForm;
import vn.edu.hust.bluemoon.chung.NghiepVuException;

@Controller
@RequestMapping("/tai-khoan")
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;

    public TaiKhoanController(TaiKhoanService taiKhoanService) {
        this.taiKhoanService = taiKhoanService;
    }

    @GetMapping
    public String danhSach(Model model) {
        if (!model.containsAttribute("form")) model.addAttribute("form", new TaiKhoanForm());
        model.addAttribute("dsTaiKhoan", taiKhoanService.danhSach());
        model.addAttribute("vaiTro", VaiTro.values());
        return "tai-khoan/danh-sach";
    }

    @PostMapping
    public String dangKy(@ModelAttribute("form") TaiKhoanForm form, BindingResult br, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) return danhSach(model);
        try {
            TaiKhoan tk = taiKhoanService.dangKy(form);
            ra.addFlashAttribute("thongBao", "Đã tạo tài khoản " + tk.getTenDangNhap() + " (" + tk.getVaiTro().getTen() + ")");
            return "redirect:/tai-khoan";
        } catch (NghiepVuException e) {
            LoiForm.ganVao(br, e);
            form.setMatKhau(null);
            return danhSach(model);
        }
    }

    @PostMapping("/{id}/vai-tro")
    public String doiVaiTro(@PathVariable Long id, @RequestParam VaiTro vaiTro, Principal nguoi, RedirectAttributes ra) {
        return thucHien(ra, "Đã đổi vai trò", () -> taiKhoanService.doiVaiTro(id, vaiTro, nguoi.getName()));
    }

    @PostMapping("/{id}/khoa")
    public String khoa(@PathVariable Long id, Principal nguoi, RedirectAttributes ra) {
        return thucHien(ra, "Đã khóa tài khoản", () -> taiKhoanService.khoa(id, nguoi.getName()));
    }

    @PostMapping("/{id}/mo-khoa")
    public String moKhoa(@PathVariable Long id, RedirectAttributes ra) {
        return thucHien(ra, "Đã mở khóa tài khoản", () -> taiKhoanService.moKhoa(id));
    }

    private static String thucHien(RedirectAttributes ra, String thanhCong, Runnable viec) {
        try {
            viec.run();
            ra.addFlashAttribute("thongBao", thanhCong);
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/tai-khoan";
    }
}

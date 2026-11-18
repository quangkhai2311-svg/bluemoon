package vn.edu.hust.bluemoon.cudan;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hust.bluemoon.chung.LoiForm;
import vn.edu.hust.bluemoon.chung.NghiepVuException;

@Controller
@RequestMapping("/ho")
public class HoController {

    private final HoService hoService;
    private final CanHoService canHoService;
    private final NhanKhauService nhanKhauService;

    public HoController(HoService hoService, CanHoService canHoService, NhanKhauService nhanKhauService) {
        this.hoService = hoService;
        this.canHoService = canHoService;
        this.nhanKhauService = nhanKhauService;
    }

    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("dsHo", hoService.danhSach());
        return "ho/danh-sach";
    }

    @GetMapping("/them")
    public String formThem(Model model) {
        model.addAttribute("form", new HoForm());
        model.addAttribute("dsCanHo", canHoService.danhSach());
        return "ho/form";
    }

    @PostMapping
    public String them(@ModelAttribute("form") HoForm form, BindingResult br, Model model, RedirectAttributes ra) {
        model.addAttribute("dsCanHo", canHoService.danhSach());
        if (br.hasErrors()) return "ho/form";
        try {
            Ho ho = hoService.tao(form);
            ra.addFlashAttribute("thongBao", "Đã thêm hộ " + ho.getMaHo());
            return "redirect:/ho/" + ho.getId();
        } catch (NghiepVuException e) {
            LoiForm.ganVao(br, e);
            return "ho/form";
        }
    }

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model) {
        return trangChiTiet(id, model, null);
    }

    /** US-004 */
    @PostMapping("/{id}/nhan-khau")
    public String themNhanKhau(@PathVariable Long id, @ModelAttribute("themForm") NhanKhauForm form, BindingResult br,
                               Principal nguoi, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) return trangChiTiet(id, model, "them");
        try {
            NhanKhau nk = nhanKhauService.them(id, form, nguoi.getName());
            ra.addFlashAttribute("thongBao", "Đã thêm nhân khẩu " + nk.getHoTen());
            return "redirect:/ho/" + id;
        } catch (NghiepVuException e) {
            LoiForm.ganVao(br, e);
            return trangChiTiet(id, model, "them");
        }
    }

    /** US-005 */
    @PostMapping("/{id}/chuyen-den")
    public String chuyenDen(@PathVariable Long id, @ModelAttribute("denForm") NhanKhauForm form, BindingResult br,
                            Principal nguoi, Model model, RedirectAttributes ra) {
        if (br.hasErrors()) return trangChiTiet(id, model, "den");
        try {
            NhanKhau nk = nhanKhauService.chuyenDen(id, form, nguoi.getName());
            ra.addFlashAttribute("thongBao", "Đã ghi chuyển đến: " + nk.getHoTen());
            return "redirect:/ho/" + id;
        } catch (NghiepVuException e) {
            LoiForm.ganVao(br, e);
            return trangChiTiet(id, model, "den");
        }
    }

    /** US-005 */
    @PostMapping("/{id}/nhan-khau/{nhanKhauId}/chuyen-di")
    public String chuyenDi(@PathVariable Long id, @PathVariable Long nhanKhauId, @ModelAttribute ChuyenDiForm form,
                           Principal nguoi, RedirectAttributes ra) {
        try {
            NhanKhau nk = nhanKhauService.chuyenDi(id, nhanKhauId, form, nguoi.getName());
            ra.addFlashAttribute("thongBao", "Đã ghi chuyển đi: " + nk.getHoTen());
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/ho/" + id;
    }

    private String trangChiTiet(Long id, Model model, String formDangMo) {
        model.addAttribute("ho", hoService.lay(id));
        model.addAttribute("dangO", nhanKhauService.dangO(id));
        model.addAttribute("lichSu", nhanKhauService.lichSu(id));
        if (!model.containsAttribute("themForm")) model.addAttribute("themForm", new NhanKhauForm());
        if (!model.containsAttribute("denForm")) model.addAttribute("denForm", new NhanKhauForm());
        model.addAttribute("diForm", new ChuyenDiForm());
        model.addAttribute("gioiTinh", GioiTinh.values());
        model.addAttribute("formDangMo", formDangMo);
        return "ho/chi-tiet";
    }
}

package vn.edu.hust.bluemoon.cudan;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hust.bluemoon.chung.LoiForm;
import vn.edu.hust.bluemoon.chung.NghiepVuException;

@Controller
@RequestMapping("/can-ho")
public class CanHoController {

    private final CanHoService canHoService;

    public CanHoController(CanHoService canHoService) {
        this.canHoService = canHoService;
    }

    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("dsCanHo", canHoService.danhSach());
        return "can-ho/danh-sach";
    }

    @GetMapping("/them")
    public String formThem(Model model) {
        model.addAttribute("form", new CanHoForm());
        return "can-ho/form";
    }

    @PostMapping
    public String them(@ModelAttribute("form") CanHoForm form, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "can-ho/form";
        try {
            CanHo c = canHoService.tao(form);
            ra.addFlashAttribute("thongBao", "Đã thêm căn hộ " + c.getMaCanHo());
            return "redirect:/can-ho";
        } catch (NghiepVuException e) {
            LoiForm.ganVao(br, e);
            return "can-ho/form";
        }
    }

    @GetMapping("/{id}/sua")
    public String formSua(@PathVariable Long id, Model model) {
        model.addAttribute("canHo", canHoService.lay(id));
        model.addAttribute("form", CanHoForm.tu(canHoService.lay(id)));
        return "can-ho/form";
    }

    @PostMapping("/{id}")
    public String sua(@PathVariable Long id, @ModelAttribute("form") CanHoForm form, BindingResult br,
                      Model model, RedirectAttributes ra) {
        CanHo canHo = canHoService.lay(id);
        model.addAttribute("canHo", canHo);
        form.setMaCanHo(canHo.getMaCanHo());
        if (br.hasErrors()) return "can-ho/form";
        try {
            canHoService.capNhat(id, form);
            ra.addFlashAttribute("thongBao", "Đã cập nhật căn hộ " + canHo.getMaCanHo());
            return "redirect:/can-ho";
        } catch (NghiepVuException e) {
            LoiForm.ganVao(br, e);
            return "can-ho/form";
        }
    }
}

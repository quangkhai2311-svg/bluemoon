package vn.edu.hust.bluemoon.chung;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Đưa tên người dùng và quyền vào mọi trang để menu chỉ hiện chức năng được phép. */
@ControllerAdvice
public class NguoiDungAdvice {

    @ModelAttribute("tenNguoiDung")
    public String tenNguoiDung(Authentication auth) {
        return dangNhap(auth) ? auth.getName() : null;
    }

    @ModelAttribute("quyen")
    public Set<String> quyen(Authentication auth) {
        if (!dangNhap(auth)) return Set.of();
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    private static boolean dangNhap(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
}

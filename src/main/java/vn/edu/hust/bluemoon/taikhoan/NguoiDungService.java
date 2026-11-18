package vn.edu.hust.bluemoon.taikhoan;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** US-016: nạp tài khoản khi đăng nhập; tài khoản bị khóa được đánh dấu disabled nên không đăng nhập được. */
@Service
public class NguoiDungService implements UserDetailsService {

    private final TaiKhoanRepository taiKhoanRepo;

    public NguoiDungService(TaiKhoanRepository taiKhoanRepo) {
        this.taiKhoanRepo = taiKhoanRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String tenDangNhap) {
        TaiKhoan tk = taiKhoanRepo.findByTenDangNhapIgnoreCase(tenDangNhap == null ? "" : tenDangNhap.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Không có tài khoản"));
        List<GrantedAuthority> quyen = new ArrayList<>();
        tk.getVaiTro().getQuyen().forEach(q -> quyen.add(new SimpleGrantedAuthority(q.name())));
        quyen.add(new SimpleGrantedAuthority("ROLE_" + tk.getVaiTro().name()));
        return User.withUsername(tk.getTenDangNhap())
                .password(tk.getMatKhau())
                .authorities(quyen)
                .disabled(!tk.isHoatDong())
                .build();
    }
}

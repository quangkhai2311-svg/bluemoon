package vn.edu.hust.bluemoon.cauhinh;

import static vn.edu.hust.bluemoon.taikhoan.Quyen.QUAN_TRI_TAI_KHOAN;
import static vn.edu.hust.bluemoon.taikhoan.Quyen.SUA_CU_DAN;
import static vn.edu.hust.bluemoon.taikhoan.Quyen.XEM_CU_DAN;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/** NFR-001: mọi chức năng phải đăng nhập; xem và sửa dữ liệu cư dân cần quyền riêng. */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/dang-nhap", "/css/**", "/error").permitAll()
                .requestMatchers("/tai-khoan/**").hasAuthority(QUAN_TRI_TAI_KHOAN.name())
                .requestMatchers(HttpMethod.GET, "/can-ho/**", "/ho/**").hasAuthority(XEM_CU_DAN.name())
                .requestMatchers("/can-ho/**", "/ho/**").hasAuthority(SUA_CU_DAN.name())
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/dang-nhap")
                .usernameParameter("tenDangNhap")
                .passwordParameter("matKhau")
                .defaultSuccessUrl("/", true)
                // cùng một thông báo cho sai tên, sai mật khẩu hay tài khoản bị khóa (US-016 AC2)
                .failureUrl("/dang-nhap?loi")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/dang-xuat")
                .logoutSuccessUrl("/dang-nhap?da-dang-xuat"))
            .exceptionHandling(ex -> ex.accessDeniedPage("/khong-co-quyen"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

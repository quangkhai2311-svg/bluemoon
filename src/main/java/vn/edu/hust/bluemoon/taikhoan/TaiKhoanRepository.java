package vn.edu.hust.bluemoon.taikhoan;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {

    Optional<TaiKhoan> findByTenDangNhapIgnoreCase(String tenDangNhap);

    boolean existsByTenDangNhapIgnoreCase(String tenDangNhap);

    List<TaiKhoan> findAllByOrderByTenDangNhapAsc();
}

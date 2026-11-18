package vn.edu.hust.bluemoon.cudan;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanKhauRepository extends JpaRepository<NhanKhau, Long> {

    Optional<NhanKhau> findByMaNhanKhauIgnoreCase(String maNhanKhau);

    List<NhanKhau> findByHoIdAndTrangThaiOrderByMaNhanKhauAsc(Long hoId, TrangThaiCuTru trangThai);

    long countByHoIdAndTrangThai(Long hoId, TrangThaiCuTru trangThai);
}

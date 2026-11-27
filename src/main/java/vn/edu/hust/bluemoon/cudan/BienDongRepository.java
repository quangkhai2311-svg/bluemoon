package vn.edu.hust.bluemoon.cudan;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BienDongRepository extends JpaRepository<BienDong, Long> {

    List<BienDong> findByHoIdOrderByNgayHieuLucDescIdDesc(Long hoId);

    Optional<BienDong> findFirstByNhanKhauIdOrderByNgayHieuLucDescIdDesc(Long nhanKhauId);
}

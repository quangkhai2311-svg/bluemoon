package vn.edu.hust.bluemoon.cudan;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoRepository extends JpaRepository<Ho, Long> {

    boolean existsByMaHoIgnoreCase(String maHo);

    boolean existsByCanHoId(Long canHoId);

    List<Ho> findAllByOrderByMaHoAsc();
}

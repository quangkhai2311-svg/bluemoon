package vn.edu.hust.bluemoon.cudan;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanHoRepository extends JpaRepository<CanHo, Long> {

    boolean existsByMaCanHoIgnoreCase(String maCanHo);

    List<CanHo> findAllByOrderByMaCanHoAsc();
}

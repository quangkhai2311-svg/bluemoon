package vn.edu.hust.bluemoon;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.edu.hust.bluemoon.cudan.CanHoRepository;
import vn.edu.hust.bluemoon.cudan.HoRepository;
import vn.edu.hust.bluemoon.cudan.NhanKhauRepository;
import vn.edu.hust.bluemoon.cudan.TrangThaiCuTru;
import vn.edu.hust.bluemoon.taikhoan.TaiKhoanRepository;

/** Dữ liệu demo phải nạp được qua đúng các quy tắc nghiệp vụ, không lỗi. */
@SpringBootTest(properties = {"bluemoon.du-lieu-mau=true",
        "spring.datasource.url=jdbc:h2:mem:bluemoon-demo;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1"})
class DuLieuMauTest {

    @Autowired CanHoRepository canHoRepo;
    @Autowired HoRepository hoRepo;
    @Autowired NhanKhauRepository nhanKhauRepo;
    @Autowired TaiKhoanRepository taiKhoanRepo;

    @Test
    void napDuDuLieuMau() {
        assertThat(canHoRepo.count()).isEqualTo(12);
        assertThat(hoRepo.count()).isEqualTo(6);
        assertThat(nhanKhauRepo.count()).isEqualTo(14);
        assertThat(nhanKhauRepo.findAll()).filteredOn(n -> n.getTrangThai() == TrangThaiCuTru.DA_CHUYEN_DI).hasSize(1);
        assertThat(taiKhoanRepo.count()).isEqualTo(4);
    }
}

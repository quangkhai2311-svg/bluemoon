package vn.edu.hust.bluemoon.cudan;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hust.bluemoon.chung.KhongTimThayException;
import vn.edu.hust.bluemoon.chung.KiemTra;

/** US-001: danh mục căn hộ kèm diện tích. */
@Service
@Transactional
public class CanHoService {

    private final CanHoRepository canHoRepo;

    public CanHoService(CanHoRepository canHoRepo) {
        this.canHoRepo = canHoRepo;
    }

    @Transactional(readOnly = true)
    public List<CanHo> danhSach() {
        return canHoRepo.findAllByOrderByMaCanHoAsc();
    }

    @Transactional(readOnly = true)
    public CanHo lay(Long id) {
        return canHoRepo.findById(id).orElseThrow(() -> new KhongTimThayException("Không tìm thấy căn hộ"));
    }

    public CanHo tao(CanHoForm f) {
        String ma = KiemTra.chuanHoa(f.getMaCanHo());
        KiemTra kt = new KiemTra().batBuoc("maCanHo", ma, "mã căn hộ");
        if (!kt.coLoi("maCanHo")) kt.neu(canHoRepo.existsByMaCanHoIgnoreCase(ma), "maCanHo", "Mã căn hộ đã tồn tại");
        kiemTraDienTich(kt, f.getDienTich());
        kt.nemNeuCoLoi();
        return canHoRepo.save(new CanHo(ma.toUpperCase(), f.getTang(), f.getDienTich(), KiemTra.chuanHoa(f.getGhiChu())));
    }

    /**
     * Mã căn hộ không sửa được vì hộ và các kỳ thu tham chiếu tới. Diện tích mới chỉ áp dụng cho các kỳ thu
     * lập sau: khi lập kỳ (Sprint 2, US-009) hệ thống chụp lại diện tích tại thời điểm đó.
     */
    public CanHo capNhat(Long id, CanHoForm f) {
        CanHo c = lay(id);
        KiemTra kt = new KiemTra();
        kiemTraDienTich(kt, f.getDienTich());
        kt.nemNeuCoLoi();
        c.capNhat(f.getTang(), f.getDienTich(), KiemTra.chuanHoa(f.getGhiChu()));
        return c;
    }

    private static void kiemTraDienTich(KiemTra kt, BigDecimal dienTich) {
        kt.batBuoc("dienTich", dienTich, "diện tích")
          .neu(dienTich != null && dienTich.signum() <= 0, "dienTich", "Diện tích phải lớn hơn 0");
    }
}

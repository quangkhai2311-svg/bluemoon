package vn.edu.hust.bluemoon.cudan;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hust.bluemoon.chung.KhongTimThayException;
import vn.edu.hust.bluemoon.chung.KiemTra;

/** US-002: thêm hộ gắn với căn hộ và người đại diện. */
@Service
@Transactional
public class HoService {

    private final HoRepository hoRepo;
    private final CanHoRepository canHoRepo;
    private final Clock clock;

    public HoService(HoRepository hoRepo, CanHoRepository canHoRepo, Clock clock) {
        this.hoRepo = hoRepo;
        this.canHoRepo = canHoRepo;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<Ho> danhSach() {
        return hoRepo.findAllByOrderByMaHoAsc();
    }

    @Transactional(readOnly = true)
    public Ho lay(Long id) {
        return hoRepo.findById(id).orElseThrow(() -> new KhongTimThayException("Không tìm thấy hộ"));
    }

    public Ho tao(HoForm f) {
        String ma = KiemTra.chuanHoa(f.getMaHo());
        String daiDien = KiemTra.chuanHoa(f.getNguoiDaiDien());
        KiemTra kt = new KiemTra()
                .batBuoc("maHo", ma, "mã hộ")
                .phaiChon("canHoId", f.getCanHoId(), "căn hộ")
                .batBuoc("nguoiDaiDien", daiDien, "người đại diện");
        if (!kt.coLoi("maHo")) kt.neu(hoRepo.existsByMaHoIgnoreCase(ma), "maHo", "Mã hộ đã tồn tại");
        CanHo canHo = null;
        if (!kt.coLoi("canHoId")) {
            canHo = canHoRepo.findById(f.getCanHoId()).orElse(null);
            kt.neu(canHo == null, "canHoId", "Căn hộ không có trong danh mục")
              // mỗi căn hộ một hộ, để phí theo diện tích không bị tính hai lần
              .neu(canHo != null && hoRepo.existsByCanHoId(canHo.getId()), "canHoId", "Căn hộ này đã có hộ");
        }
        kt.nemNeuCoLoi();
        return hoRepo.save(new Ho(ma.toUpperCase(), canHo, daiDien, KiemTra.chuanHoa(f.getSoDienThoai()), LocalDate.now(clock)));
    }
}

package vn.edu.hust.bluemoon.cudan;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hust.bluemoon.chung.KhongTimThayException;
import vn.edu.hust.bluemoon.chung.KiemTra;
import vn.edu.hust.bluemoon.chung.NgayThang;
import vn.edu.hust.bluemoon.chung.NghiepVuException;

/** US-004: thêm nhân khẩu; US-005: ghi chuyển đến, chuyển đi và giữ lịch sử biến động. */
@Service
@Transactional
public class NhanKhauService {

    private final NhanKhauRepository nhanKhauRepo;
    private final BienDongRepository bienDongRepo;
    private final HoService hoService;
    private final Clock clock;

    public NhanKhauService(NhanKhauRepository nhanKhauRepo, BienDongRepository bienDongRepo,
                           HoService hoService, Clock clock) {
        this.nhanKhauRepo = nhanKhauRepo;
        this.bienDongRepo = bienDongRepo;
        this.hoService = hoService;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<NhanKhau> dangO(Long hoId) {
        return nhanKhauRepo.findByHoIdAndTrangThaiOrderByMaNhanKhauAsc(hoId, TrangThaiCuTru.DANG_O);
    }

    @Transactional(readOnly = true)
    public List<BienDong> lichSu(Long hoId) {
        return bienDongRepo.findByHoIdOrderByNgayHieuLucDescIdDesc(hoId);
    }

    public NhanKhau them(Long hoId, NhanKhauForm f, String nguoiGhi) {
        Ho ho = hoService.lay(hoId);
        KiemTra kt = new KiemTra();
        LocalDate ngaySinh = kiemTraThongTin(kt, f);
        kt.neu(!kt.coLoi("maNhanKhau") && nhanKhauRepo.findByMaNhanKhauIgnoreCase(f.getMaNhanKhau().trim()).isPresent(),
                "maNhanKhau", "Mã nhân khẩu đã tồn tại");
        kt.nemNeuCoLoi();
        NhanKhau nk = nhanKhauRepo.save(moi(f, ngaySinh, ho));
        ghi(nk, ho, LoaiBienDong.THEM_MOI, LocalDate.now(clock), null, nguoiGhi);
        return nk;
    }

    /**
     * Người chưa có trong hệ thống thì tạo mới; người đã chuyển đi khỏi một hộ khác (cùng mã nhân khẩu)
     * thì chuyển sang hộ này. Người đang ở một hộ phải ghi chuyển đi trước.
     */
    public NhanKhau chuyenDen(Long hoId, NhanKhauForm f, String nguoiGhi) {
        Ho ho = hoService.lay(hoId);
        KiemTra kt = new KiemTra();
        LocalDate ngaySinh = kiemTraThongTin(kt, f);
        LocalDate ngayDen = kiemTraNgayHieuLuc(kt, "ngayChuyenDen", f.getNgayChuyenDen());
        NhanKhau daCo = kt.coLoi("maNhanKhau") ? null
                : nhanKhauRepo.findByMaNhanKhauIgnoreCase(f.getMaNhanKhau().trim()).orElse(null);
        kt.neu(daCo != null && daCo.getTrangThai() == TrangThaiCuTru.DANG_O, "maNhanKhau",
                daCo == null ? "" : "Người này đang ở hộ " + daCo.getHo().getMaHo() + ", cần ghi chuyển đi trước");
        kt.nemNeuCoLoi();

        NhanKhau nk;
        if (daCo != null) {
            daCo.chuyenDen(ho, KiemTra.chuanHoa(f.getQuanHeChuHo()));
            nk = daCo;
        } else {
            nk = nhanKhauRepo.save(moi(f, ngaySinh, ho));
        }
        ghi(nk, ho, LoaiBienDong.CHUYEN_DEN, ngayDen, KiemTra.chuanHoa(f.getNoiChuyenTu()), nguoiGhi);
        return nk;
    }

    public NhanKhau chuyenDi(Long hoId, Long nhanKhauId, ChuyenDiForm f, String nguoiGhi) {
        NhanKhau nk = nhanKhauRepo.findById(nhanKhauId)
                .filter(x -> x.getHo().getId().equals(hoId))
                .orElseThrow(() -> new KhongTimThayException("Không tìm thấy nhân khẩu trong hộ"));
        if (nk.getTrangThai() != TrangThaiCuTru.DANG_O) {
            throw new NghiepVuException("maNhanKhau", "Người này không còn ở trong hộ");
        }
        KiemTra kt = new KiemTra();
        LocalDate ngayDi = kiemTraNgayHieuLuc(kt, "ngayChuyenDi", f.getNgayChuyenDi());
        kt.nemNeuCoLoi();
        nk.chuyenDi();
        ghi(nk, nk.getHo(), LoaiBienDong.CHUYEN_DI, ngayDi, KiemTra.chuanHoa(f.getNoiChuyenDen()), nguoiGhi);
        return nk;
    }

    private LocalDate kiemTraThongTin(KiemTra kt, NhanKhauForm f) {
        LocalDate ngaySinh = NgayThang.doc(f.getNgaySinh());
        kt.batBuoc("maNhanKhau", KiemTra.chuanHoa(f.getMaNhanKhau()), "mã nhân khẩu")
          .batBuoc("hoTen", KiemTra.chuanHoa(f.getHoTen()), "họ tên")
          .batBuoc("ngaySinh", f.getNgaySinh(), "ngày sinh");
        if (!kt.coLoi("ngaySinh")) {
            kt.neu(ngaySinh == null, "ngaySinh", "Ngày sinh không tồn tại")
              .neu(ngaySinh != null && ngaySinh.isAfter(LocalDate.now(clock)), "ngaySinh", "Ngày sinh không được ở tương lai");
        }
        return ngaySinh;
    }

    private static LocalDate kiemTraNgayHieuLuc(KiemTra kt, String truong, String giaTri) {
        LocalDate ngay = NgayThang.doc(giaTri);
        kt.batBuoc(truong, giaTri, "ngày hiệu lực");
        if (!kt.coLoi(truong)) kt.neu(ngay == null, truong, "Ngày không tồn tại");
        return ngay;
    }

    private static NhanKhau moi(NhanKhauForm f, LocalDate ngaySinh, Ho ho) {
        return new NhanKhau(f.getMaNhanKhau().trim().toUpperCase(), f.getHoTen().trim(), ngaySinh, f.getGioiTinh(),
                KiemTra.chuanHoa(f.getSoCccd()), KiemTra.chuanHoa(f.getQuanHeChuHo()), ho);
    }

    private void ghi(NhanKhau nk, Ho ho, LoaiBienDong loai, LocalDate ngay, String diaDiem, String nguoiGhi) {
        bienDongRepo.save(new BienDong(nk, ho, loai, ngay, diaDiem, nguoiGhi, LocalDateTime.now(clock)));
    }
}

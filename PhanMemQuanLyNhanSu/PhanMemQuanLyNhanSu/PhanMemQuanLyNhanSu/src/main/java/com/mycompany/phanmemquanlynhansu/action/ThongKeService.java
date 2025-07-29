package com.mycompany.phanmemquanlynhansu.action;

import com.mycompany.phanmemquanlynhansu.entity.DanhSachNhanVien;
import com.mycompany.phanmemquanlynhansu.entity.NhanVien;
import com.mycompany.phanmemquanlynhansu.utils.FileUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Lớp dịch vụ để thực hiện các chức năng thống kê nâng cao.
 */
public class ThongKeService {
    private List<NhanVien> danhSach;
    private static final String FILE_PATH = "data/nhanvien.xml";

    public ThongKeService() {
        // Tải danh sách nhân viên khi khởi tạo service
        loadData();
    }

    /**
     * Tải dữ liệu từ file XML.
     * Phương thức này gọi đúng hàm loadFromXML từ FileUtils.
     */
    private void loadData() {
        DanhSachNhanVien wrapper = FileUtils.loadFromXML(FILE_PATH, DanhSachNhanVien.class);
        if (wrapper != null && wrapper.getDanhSach() != null) {
            this.danhSach = wrapper.getDanhSach();
        } else {
            // Nếu file không tồn tại hoặc rỗng, khởi tạo danh sách trống
            this.danhSach = new ArrayList<>();
        }
    }

    public int tongSoNhanVien() {
        return danhSach.size();
    }

    public long demNam() {
        return danhSach.stream()
                .filter(nv -> "Nam".equalsIgnoreCase(nv.getGioiTinh()))
                .count();
    }

    public long demNu() {
        return danhSach.stream()
                .filter(nv -> "Nữ".equalsIgnoreCase(nv.getGioiTinh()))
                .count();
    }

    private int tinhTuoi(Date ngaySinh) {
        if (ngaySinh == null) return 0; // Tránh NullPointerException
        LocalDate birthDate = ngaySinh.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public int tuoiNhoNhat() {
        return danhSach.stream()
                .filter(nv -> nv.getNgaySinh() != null)
                .mapToInt(nv -> tinhTuoi(nv.getNgaySinh())) // SỬA LỖI Ở ĐÂY
                .min()
                .orElse(0);
    }

    public int tuoiLonNhat() {
        return danhSach.stream()
                .filter(nv -> nv.getNgaySinh() != null)
                .mapToInt(nv -> tinhTuoi(nv.getNgaySinh())) // SỬA LỖI Ở ĐÂY
                .max()
                .orElse(0);
    }

    public double tuoiTrungBinh() {
        return danhSach.stream()
                .filter(nv -> nv.getNgaySinh() != null)
                .mapToInt(nv -> tinhTuoi(nv.getNgaySinh())) // SỬA LỖI Ở ĐÂY
                .average()
                .orElse(0);
    }

    public double luongThapNhat() {
        return danhSach.stream()
                .mapToDouble(NhanVien::getLuong)
                .min()
                .orElse(0);
    }

    public double luongCaoNhat() {
        return danhSach.stream()
                .mapToDouble(NhanVien::getLuong)
                .max()
                .orElse(0);
    }

    public double luongTrungBinh() {
        return danhSach.stream()
                .mapToDouble(NhanVien::getLuong)
                .average()
                .orElse(0);
    }
}

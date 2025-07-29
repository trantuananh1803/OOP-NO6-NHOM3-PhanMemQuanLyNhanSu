/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.phanmemquanlynhansu.action;

/**
 *
 * @author ADMIN
 */
import com.mycompany.phanmemquanlynhansu.entity.DanhSachNhanVien;
import com.mycompany.phanmemquanlynhansu.entity.NhanVien;
import com.mycompany.phanmemquanlynhansu.utils.FileUtils;
import com.mycompany.phanmemquanlynhansu.utils.SinhMaTuDong;
import java.util.ArrayList;
import java.util.List;

public class NhanVienService {

    private static final String FILE_PATH = "data/nhanvien.xml";
    private List<NhanVien> danhSach;

    public NhanVienService() {
        load();
    }

    public final void load() {
        DanhSachNhanVien wrapper = FileUtils.loadFromXML(FILE_PATH, DanhSachNhanVien.class);
        if (wrapper != null && wrapper.getDanhSach() != null) {
            danhSach = wrapper.getDanhSach();
        } else {
            danhSach = new ArrayList<>();
        }
    }

    public void save() {
        DanhSachNhanVien wrapper = new DanhSachNhanVien();
        wrapper.setDanhSach(danhSach);
        FileUtils.saveToXML(FILE_PATH, wrapper, DanhSachNhanVien.class);
    }

    public List<NhanVien> getAll() {
        return danhSach;
    }

    public void themNhanVien(NhanVien nv) {
        String newPhone = nv.getPhoneNumber();

        // Chỉ kiểm tra trùng lặp nếu số điện thoại mới không rỗng
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            for (NhanVien item : danhSach) {
                // Sử dụng .equals() một cách an toàn
                // Chỉ so sánh nếu số điện thoại của nhân viên cũ không phải là null
                if (newPhone.equals(item.getPhoneNumber())) {
                    throw new IllegalArgumentException("Số điện thoại '" + newPhone + "' đã tồn tại!");
                }
            }
        }

        int newId = SinhMaTuDong.sinhMaTuDong(danhSach);
        nv.setMa(newId);
        danhSach.add(nv);
        save();
    }

    public void suaNhanVien(NhanVien nvCapNhat) {
        String updatedPhone = nvCapNhat.getPhoneNumber();

        // Chỉ kiểm tra trùng lặp nếu số điện thoại được cập nhật không rỗng
        if (updatedPhone != null && !updatedPhone.trim().isEmpty()) {
            for (NhanVien item : danhSach) {
                // Kiểm tra nếu SĐT trùng với một người khác (khác mã nhân viên)
                if (updatedPhone.equals(item.getPhoneNumber()) && item.getMa() != nvCapNhat.getMa()) {
                    throw new IllegalArgumentException("Số điện thoại '" + updatedPhone + "' đã tồn tại!");
                }
            }
        }
        
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMa() == nvCapNhat.getMa()) {
                danhSach.set(i, nvCapNhat);
                break;
            }
        }
        save();
    }

    public void xoaNhanVien(int ma) {
        danhSach.removeIf(nv -> nv.getMa() == ma);
        save();
    }
}

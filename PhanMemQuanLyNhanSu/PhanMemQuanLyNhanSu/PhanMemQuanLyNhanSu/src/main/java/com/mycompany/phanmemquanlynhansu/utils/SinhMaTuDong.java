package com.mycompany.phanmemquanlynhansu.utils;

import com.mycompany.phanmemquanlynhansu.entity.NhanVien;
import java.util.List;

/**
 * Class tiện ích để sinh mã nhân viên mới một cách tự động.
 */
public class SinhMaTuDong {

    /**
     * Tìm mã nhân viên lớn nhất trong danh sách và trả về mã tiếp theo.
     * @param dsNhanVien Danh sách nhân viên hiện tại.
     * @return Một số nguyên là mã cho nhân viên mới.
     */
    public static int sinhMaTuDong(List<NhanVien> dsNhanVien) {
        // Nếu danh sách rỗng, bắt đầu từ 1
        if (dsNhanVien == null || dsNhanVien.isEmpty()) {
            return 1;
        }

        int maxId = 0;
        // Vì 'ma' bây giờ là int, chúng ta chỉ cần tìm số lớn nhất
        for (NhanVien nv : dsNhanVien) {
            if (nv.getMa() > maxId) {
                maxId = nv.getMa();
            }
        }
        
        // Mã mới sẽ là số lớn nhất + 1
        return maxId + 1;
    }
}

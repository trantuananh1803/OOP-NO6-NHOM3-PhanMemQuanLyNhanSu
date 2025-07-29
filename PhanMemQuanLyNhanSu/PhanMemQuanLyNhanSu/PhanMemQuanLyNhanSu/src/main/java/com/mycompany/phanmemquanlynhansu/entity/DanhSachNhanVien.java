package com.mycompany.phanmemquanlynhansu.entity;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Lớp vỏ bọc (wrapper) để chứa danh sách các nhân viên.
 * JAXB sử dụng lớp này để tạo ra một file XML có cấu trúc chuẩn,
 * với một thẻ gốc duy nhất chứa nhiều thẻ con.
 */
@XmlRootElement(name = "danhsachnhanvien") // <-- Tên của thẻ gốc trong file XML
@XmlAccessorType(XmlAccessType.FIELD)
public class DanhSachNhanVien {

    /**
     */
    @XmlElement(name = "nhanvien")
    private List<NhanVien> danhSach;

    // Getter và Setter cho danh sách
    public List<NhanVien> getDanhSach() {
        return danhSach;
    }

    public void setDanhSach(List<NhanVien> danhSach) {
        this.danhSach = danhSach;
    }
}
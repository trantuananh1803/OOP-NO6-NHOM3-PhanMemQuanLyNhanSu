package com.mycompany.phanmemquanlynhansu.entity;

import com.mycompany.phanmemquanlynhansu.utils.DateAdapter;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Lớp NhanVien định nghĩa đối tượng nhân viên.
 * Chuyển sang sử dụng XmlAccessType.FIELD để JAXB làm việc trực tiếp với các trường,
 * đảm bảo việc đọc/ghi dữ liệu XML ổn định và chính xác hơn.
 */
@XmlRootElement(name = "nhanvien")
@XmlAccessorType(XmlAccessType.FIELD) // <--- THAY ĐỔI QUAN TRỌNG
public class NhanVien {
    
    // Các trường dữ liệu, JAXB sẽ làm việc trực tiếp tại đây
    private int ma;
    private String hoTen;
    private String gioiTinh;
    
    // Giữ lại XmlJavaTypeAdapter trên trường để xử lý định dạng ngày tháng
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date ngaySinh;
    
    private String phongBan;
    private String diaChi;
    private double luong;
    private String phoneNumber;

    public NhanVien() {
    }

    public NhanVien(int ma, String hoTen, String gioiTinh, Date ngaySinh, 
                    String phongBan, String diaChi, double luong, String phoneNumber) {
        this.ma = ma;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.phongBan = phongBan;
        this.diaChi = diaChi;
        this.luong = luong;
        this.phoneNumber = phoneNumber;
    }

    // Các hàm getter và setter giữ nguyên, nhưng không còn annotation @XmlElement
    public int getMa() {
        return ma;
    }

    public void setMa(int ma) {
        this.ma = ma;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getPhongBan() {
        return phongBan;
    }

    public void setPhongBan(String phongBan) {
        this.phongBan = phongBan;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) {
        this.luong = luong;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

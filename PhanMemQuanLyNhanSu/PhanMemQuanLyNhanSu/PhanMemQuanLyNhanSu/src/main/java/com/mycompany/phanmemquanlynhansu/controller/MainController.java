package com.mycompany.phanmemquanlynhansu.controller;

import com.mycompany.phanmemquanlynhansu.action.NhanVienService;
import com.mycompany.phanmemquanlynhansu.action.ThongKeService;
import com.mycompany.phanmemquanlynhansu.view.MainView;
import com.mycompany.phanmemquanlynhansu.view.NhanVienView;
import com.mycompany.phanmemquanlynhansu.view.ThongKeView;
import javax.swing.*;

public class MainController {
    private final MainView mainView;

    public MainController(MainView mainView) {
        this.mainView = mainView;
        initListeners();
    }

    private void initListeners() {
        
        mainView.getBtnNhanVien().addActionListener(e -> {
            try {
                NhanVienView nhanVienView = new NhanVienView();
                
                
                new NhanVienController(nhanVienView);
                
                nhanVienView.setVisible(true);

            } catch (Exception ex) {
                showError("Lỗi khi mở form Quản lý Nhân viên: " + ex.getMessage());
            }
        });

       
        mainView.getBtnThongKe().addActionListener(e -> {
            try {
                ThongKeView thongKeView = new ThongKeView();
                ThongKeService thongKeService = new ThongKeService();
                
                // Giả sử ThongKeController có cấu trúc tương tự
                new ThongKeController(thongKeView);
                
                thongKeView.setVisible(true);
            } catch (Exception ex) {
                showError("Lỗi khi mở thống kê: " + ex.getMessage());
            }
        });

        // Xử lý khi click menu "Thoát"
        mainView.getBtnThoat().addActionListener(e -> {
            System.exit(0);
        });
    }

    /**
     * Hiển thị cửa sổ chính của ứng dụng
     */
    public void showMainView() {
        mainView.setLocationRelativeTo(null);
        mainView.setVisible(true);
    }
    
    /**
     * Hàm tiện ích để hiển thị thông báo lỗi
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(mainView, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
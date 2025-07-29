package com.mycompany.phanmemquanlynhansu.controller;

import com.mycompany.phanmemquanlynhansu.action.NhanVienService;
import com.mycompany.phanmemquanlynhansu.entity.NhanVien;
import com.mycompany.phanmemquanlynhansu.view.NhanVienView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.event.ListSelectionEvent;

public class NhanVienController {

    private final NhanVienView view;
    private final NhanVienService service;
    private List<NhanVien> displayedList;

    public NhanVienController(NhanVienView view) {
        this.view = view;
        this.service = new NhanVienService();
        initListeners();
        showData();
    }

    private void initListeners() {
        view.addAddListener(e -> themNhanVien());
        view.addEditListener(e -> suaNhanVien());
        view.addDeleteListener(e -> xoaNhanVien());

        view.addClearListener(e -> {
            view.clearFields();
            showData();
        });
        
        view.addTableSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                view.fillResidentFromSelectedRow();
            }
        });

        view.addSearchListener(e -> view.showSearchDialog(true));
        view.addSearchDialogListener(e -> timKiemNangCao());
        view.addCancelSearchDialogListener(e -> view.showSearchDialog(false));

        view.addUndoListener(e -> view.dispose());

        view.addSortByNameListener(e -> sapXepTheoTen());
        view.addSortByDobListener(e -> sapXepTheoNamSinh());
    }

    private void showData() {
        this.displayedList = service.getAll();
        view.showListResidents(this.displayedList);
    }

    private void themNhanVien() {
        try {
            NhanVien nv = view.getResidentInfo();
            if (nv != null) {
                service.themNhanVien(nv);
                view.showMessage("Thêm nhân viên thành công!");
                showData();
                view.clearFields();
            }
        } catch (IllegalArgumentException ex) {
            view.showMessage(ex.getMessage());
        }
    }

    private void suaNhanVien() {
        try {
            NhanVien nv = view.getResidentInfo();
            if (nv != null) {
                service.suaNhanVien(nv);
                view.showMessage("Cập nhật thông tin thành công!");
                showData();
                view.clearFields();
            }
        } catch (IllegalArgumentException ex) {
            view.showMessage(ex.getMessage());
        }
    }

    private void xoaNhanVien() {
        NhanVien nv = view.getResidentInfo();
        if (nv != null && nv.getMa() > 0) {
            boolean confirmed = view.showConfirmDialog("Bạn có chắc chắn muốn xóa nhân viên này?");
            if (confirmed) {
                service.xoaNhanVien(nv.getMa());
                view.showMessage("Xóa nhân viên thành công!");
                showData();
                view.clearFields();
            }
        } else {
            view.showMessage("Vui lòng chọn một nhân viên để xóa.");
        }
    }

    private void timKiemNangCao() {
        String searchText = view.getSearchText().trim();
        if (searchText.isEmpty()) {
            view.showMessage("Vui lòng nhập nội dung cần tìm kiếm.");
            return;
        }

        List<NhanVien> allNhanVien = service.getAll();
        List<NhanVien> ketQua;

        if (view.isNameSearchSelected()) {
            String searchTextLower = searchText.toLowerCase();
            ketQua = allNhanVien.stream()
                    .filter(nv -> nv.getHoTen().toLowerCase().contains(searchTextLower))
                    .collect(Collectors.toList());
        } else if (view.isAddressSearchSelected()) {
            String searchTextLower = searchText.toLowerCase();
            ketQua = allNhanVien.stream()
                    .filter(nv -> nv.getDiaChi().toLowerCase().contains(searchTextLower))
                    .collect(Collectors.toList());
        } else if (view.isYearSearchSelected()) {
            ketQua = allNhanVien.stream()
                    .filter(nv -> {
                        if (nv.getNgaySinh() != null) {
                            String namSinh = new java.text.SimpleDateFormat("yyyy").format(nv.getNgaySinh());
                            return namSinh.equals(searchText);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        } else {
            view.showMessage("Vui lòng chọn một tiêu chí tìm kiếm (Tên, Quê Quán, hoặc Năm Sinh).");
            return;
        }

        this.displayedList = ketQua;
        view.showListResidents(this.displayedList);
        view.showSearchDialog(false);

        if (ketQua.isEmpty()) {
            view.showMessage("Không tìm thấy kết quả nào phù hợp.");
        }
    }

    private void sapXepTheoTen() {
        if (this.displayedList != null) {
            this.displayedList.sort((nv1, nv2) -> {
                String ten1 = getFirstName(nv1.getHoTen());
                String ten2 = getFirstName(nv2.getHoTen());
                return ten1.compareToIgnoreCase(ten2);
            });
            view.showListResidents(this.displayedList);
            view.showMessage("Đã sắp xếp danh sách theo tên.");
        }
    }

    private void sapXepTheoNamSinh() {
        if (this.displayedList != null) {
            this.displayedList.sort((nv1, nv2) -> {
                if (nv1.getNgaySinh() == null && nv2.getNgaySinh() == null) return 0;
                if (nv1.getNgaySinh() == null) return 1;
                if (nv2.getNgaySinh() == null) return -1;
                return nv2.getNgaySinh().compareTo(nv1.getNgaySinh());
            });
            view.showListResidents(this.displayedList);
            view.showMessage("Đã sắp xếp danh sách theo năm sinh (mới nhất).");
        }
    }

    private String getFirstName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullName.trim().split("\\s+");
        return parts[parts.length - 1];
    }
}
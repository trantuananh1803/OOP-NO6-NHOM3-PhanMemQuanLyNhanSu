package com.mycompany.phanmemquanlynhansu.view;

import com.mycompany.phanmemquanlynhansu.entity.NhanVien;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class NhanVienView extends javax.swing.JFrame {

    private final SimpleDateFormat fDate = new SimpleDateFormat("dd/MM/yyyy");
    private final String[] columnNames = new String[]{
        "Mã NV", "Họ và tên", "Địa chỉ", "Giới tính", "Lương", "Ngày sinh", "Phòng Ban", "SĐT"
    };

    public NhanVienView() {
        initComponents();
        configUI();
    }
    
    private void configUI() {
        setLocationRelativeTo(null);
        setTitle("Quản lý Nhân viên");
        BirthdayChooser.setDateFormatString("dd/MM/yyyy");
        tableResident.setDefaultRenderer(Object.class, new MyRenderer());
        FieldMa.setEditable(false);
        resetButtonState(true);
        SearchDialog.pack();
        SearchDialog.setLocationRelativeTo(null);
    }
    
    public void showListResidents(List<NhanVien> list) {
        int size = list.size();
        Object[][] data = new Object[size][8];
        for (int i = 0; i < size; i++) {
            NhanVien nv = list.get(i);
            data[i][0] = nv.getMa();
            data[i][1] = nv.getHoTen();
            data[i][2] = nv.getDiaChi();
            data[i][3] = nv.getGioiTinh();
            data[i][4] = String.format("%,.0f", nv.getLuong());
            if (nv.getNgaySinh() != null) {
                data[i][5] = fDate.format(nv.getNgaySinh());
            } else {
                data[i][5] = "";
            }
            data[i][6] = nv.getPhongBan();
            data[i][7] = nv.getPhoneNumber();
        }
        tableResident.setModel(new DefaultTableModel(data, columnNames));
    }

    public NhanVien getResidentInfo() {
        if (!validateInput()) {
            return null;
        }
        try {
            NhanVien nv = new NhanVien();
            if (FieldMa.getText() != null && !FieldMa.getText().trim().isEmpty()) {
                nv.setMa(Integer.parseInt(FieldMa.getText()));
            }
            nv.setHoTen(FieldName.getText().trim());
            nv.setDiaChi(FieldAddress.getText().trim());
            nv.setNgaySinh(BirthdayChooser.getDate());
            nv.setGioiTinh(CheckBoxMale.isSelected() ? "Nam" : "Nữ");
            nv.setLuong(Double.parseDouble(FieldWage.getText()));
            nv.setPhongBan(FieldRole.getText().trim());
            nv.setPhoneNumber(FieldPhone.getText().trim());
            return nv;
        } catch (NumberFormatException e) {
            showMessage("Lỗi định dạng số: " + e.getMessage());
            return null;
        }
    }
    
    public void fillResidentFromSelectedRow() {
        int row = tableResident.getSelectedRow();
        if (row >= 0) {
            FieldMa.setText(tableResident.getModel().getValueAt(row, 0).toString());
            FieldName.setText(tableResident.getModel().getValueAt(row, 1).toString());
            FieldAddress.setText(tableResident.getModel().getValueAt(row, 2).toString());
            
            String gioiTinh = tableResident.getModel().getValueAt(row, 3).toString();
            if ("Nam".equalsIgnoreCase(gioiTinh)) {
                CheckBoxMale.setSelected(true);
            } else {
                CheckBoxFemale.setSelected(true);
            }

            String luongStr = tableResident.getModel().getValueAt(row, 4).toString().replaceAll("[,.]", "");
            FieldWage.setText(luongStr);
            
            try {
                Date ngaySinh = fDate.parse(tableResident.getModel().getValueAt(row, 5).toString());
                BirthdayChooser.setDate(ngaySinh);
            } catch (Exception e) {
                BirthdayChooser.setDate(null);
            }
            FieldRole.setText(tableResident.getModel().getValueAt(row, 6).toString());
            FieldPhone.setText(tableResident.getModel().getValueAt(row, 7).toString());
            
            resetButtonState(false);
        }
    }
    
    public void clearFields() {
        FieldMa.setText("");
        FieldName.setText("");
        FieldAddress.setText("");
        BirthdayChooser.setDate(null);
        btnGroupSex.clearSelection();
        FieldWage.setText("");
        FieldRole.setText("");
        FieldPhone.setText("");
        tableResident.clearSelection();
        resetButtonState(true);
    }
    
    private void resetButtonState(boolean isAdding) {
        btnAdd.setEnabled(isAdding);
        btnEdit.setEnabled(!isAdding);
        btnDelete.setEnabled(!isAdding);
    }

    private boolean validateInput() {
        if (FieldName.getText().trim().isEmpty()) {
            return showError("Họ và tên không được trống.", FieldName);
        }
        if (FieldAddress.getText().trim().isEmpty()) {
            return showError("Địa chỉ không được trống.", FieldAddress);
        }
        if (BirthdayChooser.getDate() == null) {
            return showError("Ngày sinh không được trống.", BirthdayChooser);
        }
        if (BirthdayChooser.getDate().after(new Date())) {
            return showError("Ngày sinh không được lớn hơn ngày hiện tại.", BirthdayChooser);
        }
        if (!CheckBoxMale.isSelected() && !CheckBoxFemale.isSelected()) {
            return showError("Bạn chưa chọn giới tính.", CheckBoxMale);
        }
        try {
            double luong = Double.parseDouble(FieldWage.getText());
            if (luong <= 0) {
                return showError("Lương phải là số dương.", FieldWage);
            }
        } catch (NumberFormatException e) {
            return showError("Lương phải là một con số hợp lệ.", FieldWage);
        }
        if (FieldRole.getText().trim().isEmpty()) {
            return showError("Phòng ban không được trống.", FieldRole);
        }
        if (FieldPhone.getText().trim().isEmpty()) {
            return showError("Số điện thoại không được trống.", FieldPhone);
        }
        return true;
    }

    private boolean showError(String message, JComponent component) {
        showMessage(message);
        component.requestFocus();
        return false;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public boolean showConfirmDialog(String message) {
        int choice = JOptionPane.showConfirmDialog(this, message, "Xác nhận", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }
    
    public void showSearchDialog(boolean visible) {
        SearchDialog.setVisible(visible);
    }

    public String getSearchText() {
        return FieldSearch.getText();
    }
    public boolean isNameSearchSelected() {
        return CheckBoxName.isSelected();
    }

    public boolean isAddressSearchSelected() {
        return CheckBoxAddress.isSelected();
    }

    public boolean isYearSearchSelected() {
        return CheckBoxYear.isSelected();
    }
    
    public void addAddListener(ActionListener listener) { btnAdd.addActionListener(listener); }
    public void addEditListener(ActionListener listener) { btnEdit.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { btnDelete.addActionListener(listener); }
    public void addSearchListener(ActionListener listener) { btnSearch.addActionListener(listener); }
    public void addClearListener(ActionListener listener) { btnCancelSearch.addActionListener(listener); }
    public void addUndoListener(ActionListener listener) { btnResidentUndo.addActionListener(listener); }
    public void addTableSelectionListener(ListSelectionListener listener) { tableResident.getSelectionModel().addListSelectionListener(listener); }
    public void addSearchDialogListener(ActionListener listener) { btnSearchDialog.addActionListener(listener); }
    public void addCancelSearchDialogListener(ActionListener listener) { btnCancelDialog.addActionListener(listener); }
public void addSortByNameListener(ActionListener listener) {
    btnSortByName.addActionListener(listener);
}
public void addSortByDobListener(ActionListener listener) {
    btnSortByDob.addActionListener(listener);
}
    public JTable getTableNhanVien() { return tableResident; }
    
    public class MyRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            JTableHeader header = table.getTableHeader();
            header.setBackground(new Color(0, 0, 139));
            header.setForeground(Color.WHITE);
            header.setFont(new java.awt.Font("Times New Roman", 0, 18));
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? new Color(191, 239, 255) : new Color(135, 206, 250));
            } else {
                c.setBackground(new Color(193, 255, 193));
            }
            return c;
        }
    }
    
    public class RoundedBorder implements Border {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        public Insets getBorderInsets(Component c) { return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius); }
        public boolean isBorderOpaque() { return true; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) { g.drawRoundRect(x, y, width - 1, height - 1, radius, radius); }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupSearch = new javax.swing.ButtonGroup();
        btnGroupSex = new javax.swing.ButtonGroup();
        SearchDialog = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        FieldSearch = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        CheckBoxAddress = new javax.swing.JCheckBox();
        CheckBoxName = new javax.swing.JCheckBox();
        CheckBoxYear = new javax.swing.JCheckBox();
        btnSearchDialog = new javax.swing.JButton();
        btnCancelDialog = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnCancelSearch = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnResidentUndo = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        FieldPhone = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        BirthdayChooser = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        CheckBoxMale = new javax.swing.JCheckBox();
        CheckBoxFemale = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        FieldName = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        FieldAddress = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableResident = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        FieldWage = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        FieldRole = new javax.swing.JTextField();
        FieldMa = new javax.swing.JTextField();
        btnSortByName = new javax.swing.JButton();
        btnSortByDob = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();

        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        jLabel1.setText("Tìm Kiếm ");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(150, 10, 110, 40);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Nhập Nội Dung Tìm Kiếm :");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 70, 280, 40);

        FieldSearch.setBorder(new javax.swing.border.MatteBorder(null));
        FieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldSearchActionPerformed(evt);
            }
        });
        jPanel1.add(FieldSearch);
        FieldSearch.setBounds(50, 110, 290, 40);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Chọn Tiêu Chí Tìm Kiếm :");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 160, 280, 40);

        btnGroupSearch.add(CheckBoxAddress);
        CheckBoxAddress.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        CheckBoxAddress.setText("Quê Quán");
        CheckBoxAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBoxAddressActionPerformed(evt);
            }
        });
        jPanel1.add(CheckBoxAddress);
        CheckBoxAddress.setBounds(140, 220, 100, 30);

        btnGroupSearch.add(CheckBoxName);
        CheckBoxName.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        CheckBoxName.setText("Tên");
        CheckBoxName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBoxNameActionPerformed(evt);
            }
        });
        jPanel1.add(CheckBoxName);
        CheckBoxName.setBounds(10, 220, 100, 30);

        btnGroupSearch.add(CheckBoxYear);
        CheckBoxYear.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        CheckBoxYear.setText("Năm Sinh");
        CheckBoxYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBoxYearActionPerformed(evt);
            }
        });
        jPanel1.add(CheckBoxYear);
        CheckBoxYear.setBounds(290, 220, 100, 30);

        btnSearchDialog.setBackground(new java.awt.Color(255, 255, 255, 0));
        btnSearchDialog.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnSearchDialog.setText("Tìm kiếm");
        btnSearchDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.add(btnSearchDialog);
        btnSearchDialog.setBounds(40, 260, 140, 50);
        btnSearchDialog.setBorder(new RoundedBorder(20));
        btnSearchDialog.setOpaque(false);

        btnCancelDialog.setBackground(new java.awt.Color(255, 255, 255, 0));
        btnCancelDialog.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnCancelDialog.setText("Hủy");
        btnCancelDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.add(btnCancelDialog);
        btnCancelDialog.setBounds(240, 260, 140, 50);
        btnCancelDialog.setBorder(new RoundedBorder(20));
        btnCancelDialog.setOpaque(false);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/search.png"))); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 110, 37, 40);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewSearchView.png"))); // NOI18N
        jPanel1.add(jLabel5);
        jLabel5.setBounds(0, 0, 430, 320);

        javax.swing.GroupLayout SearchDialogLayout = new javax.swing.GroupLayout(SearchDialog.getContentPane());
        SearchDialog.getContentPane().setLayout(SearchDialogLayout);
        SearchDialogLayout.setHorizontalGroup(
            SearchDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchDialogLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        SearchDialogLayout.setVerticalGroup(
            SearchDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchDialogLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(51, 153, 255));

        btnCancelSearch.setBackground(new java.awt.Color(0, 0, 102));
        btnCancelSearch.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnCancelSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelSearch.setText("Hủy Tìm Kiếm");
        btnCancelSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelSearchActionPerformed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(0, 0, 102));
        btnAdd.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(0, 0, 102));
        btnEdit.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Sửa");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(0, 0, 102));
        btnSearch.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("Tìm Kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnResidentUndo.setBackground(new java.awt.Color(0, 0, 102));
        btnResidentUndo.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnResidentUndo.setForeground(new java.awt.Color(255, 255, 255));
        btnResidentUndo.setText("Quay Lại\n");

        btnDelete.setBackground(new java.awt.Color(0, 0, 102));
        btnDelete.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(btnCancelSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResidentUndo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnCancelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(btnResidentUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jPanel2.add(jPanel3);
        jPanel3.setBounds(0, 0, 210, 670);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setText("Lương :");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(680, 210, 100, 40);

        FieldPhone.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        FieldPhone.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 51, 102)));
        FieldPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldPhoneActionPerformed(evt);
            }
        });
        jPanel2.add(FieldPhone);
        FieldPhone.setBounds(790, 340, 210, 40);
        FieldPhone.setOpaque(false);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setText("Ngày Sinh :");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(270, 230, 100, 40);

        BirthdayChooser.setBackground(new java.awt.Color(0, 204, 255));
        BirthdayChooser.setForeground(new java.awt.Color(102, 255, 255));
        BirthdayChooser.setDateFormatString("dd/MM/yyyy");
        BirthdayChooser.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        jPanel2.add(BirthdayChooser);
        BirthdayChooser.setBounds(390, 230, 220, 40);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setText("Giới tính:");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(280, 290, 80, 40);

        btnGroupSex.add(CheckBoxMale);
        CheckBoxMale.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        CheckBoxMale.setText("Nam");
        CheckBoxMale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CheckBoxMale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBoxMaleActionPerformed(evt);
            }
        });
        jPanel2.add(CheckBoxMale);
        CheckBoxMale.setBounds(390, 300, 85, 20);
        CheckBoxMale.setOpaque(false);

        btnGroupSex.add(CheckBoxFemale);
        CheckBoxFemale.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        CheckBoxFemale.setText("Nữ");
        CheckBoxFemale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CheckBoxFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBoxFemaleActionPerformed(evt);
            }
        });
        jPanel2.add(CheckBoxFemale);
        CheckBoxFemale.setBounds(480, 300, 85, 20);
        CheckBoxFemale.setOpaque(false);

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setText("Họ Và Tên :");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(270, 150, 100, 40);

        FieldName.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        FieldName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 51, 102)));
        FieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldNameActionPerformed(evt);
            }
        });
        jPanel2.add(FieldName);
        FieldName.setBounds(390, 150, 220, 40);
        FieldPhone.setOpaque(false);

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setText("Số Điện Thoại :");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(650, 340, 130, 40);

        FieldAddress.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        FieldAddress.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 51, 102)));
        FieldAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldAddressActionPerformed(evt);
            }
        });
        jPanel2.add(FieldAddress);
        FieldAddress.setBounds(780, 150, 220, 40);
        FieldPhone.setOpaque(false);

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel12.setText("Quản Lí Nhân Viên");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(360, 10, 370, 70);

        jScrollPane1.setBackground(new java.awt.Color(0, 51, 153, 125));

        tableResident.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        tableResident.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            }, columnNames
        ));
        tableResident.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        tableResident.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tableResident.setRowHeight(30);
        jScrollPane1.setViewportView(tableResident);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(240, 400, 770, 260);

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel13.setText("Địa Chỉ :");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(680, 150, 100, 40);

        FieldWage.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        FieldWage.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 51, 102)));
        FieldWage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldWageActionPerformed(evt);
            }
        });
        jPanel2.add(FieldWage);
        FieldWage.setBounds(780, 210, 220, 40);
        FieldPhone.setOpaque(false);

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel14.setText("Phòng Ban :");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(670, 280, 110, 40);

        FieldRole.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        FieldRole.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 51, 102)));
        FieldRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldRoleActionPerformed(evt);
            }
        });
        jPanel2.add(FieldRole);
        FieldRole.setBounds(790, 280, 210, 40);
        FieldPhone.setOpaque(false);

        FieldMa.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel2.add(FieldMa);
        FieldMa.setBounds(260, 70, 60, 40);

        btnSortByName.setBackground(new java.awt.Color(0, 0, 102));
        btnSortByName.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnSortByName.setForeground(new java.awt.Color(255, 255, 255));
        btnSortByName.setText("Sắp Xếp Theo Tên");
        jPanel2.add(btnSortByName);
        btnSortByName.setBounds(250, 350, 150, 40);

        btnSortByDob.setBackground(new java.awt.Color(0, 0, 102));
        btnSortByDob.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnSortByDob.setForeground(new java.awt.Color(255, 255, 255));
        btnSortByDob.setText("Sắp Xếp Theo Năm Sinh");
        jPanel2.add(btnSortByDob);
        btnSortByDob.setBounds(440, 350, 190, 40);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Lovepik_com-500330964-blue-blazed-background.jpg"))); // NOI18N
        jPanel2.add(jLabel15);
        jLabel15.setBounds(0, 0, 1020, 680);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1013, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CheckBoxAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CheckBoxAddressActionPerformed

    private void CheckBoxNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CheckBoxNameActionPerformed

    private void CheckBoxYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CheckBoxYearActionPerformed

    private void btnCancelSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelSearchActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnResidentUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResidentUndoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResidentUndoActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void FieldPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldPhoneActionPerformed

    private void CheckBoxMaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxMaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CheckBoxMaleActionPerformed

    private void CheckBoxFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxFemaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CheckBoxFemaleActionPerformed

    private void FieldNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldNameActionPerformed

    private void FieldAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldAddressActionPerformed

    private void FieldWageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldWageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldWageActionPerformed

    private void FieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldSearchActionPerformed

    private void FieldRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldRoleActionPerformed

 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser BirthdayChooser;
    private javax.swing.JCheckBox CheckBoxAddress;
    private javax.swing.JCheckBox CheckBoxFemale;
    private javax.swing.JCheckBox CheckBoxMale;
    private javax.swing.JCheckBox CheckBoxName;
    private javax.swing.JCheckBox CheckBoxYear;
    private javax.swing.JTextField FieldAddress;
    private javax.swing.JTextField FieldMa;
    private javax.swing.JTextField FieldName;
    private javax.swing.JTextField FieldPhone;
    private javax.swing.JTextField FieldRole;
    private javax.swing.JTextField FieldSearch;
    private javax.swing.JTextField FieldWage;
    private javax.swing.JDialog SearchDialog;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancelDialog;
    private javax.swing.JButton btnCancelSearch;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.ButtonGroup btnGroupSearch;
    private javax.swing.ButtonGroup btnGroupSex;
    private javax.swing.JButton btnResidentUndo;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSearchDialog;
    private javax.swing.JButton btnSortByDob;
    private javax.swing.JButton btnSortByName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableResident;
    // End of variables declaration//GEN-END:variables
}

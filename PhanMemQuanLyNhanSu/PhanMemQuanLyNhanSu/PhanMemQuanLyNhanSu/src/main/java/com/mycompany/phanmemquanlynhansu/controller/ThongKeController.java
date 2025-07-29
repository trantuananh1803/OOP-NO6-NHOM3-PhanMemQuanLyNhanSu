package com.mycompany.phanmemquanlynhansu.controller;

import com.mycompany.phanmemquanlynhansu.action.NhanVienService;
import com.mycompany.phanmemquanlynhansu.entity.NhanVien;
import com.mycompany.phanmemquanlynhansu.view.ThongKeView;
import java.awt.Color;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit; // Thêm import này
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ThongKeController {

    private final ThongKeView view;
    private final NhanVienService service;
    private final List<NhanVien> danhSachNhanVien;

    public ThongKeController(ThongKeView view) {
        this.view = view;
        this.service = new NhanVienService();
        this.danhSachNhanVien = service.getAll();

        initListeners();
        thongKeTheoChucVu();
    }

    private void initListeners() {
        view.addThongKeTuoiListener(e -> thongKeTheoTuoi());
        view.addThongKeChucVuListener(e -> thongKeTheoChucVu());
        view.addQuayLaiListener(e -> view.dispose());
    }

    private void thongKeTheoChucVu() {
        String[] columnNames = {"Chức Vụ", "Số Lượng"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        Map<String, Long> countByChucVu = danhSachNhanVien.stream()
                .collect(Collectors.groupingBy(NhanVien::getPhongBan, Collectors.counting()));
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Long> entry : countByChucVu.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
            dataset.setValue(entry.getValue(), "Số lượng", entry.getKey());
        }

        view.setDataForTable(tableModel);
        JFreeChart barChart = createBarChart(dataset, "Biểu đồ số lượng nhân viên theo chức vụ");
        view.setChart(new ChartPanel(barChart));
    }
    
    private void thongKeTheoTuoi() {
        String[] columnNames = {"Nhóm Tuổi", "Số Lượng"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        long duoi25 = danhSachNhanVien.stream().filter(nv -> getAge(nv) < 25).count();
        long tu25den35 = danhSachNhanVien.stream().filter(nv -> getAge(nv) >= 25 && getAge(nv) <= 35).count();
        long tren35 = danhSachNhanVien.stream().filter(nv -> getAge(nv) > 35).count();
        
        tableModel.addRow(new Object[]{"Dưới 25", duoi25});
        tableModel.addRow(new Object[]{"Từ 25-35", tu25den35});
        tableModel.addRow(new Object[]{"Trên 35", tren35});

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(duoi25, "Số lượng", "Dưới 25");
        dataset.setValue(tu25den35, "Số lượng", "Từ 25-35");
        dataset.setValue(tren35, "Số lượng", "Trên 35");

        view.setDataForTable(tableModel);
        JFreeChart barChart = createBarChart(dataset, "Biểu đồ số lượng nhân viên theo độ tuổi");
        view.setChart(new ChartPanel(barChart));
    }
    
    private JFreeChart createBarChart(DefaultCategoryDataset dataset, String title) {
        JFreeChart barChart = ChartFactory.createBarChart(
                title.toUpperCase(), "", "Số Lượng", dataset,
                PlotOrientation.VERTICAL, false, true, false);
        
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.getRenderer().setSeriesPaint(0, new Color(79, 129, 189));
        
       
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        
        rangeAxis.setRange(0, 9.5); 
       
        rangeAxis.setTickUnit(new NumberTickUnit(1));
        
        return barChart;
    }

    private int getAge(NhanVien nv) {
        if (nv.getNgaySinh() == null) return 0;
        LocalDate birthDate = new java.sql.Date(nv.getNgaySinh().getTime()).toLocalDate();
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
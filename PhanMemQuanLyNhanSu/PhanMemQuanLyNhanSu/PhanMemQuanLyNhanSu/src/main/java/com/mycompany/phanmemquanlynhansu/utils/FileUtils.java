/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.phanmemquanlynhansu.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Lớp tiện ích để xử lý việc đọc và ghi file XML sử dụng JAXB.
 * Phiên bản này được cải tiến để hiển thị lỗi trực tiếp lên màn hình.
 */
public class FileUtils {

    /**
     * Ghi một đối tượng vào file XML.
     */
    public static <T> void saveToXML(String filePath, T object, Class<T> clazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                marshaller.marshal(object, writer);
            }

        } catch (Exception e) {
            // HIỂN THỊ LỖI LÊN MÀN HÌNH - BƯỚC QUAN TRỌNG NHẤT
            showErrorDialog("Lỗi khi ghi file XML", e);
        }
    }

    /**
     * Đọc một đối tượng từ file XML.
     */
    @SuppressWarnings("unchecked")
    public static <T> T loadFromXML(String filePath, Class<T> clazz) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }

            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            return (T) unmarshaller.unmarshal(file);

        } catch (Exception e) {
            // HIỂN THỊ LỖI LÊN MÀN HÌNH
            showErrorDialog("Lỗi khi đọc file XML", e);
            return null;
        }
    }

    /**
     * Hiển thị một hộp thoại thông báo lỗi chi tiết.
     * @param title Tiêu đề của hộp thoại.
     * @param e Ngoại lệ (Exception) đã xảy ra.
     */
    private static void showErrorDialog(String title, Exception e) {
        // In lỗi ra console để debug sâu hơn
        e.printStackTrace();

        // Lấy thông tin chi tiết của lỗi
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String errorMessage = "Lỗi: " + e.getMessage() + "\n\n" +
                              "Chi tiết (xem thêm trong Console):\n" + sw.toString().substring(0, Math.min(sw.toString().length(), 400)) + "...";

        // Hiển thị hộp thoại lỗi
        JOptionPane.showMessageDialog(null, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }
}

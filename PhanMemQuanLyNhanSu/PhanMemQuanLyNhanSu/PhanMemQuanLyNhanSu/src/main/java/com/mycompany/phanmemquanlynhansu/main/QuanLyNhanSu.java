/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.phanmemquanlynhansu.main;

import com.mycompany.phanmemquanlynhansu.controller.LoginController;
import com.mycompany.phanmemquanlynhansu.view.LoginView;
import javax.swing.SwingUtilities;

public class QuanLyNhanSu {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }
}

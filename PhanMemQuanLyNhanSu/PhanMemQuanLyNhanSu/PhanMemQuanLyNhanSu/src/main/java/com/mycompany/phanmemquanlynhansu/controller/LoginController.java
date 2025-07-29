package com.mycompany.phanmemquanlynhansu.controller;

import com.mycompany.phanmemquanlynhansu.view.LoginView;
import com.mycompany.phanmemquanlynhansu.view.MainView;
import com.mycompany.phanmemquanlynhansu.action.UserService;
import com.mycompany.phanmemquanlynhansu.entity.User;

import javax.swing.*;

public class LoginController {
    private LoginView loginView;
    private UserService userService;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userService = new UserService();
        initView();
    }

    private void initView() {
        loginView.getbtnLogin().addActionListener(e -> {
            String username = loginView.getTxtUsername().getText().trim();
            String password = new String(loginView.getTxtPassword().getPassword());

            User user = userService.login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(loginView, "Đăng nhập thành công!");
                loginView.dispose();
                MainView mainView = new MainView();
                new MainController(mainView);
                mainView.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(loginView, "Sai tài khoản hoặc mật khẩu!");
            }
        });
    }
}

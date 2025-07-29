/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.phanmemquanlynhansu.action;

import com.mycompany.phanmemquanlynhansu.entity.User;

public class UserService {

    private final String USERNAME = "admin";
    private final String PASSWORD = "123";

    public User login(String username, String password) {
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            return new User(username, password); // trả về đối tượng User
        }
        return null;
    }
}

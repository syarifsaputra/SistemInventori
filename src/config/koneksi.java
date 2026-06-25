/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {
    private static Connection mysqlconfig;
    
    public static Connection configDB() throws SQLException {
        try {
            // Hanya buat koneksi baru kalau belum ada atau koneksi lama sudah putus/tertutup.
            // Ini mencegah aplikasi membuat koneksi baru terus-menerus setiap method dipanggil
            // (penyebab error "Too many connections" di MySQL).
            if (mysqlconfig == null || mysqlconfig.isClosed()) {
                // Ganti 'nama_db_anda' dengan nama database yang Anda buat di phpMyAdmin
                String url = "jdbc:mysql://localhost:3306/sistem_inventori";
                String user = "root";
                String pass = "";

                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                mysqlconfig = DriverManager.getConnection(url, user, pass);
            }
        } catch (Exception e) {
            System.err.println("Koneksi Gagal: " + e.getMessage());
        }
        return mysqlconfig;
    }    
}
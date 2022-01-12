package com.Tubes;

import java.sql.Connection;
import java.sql.DriverManager;

public class koneksi {
    private static  java.sql.Connection koneksi;
    public  static Connection getKoneksi(){
        if (koneksi == null) {
            try {
                String url = "jdbc:mysql://localhost/parkir";
                String user = "root";
                String pass = "";
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                koneksi = DriverManager.getConnection(url, user, pass);
                // Jika gagal koneksi
            } catch (Exception e) {
                System.out.println("Gagal koneksi " + e);
            }
        }
        return koneksi;
    }
}

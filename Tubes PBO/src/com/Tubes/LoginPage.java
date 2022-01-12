package com.Tubes;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {
    Image icon = Toolkit.getDefaultToolkit().getImage("D:\\Project\\Tubes PBO\\resource/parking-icon.png");

    private JTextField usernameField;
    private JPanel Login;
    private JPanel Header;
    private JFrame frame;
    private JButton LOGINButton;
    private JPasswordField passwordField;

    LoginPage() {
        add(Login);
        setTitle("Sistem Parkir Kendaraan");
        setSize(550, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().setDefaultButton(LOGINButton);
        setIconImage(icon);
        setLocationRelativeTo(null);
        setVisible(true);

        LOGINButton.addActionListener(e -> {
            try {
                Connection c = koneksi.getKoneksi();
                Statement s = c.createStatement();

                Parkir admin = new Parkir(usernameField.getText(), passwordField.getText(), null, null,
                        null,
                        null, 0, null, null);

                String sql = "SELECT * FROM admin WHERE username = '" + admin.getUsername() +
                        "' AND password='" + admin.getPassword() + "'";
                ResultSet r = s.executeQuery(sql);

                if (r.next()) {
                    dispose();
                    new Utama();
                } else {
                    JOptionPane.showMessageDialog(null, "Login Gagal");
                    passwordField.requestFocus();
                }
            } catch (SQLException ex) {
                System.out.println("error");
            }
        });
    }
}

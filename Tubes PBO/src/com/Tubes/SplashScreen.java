package com.Tubes;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SplashScreen extends JFrame {
    Image icon = Toolkit.getDefaultToolkit().getImage("D:\\Project\\Tubes PBO\\resource/parking-icon.png");

    private JPanel SplashPanel;
    private JPanel loadingText;
    private JProgressBar progressBar1;
    private JLabel textloading;
    private JLabel loadingProg;
    private JPanel progressPanel;
    private JLabel pict;
    private JPanel fotoPanel;

    SplashScreen() {
        SplashPanel.setBackground(Color.CYAN);
        loadingText.setBackground(Color.CYAN);

        fotoPanel.setPreferredSize(new Dimension(550, 150));
        pict.setIcon(new ImageIcon(new ImageIcon("D:\\Project\\Tubes PBO\\resource/parking-icon.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));

        fotoPanel.setSize(550, 20);
        fotoPanel.setBackground(Color.CYAN);

        setBackground(Color.CYAN);
        add(SplashPanel);
        setTitle("Sistem Parkir Kendaraan");
        setSize(550, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(icon);
        setLocationRelativeTo(null);
        setVisible(true);

        try {
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(50);
                loadingProg.setText(i + " %");
                progressBar1.setValue(i);
                progressBar1.setForeground(Color.green);

                if (i <= 25) {
                    textloading.setText("Preparing...");
                } else if (i <= 50) {
                    textloading.setText("Make GUI...");
                } else if (i <= 75) {
                    textloading.setText("Connecting Database...");
                } else if (i <= 95) {
                    textloading.setText("Finished...");
                } else if (i == 100){
                    dispose();
                    new LoginPage();
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static void main(String[] args) {
        new SplashScreen();
    }
}

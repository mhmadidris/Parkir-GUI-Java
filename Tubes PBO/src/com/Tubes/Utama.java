package com.Tubes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Utama extends JFrame {
    Image icon = Toolkit.getDefaultToolkit().getImage("D:\\Project\\Tubes PBO\\resource/parking-icon.png");

    private JTabbedPane tabPane;
    private JPanel utama;
    private JTextField platMasuk;
    private JComboBox jenisBox;
    private JButton parkirButton;
    private JButton resetButton;
    private JPanel MasukTab;
    private JPanel KeluarTab;
    private JTextField platCari;
    private JLabel platDisplay;
    private JLabel jenisDisplay;
    private JLabel waktu_keluarDisplay;
    private JLabel waktu_masukDisplay;
    private JLabel durasiDisplay;
    private JLabel hargaDisplay;
    private JLabel mobilHargaJam;
    private JLabel motorHargaDisplay;
    private JButton OKEButton;
    private JButton resetButton1;
    private JButton hitungButton;
    private JPanel ViewTab;
    private JTable table1;

    public void refreshTable() {
        try {
            Connection c = koneksi.getKoneksi();
            Statement stm = c.createStatement();

            String query = "SELECT * FROM kendaraan";
            ResultSet rs = stm.executeQuery(query);

            String[] kolom = { "Nomor", "Plat Nomor", "Jenis", "Waktu Masuk", "Waktu Keluar", "Biaya", "Status" };
            DefaultTableModel tableModel = new DefaultTableModel(kolom, 0);

            int i = 1;

            while (rs.next()) {

                Object[] kendaraan = {
                        i,
                        rs.getString("plat_nomor"),
                        rs.getString("jenis"),
                        rs.getTimestamp("waktu_masuk"),
                        rs.getTimestamp("waktu_keluar"),
                        rs.getInt("biaya"),
                        rs.getString("keterangan")
                } ;

                tableModel.addRow(kendaraan);
                i++;
            }
            table1.setModel(tableModel);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void harga() {
        try {
            Connection c = koneksi.getKoneksi();
            Statement stm = c.createStatement();

            // Mobil
            String query1 = "SELECT harga FROM jeniskendaraan WHERE jenis = 'Mobil'";
            ResultSet rsMobil = stm.executeQuery(query1);
            while(rsMobil.next()){
                String mobil = rsMobil.getString("harga");
                mobilHargaJam.setText("Rp." + mobil + "/jam");
            }

            // Motor
            String query2 = "SELECT harga FROM jeniskendaraan WHERE jenis = 'Motor'";
            ResultSet rsMotor = stm.executeQuery(query2);
            while(rsMotor.next()){
                String motor = rsMotor.getString("harga");
                motorHargaDisplay.setText("Rp." + motor + "/jam");
            }
        } catch (Exception ignored) {}
    }

    public Utama() {
        harga();

        add(utama);
        setTitle("Sistem Parkir Kendaraan");
        setSize(750, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().setDefaultButton(parkirButton);
        setIconImage(icon);
        setLocationRelativeTo(null);
        setVisible(true);

        String keteranganParkirMasuk = "Parkir";
        String keteranganParkirKeluar = "Keluar";
        Timestamp masukParkir = Timestamp.from(ZonedDateTime.now().toInstant());
        Timestamp keluarParkir = Timestamp.from(ZonedDateTime.now().toInstant());

        Parkir parkir = new Parkir(null, null, platMasuk.getText(), (String) jenisBox.getSelectedItem(), masukParkir,
                keluarParkir, 0, keteranganParkirMasuk, keteranganParkirKeluar);

        // Tombol Masuk Parkir
        parkirButton.addActionListener(e -> {
            try {
                Connection c = koneksi.getKoneksi();
                PreparedStatement stm = c.prepareStatement("INSERT INTO kendaraan (plat_nomor, jenis, " +
                        "waktu_masuk, biaya, keterangan) VALUES(?,?,?,?,?)");
                stm.setString(1, platMasuk.getText());
                stm.setString(2, (String) jenisBox.getSelectedItem());
                stm.setTimestamp(3, parkir.getMasuk());
                stm.setInt(4, parkir.getBiaya());
                stm.setString(5, parkir.getKeteranganMasuk());
                if (stm.executeUpdate() > 0) {
                    platMasuk.setText(null);
                    jenisBox.setSelectedItem("Mobil");
                    JOptionPane.showMessageDialog(null, "Data berhasil disimpan", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menyimpan data", "Pesan",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                System.out.println("error" + ex);
            }
            refreshTable();
        });

        // Tombol Reset
        resetButton.addActionListener(e -> platMasuk.setText(null));

        // Tombol Hitung
        hitungButton.addActionListener(e -> {
            try {
                Connection c = koneksi.getKoneksi();
                String sql=
                        "UPDATE kendaraan SET waktu_keluar=? WHERE " +
                                "plat_nomor='" + platCari.getText() + "' AND keterangan = 'Parkir'";
                PreparedStatement st = c.prepareStatement(sql);
                st.setTimestamp(1, parkir.getKeluar());
                st.executeUpdate();

                if (st.executeUpdate() > 0) {
                    Connection con = koneksi.getKoneksi();
                    ResultSet rs = con.createStatement().executeQuery("SELECT * FROM kendaraan WHERE " +
                            "plat_nomor='" + platCari.getText() + "'");
                    if (rs.next()) {
                        platDisplay.setText(rs.getString(2));
                        jenisDisplay.setText(rs.getString(3));
                        waktu_masukDisplay.setText(rs.getString(4));
                        waktu_keluarDisplay.setText(rs.getString(5));

                        // Menghitung Durasi Waktu
                        Timestamp timestamp1 = rs.getTimestamp(4);
                        Timestamp timestamp2 = rs.getTimestamp(5);
                        long milliseconds = timestamp2.getTime() - timestamp1.getTime();
                        int seconds = (int) milliseconds / 1000;
                        int hours = seconds / 3600;
                        durasiDisplay.setText(hours + " Jam");

                        // Menghitung Total Harga Mobil
                        String a1 = mobilHargaJam.getText();
                        String a2 = a1.replace("Rp.", "");
                        String a3 = a2.replace("/jam", "");
                        int a4 = Integer.parseInt(a3);

                        if (Objects.equals(rs.getString(3), "Mobil")) {
                            NumberFormat df = DecimalFormat.getInstance();
/*                            df.setMinimumFractionDigits(4);*/
                            df.setMaximumFractionDigits(4);
                            df.setRoundingMode(RoundingMode.UP);
                            hargaDisplay.setText("Rp." + df.format((double) hours * a4));
                        }

                        // Menghitung Total Harga Motor
                        String b1 = motorHargaDisplay.getText();
                        String b2 = b1.replace("Rp.", "");
                        String b3 = b2.replace("/jam", "");
                        int b4 = Integer.parseInt(b3);

                        if (Objects.equals(rs.getString(3), "Motor")) {
                            NumberFormat df = DecimalFormat.getInstance();
                            df.setMaximumFractionDigits(4);
                            df.setRoundingMode(RoundingMode.UP);

                            hargaDisplay.setText("Rp." + df.format((double) hours * b4));
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Kendaraan tidak ditemukan", "Pesan",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception ignored){

            }
            refreshTable();
        });

        // Tombol Oke
        OKEButton.addActionListener(e -> {
            String convert = hargaDisplay.getText();
            String removeRp = convert.replace("Rp.", "");
            String result = removeRp.replace(",", "");

            try {
                Connection c = koneksi.getKoneksi();
                String sql=
                        "UPDATE kendaraan SET biaya = ?, keterangan = ? WHERE " +
                                "plat_nomor='" + platCari.getText() + "'";
                PreparedStatement st = c.prepareStatement(sql);
                st.setString(1, result);
                st.setString(2, parkir.getKeteranganKeluar());
                st.executeUpdate();

                if (st.executeUpdate() > 0) {
                    platCari.setText(null);
                    platDisplay.setText("-");
                    jenisDisplay.setText("-");
                    waktu_masukDisplay.setText("-");
                    waktu_keluarDisplay.setText("-");
                    durasiDisplay.setText("-");
                    hargaDisplay.setText("-");
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal update data", "Pesan",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception ignored){

            }
            refreshTable();
        });

        // Tombol Reset
        resetButton1.addActionListener(e -> {
            platCari.setText(null);
            platDisplay.setText("-");
            jenisDisplay.setText("-");
            waktu_masukDisplay.setText("-");
            waktu_keluarDisplay.setText("-");
            durasiDisplay.setText("-");
            hargaDisplay.setText("-");
        });

        // Table
        table1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                try {
                    Connection c = koneksi.getKoneksi();
                    Statement stm = c.createStatement();

                    String query = "SELECT * FROM kendaraan";
                    ResultSet rs = stm.executeQuery(query);

                    String[] kolom = { "Nomor", "Plat Nomor", "Jenis", "Waktu Masuk", "Waktu Keluar", "Biaya",
                            "Status" };
                    DefaultTableModel tableModel = new DefaultTableModel(kolom, 0);

                    int i = 1;

                    while (rs.next()) {

                        Object[] kendaraan = {
                                i,
                                rs.getString("plat_nomor"),
                                rs.getString("jenis"),
                                rs.getTimestamp("waktu_masuk"),
                                rs.getTimestamp("waktu_keluar"),
                                rs.getInt("biaya"),
                                rs.getString("keterangan")
                        } ;

                        tableModel.addRow(kendaraan);
                        i++;
                    }
                    table1.setModel(tableModel);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}

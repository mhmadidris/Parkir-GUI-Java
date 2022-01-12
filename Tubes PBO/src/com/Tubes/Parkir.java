package com.Tubes;

import java.sql.Timestamp;

public class Parkir extends Admin {
    String plat, jenis, keteranganMasuk, keteranganKeluar;
    int biaya;
    Timestamp masuk, keluar;

    Parkir(String username, String password, String plat, String jenis, Timestamp masuk, Timestamp keluar, int biaya, String keteranganMasuk, String keteranganKeluar) {
        super(username, password);
        this.plat = plat;
        this.jenis = jenis;
        this.masuk = masuk;
        this.keluar = keluar;
        this.biaya = biaya;
        this.keteranganMasuk = keteranganMasuk;
        this.keteranganKeluar = keteranganKeluar;
    }

    //  Method Getter
    public String getPlat() {
        return plat;
    }
    public String getJenis() {
        return jenis;
    }
    public int getBiaya() {
        return biaya;
    }
    public String getKeteranganMasuk() {
        return keteranganMasuk;
    }
    public String getKeteranganKeluar() {
        return keteranganKeluar;
    }
    public Timestamp getMasuk() {
        return masuk;
    }
    public Timestamp getKeluar() {
        return keluar;
    }
}

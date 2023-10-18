package com.mywifi.mywifi.Model;

public class Model_Pemasangan {
    public String gambar;
    public String id_user;
    public String jenis;
    public String tanggal;

    public String getIdMaintenance() {
        return idMaintenance;
    }

    public void setIdMaintenance(String idMaintenance) {
        this.idMaintenance = idMaintenance;
    }

    public String idMaintenance;
    public boolean show;
    public int status;

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Model_Pemasangan() {
    }

    public Model_Pemasangan(String gambar, String id_user, String jenis, String tanggal, boolean show, int status, String idMaintenance) {
        this.gambar = gambar;
        this.id_user = id_user;
        this.jenis = jenis;
        this.tanggal = tanggal;
        this.show = show;
        this.status = status;
        this.idMaintenance = idMaintenance;
    }
}

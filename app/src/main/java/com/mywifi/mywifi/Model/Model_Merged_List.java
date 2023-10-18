package com.mywifi.mywifi.Model;

import java.io.Serializable;

public class Model_Merged_List implements Serializable {
    public String idMaintenance;

    public Model_Merged_List(String idMaintenance, String tanggal, String gambar, String id_user, String jenis, int status, boolean show) {
        this.idMaintenance = idMaintenance;
        this.tanggal = tanggal;
        this.gambar = gambar;
        this.id_user = id_user;
        this.jenis = jenis;
        this.status = status;
        this.show = show;
    }

    public String getIdMaintenance() {
        return idMaintenance;
    }

    public void setIdMaintenance(String idMaintenance) {
        this.idMaintenance = idMaintenance;
    }

    public String tanggal;
    public String gambar;
    public String id_user;

    public String getJenis(){
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String jenis;
    public int status;
    public boolean show;

    public Model_Merged_List() {
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Model_Merged_List(String tanggal, String gambar, String id_user, String jenis,int status, boolean show) {
        this.tanggal = tanggal;
        this.gambar = gambar;
        this.id_user = id_user;
        this.status = status;
        this.show = show;
        this.jenis = jenis;
    }
}

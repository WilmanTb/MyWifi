package com.mywifi.mywifi.Model;

import java.io.Serializable;

public class Model_Verifikasi implements Serializable {

    public String id_user, ktp, status, show;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public Model_Verifikasi() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public Model_Verifikasi(String id_user, String ktp, String status, String show) {
        this.id_user = id_user;
        this.ktp = ktp;
        this.status = status;
        this.show = show;
    }
}

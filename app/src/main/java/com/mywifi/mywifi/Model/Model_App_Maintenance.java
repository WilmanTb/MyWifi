package com.mywifi.mywifi.Model;

public class Model_App_Maintenance {
    public String tanggal;
    public boolean show;
    public int status;

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

    public Model_App_Maintenance() {
    }

    public Model_App_Maintenance(String tanggal, boolean show, int status) {
        this.tanggal = tanggal;
        this.show = show;
        this.status = status;
    }
}

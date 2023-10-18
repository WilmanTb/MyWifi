package com.mywifi.mywifi.Model;

public class Model_Tgl_Maintenance {
    public String tgl1, tgl2, tgl3, tgl4;

    public String getTgl1() {
        return tgl1;
    }

    public void setTgl1(String tgl1) {
        this.tgl1 = tgl1;
    }

    public String getTgl2() {
        return tgl2;
    }

    public void setTgl2(String tgl2) {
        this.tgl2 = tgl2;
    }

    public String getTgl3() {
        return tgl3;
    }

    public void setTgl3(String tgl3) {
        this.tgl3 = tgl3;
    }

    public String getTgl4() {
        return tgl4;
    }

    public void setTgl4(String tgl4) {
        this.tgl4 = tgl4;
    }

    public Model_Tgl_Maintenance() {
    }

    public Model_Tgl_Maintenance(String tgl1, String tgl2, String tgl3, String tgl4) {
        this.tgl1 = tgl1;
        this.tgl2 = tgl2;
        this.tgl3 = tgl3;
        this.tgl4 = tgl4;
    }
}

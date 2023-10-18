package com.mywifi.mywifi.Model;

import java.io.Serializable;

public class Model_Gangguan implements Serializable {

    public String id_user;
    public String tanggal;
    public String keterangan;
    public String status;

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public Model_Gangguan(String id_user, String tanggal, String keterangan, String status, String jam, String hp, String idGangguan) {
        this.id_user = id_user;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.status = status;
        this.jam = jam;
        this.hp = hp;
        this.idGangguan = idGangguan;
    }

    public String jam;
    public String hp;

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public Model_Gangguan(String id_user, String tanggal, String keterangan, String status, String jam, String idGangguan) {
        this.id_user = id_user;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.status = status;
        this.jam = jam;
        this.idGangguan = idGangguan;
    }

    public String getIdGangguan() {
        return idGangguan;
    }

    public void setIdGangguan(String idGangguan) {
        this.idGangguan = idGangguan;
    }

    public String idGangguan;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Model_Gangguan() {
    }

    public Model_Gangguan(String id_user,String idGangguan, String tanggal, String keterangan, String status) {
        this.id_user = id_user;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.status = status;
        this.idGangguan = idGangguan;
    }
}

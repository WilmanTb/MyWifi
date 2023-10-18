package com.mywifi.mywifi.Model;

import java.io.Serializable;

public class Model_Upgrade implements Serializable {
    public String current_layanan;
    public String upgrade_layanan;
    public String harga;
    public String tanggal;
    public String nama_pelanggan;
    public String no_rumah;
    public String blok_rumah;
    public String status;
    public String id_pelanggan;

    public String getIdUpgrade() {
        return idUpgrade;
    }

    public void setIdUpgrade(String idUpgrade) {
        this.idUpgrade = idUpgrade;
    }

    public String idUpgrade;

    public String getCurrent_layanan() {
        return current_layanan;
    }

    public void setCurrent_layanan(String current_layanan) {
        this.current_layanan = current_layanan;
    }

    public String getUpgrade_layanan() {
        return upgrade_layanan;
    }

    public void setUpgrade_layanan(String upgrade_layanan) {
        this.upgrade_layanan = upgrade_layanan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getNo_rumah() {
        return no_rumah;
    }

    public void setNo_rumah(String no_rumah) {
        this.no_rumah = no_rumah;
    }

    public String getBlok_rumah() {
        return blok_rumah;
    }

    public void setBlok_rumah(String blok_rumah) {
        this.blok_rumah = blok_rumah;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public Model_Upgrade() {
    }

    public Model_Upgrade(String current_layanan, String upgrade_layanan, String harga, String tanggal, String nama_pelanggan, String no_rumah, String blok_rumah, String status, String id_pelanggan, String idUpgrade) {
        this.current_layanan = current_layanan;
        this.upgrade_layanan = upgrade_layanan;
        this.harga = harga;
        this.tanggal = tanggal;
        this.nama_pelanggan = nama_pelanggan;
        this.no_rumah = no_rumah;
        this.blok_rumah = blok_rumah;
        this.status = status;
        this.id_pelanggan = id_pelanggan;
        this.idUpgrade = idUpgrade;
    }
}

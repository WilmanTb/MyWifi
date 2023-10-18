package com.mywifi.mywifi.Model;

import java.io.Serializable;

public class Model_Pemesanan implements Serializable {
    public String nama_layanan, harga, tanggal, nama_pelanggan, no_rumah, blok_rumah, status, id_pelanggan, tanggal_survei;

    public String getId_pelanggan() {
        return id_pelanggan;
    }

    public Model_Pemesanan(String nama_layanan, String harga, String tanggal, String nama_pelanggan, String no_rumah, String blok_rumah, String status, String id_pelanggan, String tanggal_survei) {
        this.nama_layanan = nama_layanan;
        this.harga = harga;
        this.tanggal = tanggal;
        this.nama_pelanggan = nama_pelanggan;
        this.no_rumah = no_rumah;
        this.blok_rumah = blok_rumah;
        this.status = status;
        this.id_pelanggan = id_pelanggan;
        this.tanggal_survei = tanggal_survei;
    }

    public String getTanggal_survei() {
        return tanggal_survei;
    }

    public void setTanggal_survei(String tanggal_survei) {
        this.tanggal_survei = tanggal_survei;
    }

    public void setId_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public String getNama_layanan() {
        return nama_layanan;
    }

    public void setNama_layanan(String nama_layanan) {
        this.nama_layanan = nama_layanan;
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

    public Model_Pemesanan() {
    }

    public Model_Pemesanan(String nama_layanan, String harga, String tanggal, String nama_pelanggan, String no_rumah, String blok_rumah, String status, String id_pelanggan) {
        this.nama_layanan = nama_layanan;
        this.harga = harga;
        this.tanggal = tanggal;
        this.nama_pelanggan = nama_pelanggan;
        this.no_rumah = no_rumah;
        this.blok_rumah = blok_rumah;
        this.status = status;
        this.id_pelanggan = id_pelanggan;
    }
}

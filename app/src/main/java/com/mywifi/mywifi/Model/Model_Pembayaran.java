package com.mywifi.mywifi.Model;

import java.io.Serializable;

public class Model_Pembayaran implements Serializable {
    public String nama;
    public String status;
    public String harga;
    public String idPembayaran;
    public String idPelanggan;

    public Model_Pembayaran(String nama, String status, String harga, String idPembayaran, String idPelanggan, String jenis, String tanggal) {
        this.nama = nama;
        this.status = status;
        this.harga = harga;
        this.idPembayaran = idPembayaran;
        this.idPelanggan = idPelanggan;
        this.jenis = jenis;
        this.tanggal = tanggal;
    }

    public String getIdPembayaran() {
        return idPembayaran;
    }

    public void setIdPembayaran(String idPembayaran) {
        this.idPembayaran = idPembayaran;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String jenis;
    public String tanggal;

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public Model_Pembayaran() {
    }

    public Model_Pembayaran(String nama, String status, String harga, String jenis, String tanggal) {
        this.nama = nama;
        this.status = status;
        this.harga = harga;
        this.jenis = jenis;
        this.tanggal = tanggal;
    }
}

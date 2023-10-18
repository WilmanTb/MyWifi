package com.mywifi.mywifi.Model;

import java.io.Serializable;

public class Model_Transaksi implements Serializable {

    public String idPelanggan, jenis, tanggal, harga, status, idTransaksi;

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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Model_Transaksi() {
    }

    public Model_Transaksi(String idPelanggan, String jenis, String tanggal, String harga, String status, String idTransaksi) {
        this.idPelanggan = idPelanggan;
        this.jenis = jenis;
        this.tanggal = tanggal;
        this.harga = harga;
        this.status = status;
        this.idTransaksi = idTransaksi;
    }
}

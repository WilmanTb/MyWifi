package com.mywifi.mywifi.Model;

import java.io.Serializable;

public class Model_Layanan implements Serializable {
    public String id_pelanggan, layanan, mulai_langganan, status;

    public String getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public String getMulai_langganan() {
        return mulai_langganan;
    }

    public void setMulai_langganan(String mulai_langganan) {
        this.mulai_langganan = mulai_langganan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Model_Layanan() {
    }

    public Model_Layanan(String id_pelanggan, String layanan, String mulai_langganan, String status) {
        this.id_pelanggan = id_pelanggan;
        this.layanan = layanan;
        this.mulai_langganan = mulai_langganan;
        this.status = status;
    }
}

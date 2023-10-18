package com.mywifi.mywifi.Model;

import java.io.Serializable;

public class Model_User implements Serializable {
    public String nama, email, hp, blok, noRumah, password, ktp, status, foto;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getBlok() {
        return blok;
    }

    public void setBlok(String blok) {
        this.blok = blok;
    }

    public String getNoRumah() {
        return noRumah;
    }

    public void setNoRumah(String noRumah) {
        this.noRumah = noRumah;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Model_User() {
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Model_User(String nama, String email, String hp, String blok, String noRumah, String password, String ktp, String status, String foto) {
        this.nama = nama;
        this.email = email;
        this.hp = hp;
        this.blok = blok;
        this.noRumah = noRumah;
        this.password = password;
        this.ktp = ktp;
        this.status = status;
        this.foto = foto;
    }
}

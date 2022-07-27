package com.nicomot.re_food.model;

public class DataDiri {
    String namaPemesan;
    String alamat;
    String nomerHp;

    public DataDiri(){

    }
    public DataDiri(String namaPemesan, String alamat, String nomerHp) {
        this.namaPemesan = namaPemesan;
        this.alamat = alamat;
        this.nomerHp = nomerHp;
    }

    public String getNamaPemesan() {
        return namaPemesan;
    }

    public void setNamaPemesan(String namaPemesan) {
        this.namaPemesan = namaPemesan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomerHp() {
        return nomerHp;
    }

    public void setNomerHp(String nomerHp) {
        this.nomerHp = nomerHp;
    }

}

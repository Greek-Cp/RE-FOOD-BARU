package com.nicomot.re_food.model;

public class Pesanan {
    String namaPesanan;
    int jumlahPesanan;
    int hargaPesanan;

    public Pesanan(String namaPesanan, int jumlahPesanan,int hargaPesanan) {
        this.namaPesanan = namaPesanan;
        this.jumlahPesanan = jumlahPesanan;
        this.hargaPesanan = hargaPesanan;
    }

    public int getHargaPesanan() {
        return hargaPesanan;
    }

    public void setHargaPesanan(int hargaPesanan) {
        this.hargaPesanan = hargaPesanan;
    }

    public String getNamaPesanan() {
        return namaPesanan;
    }

    public void setNamaPesanan(String namaPesanan) {
        this.namaPesanan = namaPesanan;
    }

    public int getJumlahPesanan() {
        return jumlahPesanan;
    }

    public void setJumlahPesanan(int jumlahPesanan) {
        this.jumlahPesanan = jumlahPesanan;
    }
}

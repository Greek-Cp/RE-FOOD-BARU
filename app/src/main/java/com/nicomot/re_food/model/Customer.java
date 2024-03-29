package com.nicomot.re_food.model;

import java.util.List;

public class Customer {
    private int noCustomer;
    private String name;
    private List<String> listMessage;
    private List<Pesanan> pesananMakanan;
    private List<Pesanan> pesananMinuman;
    private List<Pesanan> pesananSnack;
    private List<Pesanan> semuaPesanan;
    private int totalPesanan;
    boolean statusPesanan;
    private String messageTagihan;
    private String tanggal;

    private boolean statusSudahDiSiapkan = false;

    public boolean isStatusSudahDiSiapkan() {
        return statusSudahDiSiapkan;
    }

    public void setStatusSudahDiSiapkan(boolean statusSudahDiSiapkan) {
        this.statusSudahDiSiapkan = statusSudahDiSiapkan;
    }

    public Customer(){

    }
    public boolean isStatusPesanan() {
        return statusPesanan;
    }

    public void setStatusPesanan(boolean statusPesanan) {
        this.statusPesanan = statusPesanan;
    }

    public List<Pesanan> getSemuaPesanan() {
        return semuaPesanan;
    }

    public void setSemuaPesanan(List<Pesanan> semuaPesanan) {
        this.semuaPesanan = semuaPesanan;
    }

    public int getTotalPesanan() {
        return totalPesanan;
    }

    public void setTotalPesanan(int totalPesanan) {
        this.totalPesanan = totalPesanan;
    }

    private boolean pesananDihantarkan;
    private boolean stateKondisiMemesan;
    private DataDiri dataDiri;
    public Customer(String name , List<Pesanan> semuaPesanan,
                    int noCustomer,String tanggal){
        this.name = name;
        this.semuaPesanan = semuaPesanan;
        this.noCustomer = noCustomer;
        this.tanggal = tanggal;
    }
    public Customer(String name, List<String> listMessage , List<Pesanan> pesananMakanan ,
                    List<Pesanan> pesananMinuman , List<Pesanan> pesananLaukPauk ,
                    boolean stateKondisiMemesan,int noCustomer , String tanggal) {
        this.name = name;
        this.listMessage = listMessage;
        this.pesananMakanan = pesananMakanan;
        this.pesananMinuman = pesananMinuman;
        this.pesananSnack = pesananLaukPauk;
        this.stateKondisiMemesan = stateKondisiMemesan;
        this.noCustomer = noCustomer;
        this.tanggal = tanggal;
    }

    public int getNoCustomer() {
        return noCustomer;
    }

    public void setNoCustomer(int noCustomer) {
        this.noCustomer = noCustomer;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getMessageTagihan() {
        return messageTagihan;
    }

    public void setMessageTagihan(String messageTagihan) {
        this.messageTagihan = messageTagihan;
    }

    public boolean isStateKondisiMemesan() {
        return stateKondisiMemesan;
    }
    public void setStateKondisiMemesan(boolean stateKondisiMemesan) {
        this.stateKondisiMemesan = stateKondisiMemesan;
    }
    public boolean isPesananDihantarkan() {
        return pesananDihantarkan;
    }

    public void setPesananDihantarkan(boolean pesananDihantarkan) {
        this.pesananDihantarkan = pesananDihantarkan;
    }

    public DataDiri getDataDiri() {
        return dataDiri;
    }

    public void setDataDiri(DataDiri dataDiri) {
        this.dataDiri = dataDiri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getListMessage() {
        return listMessage;
    }

    public void setListMessage(List<String> listMessage) {
        this.listMessage = listMessage;
    }

    public List<Pesanan> getPesananMakanan() {
        return pesananMakanan;
    }

    public void setPesananMakanan(List<Pesanan> pesananMakanan) {
        this.pesananMakanan = pesananMakanan;
    }

    public List<Pesanan> getPesananMinuman() {
        return pesananMinuman;
    }

    public void setPesananMinuman(List<Pesanan> pesananMinuman) {
        this.pesananMinuman = pesananMinuman;
    }

    public List<Pesanan> getPesananSnack() {
        return pesananSnack;
    }

    public void setPesananSnack(List<Pesanan> pesananSnack) {
        this.pesananSnack = pesananSnack;
    }

}

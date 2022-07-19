package com.nicomot.re_food.model;

public class
Menu {
    String namaItem;
    int hargaItem;
    int stockItem;
    boolean statusItem; // true = stock habis , false = stock tidak habis
    String pathGambarItem;
    int gambarDrawable;

    public Menu(){
        //default constructor
    }

    public Menu(String namaItem, int hargaItem, int stockItem, boolean statusItem, int gambarDrawable) {
        this.namaItem = namaItem;
        this.hargaItem = hargaItem;
        this.stockItem = stockItem;
        this.statusItem = statusItem;
        this.gambarDrawable = gambarDrawable;
    }

    public Menu(String namaItem, int hargaItem, int stockItem, boolean statusItem, String pathGambarItem) {
        this.namaItem = namaItem;
        this.hargaItem = hargaItem;
        this.stockItem = stockItem;
        this.statusItem = statusItem;
        this.pathGambarItem = pathGambarItem;
    }

    public int getGambarDrawable() {
        return gambarDrawable;
    }

    public void setGambarDrawable(int gambarDrawable) {
        this.gambarDrawable = gambarDrawable;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public int getHargaItem() {
        return hargaItem;
    }

    public void setHargaItem(int hargaItem) {
        this.hargaItem = hargaItem;
    }

    public int getStockItem() {
        return stockItem;
    }

    public void setStockItem(int stockItem) {
        this.stockItem = stockItem;
    }

    public boolean isStatusItem() {
        return statusItem;
    }

    public void setStatusItem(boolean statusItem) {
        this.statusItem = statusItem;
    }

    public String getPathGambarItem() {
        return pathGambarItem;
    }

    public void setPathGambarItem(String pathGambarItem) {
        this.pathGambarItem = pathGambarItem;
    }
}

package com.nicomot.re_food.util;

public class UtilAttributeToko {

    enum NamaToko{
        NAMA_TOKO
    }

    static String namaToko = "TOKO Resto Enak";
    static String alamatToko = "Jalan Mastrip 67 Jember ";
    static String openToko = "08.00 Pagi";
    static String closeToko = "07.00 Malam";
    public static String getNamaToko() {
        return namaToko;
    }

    public String getAlamatToko() {
        return alamatToko;
    }

    public String getOpenToko() {
        return openToko;
    }

    public String getCloseToko() {
        return closeToko;
    }
}

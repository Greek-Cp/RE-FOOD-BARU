package com.nicomot.re_food.util;

public class MenuUtil {
    /*
    PRICE
    *3wUNIT 1k = 1000
    Makanan{
    Nasi_Goreng : 10k
    Mie_Goreng : 10k
    Mie_Kuah : 10k
    Kweatiu_goreng : 8k
    }

    Minuman{
     Air_Mineral : 4k
     Es_Teh : 3k
     Teh_Hangat : 3k
     Es_Jeruk : 3k
     Jeruj_Hangat : 3k
    }
    Lauk{
     Ikan_Goreng : 5k
     Kangkung : 3k
     Oseng_Tempe : 3k
     Semur_Sayur : 4k
    }
     */

   public static enum Makanan{
        Nasi_Goreng,
        Mie_Goreng,
        Mie_Kuah,
        Kweatiu_Goreng
    }
   public static enum Minuman{
        Air_Mineral,
        Es_Teh,
        Teh_Hangat,
        Es_Jeruk,
        Jeruk_Hangat
    }
    public static enum Lauk{
       Ikan_Goreng,
        Kangkung,
        Oseng_Tempe,
        Semur_Sayur
    }
}

package com.nicomot.re_food.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicomot.re_food.model.Customer;
import com.nicomot.re_food.model.DataDiri;
import com.nicomot.re_food.model.Pesanan;
import com.nicomot.re_food.models.Action;
import com.nicomot.re_food.robj.notificationhelperlibrary.utils.NotificationUtils;
import com.nicomot.re_food.util.MenuUtil;
import com.nicomot.re_food.util.ShowMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ServiceNotificationListener extends NotificationListenerService {

    List<Customer> listCustomer;
    List<String> chat;
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Bundle bundle = sbn.getNotification().extras;;
        String from = bundle.getString(NotificationCompat.EXTRA_TITLE);
        String message = bundle.getString(NotificationCompat.EXTRA_TEXT);
        System.out.println("From = " + from + "\n" + "Message = " + message);
        listCustomer = getModelCustomer();
        chat = new ArrayList<>();
        boolean clear = false;
            if(  message != null && !message.contains("new messages") || !message.contains("pesan baru")){

            if(message != null  && !from.isEmpty() && from != null){
                try {
                    System.out.println("get succes");
                }catch (NullPointerException e){
                }
                if(message.contains("wado")){
                    Action action = NotificationUtils.getQuickReplyAction(sbn.getNotification(), sbn.getPackageName());
                    for(int i = 0; i < 100; i++){
                        try {
                            action.sendReply(getApplicationContext(),"hello brian kunz");
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if(from.contains("+62")){
                    sendMessageToFragmentAndFilter(from,message);
                    if(listCustomer == null){
                        listCustomer = new ArrayList<>();
                        chat = new ArrayList<>();
                        chat.add(message);
                        System.out.println("size chawt from null preprare = " + chat.size());
                        System.out.println("chat null = " + chat);
                        List<Pesanan> pesananMakanan = new ArrayList<>();
                        List<Pesanan> pesananMinuman = new ArrayList<>();
                        List<Pesanan> pesananLaukPauk = new ArrayList<>();
                        listCustomer.add(new Customer(from,chat,pesananMakanan,pesananMinuman,pesananLaukPauk,false));
                        if(chat.size() == 1) {
                            Action action = NotificationUtils.getQuickReplyAction(sbn.getNotification(), sbn.getPackageName());
                            try {
                                action.sendReply(getApplicationContext(), "" +
                                        "Selamat Datang Di Resto Enak!!!\n" +
                                        "Apakah anda ingin memesan? \n" +
                                        "(jawab dengan angka saja)\n" +
                                        " 1. YA\n" + "2. TIDAK");
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                        saveModelList(listCustomer);
                    } else{
                        boolean apakahSama = false;
                        int endSize = listCustomer.size();
                        for(int index = 0; index < listCustomer.size(); index++){
                            System.out.println("Name = " + listCustomer.get(index).getName()  + " From = " + from);
                           if(listCustomer.get(index).getName().equals(from)){
                                System.out.println("nomer sama = " + listCustomer.get(index).getName());
                                List<String> pesn = listCustomer.get(index).getListMessage();
                                apakahSama = true;
                               pesn.add(message);
                               System.out.println("pesn size = " + pesn.size());
                                Action action = NotificationUtils.getQuickReplyAction(sbn.getNotification(), sbn.getPackageName());
                                 if (pesn.size() == 2 && pesn.get(1).equals("1") &&  listCustomer.get(index).isStateKondisiMemesan() == false){
                                    try {
                                        action.sendReply(getApplicationContext(),"Silahkan mengisi data diri untuk proses pengiriman.\n" +
                                            "\n" +
                                            "Nama Pemesan :\n" +
                                            "Alamat lengkap  :\n" +
                                            "Nomor Hp            :");
                                    } catch (PendingIntent.CanceledException e) {
                                        e.printStackTrace();
                                    }
                                } else if(pesn.size() == 3 && pesn.get(2).contains("Nama Pemesan") && listCustomer.get(index).isStateKondisiMemesan() == false){
                                     String dataDiriParser[] = pesn.get(2).split(" : ");
                                     DataDiri dataPribadiUser = new DataDiri(dataDiriParser[1],dataDiriParser[2],dataDiriParser[3]);
                                     String nomorClean =dataPribadiUser.getNomerHp().trim();
                                     String namaClean = dataPribadiUser.getNamaPemesan().replaceAll("Alamat lengkap","").trim();
                                     String alamatClean = dataPribadiUser.getAlamat().replaceAll("Nomor Hp","").trim();
                                     dataPribadiUser.setAlamat(alamatClean);
                                     dataPribadiUser.setNamaPemesan(namaClean);
                                     dataPribadiUser.setNomerHp(nomorClean);
                                     listCustomer.get(index).setDataDiri(dataPribadiUser);
                                         String messg = "Apakah pesanan anda ingin di antar?\n" +
                                                 "\n" +
                                                 "1. YA\n" +
                                                 "2. TIDAK\n \n (jawab dengan angka saja)";
                                         try {
                                             action.sendReply(getApplicationContext(),messg);
                                         } catch (PendingIntent.CanceledException e) {
                                             e.printStackTrace();
                                         }
                                 } else if(pesn.size() == 4 && pesn.get(3).equals("1")){
                                     listCustomer.get(index).setPesananDihantarkan(true);
                                     String messgMenu = "Silahkan memilih jenis makanan \n" +
                                             "(Jawab dengan angka saja)\n" +
                                             "\n" +
                                             "1.Makanan\n" +
                                             "2.Minuman\n" +
                                             "3.Lauk saja";
                                     try {
                                         action.sendReply(getApplicationContext(),"Baik Pesananmu Akan Kami Hantarkan");
                                         action.sendReply(getApplicationContext(),messgMenu);
                                     } catch (PendingIntent.CanceledException e) {
                                         e.printStackTrace();
                                     }
                                 } else if(pesn.size() == 4 && pesn.get(3).equals("1") && listCustomer.get(index).isStateKondisiMemesan() == true && message.equals("1")){
                                     listCustomer.get(index).setPesananDihantarkan(true);
                                     String messgMenu = "Silahkan memilih jenis makanan \n" +
                                             "(Jawab dengan angka saja)\n" +
                                             "\n" +
                                             "1.Makanan\n" +
                                             "2.Minuman\n" +
                                             "3.Lauk saja";
                                     try {
                                         action.sendReply(getApplicationContext(),"Baik Pesananmu Akan Kami Hantarkan");
                                         action.sendReply(getApplicationContext(),messgMenu);
                                     } catch (PendingIntent.CanceledException e) {
                                         e.printStackTrace();
                                     }
                                 } else if(pesn.size() == 4 && pesn.get(3).equals("2")){
                                     //pesanan tidak di hantarkan
                                     listCustomer.get(index).setPesananDihantarkan(false);
                                 } else if(pesn.size() == 5 && pesn.get(4).equals("1")){
                                     //pesananan dihantarkan
                                     //1 = pesanan berupa makanan
                                     try {
                                         action.sendReply(getApplicationContext(),"Silahkan memilih menu makanan \n" +
                                                 "(Jawab dengan angka saja)\n" +
                                                 "\n" +
                                                 "1. Nasi Goreng\n" +
                                                 "2. Mie Goreng\n" +
                                                 "3. Mie Kuah\n" +
                                                 "4. Kwentiu Goreng");
                                     } catch (PendingIntent.CanceledException e) {
                                         e.printStackTrace();
                                     }
                                 } else if(pesn.size() == 5 && pesn.get(4).equals("2")){
                                     ////minuman
                                     try {
                                         action.sendReply(getApplicationContext(),"Silahkan  memilih menu minuman \n" +
                                                 "1. Air Mineral\n" +
                                                 "2. Es Teh \n" +
                                                 "3. Teh Hangat\n" +
                                                 "4. Es Jeruk\n" +
                                                 "5. Jeruk Hangat");
                                     } catch (PendingIntent.CanceledException e) {
                                         e.printStackTrace();
                                     }

                                 } else if(pesn.size() == 5 && pesn.get(4).equals("3")){
                                     //Lauk saja
                                     try {
                                         action.sendReply(getApplicationContext(),"Silahkan  memilih menu minuman \n" +
                                                 "1. Ikan Goreng\n" +
                                                 "2. Kangkung \n" +
                                                 "3. Oseng Tempe\n" +
                                                 "4. Semur Sayur\n");
                                     } catch (PendingIntent.CanceledException e) {
                                         e.printStackTrace();
                                     }
                                 } else if(pesn.size() == 6){
                                     //index ke 6 = jumlah pesanan
                                     //Pesanan pesanan = new Pesanan();
                                     try {
                                         action.sendReply(getApplicationContext(),"Silahkan ketik jumlah pesanan \n" +
                                                 "(Jawab dengan angka saja)");
                                     } catch (PendingIntent.CanceledException e) {
                                         e.printStackTrace();
                                     }
                                 } else if(pesn.size() == 7){
                                     if(pesn.get(4).equals("1")){
                                         //khusus makanan
                                         /*
                                         index 5 == pilihan makanan
                                          */
                                         List<Pesanan> pesananMakanan = listCustomer.get(index).getPesananMakanan();
                                         Pesanan pesan_1;
                                         switch (Integer.parseInt(pesn.get(5))){
                                             case 1:
                                                 pesan_1 = new Pesanan(MenuUtil.Makanan.Nasi_Goreng.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),10000);
                                                 pesananMakanan.add(pesan_1);
                                             break;
                                             case 2:
                                                 pesan_1 = new Pesanan(MenuUtil.Makanan.Mie_Goreng.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),10000);
                                                 pesananMakanan.add(pesan_1);
                                                 break;
                                             case 3:
                                                 pesan_1 = new Pesanan(MenuUtil.Makanan.Mie_Kuah.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),10000);
                                                 pesananMakanan.add(pesan_1);
                                                 break;
                                             case 4:
                                                 pesan_1  = new Pesanan(MenuUtil.Makanan.Kweatiu_Goreng.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),8000);
                                                 pesananMakanan.add(pesan_1);
                                                 break;
                                         }
                                         listCustomer.get(index).setPesananMakanan(pesananMakanan);
                                         try {
                                             action.sendReply(getApplicationContext(),"Apakah anda memesan menu lain ?\n" +
                                                     "1. Ya\n" +
                                                     "2. Tidak");
                                         } catch (PendingIntent.CanceledException e) {
                                             e.printStackTrace();
                                         }
                                         listCustomer.get(index).setStateKondisiMemesan(true);

                                     } else if(pesn.get(4).equals("2")){
                                         List<Pesanan> pesananMinuman = listCustomer.get(index).getPesananMinuman();
                                         Pesanan pesan_1;
                                         switch (Integer.parseInt(pesn.get(5))) {
                                             case 1:
                                                 pesan_1 = new Pesanan(MenuUtil.Minuman.Air_Mineral.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),4000);
                                                 pesananMinuman.add(pesan_1);
                                                 break;
                                             case 2:
                                                 pesan_1 = new Pesanan(MenuUtil.Minuman.Es_Teh.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),3000);
                                                 pesananMinuman.add(pesan_1);
                                                 break;
                                             case 3:
                                                 pesan_1 = new Pesanan(MenuUtil.Minuman.Teh_Hangat.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),3000);
                                                 pesananMinuman.add(pesan_1);
                                                 break;
                                             case 4:
                                                 pesan_1 = new Pesanan(MenuUtil.Minuman.Jeruk_Hangat.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),3000);
                                                 pesananMinuman.add(pesan_1);
                                                 break;
                                             case 5:
                                                 pesan_1 = new Pesanan(MenuUtil.Minuman.Es_Jeruk.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),3000);
                                                 pesananMinuman.add(pesan_1);
                                                 break;
                                         }
                                         listCustomer.get(index).setPesananMinuman(pesananMinuman);
                                         try {
                                             action.sendReply(getApplicationContext(),"Apakah anda memesan menu lain ?\n" +
                                                     "1. Ya\n" +
                                                     "2. Tidak");
                                         } catch (PendingIntent.CanceledException e) {
                                             e.printStackTrace();
                                         }
                                         listCustomer.get(index).setStateKondisiMemesan(true);
                                         //khusus minuman
                                     } else if(pesn.get(4).equals("3"))
                                     {
                                         //khusus lauk
                                         List<Pesanan> pesananLauk = listCustomer.get(index).getPesananSnack();
                                         Pesanan pesan_1;
                                         switch (Integer.parseInt(pesn.get(5))) {
                                             case 1:
                                                 pesan_1 = new Pesanan(MenuUtil.Lauk.Ikan_Goreng.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),5000);
                                                 pesananLauk.add(pesan_1);
                                                 break;
                                             case 2:
                                                 pesan_1 = new Pesanan(MenuUtil.Lauk.Kangkung.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),3000);
                                                 pesananLauk.add(pesan_1);
                                                 break;
                                             case 3:
                                                 pesan_1 = new Pesanan(MenuUtil.Lauk.Oseng_Tempe.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),3000);
                                                 pesananLauk.add(pesan_1);
                                                 break;
                                             case 4:
                                                 pesan_1 = new Pesanan(MenuUtil.Lauk.Semur_Sayur.name().replaceAll("_"," "),Integer.parseInt(pesn.get(6)),3000);
                                                 pesananLauk.add(pesan_1);
                                                 break;
                                         }
                                         listCustomer.get(index).setPesananSnack(pesananLauk);
                                         try {
                                             action.sendReply(getApplicationContext(),"Apakah anda memesan menu lain ?\n" +
                                                     "1. Ya\n" +
                                                     "2. Tidak");
                                         } catch (PendingIntent.CanceledException e) {
                                             e.printStackTrace();
                                         }
                                         listCustomer.get(index).setStateKondisiMemesan(true);
                                     }
                                 } else if(pesn.size() == 8){
                                     switch (Integer.parseInt(pesn.get(7))){
                                         case 1:
                                             listCustomer.get(index).setStateKondisiMemesan( true);
                                             //YA
                                             clear = true;
                                             //remove 3 ... size desc
                                             for(int i = pesn.size() - 1; i >  3;i--){
                                                 pesn.remove(i);
                                             }
                                             System.out.println("current size after clean = " + pesn.size());
                                             String messgMenu = "Silahkan memilih jenis makanan \n" +
                                                     "(Jawab dengan angka saja)\n" +
                                                     "\n" +
                                                     "1.Makanan\n" +
                                                     "2.Minuman\n" +
                                                     "3.Lauk saja";
                                             try {
                                                 action.sendReply(getApplicationContext(),messgMenu);
                                             } catch (PendingIntent.CanceledException e) {
                                                 e.printStackTrace();
                                             }
                                             break;
                                         case 2:
                                             //TIDAK
                                             listCustomer.get(index).setStateKondisiMemesan( false);
                                             String headerText = "======NAMA TOKO======\n";
                                             StringBuilder sb = new StringBuilder();
                                             sb.append(headerText);
                                             int iterationIndex = 1;
                                              if(listCustomer.get(index).getPesananMakanan() != null){

                                                 for(Pesanan makanans : listCustomer.get(index).getPesananMakanan()){
                                                     sb.append(iterationIndex + "." + makanans.getNamaPesanan() + "\n");
                                                 }
                                                 iterationIndex++;
                                             }
                                             if(listCustomer.get(index).getPesananMinuman() != null){
                                                 for(Pesanan minuman : listCustomer.get(index).getPesananMinuman()){
                                                     sb.append(iterationIndex + "." + minuman.getNamaPesanan()+ "\n");
                                                 }
                                                 iterationIndex++;
                                             }
                                             if(listCustomer.get(index).getPesananSnack() != null){
                                                 for(Pesanan lauk : listCustomer.get(index).getPesananSnack()){
                                                     sb.append(iterationIndex + "." + lauk.getNamaPesanan()+ "\n");
                                                 }

                                                 iterationIndex++;
                                             }
                                             try {
                                                 action.sendReply(getApplicationContext(),sb.toString());
                                             } catch (PendingIntent.CanceledException e) {
                                                 e.printStackTrace();
                                             }
                                             break;
                                     }
                                 } else if(pesn.get(pesn.size() - 1).equals("RESET")){
                                     listCustomer.get(index).getListMessage().clear();
                                 }
                                System.out.println("size chat not null when user exist on list = " + pesn.size());
                                 listCustomer.set(index,listCustomer.get(index)).setListMessage(pesn);
                                saveModelList(listCustomer);
                            } else{
                            System.out.println("data di tambahkan");
                            }
                        }
                        if(apakahSama == false){
                            List<String> chatA = new ArrayList<>();
                            List<Pesanan> pesananMakanan = new ArrayList<>();
                            List<Pesanan> pesananMinuman = new ArrayList<>();
                            List<Pesanan> pesananLaukPauk = new ArrayList<>();
                            chatA.add(message);
                            if(chatA.size() == 1) {
                                Action action = NotificationUtils.getQuickReplyAction(sbn.getNotification(), sbn.getPackageName());
                                try {
                                    action.sendReply(getApplicationContext(), "" +
                                            "Selamat Datang Di Resto Enak!!!\n" +
                                            "Apakah anda ingin memesan? \n" +
                                            "(jawab dengan angka saja)\n" +
                                            " 1. YA\n" + "2. TIDAK");
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }
                            listCustomer.add(new Customer(from,chatA,pesananMakanan,pesananMinuman,pesananLaukPauk,false));
                            saveModelList(listCustomer);
                        }
                    }

                }


            }
        }


    }
    String removeDuplicatePesanan(List<Pesanan> listMakanan ,int currentIndex){
        Pesanan[] names = new Pesanan[listMakanan.size()];
        for(int i = 0; i < listMakanan.size(); i++){
            names[i] = listMakanan.get(i);
        }

        HashMap<String, Integer> repeatNames = new HashMap<String, Integer>();
        List<Pesanan> pesananNew = new ArrayList<>();
        int repeatCount = 0;
        for (int i = 0; i < names.length; i++) {
            int count = 0;
            for (int k = 0; k < names.length; k++) {
                if (names[i].getNamaPesanan() == names[k].getNamaPesanan()) {
                    count++;
                }
            }
            if (count >= 1) {
                if (!repeatNames.containsKey(names[i].getNamaPesanan())) {
                    System.out.println(names[i].getNamaPesanan() + ":" + count);
                    pesananNew.add(new Pesanan(names[i].getNamaPesanan(),count,names[i].getHargaPesanan()));
                    repeatNames.put(names[i].getNamaPesanan(), count);
                    repeatCount += count;
                }
            }
        }
        for(int i = 0; i < pesananNew.size(); i++){
            System.out.printf("%s = %d %n" ,pesananNew.get(i).getNamaPesanan(),pesananNew.get(i).getJumlahPesanan() * pesananNew.get(i).getHargaPesanan());
        }
        System.out.println("Total Count:" + repeatCount);
        return "";
    }


    void saveModelList(List<Customer> listCustomer){
        SharedPreferences sharedPreferences;
        sharedPreferences = getApplicationContext().getSharedPreferences("PREF_CUST", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        System.out.println("Json Save = " + gson.toJson(listCustomer));
        sharedPreferences.edit().putString("MODEL_CUST_KEY",gson.toJson(listCustomer)).commit();
        Toast.makeText(getApplicationContext(),"Model Saved", Toast.LENGTH_LONG).show();
    }
    List<Customer> getModelCustomer(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getApplicationContext().getSharedPreferences("PREF_CUST",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
        System.out.println("Json Load = " + sharedPreferences.getString("MODEL_CUST_KEY",""));
        List<Customer> listCust = gson.fromJson(sharedPreferences.getString("MODEL_CUST_KEY",""),typeCustomer);
        return listCust;
    }
    void sendMessageToFragmentAndFilter(String from , String message_text){
            Intent intentData = new Intent("NOTIFICzATION_DATA");
            intentData.putExtra("FROM",from);
            intentData.putExtra("CHAT",message_text);
            sendBroadcast(intentData);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Toast.makeText(getApplicationContext(),"Notification Listener Conected !",Toast.LENGTH_SHORT).show();
    }
}

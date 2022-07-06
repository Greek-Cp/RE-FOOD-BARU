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
import com.nicomot.re_food.models.Action;
import com.nicomot.re_food.robj.notificationhelperlibrary.utils.NotificationUtils;
import com.nicomot.re_food.util.ShowMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ServiceNotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Bundle bundle = sbn.getNotification().extras;;
        String from = bundle.getString(NotificationCompat.EXTRA_TITLE);
        String message = bundle.getString(NotificationCompat.EXTRA_TEXT);
        List<String> chat;
        List<Customer> listCustomer =  getModelCustomer();

        List<Customer> listNewUser = new ArrayList<>();
        if(message.contains("new messages")){
        } else{
            if(message != null  && !from.isEmpty() && from != null){
                try {
                    System.out.println("get succes");
                }catch (NullPointerException e){
                }
                if(from.contains("+62")){
                    sendMessageToFragmentAndFilter(from,message);
                    if(listCustomer == null){
                        listCustomer = new ArrayList<>();
                        chat = new ArrayList<>();
                        chat.add(message);
                        System.out.println("size chawt from null preprare = " + chat.size());
                        System.out.println("chat null = " + chat);
                        listCustomer.add(new Customer(from,chat));
                        saveModelList(listCustomer);
                    } else{
                        boolean apakahSama = false;
                        int endSize = listCustomer.size();
                        for(int index = 0; index < listCustomer.size(); index++){
                            System.out.println("Name = " + listCustomer.get(index).getName()  + " From = " + from);
                            if(listCustomer.get(index).getName().equals(from)){
                                System.out.println("nomer sama = " + listCustomer.get(index).getName());
                                List<String> pesn = listCustomer.get(index).getListMessage();
                                if(pesn.size() == 1){
                                    Action action = NotificationUtils.getQuickReplyAction(sbn.getNotification(),sbn.getPackageName());
                                    try {
                                        action.sendReply(getApplicationContext(),"" +
                                                "Selamat Datang Di Resto Enak!!!\n" +
                                                "Apakah anda ingin memesan? \n" +
                                                "(jawab dengan angka saja)\n");
                                    } catch (PendingIntent.CanceledException e) {
                                        e.printStackTrace();
                                    }
                                }
                                pesn.add(message);

                                System.out.println("size chat not null when user exist on list = " + pesn.size());
                                listCustomer.set(index,listCustomer.get(index)).setListMessage(pesn);
                                saveModelList(listCustomer);
                                apakahSama = true;
                            } else if (!listCustomer.get(index).getName().equals(listCustomer.get(listCustomer.size() - 1).getName())){
                                List<String> chatA = new ArrayList<>();
                                chatA.add(message);
                                listNewUser.add(new Customer(from,chatA));
                            } else{
                                List<String> chatA = new ArrayList<>();
                                chatA.add(message);
                                listNewUser.add(new Customer(from,chatA));
                            }
                        }
                        if(apakahSama == false){
                            System.out.println("Sebelum tambah = " + listCustomer.size());
                            for(int i = 0; i < listNewUser.size(); i++){
                                listCustomer.add(listNewUser.get(i)
                                );
                            }
                            System.out.println("Sesudah  tambah = " + listCustomer.size());
                            saveModelList(listCustomer);
                        }
                    }

                }


            }
        }


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
            Intent intentData = new Intent("NOTIFICATION_DATA");
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
    }
}

package com.nicomot.re_food.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicomot.re_food.R;
import com.nicomot.re_food.adapter.AdapterDapur;
import com.nicomot.re_food.adapter.AdapterPesanan;
import com.nicomot.re_food.fragment.SiapkanPesanan;
import com.nicomot.re_food.model.Customer;
import com.nicomot.re_food.model.Pesanan;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DapurActivity extends AppCompatActivity {

    RecyclerView recListPesananCustomer;
    AdapterDapur adapterDapur;
    AdapterDapur.clickPesanan listenerClickPesanan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapur);
        hiddenActionBar();
        recListPesananCustomer = findViewById(R.id.id_rec_list_pesanan_cust);

        setAdapterListPesanan();
    }
    List<Pesanan> removeDuplicatePesanan(List<Pesanan> listMakanan){
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
                if (names[i].getNamaPesanan().equals( names[k].getNamaPesanan())) {
                    count++;
                }
            }
            if (count >= 1) {
                if (!repeatNames.containsKey(names[i].getNamaPesanan())) {
                    System.out.println(names[i].getNamaPesanan() + ":" + count + " wasu");
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
        return pesananNew;
    }
    void hiddenActionBar(){
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    void saveListValidCustomer(List<Customer> listCustomer){
        SharedPreferences sharedPreferences;
        sharedPreferences = getApplicationContext().getSharedPreferences("OTW_SIAPKAN_DAPUR", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        System.out.println("Json Save Valid = " + gson.toJson(listCustomer));
        sharedPreferences.edit().putString("KEY_DAPUR",gson.toJson(listCustomer)).commit();
    }
    List<Customer> getListValidCustomer(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getApplicationContext().getSharedPreferences("OTW_SIAPKAN_DAPUR",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
        System.out.println("Json Load valid = " + sharedPreferences.getString("KEY_DAPUR",""));
        List<Customer> listCust = gson.fromJson(sharedPreferences.getString("KEY_DAPUR",""),typeCustomer);
        return listCust;
    }
    List<Customer> listCustomerValid;
    void setAdapterListPesanan(){
     listCustomerValid = getListValidCustomer();
        if(listCustomerValid == null){
            listCustomerValid = new ArrayList<>();
        }

        for(Customer customer : listCustomerValid){
            for(Pesanan pesanan : customer.getSemuaPesanan()){
                System.out.println("wow pesanan = " + pesanan.getJumlahPesanan());
            }
        }

        List<Customer> validCustomer = new ArrayList<>();
        for(Customer customerValid : listCustomerValid){
            List<Pesanan> listPesanan = customerValid.getSemuaPesanan();
            customerValid.setSemuaPesanan(listPesanan);
            validCustomer.add(customerValid);
        }
        listenerClickPesanan = new AdapterDapur.clickPesanan() {
            @Override
            public void clickPesanan(int pesanan) {
                Toast.makeText(getApplicationContext(), "Pesanan ke - " + pesanan + " dipilih", Toast.LENGTH_SHORT).show()    ;
            }
            @Override
            public void clickRadio(int positions,boolean status) {
                Toast.makeText(getApplicationContext(),"posisi radio ke -> " + positions,Toast.LENGTH_SHORT).show();
                validCustomer.get(positions).setStatusPesanan(status);
                saveListValidCustomer(validCustomer);
            }
        };
        adapterDapur = new AdapterDapur(validCustomer,listenerClickPesanan);
        recListPesananCustomer.setAdapter(adapterDapur);

    }
}
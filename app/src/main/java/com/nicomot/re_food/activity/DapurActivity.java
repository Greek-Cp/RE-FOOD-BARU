package com.nicomot.re_food.activity;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicomot.re_food.R;
import com.nicomot.re_food.adapter.AdapterDapur;
import com.nicomot.re_food.adapter.AdapterPesanan;
import com.nicomot.re_food.fragment.SiapkanPesanan;
import com.nicomot.re_food.model.Account;
import com.nicomot.re_food.model.Customer;
import com.nicomot.re_food.model.Menu;
import com.nicomot.re_food.model.Pesanan;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DapurActivity extends AppCompatActivity {

    RecyclerView recListPesananCustomer;
    AdapterDapur adapterDapur;
    AdapterDapur.clickPesanan listenerClickPesanan;

    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList< Customer> listCustomerFromDatabase;
    int postItem = 0;
    ImageView btnRefresh,btnLogout;
    void refresh(){
        Intent refresh = new Intent(getApplicationContext(),DapurActivity.class);
        refresh.setFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK |
                FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(getApplicationContext(),"Refresh Berhasil",Toast.LENGTH_SHORT).show();
        startActivity(refresh);

    }
    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }
    List<Customer> getDataCustomerFromDatabase(){
        listCustomerFromDatabase = new ArrayList<>();
        //        setAdapterListPesanan();
        database = FirebaseDatabase.getInstance("https://re-food-7fc1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference().child("Customer");
        if(listCustomerValid == null){
            listCustomerValid = new ArrayList<>();
        }


        /*
        List<Customer> validCustomer = new ArrayList<>();
        for(Customer customerValid : listCustomerFromDatabase){
            System.out.println("Customer valid = " + customerValid.getName());
            List<Pesanan> listPesanan = customerValid.getSemuaPesanan();
            customerValid.setSemuaPesanan(listPesanan);
            validCustomer.add(customerValid);
        }

         */
        listenerClickPesanan = new AdapterDapur.clickPesanan() {
            @Override
            public void clickPesanan(int pesanan) {
                System.out.println(listCustomerFromDatabase.get(pesanan).getName() + " Clicked lur");
            }
            @Override
            public void clickRadio(int positions,boolean status) {
                Toast.makeText(getApplicationContext(),"Delete Complete " + listCustomerFromDatabase.get(positions).getName(),Toast.LENGTH_SHORT).show();
                System.out.println("position = " + positions);
                listCustomerFromDatabase.get(positions).setStatusPesanan(status);
                Customer custTemporary = listCustomerFromDatabase.get(positions);
                DatabaseReference dbR = database.getReference().child("Customer").child(String.valueOf(custTemporary.getNoCustomer()));
                dbR.removeValue();
                saveListValidCustomer(listCustomerFromDatabase);
                insertDataValidPesananKeDatabase(listCustomerFromDatabase.get(positions));
                listCustomerFromDatabase.remove(positions);
                adapterDapur.notifyItemRemoved(positions);
                Intent refresh = new Intent(getApplicationContext(),DapurActivity.class);
                refresh.setFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK |
                        FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(refresh);
            }
        };
        List<Menu> menuMakanan = getMenuMakanan();
        List<Menu> menuMinuman = getMenuMinuman();
        List<Menu> menuLauk = getMenuLauk();
        List<Menu> listMenu = new ArrayList<>();
        if(menuMakanan != null){
            for(Menu mMakanan : menuMakanan){
                listMenu.add(mMakanan);
            }
        }
        if(menuMinuman != null){
            for(Menu mMinuman : menuMinuman){
                listMenu.add(mMinuman);
            }
        }
        if(menuLauk != null){
            for(Menu mLauk : menuLauk){
                listMenu.add(mLauk);
            }
        }


        adapterDapur = new AdapterDapur(listCustomerFromDatabase,listenerClickPesanan,listMenu);
        recListPesananCustomer.setAdapter(adapterDapur);
       System.out.println("list customer size " + listCustomerFromDatabase.size() );
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Customer customer = dataSnapshot1.getValue(Customer.class);
                    System.out.println(customer);
                    listCustomerFromDatabase.add(customer);

                }

                adapterDapur.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return listCustomerFromDatabase;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapur);
        hiddenActionBar();
        btnRefresh = findViewById(R.id.id_btn_refresh);
        btnLogout = findViewById(R.id.id_btn_logout);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DapurActivity.this, LoginActivity.class);
                Toast.makeText(getApplicationContext(),"LogOut Dari Dapur ", Toast.LENGTH_LONG).show();
                startActivity(intent);

            }
        });
        recListPesananCustomer = findViewById(R.id.id_rec_list_pesanan_cust);

        getDataCustomerFromDatabase();

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
                    pesananNew.add(new Pesanan(names[i].getNamaPesanan(),count,names[i].getHargaPesanan()));
                    repeatNames.put(names[i].getNamaPesanan(), count);
                    repeatCount += count;
                }
            }
        }
        for(int i = 0; i < pesananNew.size(); i++){
        }
        return pesananNew;
    }
    void hiddenActionBar(){
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    void insertDataValidPesananKeDatabase(Customer customer){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://re-food-7fc1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("CustomerValid").child(String.valueOf(customer.getNoCustomer()));
        myRef.setValue(customer);
    }
    void saveListValidCustomer(List<Customer> listCustomer){
        SharedPreferences sharedPreferences;
        sharedPreferences = getApplicationContext().getSharedPreferences("OTW_SIAPKAN_DAPUR", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPreferences.edit().putString("KEY_DAPUR",gson.toJson(listCustomer)).commit();
    }
    List<Customer> getListValidCustomer(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getApplicationContext().getSharedPreferences("OTW_SIAPKAN_DAPUR",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
        List<Customer> listCust = gson.fromJson(sharedPreferences.getString("KEY_DAPUR",""),typeCustomer);
        return listCust;
    }
    List<Customer> listCustomerValid;
    void setAdapterListPesanan(){
        //listCustomerValid = getListValidCustomer();
      //  listCustomerValid = getDataCustomerFromDatabase();

    }

    List<Menu> getDefaultPrefencesMenuMakanan(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PREF_MENU_MAKANAN", Context.MODE_PRIVATE);
        Type typeMenu = new TypeToken<List<Menu>>(){}.getType();
        Gson gson = new Gson();
        List<Menu> listMenuMakanan = gson.fromJson(sharedPreferences.getString("KEY_MENU_MAKANAN",""),typeMenu);
        return listMenuMakanan;
    }
    List<Menu> getDefaultPrefencesMenuMinuman(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PREF_MENU_MINUMAN", Context.MODE_PRIVATE);
        Type typeMenu = new TypeToken<List<Menu>>(){}.getType();
        Gson gson = new Gson();
        List<Menu> listMenuMakanan = gson.fromJson(sharedPreferences.getString("KEY_MENU_MINUMAN",""),typeMenu);
        return listMenuMakanan;
    }
    List<Menu> getDefaultPrefencesMenuLauk(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PREF_MENU_LAUK", Context.MODE_PRIVATE);
        Type typeMenu = new TypeToken<List<Menu>>(){}.getType();
        Gson gson = new Gson();
        List<Menu> listMenuMakanan = gson.fromJson(sharedPreferences.getString("KEY_MENU_LAUK",""),typeMenu);
        return listMenuMakanan;
    }
    List<Menu>  getMenuMakanan(){
        List<Menu> listMenuMakanan = getDefaultPrefencesMenuMakanan();

        return listMenuMakanan;
    }

    List<Menu> getMenuMinuman(){
        List<Menu> listMenuMinuman = getDefaultPrefencesMenuMinuman();

        return listMenuMinuman;
    }
    List<Menu> getMenuLauk(){
        List<Menu> listMenuLauk = getDefaultPrefencesMenuLauk();

        return listMenuLauk;
    }

}
package com.nicomot.re_food.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.nicomot.re_food.adapter.AdapterPesananSudahSiap;
import com.nicomot.re_food.model.Customer;
import com.nicomot.re_food.model.Menu;
import com.nicomot.re_food.model.Pesanan;
import com.nicomot.re_food.util.ShowMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderListFragment newInstance(String param1, String param2) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView recListPesananCustomer;
    AdapterPesananSudahSiap adapterDapur;
    AdapterPesananSudahSiap.clickPesanan listenerClickPesanan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
    }
    void saveListValidCustomer(List<Customer> listCustomer){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("OTW_SIAPKAN_DAPUR", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPreferences.edit().putString("KEY_DAPUR",gson.toJson(listCustomer)).commit();
    }

    List<Customer> getListValidCustomer(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("OTW_SIAPKAN_DAPUR",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
        List<Customer> listCust = gson.fromJson(sharedPreferences.getString("KEY_DAPUR",""),typeCustomer);
        return listCust;
    }
    ArrayList<Customer> listCustomerValid;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ImageView btnRefresh;
    void insertLogDataValidPesananKeDatabase(Customer customer){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://re-food-7fc1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("LogPesanan").child(String.valueOf(customer.getNoCustomer()));
        myRef.setValue(customer);
    }

    void setAdapterListPesanan() {
        listCustomerValid = new ArrayList<>();
        //        setAdapterListPesanan();
        database = FirebaseDatabase.getInstance("https://re-food-7fc1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference().child("CustomerValid");
        System.out.println(myRef.getKey() + " KEEEY ");
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


                /*
                List<Customer> validCustomer = new ArrayList<>();
                for (Customer customerValid : listCustomerValid) {
                    if(customerValid.isStatusPesanan() == true){
                        List<Pesanan> listPesanan = customerValid.getSemuaPesanan();
                        customerValid.setSemuaPesanan(listPesanan);
                        validCustomer.add(customerValid);

                    }
                }
                 */
                listenerClickPesanan = new AdapterPesananSudahSiap.clickPesanan() {
                    @Override
                    public void clickPesanan(int pesanan) {
                        insertLogDataValidPesananKeDatabase(listCustomerValid.get(pesanan));
                        DatabaseReference dbR = database.getReference().child("CustomerValid").child(listCustomerValid.get(pesanan).getName());
                        dbR.removeValue();
                        listCustomerValid.remove(pesanan);
                        Toast.makeText(getActivity().getApplicationContext(),"Pesanan Siap Diantarkan",Toast.LENGTH_SHORT).show();
                        adapterDapur.notifyDataSetChanged();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_base_frame, new OrderListFragment()).commit();

                    }
                    @Override
                    public void clickRadio(int positions, boolean status) {
                        listCustomerValid.get(positions).setStatusPesanan(status);

                    }
                };
                adapterDapur = new AdapterPesananSudahSiap(listCustomerValid, listenerClickPesanan,listMenu);
                recListPesananCustomer.setAdapter(adapterDapur);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Customer customer = dataSnapshot1.getValue(Customer.class);
                            System.out.println(customer.getName() + " Order Selesai");
                            listCustomerValid.add(customer);
                        }
                        adapterDapur.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recListPesananCustomer = view.findViewById(R.id.id_rec_list_pesanan_cust);
        setAdapterListPesanan();

    }
    List<Menu> getDefaultPrefencesMenuMakanan(){
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("PREF_MENU_MAKANAN", Context.MODE_PRIVATE);
        Type typeMenu = new TypeToken<List<Menu>>(){}.getType();
        Gson gson = new Gson();
        List<Menu> listMenuMakanan = gson.fromJson(sharedPreferences.getString("KEY_MENU_MAKANAN",""),typeMenu);
        return listMenuMakanan;
    }
    List<Menu> getDefaultPrefencesMenuMinuman(){
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("PREF_MENU_MINUMAN", Context.MODE_PRIVATE);
        Type typeMenu = new TypeToken<List<Menu>>(){}.getType();
        Gson gson = new Gson();
        List<Menu> listMenuMakanan = gson.fromJson(sharedPreferences.getString("KEY_MENU_MINUMAN",""),typeMenu);
        return listMenuMakanan;
    }
    List<Menu> getDefaultPrefencesMenuLauk(){
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("PREF_MENU_LAUK", Context.MODE_PRIVATE);
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_list, container, false);
    }
}
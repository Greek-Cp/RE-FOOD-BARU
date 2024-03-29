package com.nicomot.re_food.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicomot.re_food.R;
import com.nicomot.re_food.adapter.AdapterPesanan;
import com.nicomot.re_food.model.Customer;
import com.nicomot.re_food.model.Menu;
import com.nicomot.re_food.model.Pesanan;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    void saveListValidCustomer(List<Customer> listCustomer){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("ORDERAN_DITERIMA", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPreferences.edit().putString("KEY_ORDER",gson.toJson(listCustomer)).commit();
        Toast.makeText(getActivity().getApplicationContext(),"Model Saved", Toast.LENGTH_LONG).show();
    }
    List<Customer> getListValidCustomer(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("ORDERAN_DITERIMA",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
        List<Customer> listCust = gson.fromJson(sharedPreferences.getString("KEY_ORDER",""),typeCustomer);
        System.out.println(new Gson().toJson(listCust) + " ISI JSON GLIM");
        return listCust;

    }
    String getLogModelCustomer(String calledFrom){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("PREF_CUST",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
        String r = sharedPreferences.getString("MODEL_CUST_KEY","");
        System.out.println( calledFrom +  "LOAD = " + r);
        return r;
    }
    RecyclerView recListPesananCustomer;
    AdapterPesanan adapterPesanan;
    AdapterPesanan.clickPesanan listenerClickPesanan;
    EditText debugLog;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recListPesananCustomer = view.findViewById(R.id.id_rec_list_pesanan_cust);
        //debugLog = view.findViewById(R.id.id_search_test);
        //debugLog.setText(getLogModelCustomer("From Home Fragment"));
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
                if (    names[i].getNamaPesanan().equals( names[k].getNamaPesanan())) {
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

    void setAdapterListPesanan(){
        List<Customer> listCustomerValid = getListValidCustomer();
        if(listCustomerValid != null){
            List<Customer> validCustomer = new ArrayList<>();
            for(Customer customerValid : listCustomerValid){
                System.out.println("no cust = " + customerValid.getNoCustomer());
                if(customerValid.isStatusSudahDiSiapkan() == false){
                    List<Pesanan> listPesanan = customerValid.getSemuaPesanan();
                    List<Pesanan> filterBaru  = removeDuplicatePesanan(listPesanan);
                    customerValid.setSemuaPesanan(filterBaru);
                    validCustomer.add(customerValid);

                }
            }
            listenerClickPesanan = new AdapterPesanan.clickPesanan() {
                @Override
                public void clickPesanan(int pesanan) {
                    Toast.makeText(getActivity().getApplicationContext(), "Pesanan ke - " + pesanan + " dipilih", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_base_frame,new SiapkanPesanan(validCustomer.get(pesanan),pesanan)).commit();
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
            adapterPesanan = new AdapterPesanan(validCustomer,listenerClickPesanan,listMenu);
            recListPesananCustomer.setAdapter(adapterPesanan);
        }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
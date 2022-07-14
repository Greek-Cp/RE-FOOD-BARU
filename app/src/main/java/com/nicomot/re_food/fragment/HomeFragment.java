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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicomot.re_food.R;
import com.nicomot.re_food.adapter.AdapterPesanan;
import com.nicomot.re_food.model.Customer;
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
        System.out.println("Json Save Valid = " + gson.toJson(listCustomer));
        sharedPreferences.edit().putString("KEY_ORDER",gson.toJson(listCustomer)).commit();
        Toast.makeText(getActivity().getApplicationContext(),"Model Saved", Toast.LENGTH_LONG).show();
    }
    List<Customer> getListValidCustomer(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("ORDERAN_DITERIMA",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
        System.out.println("Json Load valid = " + sharedPreferences.getString("KEY_ORDER",""));
        List<Customer> listCust = gson.fromJson(sharedPreferences.getString("KEY_ORDER",""),typeCustomer);
        return listCust;
    }
    RecyclerView recListPesananCustomer;
    AdapterPesanan adapterPesanan;
    AdapterPesanan.clickPesanan listenerClickPesanan;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recListPesananCustomer = view.findViewById(R.id.id_rec_list_pesanan_cust);
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

    void setAdapterListPesanan(){
        List<Customer> listCustomerValid = getListValidCustomer();
        if(listCustomerValid != null){
            List<Customer> validCustomer = new ArrayList<>();
            for(Customer customerValid : listCustomerValid){
                List<Pesanan> listPesanan = customerValid.getSemuaPesanan();
                List<Pesanan> filterBaru  = removeDuplicatePesanan(listPesanan);
                customerValid.setSemuaPesanan(filterBaru);
                validCustomer.add(customerValid);
            }
            listenerClickPesanan = new AdapterPesanan.clickPesanan() {
                @Override
                public void clickPesanan(int pesanan) {
                    Toast.makeText(getActivity().getApplicationContext(), "Pesanan ke - " + pesanan + " dipilih", Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.id_base_frame,new SiapkanPesanan(listCustomerValid.get(pesanan))).commit();
                }
            };

            adapterPesanan = new AdapterPesanan(validCustomer,listenerClickPesanan);
            recListPesananCustomer.setAdapter(adapterPesanan);
        }

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
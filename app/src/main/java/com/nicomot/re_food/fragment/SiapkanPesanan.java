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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicomot.re_food.R;
import com.nicomot.re_food.adapter.AdapterPesananCustomer;
import com.nicomot.re_food.model.Account;
import com.nicomot.re_food.model.Customer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SiapkanPesanan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SiapkanPesanan extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Customer customer;

    public SiapkanPesanan(Customer customer){
        this.customer = customer;
    }
    public SiapkanPesanan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SiapkanPesanan.
     */
    // TODO: Rename and change types and number of parameters
    public static SiapkanPesanan newInstance(String param1, String param2) {
        SiapkanPesanan fragment = new SiapkanPesanan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView pesananRec;
    TextView tagihanTv;
    Button btnKirimKeDapur;

    List<Customer> listCust;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pesananRec = view.findViewById(R.id.id_rec_menu_makanan_inside_rec_list_pesanan);
        tagihanTv = view.findViewById(R.id.id_tv_tagihan);
        btnKirimKeDapur = view.findViewById(R.id.id_btn_kirim_ke_dapur);
        AdapterPesananCustomer adapterPesananCustomer = new AdapterPesananCustomer(customer);
        pesananRec.setAdapter(adapterPesananCustomer);
        tagihanTv.setText(customer.getMessageTagihan());
        listCust = getListValidCustomer();
        if(listCust == null){
            listCust = new ArrayList<>();
        }
        btnKirimKeDapur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customer.setStatusPesanan(false);
                listCust.add(customer);
                for(int i = 0; i < customer.getSemuaPesanan().size(); i++){
                    System.out.println("Before kirim = " + customer.getSemuaPesanan().get(i).getJumlahPesanan());
                }
                saveListValidCustomer(listCust);
                btnKirimKeDapur.setEnabled(false);
                insertDataPesananKeDatabase(customer);
                Toast.makeText(getActivity().getApplicationContext(),"Pesanan Dikirimkan Ke Dapur", Toast.LENGTH_SHORT).show();

            }
        });
    }

    void saveListValidCustomer(List<Customer> listCustomer){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("OTW_SIAPKAN_DAPUR", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        System.out.println("Json Save Valid = " + gson.toJson(listCustomer));
        sharedPreferences.edit().putString("KEY_DAPUR",gson.toJson(listCustomer)).commit();
    }

    void insertDataPesananKeDatabase(Customer customer){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://re-food-7fc1b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Customer").child(customer.getName());
        System.out.println(" teest = " + myRef.getKey());
        myRef.setValue(customer);
    }
    List<Customer> getListValidCustomer(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("OTW_SIAPKAN_DAPUR",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
        System.out.println("Json Load valid = " + sharedPreferences.getString("KEY_DAPUR",""));
        List<Customer> listCust = gson.fromJson(sharedPreferences.getString("KEY_DAPUR",""),typeCustomer);
        return listCust;
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
        return inflater.inflate(R.layout.fragment_siapkan_pesanan, container, false);
    }
}
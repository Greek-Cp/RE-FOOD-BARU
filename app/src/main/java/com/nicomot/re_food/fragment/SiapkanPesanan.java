package com.nicomot.re_food.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

    int posisi;
    public SiapkanPesanan(Customer customer,int posisi){
        this.customer = customer;
        this.posisi = posisi;
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
    Dialog dialogSuccesKirimPesanan;

    void showDialogAddPesananSuccess(){
        dialogSuccesKirimPesanan = new Dialog(getActivity());
        View view  = getActivity().getLayoutInflater().inflate(R.layout.dialog_menambahkan_succes, null);
        dialogSuccesKirimPesanan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSuccesKirimPesanan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSuccesKirimPesanan.setContentView(view);
        dialogSuccesKirimPesanan.show();
    }
    void closeDialogAddPesananSuccess(){
        dialogSuccesKirimPesanan.dismiss();
    }


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
    static String convertRupiah(int num){
        System.out.println("Num = " + num);
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        //return kursIndonesia.format(num).substring(0,kursIndonesia.format(num).length() - 3);
        return kursIndonesia.format(num);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pesananRec = view.findViewById(R.id.id_rec_menu_makanan_inside_rec_list_pesanan);
        tagihanTv = view.findViewById(R.id.id_tv_tagihan);
        btnKirimKeDapur = view.findViewById(R.id.id_btn_kirim_ke_dapur);
        AdapterPesananCustomer adapterPesananCustomer = new AdapterPesananCustomer(customer);
        pesananRec.setAdapter(adapterPesananCustomer);
        System.out.println("get status dikirim = " + customer.isStatusSudahDiSiapkan());
        if(customer.getMessageTagihan() == null){
            StringBuilder sb = new StringBuilder();
            sb.append("-------------------------" + "\n");
            sb.append("PESANAN DARI = " + customer.getDataDiri().getNamaPemesan() + "\n");
            sb.append("NOMER HANDPHONE = " + customer.getDataDiri().getNomerHp() + "\n");
            sb.append("ALAMAT = " + customer.getDataDiri().getAlamat() + "\n");
            sb.append("-------------------------" + "\n");
            sb.append("TOTAL = " + convertRupiah(customer.getTotalPesanan()));
            tagihanTv.setText(sb.toString());
        } else{
            tagihanTv.setText(customer.getMessageTagihan());
        }
        listCust = getListValidCustomer();
        if(listCust == null){
            listCust = new ArrayList<>();
        }
        btnKirimKeDapur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Customer> currentListCustomerValid = getListValidCustomerGetListOrderan();
                customer.setStatusPesanan(false);
                customer.setStatusSudahDiSiapkan(true);
                System.out.println("get status sesudah = " + customer.isStatusSudahDiSiapkan());
                for(int i = 0;i < currentListCustomerValid.size(); i++){
                    if(currentListCustomerValid.get(i).getNoCustomer() == customer.getNoCustomer()){
                        System.out.println("Current " + currentListCustomerValid.get(i).getNoCustomer() + " CUSTOMER");
                        currentListCustomerValid.get(i).setStatusSudahDiSiapkan(true);

                    }
                }
                //                currentListCustomerValid.set(posisi,customer);
                saveListValidCustomerSaveOrderan(currentListCustomerValid);
                listCust.add(customer);
                saveListValidCustomer(listCust);
                btnKirimKeDapur.setEnabled(false);
                insertDataPesananKeDatabase(customer);
                showDialogAddPesananSuccess();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeDialogAddPesananSuccess();

                    }
                },3000);
                Toast.makeText(getActivity().getApplicationContext(),"Pesanan Dikirimkan Ke Dapur", Toast.LENGTH_SHORT).show();

            }
        });
    }

    void saveListValidCustomerSaveOrderan(List<Customer> listCustomer){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("ORDERAN_DITERIMA", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPreferences.edit().putString("KEY_ORDER",gson.toJson(listCustomer)).commit();
        Toast.makeText(getActivity().getApplicationContext(),"Model Saved", Toast.LENGTH_LONG).show();
    }
    List<Customer> getListValidCustomerGetListOrderan(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("ORDERAN_DITERIMA",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
        List<Customer> listCust = gson.fromJson(sharedPreferences.getString("KEY_ORDER",""),typeCustomer);
        return listCust;
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
        DatabaseReference myRef = database.getReference("Customer").child(String.valueOf(customer.getNoCustomer()));
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
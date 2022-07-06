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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicomot.re_food.R;
import com.nicomot.re_food.model.Customer;
import com.nicomot.re_food.util.ShowMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ReceiveBroadcastReceiver receiveBroadcastReceiver;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receiveBroadcastReceiver = new ReceiveBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NOTIFICATION_DATA");
        getActivity().registerReceiver(receiveBroadcastReceiver,intentFilter);

    }

    public class ReceiveBroadcastReceiver extends BroadcastReceiver {
        List<Customer> listCustomer;
        List<String> chat;

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                listCustomer = getModelCustomer();
            }catch (NullPointerException e){
            }
            if(listCustomer == null){
                String from = intent.getStringExtra("FROM");
                String pesan = intent.getStringExtra("CHAT");
                listCustomer = new ArrayList<>();
                List<String> chat = new ArrayList<>();
                chat.add(pesan);
                System.out.println("chat null = " + chat);
                listCustomer.add(new Customer(from,chat));
                saveModelList(listCustomer);
            } else{
                String from = intent.getStringExtra(    "FROM");
                String pesan = intent.getStringExtra("CHAT");
                System.out.println("chat not null = " + pesan);
                for(int index = 0; index < listCustomer.size(); index++){
                    if(listCustomer.get(index).getName().equals(from)){
                        List<String> pesn = listCustomer.get(index).getListMessage();
                       
                        pesn.add(pesan);

                        listCustomer.set(index,listCustomer.get(index)).setListMessage(pesn);
                        Toast.makeText(getContext(),"Name Same ! Continue Chat chatSize = "  + pesn.size()  ,Toast.LENGTH_SHORT).show();
                        saveModelList(listCustomer);
                    } else{
                        List<String> listPesan = new ArrayList<>();
                        Toast.makeText(getContext(),"Name Not Same ! Insert New Data to list ! = "  ,Toast.LENGTH_SHORT).show();
                        saveModelList(listCustomer);
                    }
                }
            }

        }
    }

    void saveModelList(List<Customer> listCustomer){
        SharedPreferences sharedPreferences;
        sharedPreferences = getContext().getSharedPreferences("PREF_CUST",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPreferences.edit().putString("MODEL_CUST_KEY",gson.toJson(listCustomer)).commit();
        Toast.makeText(getContext(),"Model Saved", Toast.LENGTH_LONG).show();

    }
    List<Customer> getModelCustomer(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("PREF_CUST",Context.MODE_PRIVATE);
            Gson gson = new Gson();
            Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
            System.out.println("Json = " + sharedPreferences.getString("MODEL_CUST_KEY",""));
            List<Customer> listCust = gson.fromJson(sharedPreferences.getString("MODEL_CUST_KEY",""),typeCustomer);
            return listCust;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_list, container, false);
    }
}
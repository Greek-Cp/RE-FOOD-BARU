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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicomot.re_food.R;
import com.nicomot.re_food.adapter.AdapterImageMenuKhsusuOrderManual;
import com.nicomot.re_food.adapter.AdapterPesanan;
import com.nicomot.re_food.adapter.AdapterPesananKhususOrderManual;
import com.nicomot.re_food.model.Customer;
import com.nicomot.re_food.model.DataDiri;
import com.nicomot.re_food.model.Menu;
import com.nicomot.re_food.model.Pesanan;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateOrderManualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateOrderManualFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateOrderManualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateOrderManualFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateOrderManualFragment newInstance(String param1, String param2) {
        CreateOrderManualFragment fragment = new CreateOrderManualFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    TextInputEditText id_input_Nama , id_input_Alamat, id_input_NoTelepon,id_input_Harga, id_input_Jumlah , id_input_Total , id_input_StockItem;
    Spinner id_spinnerMenu , id_spinnerJenis;
    Button btn_Simpan,btn_SimpanPesanan;

    RecyclerView recViewMenuPiihanUser;

    List<Customer> listCustomer;
    List<Pesanan> listSemuaPesanan;
    List<Pesanan> temporaryPesanan;

    int indexCustomer;
    void saveIndexCustomer(int indexCurrent){
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("PREF_INDEX_CUSTO",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit().putInt("INDEX_CUST",indexCurrent);
        editor.commit();
    }

    int getIndexCustomer(){
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("PREF_INDEX_CUSTO",Context.MODE_PRIVATE);
        return sharedPreferences.getInt("INDEX_CUST",0);
    }
    String getDate(){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return date;
    }
    int totalHargaPesanan = 0;
    Dialog dialogs;
    void showDialogAfterDelete(){
        dialogs = new Dialog(getActivity());
        View view  = getActivity().getLayoutInflater().inflate(R.layout.dialog_stock_alert, null);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogs.setContentView(view);
        dialogs.show();
    }
    void closeDialogAfterDelete(){
        dialogs.dismiss();

    }

    Dialog dialogAddPesanan;
    void showDialogAddPesananSuccess(){
        dialogAddPesanan = new Dialog(getActivity());
        View view  = getActivity().getLayoutInflater().inflate(R.layout.dialog_menambahkan_succes, null);
        dialogAddPesanan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddPesanan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddPesanan.setContentView(view);
        dialogAddPesanan.show();
    }
    void closeDialogAddPesananSuccess(){
        dialogAddPesanan.dismiss();
    }


    Dialog dialogAddOrderan;
    void showDialogOrderanSuccess(){
        dialogAddOrderan = new Dialog(getActivity());
        View view  = getActivity().getLayoutInflater().inflate(R.layout.dialog_menambahkan_succes, null);
        dialogAddOrderan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddOrderan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddOrderan.setContentView(view);
        dialogAddOrderan.show();
    }
    void closeDialogAddOrderanSuccess(){
        dialogAddOrderan.dismiss();
    }

    Dialog dialogHapus;
    void showDialogAfterDeletePesanan(){
        dialogHapus = new Dialog(getActivity());
        View view  = getActivity().getLayoutInflater().inflate(R.layout.dialog_hapus, null);
        dialogHapus.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogHapus.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogHapus.setContentView(view);
        dialogHapus.show();
    }
    void closeDialogAfterDeletePesanan(){
        dialogHapus.dismiss();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeId(view);
        listCustomer = getListValidCustomer();
        if(listCustomer == null){
            listCustomer = new ArrayList<>();

        }
        listSemuaPesanan = new ArrayList<>();
        temporaryPesanan = new ArrayList<>();

        btn_SimpanPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jumlahPesanan = id_input_Jumlah.getText().toString();
                int currentStock = Integer.parseInt(id_input_StockItem.getText().toString());
                if(Integer.parseInt(jumlahPesanan) > currentStock){
                    showDialogAfterDelete();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            closeDialogAfterDelete();
                        }
                    },3000);
                } else{
                    btn_SimpanPesanan.setEnabled(true);
                    String namaMenu = id_spinnerJenis.getSelectedItem().toString();
                    int harga = Integer.parseInt(id_input_Harga.getText().toString());
                    System.out.println("Jumlah pesanan =  " + jumlahPesanan);
                    System.out.println("Size pesanan = " + listSemuaPesanan.size());
                    setAdapterListPesanan();
                    adapterPesanan.notifyDataSetChanged();
                    Pesanan pesanan = new Pesanan(namaMenu,Integer.parseInt(jumlahPesanan),harga);
                    temporaryPesanan.add(pesanan);
                    for(int ind = 0; ind < Integer.parseInt(jumlahPesanan); ind++){
                        listSemuaPesanan.add(pesanan);
                    }
                    totalHargaPesanan += Integer.parseInt(id_input_Total.getText().toString());
                    System.out.println("");
                    showDialogAddPesananSuccess();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            closeDialogAddPesananSuccess();
                        }
                    },2000);
                    if(id_spinnerMenu.getSelectedItem().equals("Makanan")){
                        List<Menu> menuMakananUpdate = getMenuMakanan();
                        for(int v  = 0; v < menuMakananUpdate.size(); v++){
                            if(menuMakananUpdate.get(v).getNamaItem().equals(namaMenu)){
                                int StockBaru =  Integer.parseInt(id_input_StockItem.getText().toString()) - Integer.parseInt(jumlahPesanan);
                                id_input_StockItem.setText(String.valueOf(StockBaru));
                                menuMakananUpdate.get(v).setStockItem(StockBaru);
                                saveMenuMakan(menuMakananUpdate);

                            }
                        }
                    } else if(id_spinnerMenu.getSelectedItem().equals("Minuman")){
                        List<Menu> menuMinumanUpdate = getMenuMinuman();
                        for(int v  = 0; v < menuMinumanUpdate.size(); v++){
                            if(menuMinumanUpdate.get(v).getNamaItem().equals(namaMenu)){
                                int StockBaru =  Integer.parseInt(id_input_StockItem.getText().toString()) - Integer.parseInt(jumlahPesanan);
                                id_input_StockItem.setText(String.valueOf(StockBaru));
                                menuMinumanUpdate.get(v).setStockItem(StockBaru);
                                saveMenuMinuman(menuMinumanUpdate);
                            }
                        }
                    } else if(id_spinnerMenu.getSelectedItem().equals("Lauk")){
                        List<Menu> menuLauk = getMenuLauk();
                        for(int v  = 0; v < menuLauk.size(); v++){
                            if(menuLauk.get(v).getNamaItem().equals(namaMenu)){
                                int StockBaru =  Integer.parseInt(id_input_StockItem.getText().toString()) - Integer.parseInt(jumlahPesanan);
                                id_input_StockItem.setText(String.valueOf(StockBaru));
                                menuLauk.get(v).setStockItem(StockBaru);
                                saveMenuLauk(menuLauk);
                            }
                        }
                    }
                    }

            }
        });
        btn_Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("list semua pesanan = " + listSemuaPesanan.get(0).getJumlahPesanan() + "  EEEEs");
                String namaPemesan = id_input_Nama.getText().toString();
                String alamatPemesan = id_input_Alamat.getText().toString();
                String noTeleponPemesan = id_input_NoTelepon.getText().toString();
                DataDiri dataDiri = new DataDiri(namaPemesan,alamatPemesan,noTeleponPemesan);
                int currentNoCustomer = getIndexCustomer();
                currentNoCustomer++;
                saveIndexCustomer(currentNoCustomer);
                Customer customer = new Customer(namaPemesan, listSemuaPesanan,currentNoCustomer,getDate());
                customer.setDataDiri(dataDiri);
                customer.setStatusSudahDiSiapkan(false);
                customer.setTotalPesanan(totalHargaPesanan);
                id_input_Nama.setText("");
                id_input_Jumlah.setText("");
                id_input_Alamat.setText("");
                id_input_Total.setText("");
                id_input_NoTelepon.setText("");
                id_input_Harga.setText("");
                listCustomer.add(customer);
                temporaryPesanan.clear();
                adapterPesanan.notifyDataSetChanged();
                saveListValidCustomer(listCustomer);
                showDialogOrderanSuccess();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeDialogAddOrderanSuccess();
                    }
                },3000);
            }
        });
        ArrayList<String> listKategoriMakanan = new ArrayList<>();
        listKategoriMakanan.add("Makanan");
        listKategoriMakanan.add("Minuman");
        listKategoriMakanan.add("Lauk");
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
        listKategoriMakanan);
        id_spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (listKategoriMakanan.get(i)){
                    case "Makanan":
                        List<Menu> menuMakanan = getMenuMakanan();
                        List<String> listStrMakanan = new ArrayList<>();
                        ArrayAdapter spinnerArrayAdapterMakanan = new ArrayAdapter(getActivity().getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                listStrMakanan);
                        for(int y1 = 0; y1 < menuMakanan.size() - 1; y1++){
                            listStrMakanan.add(menuMakanan.get(y1).getNamaItem());
                        }
                        id_spinnerJenis.setAdapter(spinnerArrayAdapterMakanan);
                        id_spinnerJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                           @Override
                           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                               System.out.println("Selected item = " + menuMakanan.get(i).getNamaItem());
                               id_input_Harga.setText(String.valueOf(menuMakanan.get(i).getHargaItem()));
                               id_input_StockItem.setText(String.valueOf(menuMakanan.get(i).getStockItem()));
                               id_input_Jumlah.addTextChangedListener(new TextWatcher() {
                                   @Override
                                   public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                   }

                                   @Override
                                   public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                   }

                                   @Override
                                   public void afterTextChanged(Editable editable) {
                                       int getHarga = menuMakanan.get(i).getHargaItem();
                                       if(!editable.toString().isEmpty()){
                                           int total = Integer.parseInt(editable.toString()) * getHarga;
                                           id_input_Total.setText(String.valueOf(total));
                                       } else{
                                           id_input_Total.setText("");
                                       }

                                   }
                               });
                           }

                           @Override
                           public void onNothingSelected(AdapterView<?> adapterView) {

                           }
                       });
                        break;
                    case "Minuman":
                        List<Menu> menuMinuman = getMenuMinuman();
                        List<String> listStrMinuman = new ArrayList<>();
                        ArrayAdapter spinnerArrayAdapterMinuman = new ArrayAdapter(getActivity().getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                listStrMinuman);

                        for(Menu menu : menuMinuman){
                            if(!menu.getNamaItem().equals("Tambahkan Menu")){
                                listStrMinuman.add(menu.getNamaItem());
                            }
                        }
                        id_spinnerJenis.setAdapter(spinnerArrayAdapterMinuman);
                        id_spinnerJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                System.out.println("Selected item = " + menuMinuman.get(i).getNamaItem());
                                id_input_Harga.setText(String.valueOf(menuMinuman.get(i).getHargaItem()));
                                id_input_StockItem.setText(String.valueOf(menuMinuman.get(i).getStockItem()));
                                id_input_Jumlah.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                        int getHarga = menuMinuman.get(i).getHargaItem();
                                        if(!editable.toString().isEmpty()){

                                            int total = Integer.parseInt(editable.toString()) * getHarga;
                                            id_input_Total.setText(String.valueOf(total));
                                        } else{
                                            id_input_Total.setText("");
                                        }

                                    }
                                });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case "Lauk":
                        List<Menu> menuLauk = getMenuLauk();
                        List<String> listStrLauk = new ArrayList<>();
                        ArrayAdapter spinnerArrayAdapterLauk = new ArrayAdapter(getActivity().getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                listStrLauk);
                        for(Menu menu : menuLauk){
                            if(!menu.getNamaItem().equals("Tambahkan Menu")){
                                listStrLauk.add(menu.getNamaItem());
                            }
                        }
                        id_spinnerJenis.setAdapter(spinnerArrayAdapterLauk);
                        id_spinnerJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                System.out.println("Selected item = " + menuLauk.get(i).getNamaItem());
                                id_input_Harga.setText(String.valueOf(menuLauk.get(i).getHargaItem()));
                                id_input_StockItem.setText(String.valueOf(menuLauk.get(i).getStockItem()));

                                id_input_Jumlah.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                        int getHarga = menuLauk.get(i).getHargaItem();
                                        if(!editable.toString().isEmpty()){

                                            int total = Integer.parseInt(editable.toString()) * getHarga;
                                            id_input_Total.setText(String.valueOf(total));
                                        } else{
                                            id_input_Total.setText("");
                                        }

                                    }
                                });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        id_spinnerMenu.setAdapter(spinnerArrayAdapter);

    }
    AdapterImageMenuKhsusuOrderManual adapterPesanan;
    AdapterImageMenuKhsusuOrderManual.clickItemPesanan listenerPesanan;


    void saveMenuMakan(List<Menu> menuMakananList){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF_MENU_MAKANAN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String gsonMenuMakan = gson.toJson(menuMakananList);
        editor.putString("KEY_MENU_MAKANAN",gsonMenuMakan).commit();
    }

    void saveMenuMinuman(List<Menu> menuMinumanList){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF_MENU_MINUMAN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String gsonMenuMakan = gson.toJson(menuMinumanList);
        editor.putString("KEY_MENU_MINUMAN",gsonMenuMakan).commit();
    }

    void saveMenuLauk(List<Menu> menuLaukList){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF_MENU_LAUK", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String gsonMenuMakan = gson.toJson(menuLaukList);
        editor.putString("KEY_MENU_LAUK",gsonMenuMakan).commit();
    }
    void setAdapterListPesanan(){
        List<Menu> menuMakanan = getMenuMakanan();
        List<Menu> menuMinuman = getMenuMinuman();
        List<Menu> menuLauk = getMenuLauk();
        List<Menu> listMenu = new ArrayList<>();
        for(Menu mMakanan : menuMakanan){
            listMenu.add(mMakanan);
        }
        for(Menu mMinuman : menuMinuman){
            listMenu.add(mMinuman);
        }
        for(Menu mLauk : menuLauk){
            listMenu.add(mLauk);
        }
            listenerPesanan = new AdapterImageMenuKhsusuOrderManual.clickItemPesanan() {
                @Override
                public void deleteItemSelected(int position) {
                    temporaryPesanan.remove(position);
                    adapterPesanan.notifyDataSetChanged();
                    showDialogAfterDeletePesanan();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            closeDialogAfterDeletePesanan();
                        }
                    },2000);
                }
            };
            adapterPesanan = new AdapterImageMenuKhsusuOrderManual(temporaryPesanan,listMenu,listenerPesanan);
            recViewMenuPiihanUser.setAdapter(adapterPesanan);
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
    List<Menu> getDefaultPrefencesMenuMakanan(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF_MENU_MAKANAN", Context.MODE_PRIVATE);
        Type typeMenu = new TypeToken<List<Menu>>(){}.getType();
        Gson gson = new Gson();
        List<Menu> listMenuMakanan = gson.fromJson(sharedPreferences.getString("KEY_MENU_MAKANAN",""),typeMenu);
        return listMenuMakanan;
    }
    List<Menu> getDefaultPrefencesMenuMinuman(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF_MENU_MINUMAN", Context.MODE_PRIVATE);
        Type typeMenu = new TypeToken<List<Menu>>(){}.getType();
        Gson gson = new Gson();
        List<Menu> listMenuMakanan = gson.fromJson(sharedPreferences.getString("KEY_MENU_MINUMAN",""),typeMenu);
        return listMenuMakanan;
    }
    List<Menu> getDefaultPrefencesMenuLauk(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF_MENU_LAUK", Context.MODE_PRIVATE);
        Type typeMenu = new TypeToken<List<Menu>>(){}.getType();
        Gson gson = new Gson();
        List<Menu> listMenuMakanan = gson.fromJson(sharedPreferences.getString("KEY_MENU_LAUK",""),typeMenu);
        return listMenuMakanan;
    }
    List<Menu> getMenuMakanan(){
        List<Menu> listMenuMakanan = getDefaultPrefencesMenuMakanan();
        if(listMenuMakanan == null){
            listMenuMakanan = new ArrayList<>();
            listMenuMakanan.add(new Menu("Nasi Goreng",10000,10,false, R.drawable.makanan_4_nasigoreng));
            listMenuMakanan.add(new Menu("Mie Goreng",10000,10,false,R.drawable.makanan_2_mie_goreng));
            listMenuMakanan.add(new Menu("Mie Kuah",10000,10,false,R.drawable.makanan_3_mie_rebus));
            listMenuMakanan.add(new Menu("Kweatiu Goreng ",10000,10,false,R.drawable.makanan_1_kwantiew));

        }
        return listMenuMakanan;
    }
    List<Menu> getMenuMinuman(){
        List<Menu> listMenuMinuman = getDefaultPrefencesMenuMinuman();
        if(listMenuMinuman == null){
            listMenuMinuman = new ArrayList<>();
            listMenuMinuman.add(new Menu("Air Mineral",4000,10,false,R.drawable.minuman_1_air_pputih));
            listMenuMinuman.add(new Menu("Es Teh",3000,10,false,R.drawable.minuman_2_es_teh));
            listMenuMinuman.add(new Menu("Teh Hangat",3000,10,false,R.drawable.minuman_4_teh));
            listMenuMinuman.add(new Menu("Es Jeruk",3000,10,false,R.drawable.minuman_3_es_jerul));
            listMenuMinuman.add(new Menu("Jeruk Hangat",3000,10,false,R.drawable.minuman_3_es_jerul));

        }
        return listMenuMinuman;
    }
    List<Menu> getMenuLauk(){
        List<Menu> listMenuLauk = getDefaultPrefencesMenuLauk();
        if(listMenuLauk == null){
            listMenuLauk = new ArrayList<>();
            listMenuLauk.add(new Menu("Ikan Goreng",5000,10,false,R.drawable.lauk_2_ikan));
            listMenuLauk.add(new Menu("Kangkung",3000,10,false,R.drawable.lauk_3_kangkung));
            listMenuLauk.add(new Menu("Oseng Tempe",3000,10,false,R.drawable.lauk_4_oseng_tempe));
            listMenuLauk.add(new Menu("Semur Sayur",4000,10,false,R.drawable.lauk_1_semur_sayur));

        }
        return listMenuLauk;
    }



    void initializeId(View v){
        id_input_Nama = v.findViewById(R.id.id_edit_text_nama);
        id_input_Alamat = v.findViewById(R.id.id_edit_text_alamat);
        id_input_NoTelepon = v.findViewById(R.id.id_edit_text_no_telpon);
        id_input_Harga = v.findViewById(R.id.id_edit_text_harga);
        id_input_Jumlah = v.findViewById(R.id.id_edit_text_jumlah);
        id_input_Harga = v.findViewById(R.id.id_edit_text_harga);
        id_input_Total = v.findViewById(R.id.id_edit_text_total);
        id_spinnerMenu = v.findViewById(R.id.id_spsinner_menu_category);
        id_spinnerJenis = v.findViewById(R.id.id_spinner_pilih_makanan);
        btn_SimpanPesanan = v.findViewById(R.id.id_btn_tambah_menu_pesanan);
        btn_Simpan = v.findViewById(R.id.id_btn_simpan_menu_pesanan);
        id_input_NoTelepon.setInputType(InputType.TYPE_CLASS_NUMBER);
        id_input_Harga.setInputType(InputType.TYPE_CLASS_NUMBER);
        id_input_Jumlah.setInputType(InputType.TYPE_CLASS_NUMBER);
        id_input_Total.setInputType(InputType.TYPE_CLASS_NUMBER);
        recViewMenuPiihanUser = v.findViewById(R.id.id_rec_list_menu);
        id_input_StockItem = v.findViewById(R.id.id_edit_text_stock);
        btn_Simpan.setEnabled(false);

        id_input_StockItem.setEnabled(false);

        id_input_StockItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    int stock = Integer.parseInt(editable.toString());
                    if(stock == 0){
                        btn_SimpanPesanan.setEnabled(false);
                    } else {
                        btn_SimpanPesanan.setEnabled(true);
                    }
                }
            }
        });
        id_input_Nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 0){
                    btn_Simpan.setEnabled(false);
                } else{
                    btn_Simpan.setEnabled(true);
                }
            }
        });
    }
    void saveListValidCustomer(List<Customer> listCustomer){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("ORDERAN_DITERIMA", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPreferences.edit().putString("KEY_ORDER",gson.toJson(listCustomer)).commit();
    }
    List<Customer> getListValidCustomer(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("ORDERAN_DITERIMA",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeCustomer = new TypeToken<List<Customer>>(){}.getType();
        List<Customer> listCust = gson.fromJson(sharedPreferences.getString("KEY_ORDER",""),typeCustomer);
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
        return inflater.inflate(R.layout.fragment_create_order_manual, container, false);
    }
}
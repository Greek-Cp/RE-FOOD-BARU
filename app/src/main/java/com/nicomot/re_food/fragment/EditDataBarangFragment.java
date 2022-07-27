package com.nicomot.re_food.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicomot.re_food.R;
import com.nicomot.re_food.adapter.AdapterMenuEdit;
import com.nicomot.re_food.model.Customer;
import com.nicomot.re_food.model.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDataBarangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDataBarangFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditDataBarangFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditDataBarangFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDataBarangFragment newInstance(String param1, String param2) {
        EditDataBarangFragment fragment = new EditDataBarangFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    ActivityResultLauncher<String> mActivityResult = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if(result != null){
                System.out.println("result = " + result);

                try {
                    InputStream ios = getActivity().getApplicationContext().getContentResolver().openInputStream(result);
                        OutputStream fosQ;
                        FileOutputStream fileOutputStream;
                        ContentResolver resolver = getActivity().getApplicationContext().getContentResolver();
                        Bitmap bmp = BitmapFactory.decodeStream(ios);
                        ContentValues contentValues = new ContentValues();
                        String rand = getRandomNumberString();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, rand + "_");
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "REFOOD_RES/" );
                        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        //  fosQ = resolver.openOutputStream(imageUri);
                       // System.out.println(imageUri.getPath());
                       // bmp.compress(Bitmap.CompressFormat.PNG,100,fosQ);
                        //fosQ.flush();
                        //fosQ.close();
                       File fileDefault = new File(Environment.getExternalStorageDirectory() + "/DCIM/REFOOD_RES/" + rand  +"_.png");

                       fileOutputStream = new FileOutputStream(fileDefault);
                       textViewPath.setText(fileDefault.getAbsolutePath());
                       bmp.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);

                       Bitmap defBitmat = BitmapFactory.decodeFile(fileDefault.getAbsolutePath());
                       imgPlacement.setImageBitmap(defBitmat);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    });
      String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    Spinner spinnerMenu;
    RecyclerView recyclerViewMenu;
    TextInputEditText namaItem , hargaItem , stockItem ,statusItem , idItem;
    Button buttonSimpan ,pilihGambar;
    AdapterMenuEdit adapterMenuEdit;
    TextView textViewPath;
    ImageView imgPlacement;
    String test = "";
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinnerMenu = view.findViewById(R.id.id_spsinner_menu_category);
        recyclerViewMenu = view.findViewById(R.id.id_rec_list_menu);
        namaItem = view.findViewById(R.id.id_text_input_edit_text_nama_item);
        hargaItem = view.findViewById(R.id.id_text_input_edit_text_harga_item);
        stockItem = view.findViewById(R.id.id_text_input_edit_text_stock_item);
        statusItem = view.findViewById(R.id.id_text_input_edit_text_status_item);
        buttonSimpan = view.findViewById(R.id.id_btn_simpan_menu_perubahan);
        idItem = view.findViewById(R.id.id_text_input_edit_text_id_menu);
        imgPlacement = view.findViewById(R.id.id_img_tmep);
        pilihGambar = view.findViewById(R.id.id_btn_pilih_gambar);
        textViewPath =view.findViewById(R.id.id_tv_path_img);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.menu_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(adapter);

        pilihGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityResult.launch("image/*");
            }
        });
        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                AdapterMenuEdit.clickMakananItem listenerEditMenuMakan;
                switch (adapter.getItem(i).toString()){
                    case "Menu Makanan":
                        test = "Menu Makanan";
                        break;
                    case "Menu Minuman":
                        test = "Menu Minuman";
                        break;
                    case "Menu Lauk":
                        test = "Menu Lauk";
                        break;
                }
                switch (test){
                    case "Menu Makanan":
                        List<Menu> getMenuMakan = getMenuMakanan();
                        listenerEditMenuMakan = new AdapterMenuEdit.clickMakananItem() {
                            @Override
                            public void clickItemListener(int position) {
                                namaItem.setText(getMenuMakan.get(position).getNamaItem());
                                hargaItem.setText(String.valueOf(getMenuMakan.get(position).getHargaItem()));
                                stockItem.setText(String.valueOf(getMenuMakan.get(position).getStockItem()));
                                idItem.setText(String.valueOf(position));
                                if(getMenuMakan.size() - 1 == position){
                                    getMenuMakan.add(new Menu(  "Tambahkan Menu Makanan",0,0,false,R.drawable.ic_baseline_add_24));
                                    saveMenuMakan(getMenuMakan);
                                    adapter.notifyDataSetChanged();
                                    adapterMenuEdit.notifyDataSetChanged();
                                }
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void deleteItemListener(int positionOfItem) {
                                Toast.makeText(getActivity().getApplicationContext(),"Menghapus Menu Menu " + getMenuMakan.get(positionOfItem).getNamaItem()+ " Berhasil ",Toast.LENGTH_SHORT).show();
                                getMenuMakan.remove(positionOfItem);
                                saveMenuMakan(getMenuMakan);
                                adapterMenuEdit.notifyDataSetChanged();
                            }
                        };
                        buttonSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("name item = " + namaItem.getText().toString());
                            //getMenuMinuman.set(position,new Menu(namaItem.getText().toString(),Integer.parseInt(hargaItem.getText().toString()),Integer.parseInt(stockItem.getText().toString()),false,R.drawable.minuman_3_es_jerul));
                            getMenuMakan.set(Integer.parseInt(idItem.getText().toString()),new Menu(namaItem.getText().toString(),Integer.parseInt(hargaItem.getText().toString()),Integer.parseInt(stockItem.getText().toString()),false,textViewPath.getText().toString()));
                            adapter.notifyDataSetChanged();
                            adapterMenuEdit.notifyDataSetChanged();
                            saveMenuMakan(getMenuMakan);
                        }
                    });
                        adapterMenuEdit = new AdapterMenuEdit(getMenuMakan,listenerEditMenuMakan,getActivity().getApplicationContext());
                        adapterMenuEdit.notifyDataSetChanged();
                        recyclerViewMenu.setAdapter(adapterMenuEdit);
                        break;
                    case "Menu Minuman":
                        List<Menu> getMenuMinuman = getMenuMinuman();
                        listenerEditMenuMakan = new AdapterMenuEdit.clickMakananItem() {
                            @Override
                            public void clickItemListener(int position) {
                                namaItem.setText(getMenuMinuman.get(position).getNamaItem());
                                hargaItem.setText(String.valueOf(getMenuMinuman.get(position).getHargaItem()));
                                stockItem.setText(String.valueOf(getMenuMinuman.get(position).getStockItem()));
                                idItem.setText(String.valueOf(position));
                                Toast.makeText(getActivity().getApplicationContext(),"Membuat menu baru !",Toast.LENGTH_SHORT).show();
                                if(getMenuMinuman.size() - 1 == position){
                                     System.out.println("Size = " + getMenuMinuman.size());
                                    getMenuMinuman. add(new Menu("Tambahkan Menu",0,0,false,"Kosong"));
                                    saveMenuMinuman(getMenuMinuman);
                                    adapter.notifyDataSetChanged();
                                    adapterMenuEdit.notifyDataSetChanged();
                                }
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void deleteItemListener(int positionOfItem) {
                                Toast.makeText(getActivity().getApplicationContext(),"Menghapus Menu Minuman " + getMenuMinuman.get(positionOfItem).getNamaItem()+ " Berhasil ",Toast.LENGTH_SHORT).show();
                                getMenuMinuman.remove(positionOfItem);
                                saveMenuMinuman(getMenuMinuman);
                                adapterMenuEdit.notifyDataSetChanged();
                            }
                        };
                        buttonSimpan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println("name item = " + namaItem.getText().toString());
                                //getMenuMinuman.set(position,new Menu(namaItem.getText().toString(),Integer.parseInt(hargaItem.getText().toString()),Integer.parseInt(stockItem.getText().toString()),false,R.drawable.minuman_3_es_jerul));
                                getMenuMinuman.set(Integer.parseInt(idItem.getText().toString()),new Menu(namaItem.getText().toString(),Integer.parseInt(hargaItem.getText().toString()),Integer.parseInt(stockItem.getText().toString()),false,textViewPath.getText().toString()));
                                saveMenuMinuman(getMenuMinuman);
                            }
                        });
                        adapterMenuEdit = new AdapterMenuEdit(getMenuMinuman(),listenerEditMenuMakan,getActivity().getApplicationContext());
                        adapterMenuEdit.notifyDataSetChanged();
                        recyclerViewMenu.setAdapter(adapterMenuEdit);
                        break;
                    case "Menu Lauk":
                        List<Menu> getMenuLauk = getMenuLauk();
                        listenerEditMenuMakan = new AdapterMenuEdit.clickMakananItem() {
                            @Override
                            public void clickItemListener(int position) {
                                namaItem.setText(getMenuLauk.get(position).getNamaItem());
                                hargaItem.setText(String.valueOf(getMenuLauk.get(position).getHargaItem()));
                                stockItem.setText(String.valueOf(getMenuLauk.get(position).getStockItem()));
                                idItem.setText(String.valueOf(position));
                                if(getMenuLauk.size() - 1 == position){
                                    System.out.println("Size = " + getMenuLauk.size());
                                    getMenuLauk.add(new Menu("Tambahkan Menu",0,0,false,"Kosong"));
                                    saveMenuLauk(getMenuLauk);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void deleteItemListener(int positionOfItem) {
                                getMenuLauk.remove(positionOfItem);
                                saveMenuLauk(getMenuLauk);
                                adapterMenuEdit.notifyDataSetChanged();
                                Toast.makeText(getActivity().getApplicationContext(),"Menghapus Lauk Minuman " + getMenuLauk.get(positionOfItem).getNamaItem()+ " Berhasil ",Toast.LENGTH_SHORT).show();
                            }
                        };
                        buttonSimpan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println("name item = " + namaItem.getText().toString());
                                //getMenuMinuman.set(position,new Menu(namaItem.getText().toString(),Integer.parseInt(hargaItem.getText().toString()),Integer.parseInt(stockItem.getText().toString()),false,R.drawable.minuman_3_es_jerul));
                                getMenuLauk.set(Integer.parseInt(idItem.getText().toString()),new Menu(namaItem.getText().toString(),Integer.parseInt(hargaItem.getText().toString()),Integer.parseInt(stockItem.getText().toString()),false,textViewPath.getText().toString()));
                                adapter.notifyDataSetChanged();
                                adapterMenuEdit.notifyDataSetChanged();
                                saveMenuLauk(getMenuLauk);
                            }
                        });
                        adapterMenuEdit = new AdapterMenuEdit(getMenuLauk,listenerEditMenuMakan,getActivity().getApplicationContext());
                        adapterMenuEdit.notifyDataSetChanged();
                        recyclerViewMenu.setAdapter(adapterMenuEdit);
                        break;
                }
                System.out.println("test = " + test);
                Toast.makeText(getActivity().getApplicationContext(),adapter.getItem(i),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


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

    List<Menu>  getMenuMakanan(){
        List<Menu> listMenuMakanan = getDefaultPrefencesMenuMakanan();
        if(listMenuMakanan == null){
            listMenuMakanan = new ArrayList<>();
            listMenuMakanan.add(new Menu("Nasi Goreng",10000,10,false, R.drawable.makanan_4_nasigoreng));
            listMenuMakanan.add(new Menu("Mie Goreng",10000,10,false,R.drawable.makanan_2_mie_goreng));
            listMenuMakanan.add(new Menu("Mie Kuah",10000,10,false,R.drawable.makanan_3_mie_rebus));
            listMenuMakanan.add(new Menu("Kweatiu Goreng ",10000,10,false,R.drawable.makanan_1_kwantiew));
            listMenuMakanan.add(new Menu("Tambahkan Menu",0,0,false,R.drawable.ic_baseline_add_24));
            saveMenuMakan(listMenuMakanan);
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
            listMenuMinuman.add(new Menu("Tambahkan Menu",0,0,false,R.drawable.ic_baseline_add_24));
            saveMenuMinuman(listMenuMinuman);
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
            listMenuLauk.add(new Menu("Tambahkan Menu",0,0,false,R.drawable.ic_baseline_add_24));
        }
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
        return inflater.inflate(R.layout.fragment_edit_data_barang, container, false);
    }
}
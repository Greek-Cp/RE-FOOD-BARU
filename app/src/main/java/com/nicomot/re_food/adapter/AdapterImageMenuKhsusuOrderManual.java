package com.nicomot.re_food.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicomot.re_food.R;
import com.nicomot.re_food.model.Menu;
import com.nicomot.re_food.model.Pesanan;

import java.lang.reflect.Type;
import java.util.List;

public class AdapterImageMenuKhsusuOrderManual extends RecyclerView.Adapter<AdapterImageMenuKhsusuOrderManual.ViewHolder> {
    List<Pesanan> listPesanan;
    List<Menu> listMenu;
    Context context;
    public AdapterImageMenuKhsusuOrderManual.clickItemPesanan listenerOrderManual;

    public interface clickItemPesanan{
        void deleteItemSelected(int position);
    }
    public AdapterImageMenuKhsusuOrderManual(List<Pesanan> listPesanan, List<Menu> listMenu,
                                             AdapterImageMenuKhsusuOrderManual.clickItemPesanan listenerOrderManual) {
        this.listPesanan = listPesanan;
        this.listMenu = listMenu;
        this.listenerOrderManual = listenerOrderManual;

    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_img_pesanan_khusus_order_manual,parent,false);
        return new ViewHolder(v);
    }

    List<Menu> getDefaultPrefencesMenuMakanan(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREF_MENU_MAKANAN", Context.MODE_PRIVATE);
        Type typeMenu = new TypeToken<List<Menu>>(){}.getType();
        Gson gson = new Gson();
        List<Menu> listMenuMakanan = gson.fromJson(sharedPreferences.getString("KEY_MENU_MAKANAN",""),typeMenu);
        return listMenuMakanan;
    }
    List<Menu> getDefaultPrefencesMenuMinuman(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREF_MENU_MINUMAN", Context.MODE_PRIVATE);
        Type typeMenu = new TypeToken<List<Menu>>(){}.getType();
        Gson gson = new Gson();
        List<Menu> listMenuMakanan = gson.fromJson(sharedPreferences.getString("KEY_MENU_MINUMAN",""),typeMenu);
        return listMenuMakanan;
    }
    List<Menu> getDefaultPrefencesMenuLauk(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREF_MENU_LAUK", Context.MODE_PRIVATE);
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
    public void onBindViewHolder(@NonNull AdapterImageMenuKhsusuOrderManual.ViewHolder holder, int position) {
        holder.tv_Nama_Menu.setText(listPesanan.get(position).getNamaPesanan());
        System.out.println(listPesanan.get(position).getJumlahPesanan() + " PESANAN COY");
        holder.tv_Jumlah_Pesanan.setText(String.valueOf(listPesanan.get(position).getJumlahPesanan()) + " Porsi");
        holder.btnDeletePesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerOrderManual.deleteItemSelected(position);
            }
        });
        for(Menu menu : listMenu){
            if(listPesanan.get(position).getNamaPesanan().equals(menu.getNamaItem())){
                if(menu.getPathGambarItem() != null){
                    Bitmap bitmap = BitmapFactory.decodeFile(menu.getPathGambarItem());
                    holder.img_Menu.setImageBitmap(bitmap);
                }
            }
        }



        switch (listPesanan.get(position).getNamaPesanan()){
            case "Nasi Goreng":
                holder.img_Menu.setImageResource(R.drawable.makanan_4_nasigoreng);
                break;
            case "Mie Goreng":
                holder.img_Menu.setImageResource(R.drawable.makanan_2_mie_goreng);
                break;
            case "Mie Kuah":
                holder.img_Menu.setImageResource(R.drawable.makanan_3_mie_rebus);
                break;
            case "Kweatiu Goreng":
                holder.img_Menu.setImageResource(R.drawable.makanan_1_kwantiew_testing);
                break;
            case "Air Mineral":
                holder.img_Menu.setImageResource(R.drawable.minuman_1_air_pputih);
                break;
            case "Es Teh":
                holder.img_Menu.setImageResource(R.drawable.minuman_2_es_teh);
                break;
            case "Teh Hangat":
                holder.img_Menu.setImageResource(R.drawable.minuman_4_teh);
                break;
            case "Es Jeruk":
                holder.img_Menu.setImageResource(R.drawable.minuman_3_es_jerul);
                break;
            case "Jeruk Hangat":
                holder.img_Menu.setImageResource(R.drawable.minuman_3_es_jerul);
                break;
            case "Ikan Goreng":
                holder.img_Menu.setImageResource(R.drawable.lauk_2_ikan);
                break;
            case "Kangkung":
                holder.img_Menu.setImageResource(R.drawable.lauk_3_kangkung);
                break;
            case "Oseng Tempe":
                holder.img_Menu.setImageResource(R.drawable.lauk_4_oseng_tempe);
                break;
            case "Semur Sayur":
                holder.img_Menu.setImageResource(R.drawable.lauk_1_semur_sayur);
                break;
            default:
                break;

        }
    }


    @Override
    public int getItemCount() {
        return listPesanan.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tv_Nama_Menu , tv_Jumlah_Pesanan;
        ImageView img_Menu;
        CardView btnDeletePesanan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Nama_Menu = itemView.findViewById(R.id.tv_id_nama_makanan_adapter);
            tv_Jumlah_Pesanan = itemView.findViewById(R.id.tv_id_jumlah_pesanan_adapter);
            img_Menu = itemView.findViewById(R.id.img_id_menu_adapter);
            btnDeletePesanan = itemView.findViewById(R.id.id_card_btn_delete_menu);
        }
    }
}

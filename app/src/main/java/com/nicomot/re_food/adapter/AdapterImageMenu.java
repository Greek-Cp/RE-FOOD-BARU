package com.nicomot.re_food.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nicomot.re_food.R;
import com.nicomot.re_food.model.Customer;
import com.nicomot.re_food.model.Pesanan;
import com.nicomot.re_food.util.MenuUtil;

import java.util.List;

public class AdapterImageMenu extends RecyclerView.Adapter<AdapterImageMenu.ViewHolder> {
    List<Pesanan> listPesanan;
    public AdapterImageMenu(List<Pesanan> listPesanan) {
        this.listPesanan = listPesanan;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_img_pesanan,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterImageMenu.ViewHolder holder, int position) {
        holder.tv_Nama_Menu.setText(listPesanan.get(position).getNamaPesanan());
        System.out.println(listPesanan.get(position).getJumlahPesanan() + " PESANAN COY");
        holder.tv_Jumlah_Pesanan.setText(String.valueOf(listPesanan.get(position).getJumlahPesanan()) + " Porsi");
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Nama_Menu = itemView.findViewById(R.id.tv_id_nama_makanan_adapter);
            tv_Jumlah_Pesanan = itemView.findViewById(R.id.tv_id_jumlah_pesanan_adapter);
            img_Menu = itemView.findViewById(R.id.img_id_menu_adapter);
        }
    }
}

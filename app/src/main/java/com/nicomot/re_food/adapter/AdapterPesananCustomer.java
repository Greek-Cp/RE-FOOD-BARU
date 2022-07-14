package com.nicomot.re_food.adapter;

import android.graphics.Color;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Random;

public class AdapterPesananCustomer extends RecyclerView.Adapter<AdapterPesananCustomer.ViewHolder> {


    Customer listPesanan;

    public AdapterPesananCustomer(Customer listPesanan) {
        this.listPesanan = listPesanan;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_img_siapkan_pesanan,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPesananCustomer.ViewHolder holder, int position) {
        holder.tvPorsiPesanan.setText(String.valueOf(listPesanan.getSemuaPesanan().get(position).getJumlahPesanan()) + " Porsi");
        holder.namaPesanan.setText(listPesanan.getSemuaPesanan().get(position).getNamaPesanan());
        switch (listPesanan.getSemuaPesanan().get(position).getNamaPesanan()){
            case "Nasi Goreng":
                holder.imageViewPesanan.setImageResource(R.drawable.makanan_4_nasigoreng);
                break;
            case "Mie Goreng":
                holder.imageViewPesanan.setImageResource(R.drawable.makanan_2_mie_goreng);
                break;
            case "Mie Kuah":
                holder.imageViewPesanan.setImageResource(R.drawable.makanan_3_mie_rebus);
                break;
            case "Kweatiu Goreng":
                holder.imageViewPesanan.setImageResource(R.drawable.makanan_1_kwantiew_testing);
                break;
            case "Air Mineral":
                holder.imageViewPesanan.setImageResource(R.drawable.minuman_1_air_pputih);
                break;
            case "Es Teh":
                holder.imageViewPesanan.setImageResource(R.drawable.minuman_2_es_teh);
                break;
            case "Teh Hangat":
                holder.imageViewPesanan.setImageResource(R.drawable.minuman_4_teh);
                break;
            case "Es Jeruk":
                holder.imageViewPesanan.setImageResource(R.drawable.minuman_3_es_jerul);
                break;
            case "Jeruk Hangat":
                holder.imageViewPesanan.setImageResource(R.drawable.minuman_3_es_jerul);
                break;
            case "Ikan Goreng":
                holder.imageViewPesanan.setImageResource(R.drawable.lauk_2_ikan);
                break;
            case "Kangkung":
                holder.imageViewPesanan.setImageResource(R.drawable.lauk_3_kangkung);
                break;
            case "Oseng Tempe":
                holder.imageViewPesanan.setImageResource(R.drawable.lauk_4_oseng_tempe);
                break;
            case "Semur Sayur":
                holder.imageViewPesanan.setImageResource(R.drawable.lauk_1_semur_sayur);
                break;
            default:
                break;
        }
    }

    static String convertRupiah(int num){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(num).substring(0,kursIndonesia.format(num).length() - 3)
                ;

    }
    @Override
    public int getItemCount() {
        return listPesanan.getSemuaPesanan().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContent , namaPesanan , tvPorsiPesanan;
        CardView cardColorMenu;
        ImageView imageViewPesanan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPesanan = itemView.findViewById(R.id.img_id_menu_adapter);

            namaPesanan = itemView.findViewById(R.id.tv_id_nama_makanan_adapter);
            tvPorsiPesanan = itemView.findViewById(R.id.id_tv_harga_adapter_card);
        }

    }
}

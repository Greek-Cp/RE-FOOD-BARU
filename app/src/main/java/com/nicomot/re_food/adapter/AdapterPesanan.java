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
import com.nicomot.re_food.model.Menu;
import com.nicomot.re_food.model.Pesanan;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Random;

public class AdapterPesanan extends RecyclerView.Adapter<AdapterPesanan.ViewHolder> {

    public interface clickPesanan{
        void clickPesanan(int pesanan);
    }
    List<Customer> listPesanan;
    AdapterPesanan.clickPesanan listenerClick;

    List<Menu> menuAvailable;

    public AdapterPesanan(List<Customer> listPesanan, clickPesanan listenerClick,List<Menu> menu) {
        this.listPesanan = listPesanan;
        this.listenerClick = listenerClick;
        this.menuAvailable = menu;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_pesanan,parent,false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterPesanan.ViewHolder holder, int position) {
        holder.tv_Id_Pesan.setText("#"+position);
        holder.tv_Nama_Pemesan.setText(listPesanan.get(position).getDataDiri().getNamaPemesan());
        holder.tv_Alamat_Pemesan.setText(listPesanan.get(position).getDataDiri().getAlamat());
        AdapterImageMenu adapterImageMenu = new AdapterImageMenu(listPesanan.get(position).getSemuaPesanan(),menuAvailable);
        holder.recyclerViewMenuMakan.setAdapter(adapterImageMenu);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.cardColorMenu.setCardBackgroundColor(color);
        holder.tv_Harga.setText(convertRupiah(listPesanan.get(position).getTotalPesanan()));
    }

    static String convertRupiah(int num){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(num);

    }
    @Override
    public int getItemCount() {
        return listPesanan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_Id_Pesan, tv_Nama_Pemesan , tv_Alamat_Pemesan ,tv_Harga;
        CardView cardColorMenu;
        RecyclerView recyclerViewMenuMakan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Id_Pesan = itemView.findViewById(R.id.tv_id_pesanan_adapter_);
            tv_Nama_Pemesan = itemView.findViewById(R.id.tv_id_nama_pemesan_adapter);
            tv_Alamat_Pemesan = itemView.findViewById(R.id.tv_id_alamat_pemesan_adapter);
            cardColorMenu = itemView.findViewById(R.id.id_card_status_color);
            tv_Harga = itemView.findViewById(R.id.id_tv_harga_adapter_card);
            recyclerViewMenuMakan = itemView.findViewById(R.id.id_rec_menu_makanan_inside_rec_list_pesanan);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            listenerClick.clickPesanan(getAdapterPosition());
        }
    }
}

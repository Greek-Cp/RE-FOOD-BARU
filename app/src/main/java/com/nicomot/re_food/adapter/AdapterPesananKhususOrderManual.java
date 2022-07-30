package com.nicomot.re_food.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AdapterPesananKhususOrderManual extends RecyclerView.Adapter<AdapterPesananKhususOrderManual.ViewHolder> {

    public interface clickPesanan{
        void clickPesanan(int pesanan);
    }
    List<Pesanan> listSemuaPesanan;
    AdapterPesananKhususOrderManual.clickPesanan listenerClick;
    List<Menu> menuAvailable;
    public AdapterPesananKhususOrderManual(List<Pesanan> listPesanan,
                                           clickPesanan listenerClick,
                                           List<Menu> menu) {
        this.listSemuaPesanan = listPesanan;
        this.listenerClick = listenerClick;
        this.menuAvailable = menu;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_pesanankhusus_order_manual,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPesananKhususOrderManual.ViewHolder holder, int position) {
        AdapterImageMenu adapterImageMenu = new AdapterImageMenu(listSemuaPesanan,menuAvailable);
        holder.recyclerViewMenuMakan.setAdapter(adapterImageMenu);
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
        return listSemuaPesanan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView recyclerViewMenuMakan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             recyclerViewMenuMakan = itemView.findViewById(R.id.id_rec_menu_makanan_inside_rec_list_pesanan);
             itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            listenerClick.clickPesanan(getAdapterPosition());
        }
    }
}

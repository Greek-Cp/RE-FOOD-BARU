package com.nicomot.re_food.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicomot.re_food.R;
import com.nicomot.re_food.model.Pesanan;

import java.util.List;

public class AdapterPesanan extends RecyclerView.Adapter<AdapterPesanan.ViewHolder> {

    interface clickPesanan{
        void clickPesanan(int pesanan);
    }
    List<Pesanan> listPesanan;
    AdapterPesanan.clickPesanan listenerClick;

    public AdapterPesanan(List<Pesanan> listPesanan, clickPesanan listenerClick) {
        this.listPesanan = listPesanan;
        this.listenerClick = listenerClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_pesanan,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPesanan.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return listPesanan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

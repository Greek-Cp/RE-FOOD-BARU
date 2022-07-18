package com.nicomot.re_food.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicomot.re_food.R;
import com.nicomot.re_food.model.Customer;
import com.nicomot.re_food.model.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class AdapterMenuEdit extends RecyclerView.Adapter<AdapterMenuEdit.ViewHolder> {

    List<Menu> listMenuToko;
    AdapterMenuEdit.clickMakananItem clickMakananItem;

    Context context;
    public AdapterMenuEdit(List<Menu> listMenuToko, AdapterMenuEdit.clickMakananItem clickMakananItem
    , Context context) {
        this.listMenuToko = listMenuToko;
        this.clickMakananItem = clickMakananItem;
        this.context = context;
    }

    public interface clickMakananItem{
        void clickItemListener(int position);
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_menu_tersedia,parent,false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterMenuEdit.ViewHolder holder, int position) {
        holder.namaPesanan.setText(listMenuToko.get(position).getNamaItem());
        holder.imageViewPesanan.setImageResource(listMenuToko.get(position).getGambarDrawable());
        if(listMenuToko.get(position).getPathGambarItem() != null){

                Bitmap bitmap = BitmapFactory.decodeFile(listMenuToko.get(position).getPathGambarItem());
                holder.imageViewPesanan.setImageBitmap(bitmap);

        }
    }

    static String convertRupiah(int num){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(num).substring(0,kursIndonesia.format(num).length() - 3);
    }
    @Override
    public int getItemCount() {
        return listMenuToko.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView tvContent , namaPesanan;
        ImageView imageViewPesanan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPesanan = itemView.findViewById(R.id.img_id_menu_tersedia_adapter);
            namaPesanan = itemView.findViewById(R.id.tv_id_nama_menu_tersedia_makanan_adapter);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            clickMakananItem.clickItemListener(getAdapterPosition());
        }
    }
}

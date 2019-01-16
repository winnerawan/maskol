package com.groges.wiskulmokerguide.DetailTempat;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.groges.wiskulmokerguide.ListMenu;
import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMenuWarungAdapter extends RecyclerView.Adapter<ListMenuWarungAdapter.ItemHolder> {
    private List<ListMenu> listItems;
    private Context context;
    private String harga;


    public ListMenuWarungAdapter(List<ListMenu> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu,parent,false);
        return new ItemHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ListMenu listItem = listItems.get(position);
        holder.txtNama.setText(listItem.getNama());
        holder.txtKet.setText(listItem.getKet());
        harga="IDR "+(listItem.getHarga()!=null ? listItem.getHarga()/1000 : "-")+"K";
        holder.txtHarga.setText(harga);
        holder.ratingBar.setRating(listItem.getRating()!=null ? listItem.getRating() :0);
        String urlImage = listItem.getUriImg();
        if (urlImage != null) {
            Picasso.with(context)
                    .load(Uri.parse(urlImage))
                    .into(holder.imgMenu);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
    public TextView txtNama, txtKet, txtHarga;
        public ImageView imgMenu;
        public RatingBar ratingBar;

        public ItemHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.menuWarung_lst_txtNama);
            txtKet = itemView.findViewById(R.id.menuWarung_lst_txtKet);
            txtHarga = itemView.findViewById(R.id.menuWarung_lst_txtHarga);
            imgMenu = itemView.findViewById(R.id.menuWarung_lst_img);
            ratingBar = itemView.findViewById(R.id.menuWarung_lst_ratingBar);


        }

    }
}

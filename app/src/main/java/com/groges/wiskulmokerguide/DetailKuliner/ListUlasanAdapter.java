package com.groges.wiskulmokerguide.DetailKuliner;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.groges.wiskulmokerguide.ListMenu;
import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListUlasanAdapter extends RecyclerView.Adapter<ListUlasanAdapter.ItemHolder> {
    private List<ListUlasan> listItems;
    private Context context;



    public ListUlasanAdapter(List<ListUlasan> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lst_ulasan_kuliner,parent,false);
        return new ItemHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ListUlasan listItem = listItems.get(position);
        holder.txtNama.setText(listItem.getNmPengguna());
        holder.txtUlasan.setText(listItem.getUlasan());
        holder.ratingBar.setRating(listItem.getRating()!=null ? listItem.getRating() :0);
//        String urlImage = listItem.getUriImg();
//        if (urlImage != null) {
//            Picasso.with(context)
//                    .load(Uri.parse(urlImage))
//                    .into(holder.imgUser);
//        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
    public TextView txtNama, txtUlasan;
        public CircleImageView imgUser;
        public RatingBar ratingBar;

        public ItemHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.ulasan_lst_txtNama);
            txtUlasan = itemView.findViewById(R.id.ulasan_lst_txtKet);
            ratingBar = itemView.findViewById(R.id.ulasan_lst_ratingBar);
            imgUser = itemView.findViewById(R.id.ulasan_lst_img);

        }

    }
}

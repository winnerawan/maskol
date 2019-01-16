package com.groges.wiskulmokerguide.Kuliner.Kategory;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class KatKulinerAdapter extends RecyclerView.Adapter<KatKulinerAdapter.ItemHolder> {
    private List<ListKatKuliner> listItems;
    private Context context;
    private OnItemClickListener mListener;


    public KatKulinerAdapter(List<ListKatKuliner> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_kat_wisata,parent,false);
        return new ItemHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ListKatKuliner listItem = listItems.get(position);
        holder.txtNama.setText(listItem.getKategory());

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

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtNama;
        public ImageView imgMenu;


        public ItemHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.katWisata_lst_txtNama);
            imgMenu = itemView.findViewById(R.id.katWisata_lst_img);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                      mListener.onItemClick(position);
                }
            }
        }

    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}

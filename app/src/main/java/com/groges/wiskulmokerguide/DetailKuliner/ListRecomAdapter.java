package com.groges.wiskulmokerguide.DetailKuliner;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groges.wiskulmokerguide.ListMenu;
import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListRecomAdapter extends RecyclerView.Adapter<ListRecomAdapter.ItemHolder> {
    private List<ListMenu> listItems;
    private Context context;
    private OnItemClickListener mListener;
    private String harga;

    public ListRecomAdapter(List<ListMenu> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lst_item_recom,parent,false);
        return new ItemHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ListMenu listItem = listItems.get(position);
        holder.txtNama.setText(listItem.getNama());
        holder.txtNmTempat.setText(listItem.getTempat());
        if(listItem.getHarga()!=null) {
            harga="IDR " + (listItem.getHarga() / 1000) + "K";
        }else{
            harga="-";
        }
        holder.txtHarga.setText(harga);
        String urlImage = listItem.getUriImg();
        String urlImgTempat = listItem.getUriImgTempat();
        if (urlImage != null) {
            Picasso.with(context)
                    .load(Uri.parse(urlImage))
                    .into(holder.imgMenu);
        }
        if (urlImgTempat != null) {
            Picasso.with(context)
                    .load(Uri.parse(urlImgTempat))
                    .into(holder.imgTempat);
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        public TextView txtNama, txtNmTempat, txtHarga;
        public CircleImageView imgTempat;
        public ImageView imgMenu;


        public ItemHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.recom_lst_txtNama);
            txtNmTempat = itemView.findViewById(R.id.recom_lst_txtTempat);
            txtHarga = itemView.findViewById(R.id.recom_lst_txtHarga);
            imgTempat = itemView.findViewById(R.id.recom_lst_imgTempat);
            imgMenu = itemView.findViewById(R.id.recom_lst_img);

            itemView.setOnClickListener(this);
            imgTempat.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }
        @Override
        public void onClick(View v) {
            if(mListener != null){
                int position = getAdapterPosition();
                int id = v.getId();
                if(position != RecyclerView.NO_POSITION){
                    if (id == R.id.recom_lst_imgTempat) {
                        //Toast.makeText(context,"peta",Toast.LENGTH_SHORT).show();
                        mListener.onDeleteClick(position);
                    }else {
                        mListener.onItemClick(position);
                    }
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Mau Ngapain");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Tampilkan");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Petunjuk Peta");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onWhatEverClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}

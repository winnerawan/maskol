package com.groges.wiskulmokerguide.Wisata;

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

import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.ItemHolder> {
    private List<ListWisata> listItems;
    private Context context;
    private OnItemClickListener mListener;


    public WisataAdapter(List<ListWisata> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_wisata,parent,false);
        return new ItemHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ListWisata listItem = listItems.get(position);
        holder.txtNama.setText(listItem.getNama());
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

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        public TextView txtNama;
        public ImageView imgMenu;


        public ItemHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.wisata_lst_txtNama);
            imgMenu = itemView.findViewById(R.id.wisata_lst_img);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

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

package com.groges.wiskulmokerguide.Warung;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WarungAdapter extends RecyclerView.Adapter<WarungAdapter.ItemHolder> {
    private List<ListWarung> listItems;
    private Context context;
    private OnItemClickListener mListener;
    public WarungAdapter(List<ListWarung> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_placev2,parent,false);
        return new ItemHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ListWarung listItem = listItems.get(position);
        holder.txtNama.setText(listItem.getNama());
        holder.txtAlamat.setText(context.getString(R.string.address_resto, listItem.getJarak(), listItem.getAlamat()));
//        holder.txtJarak.setText( String.format("%.1f",listItem.getJarak())+" KM");
//        holder.ratingBar.setRating (listItem.getRating()!=null ? listItem.getRating() : 0);
        String urlImage = listItem.getUriImgTempat();
        if (urlImage != null) {
            Picasso.with(context)
                    .load(Uri.parse(urlImage))
                    .into(holder.imgTempat);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        public TextView txtNama, txtAlamat, txtJarak;
        public RatingBar ratingBar;
        public ImageView imgTempat;
        public LinearLayout btnGo;

        public ItemHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.place_lst_txtNama);
            imgTempat = itemView.findViewById(R.id.place_lst_imgTempat);
            txtAlamat = itemView.findViewById(R.id.text_merchant_address);
            btnGo = itemView.findViewById(R.id.go);

            itemView.setOnClickListener(this);
            btnGo.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }
        @Override
        public void onClick(View v) {
            if(mListener != null){
                int position = getAdapterPosition();
                int id = v.getId();
                if(position != RecyclerView.NO_POSITION){
                    if (id == R.id.place_lst_btnMap) {
                       // Toast.makeText(context,"peta",Toast.LENGTH_SHORT).show();
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

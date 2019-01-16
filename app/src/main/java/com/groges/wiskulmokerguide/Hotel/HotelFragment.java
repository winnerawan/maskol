package com.groges.wiskulmokerguide.Hotel;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groges.wiskulmokerguide.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotelFragment extends Fragment implements HotelAdapter.OnItemClickListener {
    private static final String TABEL_MENU = "spotHotel" ;
    private RecyclerView recyclerView;
    private List<ListHotel> listItems;
    private HotelAdapter myAdapter;
    private ProgressBar mProgressCircle;

//    private FirebaseStorage mStorage;

    private FirebaseFirestore db;
    private ViewGroup pangkon;
    private String query;

    public HotelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_home, container, false);

        pangkon = container;
        mProgressCircle = v.findViewById(R.id.prgrs_home);

        Bundle bundle = getArguments();

        if (bundle!=null) {
            query = bundle.getString("kategory");
        } else{
            query="";
        }
        db = FirebaseFirestore.getInstance();

        recyclerView =v.findViewById(R.id.recycler_home);
        listItems = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter = new HotelAdapter(listItems,getActivity());
        myAdapter.setOnItemClickListener(HotelFragment.this);
        recyclerView.setAdapter(myAdapter);

        isiAdapter();


        return v;
    }

    private void isiAdapter() {
        listItems.clear();
            myAdapter.notifyDataSetChanged();
            db.collection(TABEL_MENU)
                    .limit(8)
                    .whereEqualTo("kategory",query)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    listItems.add(new ListHotel(document.getId(),
                                            document.getString("nama"),
                                            document.getString("alamat"),
                                            document.getString("imgUrl"),
                                            document.getString("short_desc"),
                                            document.getString("descripsi"),
                                            document.getDouble("lat"),
                                            document.getDouble("lng")));
                                }
                                myAdapter.notifyDataSetChanged();
                                mProgressCircle.setVisibility(View.INVISIBLE);
                            } else {
                                mProgressCircle.setVisibility(View.INVISIBLE);
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }
                    });

    }

    @Override
    public void onItemClick(int position) {
        ListHotel selectedItem = listItems.get(position);
        Intent intent = new Intent(getActivity(), DetailHotel.class);
        intent.putExtra("idHotel",selectedItem.getId());
        intent.putExtra("nmHotel",selectedItem.getNama());
        intent.putExtra("uriImg",selectedItem.getUriImg());
        intent.putExtra("lat",selectedItem.getLat());
        intent.putExtra("lng",selectedItem.getLng());
        startActivity(intent);

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        ListHotel selectedItem = listItems.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+selectedItem.getLat()+","+ selectedItem.getLng()));
        startActivity(intent);
    }
}

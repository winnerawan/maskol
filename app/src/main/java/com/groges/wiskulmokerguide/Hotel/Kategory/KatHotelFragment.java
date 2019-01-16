package com.groges.wiskulmokerguide.Hotel.Kategory;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.groges.wiskulmokerguide.Hotel.HotelFragment;
import com.groges.wiskulmokerguide.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class KatHotelFragment extends Fragment implements KatHotelAdapter.OnItemClickListener {
    private static final String TABEL_MENU = "katHotel" ;
    private RecyclerView recyclerView;
    private List<ListKatHotel> listItems;
    private KatHotelAdapter myAdapter;
    private ProgressBar mProgressCircle;

//    private FirebaseStorage mStorage;
    private FirebaseFirestore db;
    private ViewGroup pangkon;

    public KatHotelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_home, container, false);

        pangkon = container;
        mProgressCircle = v.findViewById(R.id.prgrs_home);

        //mAuth = FirebaseAuth.getInstance();
//        mStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView =v.findViewById(R.id.recycler_home);
        listItems = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter = new KatHotelAdapter(listItems,getActivity());
        myAdapter.setOnItemClickListener(KatHotelFragment.this);
        recyclerView.setAdapter(myAdapter);

        isiAdapter();


        return v;
    }

    private void isiAdapter() {
        listItems.clear();
            myAdapter.notifyDataSetChanged();
            db.collection(TABEL_MENU)
                    .limit(8)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    listItems.add(new ListKatHotel(document.getId(),document.getString("kategory"),document.getString("imgUrl"),document.getString("ket")));
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
        ListKatHotel selectedItem = listItems.get(position);
        HotelFragment newfragment = new HotelFragment();
        Bundle args = new Bundle();
        args.putString("idkategory", selectedItem.getId());
        args.putString("kategory",selectedItem.getKategory());
        newfragment.setArguments(args);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, newfragment)
                .addToBackStack(null)
                .commit();

    }

}

package com.groges.wiskulmokerguide;


import android.content.Intent;
import android.net.Uri;
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
import com.groges.wiskulmokerguide.DetailKuliner.DetailKuliner;
import com.groges.wiskulmokerguide.DetailKuliner.DetailKulinerActivity;
import com.groges.wiskulmokerguide.DetailKuliner.DetailMenu;
import com.groges.wiskulmokerguide.DetailTempat.DetailWarung;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeAdapter.OnItemClickListener {
    public static final String TAG = HomeFragment.class.getSimpleName();

    private static final String TABEL_MENU = "menuKuliner" ;
    private RecyclerView recyclerView;
    private List<ListMenu> listItems;
    private HomeAdapter myAdapter;
    private ProgressBar mProgressCircle;

//    private FirebaseStorage mStorage;
    private FirebaseFirestore db;
    private ViewGroup pangkon;


    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
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
        myAdapter = new HomeAdapter(listItems,getActivity());
        myAdapter.setOnItemClickListener(HomeFragment.this);
        recyclerView.setAdapter(myAdapter);

        isiAdapter();


        return v;
    }

    private void isiAdapter() {
        listItems.clear();
            myAdapter.notifyDataSetChanged();
            db.collection(TABEL_MENU)
                    .limit(50)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    listItems.add(new ListMenu(document.getId(),
                                            document.getString("idTempat"),
                                            document.getString("nmMenu"),
                                            document.getString("nmTempat"),
                                            document.getString("imgUri"),
                                            document.getString("uriImgTempat"),
                                            document.getDouble("harga"),
                                            document.getDouble("lat"),
                                            document.getDouble("lng"),
                                            document.getDouble("rating"),
                                            document.getString("ket")));
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
        ListMenu selectedItem = listItems.get(position);
        Intent intent = new Intent(getActivity(), DetailMenu.class);
        intent.putExtra("idKuliner",selectedItem.getId());

        intent.putExtra("nmKuliner",selectedItem.getNama());
        intent.putExtra("uriImg",selectedItem.getUriImg());
        if(selectedItem.getHarga()!=null) {
        intent.putExtra("hrgKuliner", selectedItem.getHarga());
        }
        intent.putExtra("idTempat",selectedItem.getIdTempat());
        intent.putExtra("nmTempat",selectedItem.getTempat());

        startActivity(intent);
//        DetailKuliner newfragment = new DetailKuliner();
//        Bundle args = new Bundle();
//        args.putString("idKuliner",selectedItem.getId());
//        args.putString("idWarung",selectedItem.getIdTempat());
//        args.putString("nmKuliner",selectedItem.getNama());
//        args.putString("uriImg",selectedItem.getUriImg());
//        if(selectedItem.getHarga()!=null) {
//            args.putDouble("hrgKuliner", selectedItem.getHarga());
//        }
//        args.putString("nmTempat",selectedItem.getTempat());
//
//        newfragment.setArguments(args);
//
//        FragmentManager manager = getFragmentManager();
//        //FragmentManager manager = getChildFragmentManager();
//        manager.beginTransaction().replace(pangkon.getId(), newfragment)
//                .addToBackStack(null)
//                .commit();

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        ListMenu selectedItem = listItems.get(position);
//        Intent intent = new Intent(getActivity(),ShowMap.class);
//        intent.putExtra("lat",selectedItem.getLat());
//        intent.putExtra("lat",selectedItem.getLng());
//        intent.putExtra("nmRM",selectedItem.getTempat());
//        startActivity(intent);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+selectedItem.getLat()+","+ selectedItem.getLng()));
        startActivity(intent);
    }
}

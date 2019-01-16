package com.groges.wiskulmokerguide.Warung;


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
import com.groges.wiskulmokerguide.DetailTempat.DetailTempat;
import com.groges.wiskulmokerguide.DetailTempat.DetailWarung;
import com.groges.wiskulmokerguide.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListWarungFragment extends Fragment implements WarungAdapter.OnItemClickListener {
    public static final String TAG = ListWarungFragment.class.getSimpleName();

    private static final String TEMPAT_KULINER = "tempatKuliner";
    private RecyclerView recyclerView;
    private List<ListWarung> listItems;
    private WarungAdapter myAdapter;
    private ProgressBar mProgressCircle;

//    private FirebaseStorage mStorage;
    private FirebaseFirestore db;
    private String query;

    public static ListWarungFragment newInstance() {

        Bundle args = new Bundle();

        ListWarungFragment fragment = new ListWarungFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_lst_warung, container, false);


        mProgressCircle = v.findViewById(R.id.prgrs_lstWarung);

        Bundle bundle = getArguments();

        if (bundle!=null) {
            query = bundle.getString("queri");
        } else{
            query="";
        }

        //mAuth = FirebaseAuth.getInstance();
//        mStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView =v.findViewById(R.id.recycler_lstWarung);
        listItems = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter = new WarungAdapter(listItems,getActivity());
        myAdapter.setOnItemClickListener(ListWarungFragment.this);
        recyclerView.setAdapter(myAdapter);

        isiAdapter();


        return v;
    }

    private void isiAdapter() {
        listItems.clear();
            myAdapter.notifyDataSetChanged();
            db.collection(TEMPAT_KULINER)
                    .limit(100)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    listItems.add(new ListWarung(document.getId(),
                                            document.getString("nmTempat"),
                                            document.getString("alamat"),
                                            document.getString("telpon"),
                                            document.getString("imgUri"),
                                            document.getDouble("lat"),
                                            document.getDouble("lng"),
                                            document.getDouble("rating")
                                    ));
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
        ListWarung selectedItem = listItems.get(position);
//        DetailWarung newfragment = new DetailWarung();
//        Bundle args = new Bundle();
//
//
//        args.putString("idWarung", selectedItem.getId());
//        args.putString("nmWarung",selectedItem.getNama());
//        args.putString("almWarung",selectedItem.getAlamat());
//        args.putString("tlpWarung",selectedItem.getTelpon());
//        args.putString("uriImg",selectedItem.getUriImgTempat());
//        newfragment.setArguments(args);
//
//        FragmentManager manager = getFragmentManager();
//        manager.beginTransaction().replace(R.id.main_frame, newfragment)
//                .addToBackStack(null)
//                .commit();
        Intent intent1= new Intent(getActivity(), DetailTempat.class);
        intent1.putExtra("idTempat",selectedItem.getId());
        intent1.putExtra("nmTempat",selectedItem.getNama());
        intent1.putExtra("lat", selectedItem.getLat());
        intent1.putExtra("lng", selectedItem.getLng());
        startActivity(intent1);

    }

    @Override
    public void onWhatEverClick(int position) {
        ListWarung selectedItem = listItems.get(position);
        DetailWarung newfragment = new DetailWarung();
        Bundle args = new Bundle();


        args.putString("idWarung", selectedItem.getId());
        args.putString("nmWarung",selectedItem.getNama());
        args.putString("almWarung",selectedItem.getAlamat());
        args.putString("tlpWarung",selectedItem.getTelpon());
        args.putString("uriImg",selectedItem.getUriImgTempat());
        newfragment.setArguments(args);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, newfragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onDeleteClick(int position) {
        ListWarung selectedItem = listItems.get(position);

//        Intent intent = new Intent(getActivity(),ShowMap.class);
//        intent.putExtra("lat",selectedItem.getLat());
//        intent.putExtra("lat",selectedItem.getLng());
//        intent.putExtra("nmRM",selectedItem.getNama());
//        startActivity(intent);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+selectedItem.getLat()+","+ selectedItem.getLng()));
        startActivity(intent);
    }
}

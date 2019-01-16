package com.groges.wiskulmokerguide.RekomendasiUser;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groges.wiskulmokerguide.FragmentUserRekomendasi;
import com.groges.wiskulmokerguide.R;
import com.groges.wiskulmokerguide.Warung.ListWarung;
import com.groges.wiskulmokerguide.Warung.WarungAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PilihTempat extends Fragment  implements WarungAdapter.OnItemClickListener {
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private List<ListWarung> listItems;
    private WarungAdapter myAdapter;
    private ProgressBar mProgressCircle;

    //    private FirebaseStorage mStorage;
    private FirebaseFirestore db;

    public PilihTempat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Rekomendasikan Tempat");
        }
        View v;
        v= inflater.inflate(R.layout.fragment_pilih_tempat, container, false);


        mProgressCircle = v.findViewById(R.id.progres_placeRekom);

        //mAuth = FirebaseAuth.getInstance();
//        mStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView =v.findViewById(R.id.recycler_placeRekom);
        listItems = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter = new WarungAdapter(listItems,getActivity());
        myAdapter.setOnItemClickListener(PilihTempat.this);
        recyclerView.setAdapter(myAdapter);

        isiAdapter();

        return v;
    }

    private void isiAdapter() {
        listItems.clear();
        myAdapter.notifyDataSetChanged();
        db.collection("tempatKuliner")
                .limit(8)
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onItemClick(int position) {
        ListWarung selectedItem = listItems.get(position);
        FragmentUserRekomendasi newfragment = new FragmentUserRekomendasi();
        Bundle args = new Bundle();


        args.putString("idWarung", selectedItem.getId());
        args.putString("nmWarung",selectedItem.getNama());
        args.putString("almWarung",selectedItem.getAlamat());
        args.putString("tlpWarung",selectedItem.getTelpon());
        args.putString("uriImg",selectedItem.getUriImgTempat());
        args.putDouble("lat",selectedItem.getLat());
        args.putDouble("lng",selectedItem.getLng());

        newfragment.setArguments(args);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, newfragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String title);
    }

}

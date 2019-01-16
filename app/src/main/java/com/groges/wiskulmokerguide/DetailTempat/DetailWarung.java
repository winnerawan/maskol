package com.groges.wiskulmokerguide.DetailTempat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groges.wiskulmokerguide.ListMenu;
import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DetailWarung extends Fragment {
    private static final String TABEL_MENU = "menuKuliner";
    private TextView txtNama,txtAlamat,txtTelpon;
    private ImageView imgWarung;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<ListMenu> listItems;
    private ListMenuWarungAdapter myAdapter;
    private FirebaseFirestore db;


    private String idWarung;
    private String nmWarung,almWarung, tlpWarung;
    private String uriImg;
    private ViewGroup pangkon;


    public DetailWarung() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_detail_warung, container, false);
        pangkon = container;
        Bundle bundle = getArguments();
        idWarung = bundle.getString("idWarung");
        nmWarung = bundle.getString("nmWarung");
        almWarung = bundle.getString("almWarung");
        tlpWarung = bundle.getString("tlpWarung");
        uriImg = bundle.getString("uriImg");

        txtNama = v.findViewById(R.id.warung_txtNama);
        txtAlamat = v.findViewById(R.id.warung_txtAlamat);
        txtTelpon = v.findViewById(R.id.warung_txtTelpon);
        imgWarung = v.findViewById(R.id.warung_img);

        progressBar = v.findViewById(R.id.warung_progressBar);

        txtNama.setText(nmWarung);
        txtAlamat.setText(almWarung);
        txtTelpon.setText(tlpWarung);
        Picasso.with(getContext())
                .load(uriImg)
                .into(imgWarung);



        //mAuth = FirebaseAuth.getInstance();
//        mStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView =v.findViewById(R.id.warung_recycler);
        listItems = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter = new ListMenuWarungAdapter(listItems,getActivity());

        recyclerView.setAdapter(myAdapter);

        isiAdapter();





        return v;
    }

    private void isiAdapter() {

        listItems.clear();
        myAdapter.notifyDataSetChanged();
        db.collection(TABEL_MENU)
                .whereEqualTo("idTempat",idWarung)
                .limit(25)
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
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

}

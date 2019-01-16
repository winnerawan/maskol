package com.groges.wiskulmokerguide.DetailKuliner;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailKuliner extends Fragment {
    private static final String TABEL_MENU = "menuKuliner";
    private static final String TABEL_ULASAN = "ulasanKuliner";
    private static final int JUMLAH_SLIDE = 5 ;
    private static final String TABEL_GAMBAR = "imageKuliner";


    private TextView txtNama,txtAlamat,txtTelpon, txtHarga, txtUlasan, txtRatingAvg, txtNmProfil;
    private ImageView imgWarung;
    private RatingBar inputRating;
    private Button btnKirim;



    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<ListUlasan> listItems;
    private ListUlasanAdapter myAdapter;
    private FirebaseFirestore db;


    private String idKuliner;
    private String nmKuliner,almKuliner, tlpKuliner;
    private String uriImg;
    private Double hrgKuliner;

    CarouselView carouselView;
    ArrayList<Object> urlImage;

    public DetailKuliner() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_detail_kuliner, container, false);

        Bundle bundle = getArguments();
        idKuliner = bundle.getString("idKuliner");
        nmKuliner = bundle.getString("nmKuliner");
        almKuliner= bundle.getString("nmTempat");
        tlpKuliner = bundle.getString("tlpKuliner");
        hrgKuliner = bundle.getDouble("hrgKuliner");
        uriImg = bundle.getString("uriImg");

        txtNama = v.findViewById(R.id.kuliner_txtNama);
        txtAlamat = v.findViewById(R.id.kuliner_txtAlamat);
        txtTelpon = v.findViewById(R.id.kuliner_txtTelpon);
        imgWarung = v.findViewById(R.id.kuliner_img);
        txtHarga = v.findViewById(R.id.kuliner_txtharga);

        txtRatingAvg = v.findViewById(R.id.kuliner_txtRatAverage);
        txtUlasan = v.findViewById(R.id.kuliner_txtUlasan);
        inputRating = v.findViewById(R.id.kuliner_ratingbar);
        txtNmProfil = v.findViewById(R.id.kuliner_txtNmProfil);
        btnKirim = v.findViewById(R.id.kuliner_btnKirim);

        progressBar = v.findViewById(R.id.kuliner_progressBar);

        carouselView = v.findViewById(R.id.carouselDetail);

        txtNama.setText(nmKuliner);
        txtAlamat.setText(almKuliner);
        txtTelpon.setText(tlpKuliner);
        txtHarga.setText(hrgKuliner.toString()+"K");

//        Picasso.with(getContext())
//                .load(uriImg)
//                .into(imgWarung);

        //TODO nama profile pengguna
        txtNmProfil.setText("Groges");

        //mAuth = FirebaseAuth.getInstance();
//        mStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        urlImage =   new ArrayList<>();
        db.collection(TABEL_GAMBAR)
                .whereEqualTo("idKuliner",idKuliner)
                .limit(JUMLAH_SLIDE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (DocumentSnapshot document : task.getResult()) {
                                urlImage.add(i ,document.getString("imgUrl"));
                                i++;
                            }
                            ImageListener imageListener = new ImageListener() {
                                @Override
                                public void setImageForPosition(int position, ImageView imageView) {
                                    Picasso.with(getContext())
                                            .load(Uri.parse(urlImage.get(position).toString()))
                                            .into(imageView);
                                }
                            };
                            carouselView.setImageListener(imageListener);
                            carouselView.setPageCount(JUMLAH_SLIDE);


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        recyclerView =v.findViewById(R.id.kuliner_recycler);
        listItems = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter = new ListUlasanAdapter(listItems,getActivity());

        recyclerView.setAdapter(myAdapter);
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> newUlasan = new HashMap<>();
                newUlasan.put("idKuliner",idKuliner);
                newUlasan.put("nmPengguna",txtNmProfil.getText().toString());
                newUlasan.put("ulasan",txtUlasan.getText().toString());
                newUlasan.put("rating", inputRating.getRating());

                db.collection(TABEL_ULASAN)
                        .add(newUlasan)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(getContext(),"Terima Kasih atas parsitipasi anda...",Toast.LENGTH_SHORT).show();
                                listItems.add(new ListUlasan(task.getResult().getId(),idKuliner,txtNmProfil.getText().toString(), txtUlasan.getText().toString(), inputRating.getRating()));
                                myAdapter.notifyDataSetChanged();
                                txtUlasan.setText("");
                                inputRating.setRating(0);
                            }
                        });
            }
        });

        isiAdapter();


        return v;
    }

    private void isiAdapter() {

        listItems.clear();
        myAdapter.notifyDataSetChanged();
        db.collection(TABEL_ULASAN)
                .whereEqualTo("idKuliner",idKuliner)
                .limit(25)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                listItems.add(new ListUlasan(document.getId(),
                                        document.getString("nmPengguna"),
                                        document.getString("idKuliner"),
                                        document.getString("ulasan"),  document.getDouble("rating").floatValue()));
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

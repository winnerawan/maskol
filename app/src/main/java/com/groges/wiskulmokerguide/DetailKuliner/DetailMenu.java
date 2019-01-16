package com.groges.wiskulmokerguide.DetailKuliner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groges.wiskulmokerguide.DetailTempat.DetailTempat;
import com.groges.wiskulmokerguide.ListMenu;
import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailMenu extends AppCompatActivity implements ListRecomAdapter.OnItemClickListener {
    private static final String TABEL_MENU = "menuKuliner";
    private static final String TABEL_ULASAN = "ulasanKuliner";
    private static final int JUMLAH_SLIDE = 5 ;
    private static final String TABEL_GAMBAR = "imageKuliner";

    private TextView txtNama, txtHarga, txtAvgRating, txtAlamat,txtNmProfil,txtUlasan, txtTelp;
    private ImageView imgMenu;
    private Button btnKirim;
    private RatingBar inputRating;
    RecyclerView recyclerView,reclr_recom;

    private ProgressBar progressBar;
    private List<ListUlasan> listItems;
    private ListUlasanAdapter myAdapter;
    private ListRecomAdapter myAdapter1;
    private List<ListMenu> listItems1;


    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private String idKuliner;
    private String idTempat,nmTempat;

    CarouselView carouselView;
    ArrayList<Object> urlImage;
    private String nmKuliner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        final Intent intent = getIntent();

        idKuliner = intent.getStringExtra("idKuliner");
        nmKuliner = intent.getStringExtra("nmKuliner");
        String uriImg = intent.getStringExtra("uriImg");

        double hrgKuliner = intent.getDoubleExtra("hrgKuliner",0);

        idTempat = intent.getStringExtra("idTempat");
        nmTempat = intent.getStringExtra("nmTempat");

        txtNama = findViewById(R.id.kuliner_txtNama);
//        imgMenu = findViewById(R.id.kuliner_img);
        txtHarga = findViewById(R.id.kuliner_txtharga);
        txtAvgRating = findViewById(R.id.kuliner_txtRatAverage);
        txtAlamat = findViewById(R.id.kuliner_txtAlamat);
        txtTelp = findViewById(R.id.kuliner_txtTelpon);
        txtNmProfil = findViewById(R.id.kuliner_txtNmProfil);
        btnKirim = findViewById(R.id.kuliner_btnKirim);
        txtUlasan = findViewById(R.id.kuliner_txtUlasan);
        //txtUlasan.setFocusableInTouchMode(true);
        //txtUlasan.setFocusable(false);
        inputRating =findViewById(R.id.kuliner_ratingbar);

        progressBar = findViewById(R.id.kuliner_progressBar);

//        if(!uriImg.isEmpty()){
//            Picasso.with(this).load(uriImg).into(imgMenu);
//        }
        txtNama.setText(nmKuliner);
        txtTelp.setText(nmTempat);
        txtHarga.setText(String.format("%.1f", hrgKuliner/1000) + "K");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        if(mUser!=null) {
            if(mUser.getDisplayName()!=null) {
                txtNmProfil.setText(mUser.getDisplayName());
            }else{
                txtNmProfil.setText(mUser.getUid());
            }
        }

        //mAuth = FirebaseAuth.getInstance();
//        mStorage = FirebaseStorage.getInstance();

        db = FirebaseFirestore.getInstance();

        carouselView = findViewById(R.id.carouselDetail);
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
                                    Picasso.with(getBaseContext())
                                            .load(Uri.parse(urlImage.get(position).toString()))
                                            .into(imageView);
                                }
                            };
                            carouselView.setImageListener(imageListener);
                            carouselView.setPageCount(i);


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        recyclerView =findViewById(R.id.kuliner_recycler);
        listItems = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter = new ListUlasanAdapter(listItems,this);

        recyclerView.setAdapter(myAdapter);


        reclr_recom =findViewById(R.id.kuliner_recom);
        listItems1 = new ArrayList<>();
        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.HORIZONTAL,false);
        reclr_recom.setLayoutManager(manager1);
        reclr_recom.setItemAnimator(new DefaultItemAnimator());
        myAdapter1 = new ListRecomAdapter(listItems1,this);
        myAdapter1.setOnItemClickListener(this);
        reclr_recom.setAdapter(myAdapter1);


        txtTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(), "warung :" + nmTempat + idTempat , Toast.LENGTH_SHORT).show();
                Intent intent1= new Intent(getBaseContext(), DetailTempat.class);
                intent1.putExtra("idTempat",idTempat);
                intent1.putExtra("nmTempat",nmTempat);
                startActivity(intent1);

            }
        });

        inputRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtUlasan.setFocusable(true);
            }
        });
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
                                Toast.makeText(getBaseContext(),"Terima Kasih atas parsitipasi anda...",Toast.LENGTH_SHORT).show();
                                listItems.add(new ListUlasan(task.getResult().getId(),idKuliner,txtNmProfil.getText().toString(), txtUlasan.getText().toString(), inputRating.getRating()));
                                myAdapter.notifyDataSetChanged();
                                txtUlasan.setText("");
                                inputRating.setRating(0);
                            }
                        });
            }
        });

        isiAdapter();
        isiAdapter1();

    }

    private void isiAdapter1() {
        listItems1.clear();
        myAdapter1.notifyDataSetChanged();
        db.collection(TABEL_MENU)
                .whereGreaterThan("nmMenu",nmKuliner)
                .limit(8)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                listItems1.add(new ListMenu(document.getId(),
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
                            myAdapter1.notifyDataSetChanged();
//                            mProgressCircle.setVisibility(View.INVISIBLE);
                        } else {
  //                          mProgressCircle.setVisibility(View.INVISIBLE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        mProgressCircle.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        db.collection(TABEL_MENU)
                .whereLessThan("nmMenu",nmKuliner)
                .limit(8)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                listItems1.add(new ListMenu(document.getId(),
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
                            myAdapter1.notifyDataSetChanged();
//                            mProgressCircle.setVisibility(View.INVISIBLE);
                        } else {
                            //                          mProgressCircle.setVisibility(View.INVISIBLE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        mProgressCircle.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

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
                            double totRating = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                listItems.add(new ListUlasan(document.getId(),
                                        document.getString("nmPengguna"),
                                        document.getString("idKuliner"),
                                        document.getString("ulasan"),  document.getDouble("rating").floatValue()));
                                totRating += document.getDouble("rating");
                            }
                            double avgRating = ( totRating/ listItems.size());
                            txtAvgRating.setText( String.format("%.1f",avgRating ));
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


    @Override
    public void onItemClick(int position) {
        ListMenu selectedItem = listItems1.get(position);
        Intent intent = new Intent(this, DetailMenu.class);
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
        ListMenu selectedItem = listItems1.get(position);
//        Intent intent = new Intent(getActivity(),ShowMap.class);
//        intent.putExtra("lat",selectedItem.getLat());
//        intent.putExtra("lat",selectedItem.getLng());
//        intent.putExtra("nmRM",selectedItem.getTempat());
//        startActivity(intent);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+selectedItem.getLat()+","+ selectedItem.getLng()));
        startActivity(intent);
    }}

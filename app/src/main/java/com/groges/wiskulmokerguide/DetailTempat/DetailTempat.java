package com.groges.wiskulmokerguide.DetailTempat;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groges.wiskulmokerguide.ListMenu;
import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailTempat extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TABEL_MENU = "menuKuliner";
    private static final String TABEL_TEMPAT = "tempatKuliner";
    private static final String TAG = "det_TEMPAT";
    private TextView txtNama,txtAlamat,txtTelpon;
    private ImageView imgWarung;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<ListMenu> listItems;
    private ListMenuWarungAdapter myAdapter;
    private FirebaseFirestore db;
    private Toolbar mToolbar;

    private String idWarung;
    private String nmWarung,almWarung, tlpWarung;
    private String uriImg;
    private LatLng latLng;

    private Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tempat);

        mToolbar = findViewById(R.id.toolbar);
        btnCall = findViewById(R.id.button_call);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        txtNama = findViewById(R.id.tempat_txtNama);
        txtAlamat = findViewById(R.id.tempat_txtAlamat);
        txtTelpon = findViewById(R.id.tempat_txtTelpon);

        //imgWarung = findViewById(R.id.tempat_img);
        recyclerView = findViewById(R.id.tempat_recycler);

        Intent intent = getIntent();
        idWarung = intent.getStringExtra("idTempat");
        nmWarung = intent.getStringExtra("nmTempat");
        latLng = new LatLng(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lng", 0.0));

        db = FirebaseFirestore.getInstance();


        DocumentReference docRef = db.collection(TABEL_TEMPAT).document(idWarung);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        nmWarung = document.getString("nmTempat");
                        almWarung = document.getString("alamat");
                        tlpWarung = document.getString("telpon");
                        uriImg = document.getString("imgUri");
                        latLng = new LatLng(document.getDouble("lat"), document.getDouble("lng"));
                        txtNama.setText(nmWarung);
                        txtAlamat.setText(almWarung);
                        txtTelpon.setText(tlpWarung);




                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });




        listItems = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter = new ListMenuWarungAdapter(listItems,this);

        recyclerView.setAdapter(myAdapter);

        isiAdapter();

        createMap(savedInstanceState);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(Uri.parse("tel:"+txtTelpon.getText().toString()));
                startActivity(dial);
            }
        });
    }

    private void createMap(Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview_food_map);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.getMapAsync(this);
    }

    private void setupMap(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(latLng).title(nmWarung).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 15.0f);
        googleMap.animateCamera(location);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setupMap(googleMap);
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
                        } else {
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}

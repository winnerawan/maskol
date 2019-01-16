package com.groges.wiskulmokerguide.Hotel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;

public class DetailHotel extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TABEL = "spotHotel";


    private TextView txtNama, txtAlamat,txtTelp, txtShortDesc, txtLongDesc, txtJamBuka;
    private ImageView imgWisata;
    private FirebaseFirestore db;
    private String idTempat,nmTempat;
    private double lat,lng;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);

        final Intent intent = getIntent();

        idTempat = intent.getStringExtra("idHotel");
        nmTempat = intent.getStringExtra("nmHotel");
        String uriImg = intent.getStringExtra("uriImg");
        lat = intent.getDoubleExtra("lat",-7.701486);
        lng = intent.getDoubleExtra("lng",112.5079032);


        txtNama = findViewById(R.id.det_wisata_txtNama);
        imgWisata = findViewById(R.id.det_wisata_img);
        txtAlamat = findViewById(R.id.det_wisata_txtAlamat);
        txtTelp = findViewById(R.id.det_wisata_txtTelpon);
        txtShortDesc=findViewById(R.id.det_wisata_txtShortDesc);
        txtJamBuka=findViewById(R.id.det_wisata_txtJamBuka);
        txtLongDesc=findViewById(R.id.det_wisata_txtDescr);

        if(!uriImg.isEmpty()){
            Picasso.with(this).load(uriImg).into(imgWisata);
        }
        txtNama.setText(nmTempat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = FirebaseFirestore.getInstance();
        db.collection(TABEL).document(idTempat).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    txtAlamat.setText(task.getResult().getString("alamat"));
                    txtShortDesc.setText(task.getResult().getString("short_desc"));
                    txtTelp.setText(task.getResult().getString("telpon"));
                    txtJamBuka.setText(task.getResult().getString("jamBuka"));
                    txtLongDesc.setText(task.getResult().getString("descripsi"));
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(latLng).title(nmTempat).draggable(true));

        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

    }
}

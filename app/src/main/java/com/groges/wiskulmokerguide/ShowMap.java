package com.groges.wiskulmokerguide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double lat,lng;
    private String nmRM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat",-34);
        lng = intent.getDoubleExtra("lng",3);
        nmRM = intent.getStringExtra("nmRM");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        
        LatLng LokasiRM = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(LokasiRM).title(nmRM));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(LokasiRM));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LokasiRM,16));


    }
}

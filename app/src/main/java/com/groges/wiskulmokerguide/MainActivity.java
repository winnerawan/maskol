package com.groges.wiskulmokerguide;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groges.wiskulmokerguide.Hotel.Kategory.KatHotelFragment;
import com.groges.wiskulmokerguide.Kuliner.Kategory.KatKulinerFragment;
import com.groges.wiskulmokerguide.RekomendasiUser.FragmentAddPlace;
import com.groges.wiskulmokerguide.RekomendasiUser.PilihTempat;
import com.groges.wiskulmokerguide.Warung.ListWarungFragment;
import com.groges.wiskulmokerguide.Wisata.Kategory.KatWisataFragment;
import com.groges.wiskulmokerguide.Wisata.Kategory.ListKatWisata;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PilihTempat.OnFragmentInteractionListener,
        SearchView.OnQueryTextListener
{

    private static final String TAG = "####";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 100 ;
    private static final int JUMLAH_SLIDE = 3;
    private static final String TABEL_MENU = "katWisata";
    private String mTitle;
    FloatingActionButton fab;
    CarouselView carouselView;
    private FirebaseFirestore db;
    //String[] urlImage = {"https://firebasestorage.googleapis.com/v0/b/wiskulmokerguide.appspot.com/o/foodImage%2Fes%20buah.jpg?alt=media&token=379c9fd4-c5bb-4f1e-98dd-dfb107c38ccb",
//        "https://firebasestorage.googleapis.com/v0/b/wiskulmokerguide.appspot.com/o/foodImage%2Fes%20teh.jpg?alt=media&token=5f6da19b-4620-4290-ba7d-14474909c6c8",
//        "https://firebasestorage.googleapis.com/v0/b/wiskulmokerguide.appspot.com/o/foodImage%2Fes%20kuwut.jpg?alt=media&token=4ddcbe28-7d9e-4093-b2a9-3fbe9321a071"
//        };
    ArrayList<Object> urlImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set orientasi tetep potrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        carouselView = findViewById(R.id.carouselView);

        urlImage =   new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection(TABEL_MENU)
                .limit(3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (DocumentSnapshot document : task.getResult()) {

                                //listItems.add(new ListKatWisata(document.getId(),document.getString("kategory"),document.getString("imgUrl"),document.getString("ket")));
                                urlImage.add(i ,document.getString("imgUrl"));
                                Log.e("ADD",document.getString("imgUrl"));
                                i++;
                            }
                            ImageListener imageListener = new ImageListener() {
                                @Override
                                public void setImageForPosition(int position, ImageView imageView) {
                                    Picasso.with(getBaseContext())
                                            .load(Uri.parse(urlImage.get(position).toString()))
                                            .into(imageView);
                                    Log.e("GET",urlImage.get(position).toString());
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


        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermissions()) {
                    if (mTitle == "Rekomendasikan Tempat") {

                        FragmentAddPlace newfragment = new FragmentAddPlace();
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction().replace(R.id.main_frame, newfragment)
                                .addToBackStack(null)
                                .commit();

                    }
                }else{
                    requestPermissions();
                }
            }
        });
        fab.setVisibility(View.GONE);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem =menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment newfragment = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.main_frame, newfragment)
                    .addToBackStack(null)
                    .commit();


        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mTitle == "Rekomendasikan Tempat") {

                    FragmentAddPlace newfragment = new FragmentAddPlace();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.main_frame, newfragment)
                            .addToBackStack(null)
                            .commit();

                }

            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length()>=2) {
            ListWarungFragment newfragment = new ListWarungFragment();
            Bundle args = new Bundle();
            args.putString("queri", query);
            newfragment.setArguments(args);

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.main_frame, newfragment)
                    .addToBackStack(null)
                    .commit();

            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }

    @Override
    public void onFragmentInteraction(String title) {
        if(title=="Rekomendasikan Tempat"){
            fab.setVisibility(View.VISIBLE);
        }
        mTitle=title;

        getSupportActionBar().setTitle(mTitle);
    }

    public void goWisata(View view) {
//        Toast.makeText(getApplicationContext(),"Wisata",Toast.LENGTH_SHORT).show();
        KatWisataFragment newfragment = new KatWisataFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, newfragment)
                .addToBackStack(null)
                .commit();

    }
    public void goHotel(View view) {
//        Toast.makeText(getApplicationContext(),"Hotel",Toast.LENGTH_SHORT).show();
        KatHotelFragment newfragment = new KatHotelFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, newfragment)
                .addToBackStack(null)
                .commit();

    }
    public void goMojokerto(View view) {
        //Toast.makeText(getApplicationContext(),"Tentang Mojokerto",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MokerActivity.class);
        startActivity(intent);

    }
    public void goAbout(View view) {
        Toast.makeText(getApplicationContext(),"About",Toast.LENGTH_SHORT).show();
    }
    public void goKuliner(View view) {
        //Toast.makeText(getApplicationContext(),"Kuliner",Toast.LENGTH_SHORT).show();
        KatKulinerFragment newfragment = new KatKulinerFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, newfragment)
                .addToBackStack(null)
                .commit();

    }
    public void goNearby(View view) {
        Toast.makeText(getApplicationContext(),"Nearby",Toast.LENGTH_SHORT).show();
    }
}

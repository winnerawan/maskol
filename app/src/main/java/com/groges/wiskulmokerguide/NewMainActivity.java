package com.groges.wiskulmokerguide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.groges.wiskulmokerguide.ProfileUser.PosisiUser;
import com.groges.wiskulmokerguide.ProfileUser.UserFragment;
import com.groges.wiskulmokerguide.Warung.ListWarungFragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NewMainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final int RC_SIGN_IN = 10 ;
    private static final int REQUEST_GPS = 15;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */



    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient client;

    private FrameLayout btnHome, btnPlace, btnProfile;
    private ImageView icHome, icPlace, icProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainv2);

//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initButton();
        openFragment(HomeFragment.newInstance(), HomeFragment.TAG);
//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.container1);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mAuth = FirebaseAuth.getInstance();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(HomeFragment.newInstance(), HomeFragment.TAG);
                setNonActive();
                icHome.setImageResource(R.drawable.ic_home_on);
            }
        });

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(ListWarungFragment.newInstance(), ListWarungFragment.TAG);
                setNonActive();
                icPlace.setImageResource(R.drawable.ic_place_on);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(UserFragment.newInstance(), UserFragment.TAG);
                setNonActive();
                icProfile.setImageResource(R.drawable.ic_profile_on);
            }
        });
    }

    private void initButton() {
        btnHome = (FrameLayout) findViewById(R.id.f_btn_home);
        btnPlace = (FrameLayout) findViewById(R.id.f_btn_place);
        btnProfile = (FrameLayout) findViewById(R.id.f_btn_profile);
        icHome = (ImageView) findViewById(R.id.btn_home);
        icPlace = (ImageView) findViewById(R.id.btn_place);
        icProfile = (ImageView) findViewById(R.id.btn_profile);
    }

    private void openFragment(Fragment fragment, final String TAG) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.container1, fragment, TAG);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_main, menu);
        MenuItem searchItem =menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length()>=2) {
            Intent intentSearch = new Intent(this,Search.class);
            intentSearch.putExtra("query",query);
            startActivity(intentSearch);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    private void setNonActive() {
        //ImageView img_feeds =
        this.icHome.setImageResource(R.drawable.ic_home);
        this.icPlace.setImageResource(R.drawable.ic_place);
        this.icProfile.setImageResource(R.drawable.ic_profile);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    HomeFragment homeFragment = new HomeFragment();
                    return homeFragment;
                case 1:
                    ListWarungFragment listWarungFragment = new ListWarungFragment();
                    return listWarungFragment;
                case 2:
                    UserFragment userFragment = new UserFragment();
                    return userFragment;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if (mUser == null){
            loginForm();
        }else{
            client = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(getContext(),"Anda harus mengaktifkan seting lokasi anda",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},REQUEST_GPS);

            }else {
                client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!=null) {
                            PosisiUser.setLat(location.getLatitude());
                            PosisiUser.setLng(location.getLongitude());


                        }
                    }
                });
            }

        }


    }

    public void loginForm(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.mipmap.ic_mojokerto)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {

                mUser = mAuth.getCurrentUser();
                Map<String, Object> dataUser = new HashMap<>();
                if(mUser.getDisplayName()!=null) {
                    dataUser.put("nmUser", mUser.getDisplayName());

                }
                if(mUser.getEmail()!=null) {
                    dataUser.put("email", mUser.getEmail());
                }
                if(mUser.getPhoneNumber()!=null) {
                    dataUser.put("phone", mUser.getPhoneNumber());
                }
                client = LocationServices.getFusedLocationProviderClient(this);
                if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getContext(),"Anda harus mengaktifkan seting lokasi anda",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},REQUEST_GPS);

                }else {
                    client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location!=null) {
                                PosisiUser.setLat(location.getLatitude());
                                PosisiUser.setLng(location.getLongitude());


                            }
                        }
                    });
                }


//                UpdateUi();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    return;
                }

            }
        }

    }
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GPS) {
            if (grantResults.length <= 0) {
                //Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!=null) {
                            PosisiUser.setLat(location.getLatitude());
                            PosisiUser.setLng(location.getLongitude());


                        }
                    }
                });

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
}

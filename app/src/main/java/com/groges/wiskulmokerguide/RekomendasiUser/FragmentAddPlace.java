package com.groges.wiskulmokerguide.RekomendasiUser;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.groges.wiskulmokerguide.BuildConfig;
import com.groges.wiskulmokerguide.FragmentUserRekomendasi;
import com.groges.wiskulmokerguide.MapsActivity;
import com.groges.wiskulmokerguide.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddPlace extends Fragment {

    private static final int REQUEST_GPS = 125;
    private static final int REQUEST_LOKASI = 130;
    private static final int REQUEST_IMAGE = 135;


    private static final String namaTabel="tempatKuliner";


    private EditText txtNmTempat,txtAlamat,txtTelpon;

    private TextView txtLokasi;

    private FusedLocationProviderClient client;
    private double lat,lng;
    private ImageView img;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private FirebaseFirestore db;
//    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private String newId;


    Button btnLokasi, btnKirim;
    Uri mImageItemUri;


    public FragmentAddPlace() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view= inflater.inflate(R.layout.fragment_add_place, container, false);

        txtNmTempat = view.findViewById(R.id.addPlace_txtNmTempat);
        txtAlamat = view.findViewById(R.id.addPlace_txtAlamat);
        txtTelpon = view.findViewById(R.id.addPlace_txtTelpon);
        txtLokasi=view.findViewById(R.id.addPlace_txtLatLng);
        btnLokasi = view.findViewById(R.id.addPlace_setLokasi);
        btnKirim = view.findViewById(R.id.addPlace_btnKirim);
        mProgressBar = view.findViewById(R.id.addPlace_progress);

        img = view.findViewById(R.id.addPlace_img);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        db = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//
        mStorageRef = FirebaseStorage.getInstance().getReference("PlaceImage");

        client = LocationServices.getFusedLocationProviderClient(getActivity());


        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getContext(),"Anda harus mengaktifkan seting lokasi anda",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION},REQUEST_GPS);

        }else {
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null) {
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        txtLokasi.setText("Latitude :"+lat + ", Longitude :" + lng);

                    }
                }
            });
        }

        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("frmAwal","addPlace");

                startActivityForResult(intent,REQUEST_LOKASI);


            }
        });
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Sedang Mengupload", Toast.LENGTH_SHORT).show();
                    return;
                }

                if((lat==0 || lng==0) || txtNmTempat.getText().toString().isEmpty() ){
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);



                Map<String,Object> laporan = new HashMap<>();
                laporan.put("nmTempat",txtNmTempat.getText().toString());
                laporan.put("alamat",txtAlamat.getText().toString());
                laporan.put("telpon",txtTelpon.getText().toString());
                laporan.put("lat", lat);
                laporan.put("lng", lng);
//                laporan.put("pelapor",mAuth.getCurrentUser().getUid());



//                if(mAuth.getCurrentUser().getPhoneNumber()!=null) {
//                    laporan.put("hppelapor",mAuth.getCurrentUser().getPhoneNumber().toString());
//                }
//                if(mAuth.getCurrentUser().getDisplayName()!=null) {
//                    laporan.put("nmpelapor",mAuth.getCurrentUser().getDisplayName().toString());
//                }

                laporan.put("updatetime", FieldValue.serverTimestamp());

                db.collection(namaTabel)
                        .add(laporan)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                newId = documentReference.getId();

                                uploadFile();
                                Toast.makeText(getContext(),"Terima kasih atas partisipasi anda, Rekomendasi anda sudah terkirim",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Rekomendasi GAGAL, periksa internet anda dan ulangi lagi",Toast.LENGTH_LONG).show();
                            }
                        });


            }
        });
        return view;

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_IMAGE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageItemUri = data.getData();

            Picasso.with(getContext()).load(mImageItemUri)
                    .resize(700,1024)
                    .onlyScaleDown()
                    .into(img);

        }
        if(requestCode==REQUEST_LOKASI){
            if (data != null) {
                lat = data.getDoubleExtra("lat", lat);
                lng = data.getDoubleExtra("lng", lng);
                txtLokasi.setText("Lat:"+ lat + ", Long:" + lng);
            }

        }
    }
//
//    public class Notify extends AsyncTask<Void,Void,Void>
//    {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//
//            try {
//
//                URL url = new URL("https://fcm.googleapis.com/fcm/send");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                conn.setUseCaches(false);
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Authorization","key=AAAAYfDWyP4:APA91bHxuoXUut4J3MpCK7lqjbvF_cwYkVVjYCRQtIgTb1WsZWNySMsZshIiVekhKeEPYWb6xHPfCuQbX5iJg9Ybalv2K2fi6XFgDeRK3tFHOFW7jj6PRDceuvmyQIyf_iOgrpujmfG8");
//                conn.setRequestProperty("Content-Type", "application/json");
//
//                JSONObject json = new JSONObject();
//
//                json.put("to", "/topics/lapor");
//
//
//                JSONObject info = new JSONObject();
//                info.put("title", "Laporan baru");   // Notification title
//                info.put("body", txtKet.getText().toString()); // Notification body
//                info.put("sound", "default");
//
//                json.put("notification", info);
//
//                JSONObject datanya = new JSONObject();
//                datanya.put("lat",lat);
//                datanya.put("lng",lng);
//                if(mAuth.getCurrentUser().getDisplayName()!=null) {
//                    datanya.put("plp", mAuth.getCurrentUser().getDisplayName());
//                }else{
//                    datanya.put("plp", mAuth.getCurrentUser().getPhoneNumber());
//                }
//                json.put("data",datanya);
//
//                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//                wr.write(json.toString());
//                wr.flush();
//                conn.getInputStream();
////                Log.d("NOTIF:","Terkirim");
//
//            }
//            catch (Exception e)
//            {
////                Log.d("Error",""+e);
//            }
//
//
//            return null;
//        }
//    }
    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = getView().findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
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
        Snackbar.make(getView().findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_GPS) {
            if (grantResults.length <= 0) {
                Log.i("####", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!=null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            txtLokasi.setText("Lat:"+lat + ", Long:" + lng);
                            
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
    private String getFileExtension(Uri uri) {
        ContentResolver cR =getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {

        if(mImageItemUri!=null) {
            mProgressBar.setVisibility(View.VISIBLE);

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageItemUri));
            mUploadTask = fileReference.putFile(mImageItemUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressBar.setProgress(0);
                            mProgressBar.setVisibility(View.GONE);
                            Map<String,Object> dataFoto = new HashMap<>();
                            dataFoto.put("imgUri",taskSnapshot.getDownloadUrl().toString());
                            db.collection(namaTabel).document(newId)
                                    .set(dataFoto, SetOptions.merge())
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(),"Gagal mencatat url foto",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            FragmentUserRekomendasi newfragment = new FragmentUserRekomendasi();
                            Bundle args = new Bundle();


                            args.putString("idWarung", newId);
                            args.putString("nmWarung",txtNmTempat.getText().toString());
                            args.putString("almWarung",txtAlamat.getText().toString());
                            args.putString("tlpWarung",txtTelpon.getText().toString());

                            args.putDouble("lat",lat);
                            args.putDouble("lng",lng);

                            newfragment.setArguments(args);

                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction().replace(R.id.main_frame, newfragment)
                                    .addToBackStack(null)
                                    .commit();



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setVisibility(View.VISIBLE);
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }
        //getFragmentManager().popBackStack();
    }
}

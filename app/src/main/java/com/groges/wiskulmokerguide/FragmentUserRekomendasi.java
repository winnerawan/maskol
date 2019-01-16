package com.groges.wiskulmokerguide;


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
public class FragmentUserRekomendasi extends Fragment {

    private static final int REQUEST_IMAGE = 135;
    private static final String NAMA_TABLE = "menuKuliner" ;
    private EditText txtNmMenu;
    private AutoCompleteTextView txtJns;
    private TextView txtNmTempat, txtAlamat;


    private double lat,lng;
    private ImageView img;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private FirebaseFirestore db;
//    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private String newId;


    Button  btnKirim;
    Uri mImageItemUri;
    private String idWarung, nmWarung,almWarung,tlpWarung,uriImg;

    public FragmentUserRekomendasi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;


        view= inflater.inflate(R.layout.fragment_rekomen, container, false);

        txtNmMenu=view.findViewById(R.id.rekom_txtNmMenu);
        txtJns = view.findViewById(R.id.rekom_txtJnsMenu);
        txtNmTempat = view.findViewById(R.id.rekom_txtNmTempat);
        txtAlamat = view.findViewById(R.id.rekom_txtAlamat);
        btnKirim = view.findViewById(R.id.rekom_btnKirim);
        mProgressBar = view.findViewById(R.id.rekom_progress);

        img = view.findViewById(R.id.rekom_imgMenu);

        Bundle bundle = getArguments();

        idWarung = bundle.getString("idWarung");
        nmWarung = bundle.getString("nmWarung");
        almWarung = bundle.getString("almWarung");
        tlpWarung = bundle.getString("tlpWarung");
        uriImg = bundle.getString("uriImg");
        lat = bundle.getDouble("lat");
        lng = bundle.getDouble("lng");

        txtNmTempat.setText(nmWarung);
        txtAlamat.setText(almWarung);


        List<String> list = new ArrayList<>();
        list.add("Soto");
        list.add("Bakso");
        list.add("Pecel");
        list.add("Sate");
        list.add("Nasi Goreng");
        list.add("Mie");
        list.add("Kue Basah");


        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_dropdown_item_1line, list);
        txtJns.setAdapter(adapter);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        db = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//
        mStorageRef = FirebaseStorage.getInstance().getReference("foodImage");

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Sedang Mengupload", Toast.LENGTH_SHORT).show();
                    return;
                }

                if((lat==0 || lng==0) || txtNmMenu.getText().toString().isEmpty()|| txtNmTempat.getText().toString().isEmpty() ){
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);



                Map<String,Object> laporan = new HashMap<>();
                laporan.put("idTempat", idWarung);
                laporan.put("jenis",txtJns.getText().toString());
                laporan.put("nmMenu",txtNmMenu.getText().toString());
                laporan.put("nmTempat",txtNmTempat.getText().toString());
                laporan.put("alamat",txtAlamat.getText().toString());
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

                db.collection(NAMA_TABLE)
                        .add(laporan)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                newId = documentReference.getId();

                                //send Notify
//                           new Notify().execute();

                                //upload foto
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

    }

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
                            db.collection(NAMA_TABLE).document(newId)
                                    .set(dataFoto, SetOptions.merge())
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(),"Gagal mencatat url foto",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            //Toast.makeText(getContext(), "1 Foto terupload", Toast.LENGTH_SHORT).show();
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

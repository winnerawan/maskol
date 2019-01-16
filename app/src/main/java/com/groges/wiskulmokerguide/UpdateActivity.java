package com.groges.wiskulmokerguide;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Button btnSave;
    AppCompatEditText txtNama, txtEmail, txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        btnSave = (Button) findViewById(R.id.save);
        txtEmail = findViewById(R.id.email);
        txtNama = findViewById(R.id.username);
        txtPhone= findViewById(R.id.phone);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser!=null){
            if(mUser.getEmail()!=null){
                txtEmail.setText(mUser.getEmail());
            }
            if(mUser.getDisplayName()!=null){
                txtNama.setText(mUser.getDisplayName());
            }
            if(mUser.getPhoneNumber()!=null){
                txtPhone.setText(mUser.getPhoneNumber());
            }

        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtNama.getText().toString().isEmpty()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(txtNama.getText().toString())
                            .build();

                    mUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                        if (!txtEmail.getText().toString().isEmpty()) {
                                            mUser.updateEmail(txtEmail.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(), "Data User  telah tersimpan", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Data User telah tersimpan", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }
}
package com.groges.wiskulmokerguide.ProfileUser;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.groges.wiskulmokerguide.R;
import com.groges.wiskulmokerguide.UpdateActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    public static final String TAG = UserFragment.class.getSimpleName();
    private TextView txtEmail, txtNama, txtPhone;
    private Button btnUpdate;
    private Button txtLogout;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;


    public static UserFragment newInstance() {

        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        txtEmail = view.findViewById(R.id.txtEmail_user);
        txtNama = view.findViewById(R.id.txtNama_User);
        txtLogout = view.findViewById(R.id.txtLogOut);
        //txtPhone=view.findViewById(R.id.txtPhone_user);

        btnUpdate=view.findViewById(R.id.update);
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        if (mUser!=null){
            if(mUser.getEmail()!=null){
                txtEmail.setText(mUser.getEmail());
            }
            if(mUser.getDisplayName()!=null){
                txtNama.setText(mUser.getDisplayName());
            }

        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UpdateActivity.class));
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(getContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getActivity().finish();
                    }
                });

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        if (mUser!=null){
            if(mUser.getEmail()!=null){
                txtEmail.setText(mUser.getEmail());
            }
            if(mUser.getDisplayName()!=null){
                txtNama.setText(mUser.getDisplayName());
            }
        }
        super.onResume();
    }
}

package com.groges.wiskulmokerguide;

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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groges.wiskulmokerguide.DetailKuliner.DetailMenu;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity implements HomeAdapter.OnItemClickListener {
    private static final String TABEL_MENU = "menuKuliner" ;
    private RecyclerView recyclerView;
    private List<ListMenu> listItems;
    private HomeAdapter myAdapter;
    private ProgressBar mProgressCircle;
    private TextView txtNF,txtFound;

    //    private FirebaseStorage mStorage;
    private FirebaseFirestore db;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_search);
        mProgressCircle = findViewById(R.id.progressBar_search);
        txtNF=findViewById(R.id.txtNotFound);
        txtFound=findViewById(R.id.txtFound);


        db = FirebaseFirestore.getInstance();
        final Intent intent = getIntent();

        query = intent.getStringExtra("query");
        if(query!=null && !query.isEmpty()){
            String str = query;
            String[] strArray = str.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap + " ");
            }
            query = builder.toString();
            txtFound.setText("Mungkin menu yang anda maksud adalah : " + query);
        }

        listItems = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter = new HomeAdapter(listItems,this);
        myAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(myAdapter);

        isiAdapter();

    }
    private void isiAdapter() {
        listItems.clear();
        myAdapter.notifyDataSetChanged();
        db.collection(TABEL_MENU)
                .orderBy("nmMenu").startAt(query).endAt(query+'\uf8ff')
//                .whereGreaterThanOrEqualTo("nmMenu",query)
//                .limit(1)
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
                            if(listItems.size()<=0){
                                txtNF.setVisibility(View.VISIBLE);
                                txtFound.setVisibility(View.GONE);
                            }else{
                                txtNF.setVisibility(View.GONE);
                                txtFound.setVisibility(View.VISIBLE);
                            }

                            mProgressCircle.setVisibility(View.INVISIBLE);
                        } else {
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressCircle.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        ListMenu selectedItem = listItems.get(position);
        Intent intent = new Intent(getApplicationContext(), DetailMenu.class);
        intent.putExtra("idKuliner",selectedItem.getId());

        intent.putExtra("nmKuliner",selectedItem.getNama());
        intent.putExtra("uriImg",selectedItem.getUriImg());
        if(selectedItem.getHarga()!=null) {
            intent.putExtra("hrgKuliner", selectedItem.getHarga());
        }
        intent.putExtra("idTempat",selectedItem.getIdTempat());
        intent.putExtra("nmTempat",selectedItem.getTempat());

        startActivity(intent);

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        ListMenu selectedItem = listItems.get(position);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+selectedItem.getLat()+","+ selectedItem.getLng()));
        startActivity(intent);
    }
}

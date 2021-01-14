package com.abdullah.duvarkagitlari.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.abdullah.duvarkagitlari.Adapter.ImageAdapter;
import com.abdullah.duvarkagitlari.Model.Model;
import com.abdullah.duvarkagitlari.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DigerActivity extends AppCompatActivity {

    RecyclerView recyclerView_diger;
    ImageAdapter imageAdapter;

    private DatabaseReference databaseReference;
    private List<Model> images;

    private Toolbar toolbar_diger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diger);

        toolbar_diger = findViewById(R.id.toolbar_diger);
        setSupportActionBar(toolbar_diger);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView_diger = findViewById(R.id.recyclerView_diger);
        recyclerView_diger.setHasFixedSize(true);
        recyclerView_diger.setLayoutManager(new GridLayoutManager(this,2));

        images = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Diger");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Model image = postSnapshot.getValue(Model.class);
                    images.add(image);
                }

                imageAdapter = new ImageAdapter(DigerActivity.this, images);
                recyclerView_diger.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(DigerActivity.this, "HATA!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return true;
    }
}
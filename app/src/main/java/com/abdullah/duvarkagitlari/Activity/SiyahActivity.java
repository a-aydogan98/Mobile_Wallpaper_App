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

public class SiyahActivity extends AppCompatActivity {

    RecyclerView recyclerView_siyah;
    ImageAdapter imageAdapter;

    private DatabaseReference databaseReference;
    private List<Model> images;

    private Toolbar toolbar_siyah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siyah);

        toolbar_siyah = findViewById(R.id.toolbar_siyah);
        setSupportActionBar(toolbar_siyah);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView_siyah = findViewById(R.id.recyclerView_siyah);
        recyclerView_siyah.setHasFixedSize(true);
        recyclerView_siyah.setLayoutManager(new GridLayoutManager(this,2));

        images = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Siyah");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Model image = postSnapshot.getValue(Model.class);
                    images.add(image);
                }

                imageAdapter = new ImageAdapter(SiyahActivity.this, images);
                recyclerView_siyah.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(SiyahActivity.this, "HATA!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return true;
    }
}
package com.abdullah.duvarkagitlari.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.abdullah.duvarkagitlari.R;

public class HakkindaActivity extends AppCompatActivity {

    private Toolbar toolbar_hakkinda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hakkinda);

        toolbar_hakkinda = findViewById(R.id.toolbar_hakkinda);
        setSupportActionBar(toolbar_hakkinda);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return true;
    }
}
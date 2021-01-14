package com.abdullah.duvarkagitlari.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.abdullah.duvarkagitlari.R;

public class AcilisEkraniActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis_ekrani);

        Thread timerThread = new Thread() {

            public void run() {

                try {

                    sleep(2000);
                }

                catch (InterruptedException e) {

                    e.printStackTrace();
                }

                finally {

                    Intent intent = new Intent(AcilisEkraniActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        timerThread.start();
    }

    @Override
    protected void onPause() {

        super.onPause();

        finish();
    }
}
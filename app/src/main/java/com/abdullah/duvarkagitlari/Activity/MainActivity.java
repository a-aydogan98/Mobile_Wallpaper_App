package com.abdullah.duvarkagitlari.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.abdullah.duvarkagitlari.Model.CustomItems;
import com.abdullah.duvarkagitlari.VeriTabani.Dao;
import com.abdullah.duvarkagitlari.R;
import com.abdullah.duvarkagitlari.Adapter.RecyclerViewAdapter;
import com.abdullah.duvarkagitlari.VeriTabani.VeriTabaniYardimcisi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<CustomItems> itemsList;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    private Toolbar toolbar_main;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton, fab_sil;

    private VeriTabaniYardimcisi vt;

    public static final String MyPREFERENCES = "nightModePrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isConnected()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setIcon(R.drawable.internet);
            alertDialog.setTitle("İnternet Erişimi Uyarısı");
            alertDialog.setMessage("Lütfen internet bağlantınızı kontrol ediniz.");

            alertDialog.setPositiveButton("Kapat", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });

            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        checkNightModeActivated();

        floatingActionButton = findViewById(R.id.floatingActionButton);
        fab_sil = findViewById(R.id.fab_sil);

        vt = new VeriTabaniYardimcisi(this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                View tasarim = getLayoutInflater().inflate(R.layout.alertview_tasarim, null);
                final EditText editTextAlert = tasarim.findViewById(R.id.editTextAlert);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                alertDialog.setMessage("Eklemek istediğiniz duvar kağıdının indirme linkini giriniz.");
                alertDialog.setView(tasarim);
                alertDialog.setTitle("Duvar Kağıdı Ekle");
                alertDialog.setIcon(R.drawable.ekle);

                alertDialog.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final String url = editTextAlert.getText().toString();

                        if(TextUtils.isEmpty(url)) {

                            Toast.makeText(MainActivity.this, "Boş URL girdiniz.", Toast.LENGTH_SHORT).show();
                        }

                        else {

                            new Dao().resimEkle(vt, editTextAlert.getText().toString());

                            itemsList = new Dao().customItem(vt);

                            recyclerViewAdapter = new RecyclerViewAdapter(itemsList, MainActivity.this);

                            recyclerView.setAdapter(recyclerViewAdapter);

                            Toast.makeText(MainActivity.this, "Yeni duvar kağıdı veritabanına kaydedildi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(MainActivity.this, "İptal edildi.", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.show();
            }
        });

        fab_sil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                alertDialog.setMessage("Eklediğiniz duvar kağıtlarını silmek istiyor musunuz?");
                alertDialog.setIcon(R.drawable.sil_dialog);
                alertDialog.setTitle("Sil");

                alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Dao().resimSil(vt);

                        itemsList = new Dao().customItem(vt);

                        recyclerViewAdapter = new RecyclerViewAdapter(itemsList, MainActivity.this);

                        recyclerView.setAdapter(recyclerViewAdapter);

                        Toast.makeText(MainActivity.this, "Eklediğiniz duvar kağıtları silindi.", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(MainActivity.this, "İptal edildi.", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.show();
            }
        });

        ArrayList<CustomItems> gelenResimler = new Dao().customItem(vt);

        toolbar_main = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar_main);

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar_main, 0, 0);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View baslik = navigationView.inflateHeaderView(R.layout.navigation_baslik);

        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        itemsList = new ArrayList<>();

        for(CustomItems k : gelenResimler)
        {
            itemsList.add(new CustomItems(String.valueOf(k.getUrl())));
        }

        recyclerViewAdapter = new RecyclerViewAdapter(itemsList,this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_koyuTema) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            saveNightModeState(true);
            recreate();
        }

        if(id == R.id.action_acikTema) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            saveNightModeState(false);
            recreate();
        }

        return true;
    }

    private void saveNightModeState(boolean b) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(KEY_ISNIGHTMODE, b);

        editor.apply();
    }

    public void checkNightModeActivated() {

        if(sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate .MODE_NIGHT_YES);
        }

        else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id == R.id.action_renkler) {

            Intent intent = new Intent(MainActivity.this, RenklerActivity.class);
            startActivity(intent);
        }

        if(id == R.id.action_hayvanlar) {

            Intent intent = new Intent(MainActivity.this, HayvanlarActivity.class);
            startActivity(intent);
        }

        if(id == R.id.action_siyah) {

            Intent intent = new Intent(MainActivity.this, SiyahActivity.class);
            startActivity(intent);
        }

        if(id == R.id.action_diger) {

            Intent intent = new Intent(MainActivity.this, DigerActivity.class);
            startActivity(intent);
        }

        if(id == R.id.action_uygula) {

            Intent intent = new Intent(MainActivity.this, UygulaActivity.class);
            startActivity(intent);
        }

        if(id == R.id.action_hakkinda) {

            Intent intent = new Intent(MainActivity.this, HakkindaActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }
}
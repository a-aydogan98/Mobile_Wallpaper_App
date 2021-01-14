package com.abdullah.duvarkagitlari.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.abdullah.duvarkagitlari.BuildConfig;
import com.abdullah.duvarkagitlari.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;

public class UygulaActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton btn_set_lock, btn_set_home, btn_share, btn_add;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private Toolbar toolbar_uygula;

    Bitmap bitmap;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Gerekli izinler verildi.", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(this, "Gerekli izinler verilemedi.", Toast.LENGTH_SHORT).show();
            }

            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uygula);

        Toast.makeText(this, "Duvar kağıdı seçmek için sağ alttaki butona dokunun.", Toast.LENGTH_LONG).show();

        toolbar_uygula = findViewById(R.id.toolbar_uygula);
        setSupportActionBar(toolbar_uygula);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }

        floatingActionMenu = findViewById(R.id.floatingActionMenu);

        btn_set_lock = findViewById(R.id.setLockWallpaper);
        btn_set_home = findViewById(R.id.setHomeWallpaper);
        btn_share = findViewById(R.id.shareWallpaper);
        btn_add = findViewById(R.id.addWallpaper);

        btn_set_lock.setOnClickListener(this);
        btn_set_home.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        imageView = findViewById(R.id.fullImage);

        Picasso.get().load(getIntent().getStringExtra("images")).into(imageView);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.setLockWallpaper:
                setLockBackground();
                floatingActionMenu.close(true);
                break;

            case R.id.setHomeWallpaper:
                setHomeBackground();
                floatingActionMenu.close(true);
                break;

            case R.id.shareWallpaper:
                shareImage();
                floatingActionMenu.close(true);
                break;

            case R.id.addWallpaper:
                addImage();
                floatingActionMenu.close(true);
                break;
        }
    }

    private void shareImage() {

        bitmap = getBitmapFromView(imageView);

        try {

            File file = new File(this.getExternalCacheDir(), "image.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(UygulaActivity.this, BuildConfig.APPLICATION_ID + ".provider", file));
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent, "Duvar Kağıdını Paylaş"));

            Toast.makeText(this, "Duvar kağıdı paylaşılıyor...", Toast.LENGTH_SHORT).show();
         }

         catch (Exception e) {

             e.printStackTrace();
         }
    }

    private Bitmap getBitmapFromView(@NotNull View view) {

        Bitmap returnBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnBitmap);
        Drawable bgDrawable = view.getBackground();

        if (bgDrawable !=null) {

            bgDrawable.draw(canvas);
        }

        else {

            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);

        return returnBitmap;
    }

    private void setLockBackground() {

        try {

            bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());

            manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
            Toast.makeText(this, "Duvar kağıdı başarıyla telefona uygulandı.", Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {

            Toast.makeText(this, "Duvar kağıdı eklemediniz. Lütfen herhangi bir duvar kağıdı ekleyip yeniden deneyiniz.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setHomeBackground() {

        try {

            bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());

            manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
            Toast.makeText(this, "Duvar kağıdı başarıyla telefona uygulandı.", Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {

            Toast.makeText(this, "Duvar kağıdı eklemediniz. Lütfen herhangi bir duvar kağıdı ekleyip yeniden deneyiniz.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void addImage() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            imageView.setImageURI(data.getData());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return true;
    }
}
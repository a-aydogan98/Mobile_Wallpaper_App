package com.abdullah.duvarkagitlari.Activity;

import androidx.annotation.NonNull;
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
import com.abdullah.duvarkagitlari.Helper.SaveImageHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ViewActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton btn_set_lock, btn_set_home, btn_share, btn_download;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    private Toolbar toolbar_view;

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
        setContentView(R.layout.activity_view);

        toolbar_view = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }

        floatingActionMenu = findViewById(R.id.floatingActionMenu);

        btn_set_lock = findViewById(R.id.setLockWallpaper);
        btn_set_home = findViewById(R.id.setHomeWallpaper);
        btn_share = findViewById(R.id.shareWallpaper);
        btn_download = findViewById(R.id.downloadWallpaper);

        btn_set_lock.setOnClickListener(this);
        btn_set_home.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_download.setOnClickListener(this);

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

            case R.id.downloadWallpaper:
                saveImage();
                floatingActionMenu.close(true);
                break;
        }
    }

    private void shareImage() {

        Bitmap bitmap = getBitmapFromView(imageView);

        try {

            File file = new File(this.getExternalCacheDir(), "image.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ViewActivity.this, BuildConfig.APPLICATION_ID + ".provider", file));
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

        if (bgDrawable != null) {

            bgDrawable.draw(canvas);
        }

        else {

            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);

        return returnBitmap;
    }

    private void setLockBackground() {

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());

        try {

            manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
            Toast.makeText(this, "Duvar kağıdı başarıyla telefona uygulandı.", Toast.LENGTH_SHORT).show();
        }

        catch (IOException e) {

            Toast.makeText(this, "Bir şeyler yanlış gitti. :(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setHomeBackground() {

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());

        try {

            manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
            Toast.makeText(this, "Duvar kağıdı başarıyla telefona uygulandı.", Toast.LENGTH_SHORT).show();
        }

        catch (IOException e) {

            Toast.makeText(this, "Bir şeyler yanlış gitti. :(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void saveImage() {

        if (ActivityCompat.checkSelfPermission(ViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(ViewActivity.this, "Gerekli izinleri vermelisiniz.", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }

            return;
        }

        else {

            String fileName = UUID.randomUUID().toString() + ".jpg";

            Picasso.get().load(getIntent().getStringExtra("images")).into(new SaveImageHelper(getBaseContext(), getApplicationContext().getContentResolver(), fileName, "Image Description"));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return true;
    }
}
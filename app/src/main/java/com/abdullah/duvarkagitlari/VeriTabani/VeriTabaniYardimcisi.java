package com.abdullah.duvarkagitlari.VeriTabani;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VeriTabaniYardimcisi extends SQLiteOpenHelper {

    private static final int SURUM = 1;
    private static String veritabaniAdi = "images";

    public VeriTabaniYardimcisi (Context context) {

        super(context, veritabaniAdi, null, SURUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE image (image_id INTEGER PRIMARY KEY AUTOINCREMENT, image_url TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS image");

        onCreate(db);
    }
}
package com.abdullah.duvarkagitlari.VeriTabani;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abdullah.duvarkagitlari.Model.CustomItems;

import java.util.ArrayList;

public class Dao {

    public void resimEkle(VeriTabaniYardimcisi vt, String image_url) {

        SQLiteDatabase dbx = vt.getWritableDatabase();
        ContentValues degerler = new ContentValues();

        degerler.put("image_url", image_url);

        dbx.insertOrThrow("image", null, degerler);
    }

    public void resimSil(VeriTabaniYardimcisi vt) {

        SQLiteDatabase dbx = vt.getWritableDatabase();

        dbx.execSQL("DELETE FROM image");

        dbx.close();
    }

    public ArrayList<CustomItems> customItem(VeriTabaniYardimcisi vt) {

        ArrayList<CustomItems> customItems = new ArrayList<>();
        SQLiteDatabase dbx = vt.getWritableDatabase();

        Cursor c = dbx.rawQuery("SELECT * FROM image", null);

        while (c.moveToNext()) {

            CustomItems customItem = new CustomItems(c.getString(c.getColumnIndex("image_url")));

            customItems.add(customItem);
        }

        return customItems;
    }
}
package com.example.redelcom_test.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;

import com.example.redelcom_test.model.MusicModel;

import java.util.ArrayList;

public class MusicFacade extends SqliteHelper {
    public MusicFacade(@NonNull Context context) {
        super(context);
    }

    public boolean createSing(MusicModel singModel) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SING_COLUMN_TRACKNAME, singModel.getTrackName());
            contentValues.put(SING_COLUMN_ARTISTNAME, singModel.getArtistName());
            contentValues.put(SING_COLUMN_ARTWORK, singModel.getArtworkUrl100());
            contentValues.put(SING_COLUMN_PREVIEWURL, singModel.getPreviewUrl());
            contentValues.put(SING_COLUMN_COLLECIONID, singModel.getCollectionId());
            contentValues.put(SING_COLUMN_COLLECTIONNAME, singModel.getCollectionName());
            database.insert(SING_TABLE_NAME, null, contentValues);
            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<MusicModel> getSingsByCollection(Integer collectionId) {
        ArrayList<MusicModel> arrayList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor res = database.rawQuery("SELECT * FROM " + SING_TABLE_NAME + " WHERE " + SING_COLUMN_COLLECIONID + " =?", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            MusicModel model = new MusicModel();
            model.setTrackName(res.getString(res.getColumnIndex(SING_COLUMN_TRACKNAME)));
            model.setArtistName(res.getString(res.getColumnIndex(SING_COLUMN_ARTISTNAME)));
            model.setArtworkUrl100(res.getString(res.getColumnIndex(SING_COLUMN_ARTWORK)));
            model.setPreviewUrl(res.getString(res.getColumnIndex(SING_COLUMN_PREVIEWURL)));
            model.setCollectionId(res.getString(res.getColumnIndex(SING_COLUMN_COLLECIONID)));
            model.setCollectionName(res.getString(res.getColumnIndex(SING_COLUMN_COLLECTIONNAME)));
            arrayList.add(model);
            res.moveToNext();
        }
        return arrayList;
    }
}

package com.example.redelcom_test.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SqliteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RedelCom.db";
    public static final String HISTORIC_TABLE_NAME = "historic";
    public static final String HISTORIC_COLUMN_ID = "id";
    public static final String HISTORIC_COLUMN_TEXT = "text";
    public static final String HISTORIC_COLUMN_RESULT = "result";
    public static final String HISTORIC_COLUMN_CHECKED = "checked";
    public static final String SING_TABLE_NAME = "sing";
    public static final String SING_COLUMN_ID = "id";
    public static final String SING_COLUMN_TRACKNAME = "trackName";
    public static final String SING_COLUMN_ARTISTNAME = "artistName";
    public static final String SING_COLUMN_ARTWORK = "artworkUrl100";
    public static final String SING_COLUMN_PREVIEWURL = "previewUrl";
    public static final String SING_COLUMN_COLLECIONID = "collectionId";
    public static final String SING_COLUMN_COLLECTIONNAME = "collectionName";

    public SqliteHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SqliteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SqliteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + HISTORIC_TABLE_NAME
                + " (" + HISTORIC_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + HISTORIC_COLUMN_TEXT + " TEXT UNIQUE, "
                + HISTORIC_COLUMN_RESULT + " TEXT, "
                + HISTORIC_COLUMN_CHECKED + " TEXT)"
        );
        db.execSQL("CREATE TABLE " + SING_TABLE_NAME
                + " (" + SING_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + SING_COLUMN_TRACKNAME + " TEXT, "
                + SING_COLUMN_ARTISTNAME + " TEXT, "
                + SING_COLUMN_ARTWORK + " TEXT, "
                + SING_COLUMN_PREVIEWURL + " TEXT, "
                + SING_COLUMN_COLLECIONID + " TEXT, "
                + SING_COLUMN_COLLECTIONNAME+ " TEXT, "
                + "UNIQUE (" + SING_COLUMN_TRACKNAME
                + ", " + SING_COLUMN_COLLECIONID
                +"))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db.getVersion() == oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + HISTORIC_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SING_TABLE_NAME);
            onCreate(db);
        }
    }
}

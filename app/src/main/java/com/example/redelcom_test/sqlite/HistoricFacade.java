package com.example.redelcom_test.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;

import com.example.redelcom_test.sqlite.model.HistoricModel;

import java.util.ArrayList;

public class HistoricFacade extends SqliteHelper {
    public HistoricFacade(@NonNull Context context) {
        super(context);
    }

    public void createHistoric(HistoricModel historicModel) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(HISTORIC_COLUMN_TEXT, historicModel.getText());
            contentValues.put(HISTORIC_COLUMN_RESULT, historicModel.getResult());
            contentValues.put(HISTORIC_COLUMN_CHECKED, historicModel.getChecked());
            database.insert(HISTORIC_TABLE_NAME, null, contentValues);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public Cursor getData(String word) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(
                "SELECT " + HISTORIC_COLUMN_RESULT + " FROM " + HISTORIC_TABLE_NAME
                        + " WHERE " + HISTORIC_COLUMN_TEXT + "=?", new String[]{String.valueOf(word)}
                        );
    }

    public int rowCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(database, HISTORIC_TABLE_NAME);
    }

    public void updateCheked(String text, int trackId) {
        String checkered = getHistoricChecked(text);
        try (SQLiteDatabase database = this.getWritableDatabase()) {
            if (checkered.equals("")) checkered = Integer.toString(trackId);
            if (!checkered.contains(Integer.toString(trackId))) checkered = checkered.concat(", "+ trackId);
            ContentValues contentValues = new ContentValues();
            contentValues.put(HISTORIC_COLUMN_CHECKED, checkered);
            database.update(HISTORIC_TABLE_NAME, contentValues, "text=?", new String[]{text});
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public boolean updateResult(int id, String result) {
        try (SQLiteDatabase database = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(HISTORIC_COLUMN_RESULT, result);
            database.update(HISTORIC_TABLE_NAME, contentValues, "id=?", new String[]{Integer.toString(id)});
            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getHistoricChecked(String text){
        try (SQLiteDatabase database = this.getReadableDatabase()) {
            Cursor cursor = database.rawQuery(
                    "SELECT " + HISTORIC_COLUMN_CHECKED + " FROM " + HISTORIC_TABLE_NAME
                            + " WHERE " + HISTORIC_COLUMN_TEXT + "=?", new String[]{String.valueOf(text)}
            );
            cursor.moveToFirst();
            return cursor.getString(0);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return "";
        }
    }

    public ArrayList<HistoricModel> getAllHistoric() {
        ArrayList<HistoricModel> arrayList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor res = database.rawQuery("SELECT * FROM " + HISTORIC_TABLE_NAME, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            HistoricModel model = new HistoricModel();
            model.setId(res.getInt(res.getColumnIndex(HISTORIC_COLUMN_ID)));
            model.setText(res.getString(res.getColumnIndex(HISTORIC_COLUMN_TEXT)));
            model.setResult(res.getString(res.getColumnIndex(HISTORIC_COLUMN_RESULT)));
            model.setChecked(res.getString(res.getColumnIndex(HISTORIC_COLUMN_CHECKED)));
            arrayList.add(model);
            res.moveToNext();
        }
        return arrayList;
    }
}

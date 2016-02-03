package com.forecast.weather.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.forecast.weather.database.model.Forecast;
import com.forecast.weather.database.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksei Romashkin
 * on 03/02/16.
 */
public class DatabaseController {

    private DatabaseOpenHelper databaseOpenHelper;

    public DatabaseController(Context context) {
        this.databaseOpenHelper = new DatabaseOpenHelper(context);
    }

    public long insertForecast(Forecast forecast) {
        SQLiteDatabase sqLiteDatabase = this.databaseOpenHelper.getWritableDatabase();
        long raw = sqLiteDatabase.insertWithOnConflict(Forecast.Columns.TABLE_NAME, null,
                forecast.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
        sqLiteDatabase.close();
        return raw;
    }

    public long updateForecast(Forecast forecast) {
        SQLiteDatabase sqLiteDatabase = this.databaseOpenHelper.getWritableDatabase();
        long raw = sqLiteDatabase.update(
                Forecast.Columns.TABLE_NAME,
                forecast.getContentValues(),
                Forecast.Columns._ID + " = ? ",
                new String[]{forecast.getCityName()}
        );
        sqLiteDatabase.close();
        return raw;
    }

    public Forecast selectForecast(String cityName) {
        Forecast result = null;
        SQLiteDatabase sqLiteDatabase = this.databaseOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                Forecast.Columns.TABLE_NAME,
                null,
                Forecast.Columns._ID + " = ? ",
                new String[]{cityName},
                null, null, null);
        if (cursor.moveToNext()) {
            result = new Forecast().fromCursor(cursor);
        }
        cursor.close();
        sqLiteDatabase.close();
        return result;
    }

    public void deleteForecast(Forecast forecast) {
        SQLiteDatabase sqLiteDatabase = this.databaseOpenHelper.getWritableDatabase();
        sqLiteDatabase.delete(
                Forecast.Columns.TABLE_NAME,
                Forecast.Columns._ID + " = ? ",
                new String[]{forecast.getCityName()});
        sqLiteDatabase.close();
    }

    public List<Forecast> selectForecast() {
        List<Forecast> result = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.databaseOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                Forecast.Columns.TABLE_NAME,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            result.add(new Forecast().fromCursor(cursor));
        }
        cursor.close();
        sqLiteDatabase.close();
        return result;
    }

    public long insertImage(Image image) {
        SQLiteDatabase sqLiteDatabase = this.databaseOpenHelper.getWritableDatabase();
        long raw = sqLiteDatabase.insertWithOnConflict(Image.Columns.TABLE_NAME, null,
                image.getContentValues(), SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
        return raw;
    }

    public Image selectImage(String id) {
        Image result = null;
        SQLiteDatabase sqLiteDatabase = this.databaseOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                Image.Columns.TABLE_NAME,
                null,
                Image.Columns._ID + " = ? ",
                new String[]{id},
                null, null, null);
        if (cursor.moveToNext()) {
            result = new Image().fromCursor(cursor);
        }
        cursor.close();
        sqLiteDatabase.close();
        return result;
    }
}

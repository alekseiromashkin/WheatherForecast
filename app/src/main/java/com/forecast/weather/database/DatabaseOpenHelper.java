package com.forecast.weather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.forecast.weather.database.model.Forecast;
import com.forecast.weather.database.model.Image;

/**
 * Created by Aleksei Romashkin
 * on 03/02/16.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "forecast.db";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS ";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Image.sqlCreateQuery());
        db.execSQL(Forecast.sqlCreateQuery());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE + Image.Columns.TABLE_NAME);
        db.execSQL(SQL_DROP_TABLE + Forecast.Columns.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}

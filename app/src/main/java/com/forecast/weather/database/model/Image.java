package com.forecast.weather.database.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by Aleksei Romashkin
 * on 03.02.16.
 */
public class Image {

    private String id;
    private byte[] image;

    public interface Columns extends BaseColumns {
        String TABLE_NAME = "images";
        String COLUMN_IMAGE = "image";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns._ID, this.id);
        contentValues.put(Columns.COLUMN_IMAGE, this.image);
        return contentValues;
    }

    public Image fromCursor(Cursor cursor) {
        this.id = cursor.getString(cursor.getColumnIndexOrThrow(Columns._ID));
        this.image = cursor.getBlob(cursor.getColumnIndexOrThrow(Columns.COLUMN_IMAGE));
        return this;
    }

    public static String sqlCreateQuery() {
        return "CREATE TABLE " + Columns.TABLE_NAME + " (" +
                Columns._ID + " TEXT PRIMARY KEY," +
                Columns.COLUMN_IMAGE + " BLOB" +
                ")";
    }
}

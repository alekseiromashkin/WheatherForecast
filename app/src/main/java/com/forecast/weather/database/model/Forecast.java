package com.forecast.weather.database.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by Aleksei Romashkin
 * on 03.02.16.
 */
public class Forecast {
    private String cityName; // _ID
    private float tempNow;
    private float tempMorning;
    private float tempDay;
    private float tempEve;
    private String imageId;
    private String description;

    public interface Columns extends BaseColumns {
        String TABLE_NAME = "forecasts";
        String COLUMN_TEMP_NOW = "temp_now";
        String COLUMN_TEMP_MORNING = "temp_morning";
        String COLUMN_TEMP_DAY = "temp_day";
        String COLUMN_TEMP_EVE = "temp_eve";
        String COLUMN_IMAGE_ID = "image_id";
        String COLUMN_DESCRIPTION = "description";
    }


    public float getTempNow() {
        return tempNow;
    }

    public void setTempNow(float tempNow) {
        this.tempNow = tempNow;
    }

    public float getTempMorning() {
        return tempMorning;
    }

    public void setTempMorning(float tempMorning) {
        this.tempMorning = tempMorning;
    }

    public float getTempDay() {
        return tempDay;
    }

    public void setTempDay(float tempDay) {
        this.tempDay = tempDay;
    }

    public float getTempEve() {
        return tempEve;
    }

    public void setTempEve(float tempEve) {
        this.tempEve = tempEve;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns._ID, this.cityName);
        contentValues.put(Columns.COLUMN_TEMP_NOW, this.tempNow);
        contentValues.put(Columns.COLUMN_TEMP_MORNING, this.tempMorning);
        contentValues.put(Columns.COLUMN_TEMP_DAY, this.tempDay);
        contentValues.put(Columns.COLUMN_TEMP_EVE, this.tempEve);
        contentValues.put(Columns.COLUMN_IMAGE_ID, this.imageId);
        contentValues.put(Columns.COLUMN_DESCRIPTION, this.description);
        return contentValues;
    }

    public Forecast fromCursor(Cursor cursor) {
        this.cityName = cursor.getString(cursor.getColumnIndexOrThrow(Columns._ID));
        this.tempNow = cursor.getFloat(cursor.getColumnIndexOrThrow(Columns.COLUMN_TEMP_NOW));
        this.tempMorning = cursor.getFloat(cursor.getColumnIndexOrThrow(Columns.COLUMN_TEMP_MORNING));
        this.tempDay = cursor.getFloat(cursor.getColumnIndexOrThrow(Columns.COLUMN_TEMP_DAY));
        this.tempEve = cursor.getFloat(cursor.getColumnIndexOrThrow(Columns.COLUMN_TEMP_EVE));
        this.imageId = cursor.getString(cursor.getColumnIndexOrThrow(Columns.COLUMN_IMAGE_ID));
        this.description = cursor.getString(cursor.getColumnIndexOrThrow(Columns.COLUMN_DESCRIPTION));
        return this;
    }

    public static String sqlCreateQuery() {
        return "CREATE TABLE " + Columns.TABLE_NAME + " (" +
                Columns._ID + " TEXT PRIMARY KEY," +
                Columns.COLUMN_TEMP_NOW + " REAL," +
                Columns.COLUMN_TEMP_MORNING + " REAL," +
                Columns.COLUMN_TEMP_DAY + " REAL," +
                Columns.COLUMN_TEMP_EVE + " REAL," +
                Columns.COLUMN_IMAGE_ID + " TEXT," +
                Columns.COLUMN_DESCRIPTION + " TEXT" +
                ")";
    }
}

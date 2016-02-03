package com.forecast.weather.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.forecast.weather.R;
import com.forecast.weather.database.DatabaseController;
import com.forecast.weather.database.model.Image;
import com.forecast.weather.http.Client;

import java.io.ByteArrayOutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Aleksei Romashkin
 * on 03/02/16.
 */
public class WeatherUtil {

    public static final String IMAGE_PNG = ".png";

    public static void setIcon(Context context, final ImageView imageView, final String icon) {
        if (icon == null) {
            return;
        }
        final DatabaseController databaseController = new DatabaseController(context);
        Image image = databaseController.selectImage(icon);
        if (image == null) {
            Call<ResponseBody> request = Client.getService().icon(icon + IMAGE_PNG);
            request.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response) {
                    final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    imageView.setImageBitmap(bitmap);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Image newImage = new Image();
                            newImage.setId(icon);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            newImage.setImage(byteArray);
                            databaseController.insertImage(newImage);
                        }
                    }).run();
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image.getImage(), 0, image.getImage().length);
            imageView.setImageBitmap(bitmap);
        }
    }

    public static void setWeather(Context context, TextView textView, float temp) {
        int val = Math.round(temp);
        textView.setText(String.format(context.getString(R.string.temp), val));
    }
}

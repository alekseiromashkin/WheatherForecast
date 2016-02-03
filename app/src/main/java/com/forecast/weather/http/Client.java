package com.forecast.weather.http;

import com.forecast.weather.http.response.CurrentWeather;
import com.forecast.weather.http.response.DailyForecast;

import java.io.IOException;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Aleksei Romashkin
 * on 03/02/16.
 */
public class Client {

    public static final String APPID = "4e8ea7f7026d8cdd94c69529c816b07b";
    public static final String API_URL = "http://api.openweathermap.org/";
    public static final String BASE_URL = "http://openweathermap.org/";
    public static final String QUERY_ID = "id";
    public static final String QUERY_LAT = "lat";
    public static final String QUERY_LON = "lon";
    public static final String QUERY_CITY = "q";
    public static final String QUERY_UNITS = "units";
    public static final String QUERY_IMAGE = "image";
    public static final String QUERY_APPID = "appid";
    public static final String QUERY_CNT = "cnt";
    public static final String QUERY_LANG = "lang";
    public static final String UNITS_METRIC = "metric";

    public interface API {

        @GET("data/2.5/forecast/daily")
        Call<DailyForecast> dailyForecast(
                @Query(QUERY_CITY) String city,
                @Query(QUERY_CNT) int cnt);

        @GET("data/2.5/weather")
        Call<CurrentWeather> currentWeather(
                @Query(QUERY_CITY) String city);

        @GET("data/2.5/weather")
        Call<CurrentWeather> currentWeather(
                @Query(QUERY_LAT) double lat,
                @Query(QUERY_LON) double lon);

        @GET("img/w/{image}")
        Call<ResponseBody> icon(
                @Path(QUERY_IMAGE) String image);
    }

    public static API getApiService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url();
                url = url.newBuilder()
                        .addQueryParameter(QUERY_APPID, APPID)
                        .addQueryParameter(QUERY_UNITS, UNITS_METRIC)
                        .addQueryParameter(QUERY_LANG, Locale.getDefault().getLanguage()).build();
                request = request.newBuilder().url(url).build();
                Response response = chain.proceed(request);
                return response;
            }
        }).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(API.class);
    }

    public static API getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(API.class);
    }
}

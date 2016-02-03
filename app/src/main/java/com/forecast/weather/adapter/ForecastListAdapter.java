package com.forecast.weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.forecast.weather.R;
import com.forecast.weather.database.DatabaseController;
import com.forecast.weather.database.model.Forecast;
import com.forecast.weather.http.Client;
import com.forecast.weather.http.model.Day;
import com.forecast.weather.http.response.CurrentWeather;
import com.forecast.weather.http.response.DailyForecast;
import com.forecast.weather.util.WeatherUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Aleksei Romashkin
 * on 03/02/16.
 */
public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ViewHolder> {

    private Context mContext;
    private List<Forecast> mItems;

    public ForecastListAdapter(Context context, List<Forecast> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public ForecastListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_forecast, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Forecast forecast = this.mItems.get(position);
        final DatabaseController databaseController = new DatabaseController(this.mContext);
        Call<DailyForecast> request = Client.getApiService().dailyForecast(forecast.getCityName(), 1);
        request.enqueue(new Callback<DailyForecast>() {
            @Override
            public void onResponse(Response<DailyForecast> response) {
                if (holder != null && response.body().list.size() > 0) {
                    Day day = response.body().list.get(0);
                    if (day != null && day.temp != null) {
                        WeatherUtil.setWeather(mContext, holder.txtMorningTemp, day.temp.morn);
                        WeatherUtil.setWeather(mContext, holder.txtMiddayTemp, day.temp.day);
                        WeatherUtil.setWeather(mContext, holder.txtEveningTemp, day.temp.eve);
                        forecast.setTempMorning(day.temp.morn);
                        forecast.setTempDay(day.temp.day);
                        forecast.setTempEve(day.temp.eve);
                        databaseController.updateForecast(forecast);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                WeatherUtil.setWeather(mContext, holder.txtMorningTemp, forecast.getTempMorning());
                WeatherUtil.setWeather(mContext, holder.txtMiddayTemp, forecast.getTempDay());
                WeatherUtil.setWeather(mContext, holder.txtEveningTemp, forecast.getTempEve());
            }
        });

        Call<CurrentWeather> currentWeatherCall = Client.getApiService().currentWeather(forecast.getCityName());
        currentWeatherCall.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Response<CurrentWeather> response) {
                CurrentWeather currentWeather = response.body();
                WeatherUtil.setWeather(mContext, holder.txtNowTemp, currentWeather.main.temp);
                String icon = currentWeather.weather.get(0).icon;
                String description = currentWeather.weather.get(0).description;
                WeatherUtil.setIcon(mContext, holder.imgNowIcon, icon);
                holder.txtLocation.setText(currentWeather.name);
                holder.txtDescription.setText(description);
                forecast.setDescription(description);
                forecast.setTempNow(currentWeather.main.temp);
                forecast.setImageId(icon);
                databaseController.updateForecast(forecast);
            }

            @Override
            public void onFailure(Throwable t) {
                WeatherUtil.setIcon(mContext, holder.imgNowIcon, forecast.getImageId());
                holder.txtLocation.setText(forecast.getCityName());
                holder.txtDescription.setText(forecast.getDescription());
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void delete(int position) {
        Forecast forecast = this.mItems.get(position);
        DatabaseController databaseController = new DatabaseController(this.mContext);
        databaseController.deleteForecast(forecast);
        this.mItems.remove(position);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNowTemp;
        public TextView txtLocation;
        public TextView txtDescription;
        public TextView txtMorningTemp;
        public TextView txtMiddayTemp;
        public TextView txtEveningTemp;
        public ImageView imgNowIcon;
        public ImageButton buttonDelete;

        public ViewHolder(View v) {
            super(v);
            txtNowTemp = (TextView) v.findViewById(R.id.txt_now_temp);
            txtLocation = (TextView) v.findViewById(R.id.txt_location);
            txtDescription = (TextView) v.findViewById(R.id.txt_description);
            txtMorningTemp = (TextView) v.findViewById(R.id.txt_morning_temp);
            txtMiddayTemp = (TextView) v.findViewById(R.id.txt_day_temp);
            txtEveningTemp = (TextView) v.findViewById(R.id.txt_evening_temp);
            imgNowIcon = (ImageView) v.findViewById(R.id.img_now_icon);
            buttonDelete = (ImageButton) v.findViewById(R.id.button_delete);
        }
    }
}

package com.forecast.weather.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.forecast.weather.R;
import com.forecast.weather.adapter.ForecastListAdapter;
import com.forecast.weather.database.DatabaseController;
import com.forecast.weather.database.model.Forecast;
import com.forecast.weather.decorator.SpacesItemDecoration;
import com.forecast.weather.http.Client;
import com.forecast.weather.http.response.CurrentWeather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String TAG = "fragment.mainactivity";
    final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private RecyclerView listForecast;
    private LocationManager locationManager;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            addLocationForecast(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (CONNECTIVITY_CHANGE_ACTION.equals(action)) {
                draw();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.draw();
        this.showCurrentLocationWeather();
        IntentFilter filter = new IntentFilter(CONNECTIVITY_CHANGE_ACTION);
        this.getActivity().registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(locationListener);
        }
        this.getActivity().unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        this.listForecast = (RecyclerView) v.findViewById(R.id.list_forecast);
        this.listForecast.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        this.listForecast.addItemDecoration(new SpacesItemDecoration());

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.button_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCity();
            }
        });
    }

    private void draw() {
        DatabaseController databaseController = new DatabaseController(this.getActivity());
        this.listForecast.setAdapter(new ForecastListAdapter(this.getActivity(), databaseController.selectForecast()));
    }

    private void addCity() {
        @SuppressLint("InflateParams") View promptsView
                = LayoutInflater.from(this.getActivity()).inflate(R.layout.layout_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.city_name);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseController databaseController = new DatabaseController(getActivity());
                                Forecast forecast = new Forecast();
                                forecast.setCityName(userInput.getText().toString());
                                databaseController.insertForecast(forecast);
                                draw();
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }
                ).create().show();
    }

    private void showCurrentLocationWeather() {
        this.locationManager = (LocationManager) this.getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setSpeedRequired(false);
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.locationManager.requestSingleUpdate(criteria, locationListener, null);
        }
    }

    private void addLocationForecast(Location location) {
        Call<CurrentWeather> response = Client.getApiService().currentWeather(location.getLatitude(), location.getLongitude());
        response.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Response<CurrentWeather> response) {
                Context context = getActivity();
                CurrentWeather currentWeather = response.body();
                if (context != null && currentWeather != null) {
                    DatabaseController databaseController = new DatabaseController(context);
                    Forecast forecast = new Forecast();
                    forecast.setCityName(currentWeather.name);
                    forecast.setDescription(currentWeather.weather.get(0).description);
                    forecast.setTempNow(currentWeather.main.temp);
                    forecast.setImageId(currentWeather.weather.get(0).icon);
                    databaseController.insertForecast(forecast);
                    draw();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}

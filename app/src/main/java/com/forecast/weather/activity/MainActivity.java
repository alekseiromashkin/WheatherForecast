package com.forecast.weather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.forecast.weather.R;
import com.forecast.weather.database.DatabaseController;
import com.forecast.weather.database.model.Forecast;
import com.forecast.weather.fragment.MainActivityFragment;
import com.forecast.weather.preferences.Preferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Preferences preferences = new Preferences(this);
        if (!preferences.isAlreadyStarted()) {
            DatabaseController databaseController = new DatabaseController(this);

            Forecast london = new Forecast();
            london.setCityName("London");
            databaseController.insertForecast(london);

            Forecast tokyo = new Forecast();
            tokyo.setCityName("Tokyo");
            databaseController.insertForecast(tokyo);

            Forecast newyork = new Forecast();
            newyork.setCityName("New-York");
            databaseController.insertForecast(newyork);

            preferences.setFirstStart();
        }

        getFragmentManager().beginTransaction().replace(
                R.id.content, new MainActivityFragment(), MainActivityFragment.TAG).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }
}

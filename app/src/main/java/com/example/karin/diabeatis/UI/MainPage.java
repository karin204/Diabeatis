package com.example.karin.diabeatis.UI;

import android.Manifest;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.Person;

import java.util.Calendar;

public class MainPage extends AppCompatActivity implements View.OnClickListener,LocationListener {

    private Button btnHelp;
    private Button btnInj;
    private Button btnFood;
    private Button btnReminders;
    private LocationManager locationManager;
    private Location location;
    private double longitude;
    private double latitude;
    private Intent intent;
    private Person p;
    private final String TAG = MainPage.class.getSimpleName();
    private AlarmManager alarmMgr;

    Handler gpsPosHandler = new Handler();
    Runnable selfPositionThred = new Runnable() {
        @Override
        public void run() {
            if (ActivityCompat.checkSelfPermission(MainPage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainPage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return;
            }
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
                Log.d(TAG, "Location obtained");
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainPage.this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.main_page2);
        btnHelp = (Button)findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(this);
        btnInj = (Button)findViewById(R.id.btn);
        btnInj.setOnClickListener(this);
        btnFood = (Button) findViewById(R.id.btnfood);
        btnFood.setOnClickListener(this);
        btnReminders = (Button) findViewById(R.id.btnReminders);
        btnReminders.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        p = (Person) getIntent().getSerializableExtra("person");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch(id)
        {
            case R.id.btnHelp:
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MapHelpFragment f = new MapHelpFragment();
                if(location != null)
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                else
                {
                    latitude = 0.0;
                    longitude = 0.0;
                }

                Bundle bundle = new Bundle();
                bundle.putInt("ActivityId", R.id.main_page2);
                bundle.putDouble("Latitude",latitude);
                bundle.putDouble("Longitude",longitude);
                bundle.putString("Number",p.getPhone());
                f.setArguments(bundle);
                //fragmentTransaction.replace(R.id.main_page2, f);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fregmentPlace,f);
                fragmentTransaction.commit();
                break;
            }
            case R.id.btn:
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                InsulinCalculate f = new InsulinCalculate();
                Bundle bundle = new Bundle();
                bundle.putSerializable("person", p);
                f.setArguments(bundle);
                //fragmentTransaction.replace(R.id.main_page2, f);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fregmentPlace,f);
                fragmentTransaction.commit();
                break;
            }

            case R.id.btnfood:
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FoodInsertion f = new FoodInsertion();
                Bundle bundle = new Bundle();
                bundle.putSerializable("person", p);
                f.setArguments(bundle);
                //fragmentTransaction.replace(R.id.main_page2, f);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fregmentPlace,f);
                fragmentTransaction.commit();
                break;
            }

            case R.id.btnReminders:
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NotificationsSettings f = new NotificationsSettings();
                Bundle bundle = new Bundle();
                bundle.putSerializable("person", p);
                f.setArguments(bundle);
                fragmentTransaction.replace(R.id.fregmentPlace,f);
                fragmentTransaction.commit();
                break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        else
            gpsPosHandler.postDelayed(selfPositionThred, 0);
    }

    @Override
    public void onLocationChanged(Location location)
    { if (location != null)
        {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

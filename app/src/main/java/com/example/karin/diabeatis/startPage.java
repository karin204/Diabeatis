package com.example.karin.diabeatis;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.MapFragment;

import java.util.Calendar;

public class startPage extends AppCompatActivity implements View.OnClickListener,LocationListener {

    private Button btnHelp;
    private String number = "0528840354";
    private LocationManager locationManager;
    private Location location;
    private long startTime = 0;
    private double longitude;
    private double latitude;
    private final String TAG = startPage.class.getSimpleName();



    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            startTime += 1000;
            timerHandler.postDelayed(this, 1000);
        }
    };


    Handler gpsPosHandler = new Handler();
    Runnable selfPositionThred = new Runnable() {
        @Override
        public void run() {
            if (ActivityCompat.checkSelfPermission(startPage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(startPage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(startPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return;
            }
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
                Log.d(TAG, "Location obtained");
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, startPage.this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHelp = (Button)findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        startTime = 0;
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
                mapHelpFregment f = new mapHelpFregment();
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
                btnHelp.setVisibility(View.INVISIBLE);
                bundle.putInt("ActivityId", R.id.activity_main);
                bundle.putDouble("Latitude",latitude);
                bundle.putDouble("Longitude",longitude);
                bundle.putString("Number",number);
                f.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_main, f);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        else {
            gpsPosHandler.postDelayed(selfPositionThred, 0);
            timerHandler.postDelayed(timerRunnable, 0);
        }


    }

    @Override
    public void onLocationChanged(Location location) { if (location != null) {
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
                        timerHandler.postDelayed(timerRunnable, 0);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



}

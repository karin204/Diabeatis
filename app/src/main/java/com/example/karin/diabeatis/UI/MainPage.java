package com.example.karin.diabeatis.UI;

import android.Manifest;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.ItemSlideMenu;
import com.example.karin.diabeatis.logic.Person;
import com.example.karin.diabeatis.logic.SlidingMenuAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainPage extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private Button btnHelp;
    private LocationManager locationManager;
    private Location location;
    private double longitude;
    private double latitude;
    private Intent intent;
    private Person p;
    private final String TAG = MainPage.class.getSimpleName();
    private AlarmManager alarmMgr;
    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


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
        setContentView(R.layout.main_page2);
        btnHelp = (Button)findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        p = (Person) getIntent().getSerializableExtra("person");
        if(p == null)
            p = buildPerson();

        listViewSliding = (ListView)findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout)findViewById(R.id.main_page2);
        listSliding = new ArrayList<>();
        listSliding.add(new ItemSlideMenu(R.drawable.hist,"עמוד הבית"));
        listSliding.add(new ItemSlideMenu(R.drawable.hist,"הסטוריה"));
        listSliding.add(new ItemSlideMenu(R.drawable.alarmicon,"תזכורות"));
        listSliding.add(new ItemSlideMenu(R.drawable.food,"ארוחות"));
        listSliding.add(new ItemSlideMenu(R.drawable.appicon,"מתן אינסולין"));
        listSliding.add(new ItemSlideMenu(R.drawable.food,"היסטוריה אוכל"));
        listSliding.add(new ItemSlideMenu(R.drawable.detailsicon,"עריכת פרטים"));
        adapter = new SlidingMenuAdapter(this,listSliding);
        listViewSliding.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("תפריט");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        listViewSliding.setItemChecked(0,true);
        drawerLayout.closeDrawer(listViewSliding);

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTitle(listSliding.get(position).getTitle());
                listViewSliding.setItemChecked(position,true);
                replaceFragments(position);
                drawerLayout.closeDrawer(listViewSliding);
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_opened, R.string.drawer_closed){


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        if(getIntent().getStringExtra("from").equals("noti"))
            replaceFragments(3);
        else
            replaceFragments(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
            else {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        }
        return false;
    }



    @Override
    public void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void replaceFragments(int pos)
    {
       switch(pos)
        {
            case 0:
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment f = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("person", p);
                f.setArguments(bundle);
                fragmentTransaction.replace(R.id.fregmentPlace,f);
                fragmentTransaction.commit();
                break;
            }

            case 1:
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                HistoryDisplay f = new HistoryDisplay();
                Bundle bundle = new Bundle();
                bundle.putSerializable("person", p);
                f.setArguments(bundle);
                fragmentTransaction.replace(R.id.fregmentPlace,f);
                fragmentTransaction.commit();
                break;
            }

            case 2:
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
            case 3:
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FoodInsertion f = new FoodInsertion();
                Bundle bundle = new Bundle();
                bundle.putSerializable("person", p);
                f.setArguments(bundle);
                fragmentTransaction.replace(R.id.fregmentPlace,f);
                fragmentTransaction.commit();
                break;
            }

            case 4:
            {
                buildOptionsMessage();
                break;
            }

            case 5:
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FoodHistory f = new FoodHistory();
                Bundle bundle = new Bundle();
                bundle.putSerializable("person", p);
                f.setArguments(bundle);
                fragmentTransaction.replace(R.id.fregmentPlace,f);
                fragmentTransaction.commit();
                break;
            }

            case 6:
            {
                final Intent intent = new Intent(this, StartPage.class);
                intent.putExtra("person",p);
                startActivity(intent);
                finish();
                break;
            }
        }
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

    public Person buildPerson()
    {
        SharedPreferences userPref = getSharedPreferences("user", MODE_PRIVATE);
        Person p = new Person(userPref.getString("name",""), userPref.getString("lName",""), userPref.getInt("age",0), userPref.getFloat("height",0), userPref.getFloat("weight",0),
                userPref.getInt("dbType",2), userPref.getString("phone",""));
        return p;
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
                fragmentTransaction.replace(R.id.main_content,f);
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

    private void buildOptionsMessage() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("באיזה צורה הינך צורך אינסולין?")
                .setCancelable(false)
                .setPositiveButton("משאבה", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ExtractingCalculate f = new ExtractingCalculate();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("person", p);
                        f.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fregmentPlace,f);
                        fragmentTransaction.commit();

                    }
                })
                .setNegativeButton("הזרקה", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        InsulinCalculate f = new InsulinCalculate();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("person", p);
                        f.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fregmentPlace,f);
                        fragmentTransaction.commit();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

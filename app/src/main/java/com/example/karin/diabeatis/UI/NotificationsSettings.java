package com.example.karin.diabeatis.UI;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.InputFilterMinMax;
import com.example.karin.diabeatis.logic.MyBootReceiver;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Avi on 27/03/2017.
 */

public class NotificationsSettings extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener {
    private static final int NUMOFALARMS = 5;
    private Application activity;
    private TableLayout tableLayout;
    private View v;
    private ArrayList<EditText> hours = new ArrayList<>();
    private ArrayList<EditText> minutes = new ArrayList<>();
    private ArrayList<Switch> switches = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.notifications, container, false);
        activity = this.getActivity().getApplication();

        tableLayout = (TableLayout) v.findViewById(R.id.tableLayout5);
        createTable();

        return v;
    }

    public void createTable()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        for(int i = 0; i < NUMOFALARMS; i++)
        {
            TableRow row = new TableRow(v.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            EditText hour = new EditText(v.getContext());
            hour.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            hour.setTextSize(30);
            hour.setText("00");
            hour.setInputType(InputType.TYPE_CLASS_NUMBER);
            hour.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
            hour.setId(i);
            hour.setOnFocusChangeListener(this);
            hour.setFilters(new InputFilter[]{ new InputFilterMinMax("01", "24")});
            hours.add(hour);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(180, 0, 0, 0);
            hour.setLayoutParams(params);

            TextView t = new TextView(v.getContext());
            t.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            t.setTextSize(30);
            t.setText(":");

            EditText minute = new EditText(v.getContext());
            minute.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            minute.setTextSize(30);
            minute.setText("00");
            minute.setInputType(InputType.TYPE_CLASS_NUMBER);
            minute.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
            minute.setId(i);
            minute.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
            minutes.add(minute);

            Switch s = new Switch(v.getContext());
            s.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            s.setLayoutParams(params);
            s.setId(i);
            s.setOnCheckedChangeListener(this);
            switches.add(s);


            row.addView(hour);
            row.addView(t);
            row.addView(minute);
            row.addView(s);

            tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public void enableNoti()
    {
        ComponentName receiver = new ComponentName(v.getContext(), MyBootReceiver.class);
        PackageManager pm = v.getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void disableNoti()
    {
        ComponentName receiver = new ComponentName(v.getContext(), MyBootReceiver.class);
        PackageManager pm = v.getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();

        if(isChecked)
        {
            int hour = Integer.parseInt(hours.get(id).getText().toString());
            int minute = Integer.parseInt(minutes.get(id).getText().toString());
            System.out.println(hour + ":" + minute);
            setNotiTime(hour, minute, id);
        }

        else
        {
            Intent intent = new Intent(v.getContext(), MyBootReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(v.getContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            sender.cancel();
        }
    }

    public void setNotiTime(int hour, int minute, int id)
    {
        AlarmManager alarmMgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(v.getContext(), MyBootReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(v.getContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        int id = v.getId();
        Switch s = switches.get(id);

        if(hasFocus && s.isChecked())
            s.setChecked(false);

        else if(!hasFocus)
        {
            EditText hour = hours.get(id);
            EditText minute = minutes.get(id);

            //if(Integer.parseInt(minute.getText().toString()) < 10)
                //minute.setText("00");
        }
    }
}

package com.example.karin.diabeatis.UI;

import android.app.Application;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.Person;

/**
 * Created by Avi on 02/04/2017.
 */

public class HomeFragment extends Fragment
{
    private Person p;
    private Application activity;
    private View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.home_page, container, false);
        activity = this.getActivity().getApplication();
        p = (Person) getArguments().getSerializable("person");

        TextView t =(TextView) v.findViewById(R.id.name);
        t.setText("שלום " + p.getName());

        t =(TextView) v.findViewById(R.id.insPerDayText);
        t.setText(p.getDailyUnit() + "");

        t =(TextView) v.findViewById(R.id.insLowerText);
        t.setText((1800 / p.getDailyUnit()) + "");

        t =(TextView) v.findViewById(R.id.lastCheckText);
        SharedPreferences userPref = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        String lastCheck = userPref.getString("lastCheck","");
        if(lastCheck.equals(""))
            t.setText("אין");
        else
            t.setText(lastCheck);

        return v;
    }
}

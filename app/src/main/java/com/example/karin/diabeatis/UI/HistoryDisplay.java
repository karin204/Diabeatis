package com.example.karin.diabeatis.UI;

import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karin.diabeatis.R;

public class HistoryDisplay extends Fragment
{
    private Application activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_history_display, container, false);
        activity = this.getActivity().getApplication();

        return v;
    }
}

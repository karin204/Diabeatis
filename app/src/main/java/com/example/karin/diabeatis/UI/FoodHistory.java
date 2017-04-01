package com.example.karin.diabeatis.UI;

import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.Food;
import com.example.karin.diabeatis.logic.FoodDbHandler;

import java.util.ArrayList;

/**
 * Created by Avi on 01/04/2017.
 */

public class FoodHistory extends Fragment
{
    private Application activity;
    private FoodDbHandler foodDbHandler;
    private ListView lv;
    private View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.food_history, container, false);
        activity = this.getActivity().getApplication();
        foodDbHandler = FoodDbHandler.getInstance(v.getContext());
        lv = (ListView) v.findViewById(R.id.lv);
        buildListView();

        return v;
    }

    public void buildListView()
    {
        ArrayList<Food> foods = foodDbHandler.getAllFoods();
        ArrayAdapter<Food> adapter = new ArrayAdapter<Food>(v.getContext(), R.layout.list_detail, foods);
        lv.setAdapter(adapter);

    }
}

package com.example.karin.diabeatis.UI;

import android.app.Application;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.Food;
import com.example.karin.diabeatis.logic.FoodDbHandler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Avi on 25/03/2017.
 */

public class FoodInsertion extends Fragment implements View.OnClickListener {
    private Application activity;
    private TableLayout tableLayout;
    private Button calcBtn;
    private FoodDbHandler foodDbHandler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.food_insertion, container, false);
        activity = this.getActivity().getApplication();
        foodDbHandler = FoodDbHandler.getInstance(v.getContext());
        tableLayout = (TableLayout) v.findViewById(R.id.tableLayout);
        calcBtn = (Button) v.findViewById(R.id.calcBtn);
        calcBtn.setOnClickListener(this);

        for(int i = 0; i < 5; i++)
        {
            TableRow row = new TableRow(v.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            EditText edit1 = new EditText(v.getContext());
            edit1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            EditText edit2 = new EditText(v.getContext());
            edit2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            EditText edit3 = new EditText(v.getContext());
            edit3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            row.addView(edit1);
            row.addView(edit2);
            row.addView(edit3);
            tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        return v;
    }

    public void addFood()
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference conRef = myRef.child("Foods");
        DatabaseReference id1 = conRef.push();
        id1.setValue("something");
    }

    @Override
    public void onClick(View v)
    {
        ArrayList<Food> foods = new ArrayList<>();
        for(int i = 1; i < tableLayout.getChildCount(); i++)
        {
            TableRow row = (TableRow) tableLayout.getChildAt(i);

            EditText qText = (EditText) row.getChildAt(0);
            EditText calText = (EditText) row.getChildAt(1);
            EditText nameText = (EditText) row.getChildAt(2);
            String foodName = nameText.getText().toString();
            String cal = calText.getText().toString();
            String qun = qText.getText().toString();
            if(foodName.isEmpty() || cal.isEmpty() || qun.isEmpty())
                break;
            else
            {
                Food f = newFood(foodName, cal, qun);
                if(f != null)
                    foods.add(f);
            }
        }

        if(foods.size() > 0) {
            for (Food f : foods) {
                foodDbHandler.insertFood(f);
            }

            //show calc
        }
    }

    public Food newFood(String foodName, String calText, String qunText)
    {
        double cal;
        double qun;
        Food f = null;

        if(!tryParseDouble(calText) || !tryParseDouble(qunText))
            buildAlertMessage("קלוריות/כמות חייבות להיות בעלות ערך מספרי");
        else
        {
            cal = Double.parseDouble(calText);
            qun = Double.parseDouble(qunText);
            f = new Food(foodName,cal,qun);
        }

        return f;
    }

    private boolean tryParseDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void buildAlertMessage(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
        builder.setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("חזור", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

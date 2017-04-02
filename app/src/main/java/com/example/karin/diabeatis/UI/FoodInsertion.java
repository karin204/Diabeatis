package com.example.karin.diabeatis.UI;

import android.app.Application;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.Food;
import com.example.karin.diabeatis.logic.FoodDbHandler;
import com.example.karin.diabeatis.logic.Person;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by Avi on 25/03/2017.
 */

public class FoodInsertion extends Fragment implements View.OnClickListener {
    private Application activity;
    private TableLayout tableLayout;
    private Button calcBtn;
    private FoodDbHandler foodDbHandler;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ArrayList<Food> foodsInFireBase;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> foodsForAdapter;
    private Person p;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.food_insertion, container, false);
        activity = this.getActivity().getApplication();
        p = (Person) getArguments().getSerializable("person");
        foodDbHandler = FoodDbHandler.getInstance(v.getContext());
        tableLayout = (TableLayout) v.findViewById(R.id.tableLayout);
        calcBtn = (Button) v.findViewById(R.id.calcBtn);
        calcBtn.setOnClickListener(this);
        foodsInFireBase = new ArrayList<>();
        foodsForAdapter = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.list_detail, foodsForAdapter);
        initilizeFireBase();

        for(int i = 0; i < 5; i++)
        {
            TableRow row = new TableRow(v.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            final EditText edit1 = new EditText(v.getContext());
            edit1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            edit1.setFilters(new InputFilter[] {new InputFilter.LengthFilter(5)});
            edit1.setInputType(InputType.TYPE_CLASS_NUMBER);
            final EditText edit2 = new EditText(v.getContext());
            edit2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            edit2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(5)});
            edit2.setInputType(InputType.TYPE_CLASS_NUMBER);
            final AutoCompleteTextView edit3 = new AutoCompleteTextView(v.getContext());
            edit3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            edit3.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
            edit3.setFilters(new InputFilter[] {new InputFilter.LengthFilter(12)});
            edit3.setThreshold(1);
            edit3.setAdapter(adapter);
            edit3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView t = (TextView) view;
                    String fullName = (String) t.getText();
                    for (Food f: foodsInFireBase) {
                        if(f.toString().equals(fullName))
                        {
                            edit1.setText(f.getFoodQuantity() + "");
                            edit2.setText(f.getFoodCarbs() + "");
                            edit3.setText(f.getFoodName());
                            break;
                        }
                    }
                }
            });
            row.addView(edit3);
            row.addView(edit2);
            row.addView(edit1);

            tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
        return v;
    }

    public void initilizeFireBase()
    {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Foods");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Food newFood = dataSnapshot.getValue(Food.class);
                foodsInFireBase.add(newFood);
                foodsForAdapter.add(newFood.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Food food = dataSnapshot.getValue(Food.class);
                foodsInFireBase.remove(food.getFoodName());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addFoodToFireBase(Food food)
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference conRef = myRef.child("Foods");
        DatabaseReference id = conRef.push();
        id.setValue(food);
    }

    @Override
    public void onClick(View v)
    {
        ArrayList<Food> foods = new ArrayList<>();
        for(int i = 1; i < tableLayout.getChildCount(); i++)
        {
            TableRow row = (TableRow) tableLayout.getChildAt(i);

            EditText qText = (EditText) row.getChildAt(2);
            EditText calText = (EditText) row.getChildAt(1);
            EditText nameText = (EditText) row.getChildAt(0);
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

        if(foods.size() > 0)
        {

            for (Food f : foods) {
                foodDbHandler.insertFood(f);
            }
            buildSubmitMessage(getTotalCarbs(foods));
        }
    }

    public double getTotalCarbs(ArrayList<Food> foods)
    {
        double amount = 0;
        for (Food f: foods) {
            amount += f.getFoodCarbs();
        }

        double temp = 500/p.getDailyUnit();

        return round(amount/temp,1);
    }
    public Food newFood(String foodName, String calText, String qunText)
    {
        double cal;
        double qun;
        Food f = null;

        if(!tryParseDouble(calText) || !tryParseDouble(qunText))
            buildAlertMessage("פחמימות/כמות חייבות להיות בעלות ערך מספרי");
        else
        {
            cal = Double.parseDouble(calText);
            qun = Double.parseDouble(qunText);
            f = new Food(foodName,cal,qun);
            if(!foodInFireBase(f))
                addFoodToFireBase(f);
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

    private boolean foodInFireBase(Food f)
    {
        for (Food f1: foodsInFireBase) {
            if(f1.getFoodName().equals(f.getFoodName()) && f1.getFoodCarbs() == f.getFoodCarbs() && f1.getFoodQuantity() == f.getFoodQuantity())
                return true;
        }
        return false;
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

    private void buildSubmitMessage(double amount) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
        builder.setMessage("עליך להזריק " + amount + " יחידות אינסולין")
                .setCancelable(false)
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

package com.example.karin.diabeatis.UI;

import android.app.Application;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.InputFilterMinMax;
import com.example.karin.diabeatis.logic.Person;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExtractingCalculate extends Fragment implements View.OnClickListener, View.OnFocusChangeListener{

    private Application activity;
    private  View v;
    private Person p;
    private TableLayout tableLayout;
    private TextView txtName;
    private TextView result;
    private TextView ans;
    private Button calc,del;
    private ArrayList<EditText> hoursFrom = new ArrayList<>();
    private ArrayList<EditText> hoursTo = new ArrayList<>();
    private ArrayList<EditText> minutesFrom = new ArrayList<>();
    private ArrayList<EditText> minutesTo = new ArrayList<>();
    private ArrayList<EditText> units  = new ArrayList<>();



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_extracting_calculate, container, false);
        activity = this.getActivity().getApplication();
        p = (Person) getArguments().getSerializable("person");
        result = (TextView)v.findViewById(R.id.txtFinalE);
        txtName = (TextView)v.findViewById(R.id.txtHello);
        txtName.setText("שלום " + p.getName());
        ans = (TextView)v.findViewById(R.id.txtAns);
        calc = (Button)v.findViewById(R.id.btnCalc);
        del = (Button)v.findViewById(R.id.btnDelete);
        calc.setOnClickListener(this);
        del.setOnClickListener(this);
        tableLayout = (TableLayout) v.findViewById(R.id.top);
        createTable();

        return v;
    }

        @Override
    public void onClick(View v) {
            int id = v.getId();
            switch (id)
            {
                case R.id.btnDelete:
                {
                    for(int i=0;i<hoursTo.size();i++) {
                        hoursTo.get(i).setText("08");
                        hoursFrom.get(i).setText("08");
                        minutesTo.get(i).setText("00");
                        minutesFrom.get(i).setText("00");
                    }
                    break;
                }
                case R.id.btnCalc:
                {
                    calculateDailyExtraction();
                    break;
                }
            }
    }

    public void createTable()
    {
        DisplayMetrics displayMatrix = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay();

        for(int i=0; i<5; i++)
        {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 80, 0);
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0, 0, 115, 0);
            TableRow.LayoutParams params3 = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
            params3.setMargins(0, 0, 160, 0);
            TableRow row = new TableRow(v.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            EditText hourFrom = new EditText(v.getContext());
            hourFrom.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            hourFrom.setTextSize(20);
            hourFrom.setText("08");
            hourFrom.setInputType(InputType.TYPE_CLASS_NUMBER);
            hourFrom.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
            hourFrom.setId(i);
            hourFrom.setOnFocusChangeListener(this);
            hourFrom.setFilters(new InputFilter[]{ new InputFilterMinMax("01", "24")});
            hourFrom.setSelectAllOnFocus(true);
            hoursFrom.add(hourFrom);
            TextView t = new TextView(v.getContext());
            t.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            t.setTextSize(20);
            t.setText(":");

            EditText minuteFrom = new EditText(v.getContext());
            minuteFrom.setLayoutParams(params);
            minuteFrom.setTextSize(20);
            minuteFrom.setText("00");
            minuteFrom.setInputType(InputType.TYPE_CLASS_NUMBER);
            minuteFrom.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
            minuteFrom.setId(i);
            minuteFrom.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
            minuteFrom.setSelectAllOnFocus(true);
            minuteFrom.setOnFocusChangeListener(this);
            minutesFrom.add(minuteFrom);

            EditText hourTo = new EditText(v.getContext());
            hourTo.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            hourTo.setTextSize(20);
            hourTo.setText("08");
            hourTo.setInputType(InputType.TYPE_CLASS_NUMBER);
            hourTo.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
            hourTo.setId(i);
            hourTo.setOnFocusChangeListener(this);
            hourTo.setFilters(new InputFilter[]{ new InputFilterMinMax("01", "24")});
            hourTo.setSelectAllOnFocus(true);
            hoursTo.add(hourFrom);
            TextView t2 = new TextView(v.getContext());
            t2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            t2.setTextSize(20);
            t2.setText(":");

            EditText minuteTo = new EditText(v.getContext());
            minuteTo.setLayoutParams(params2);
            minuteTo.setTextSize(20);
            minuteTo.setText("00");
            minuteTo.setInputType(InputType.TYPE_CLASS_NUMBER);
            minuteTo.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
            minuteTo.setId(i);
            minuteTo.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
            minuteTo.setSelectAllOnFocus(true);
            minuteTo.setOnFocusChangeListener(this);
            minutesTo.add(minuteTo);

            EditText unit = new EditText(v.getContext());
            unit.setLayoutParams(params3);
            unit.setTextSize(20);
            unit.setText("00");
            unit.setInputType(InputType.TYPE_CLASS_NUMBER);
            unit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
            unit.setId(i);
            unit.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
            unit.setSelectAllOnFocus(true);
            unit.setOnFocusChangeListener(this);
            units.add(unit);

            row.addView(minuteFrom);
            row.addView(t);
            row.addView(hourFrom);

            row.addView(minuteTo);
            row.addView(t2);
            row.addView(hourTo);
            row.addView(unit);
            tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }


    public void calculateDailyExtraction()
    {
        double finalRes = 0;
        String[] allStartingTimes = {hoursFrom.get(0).toString().concat(minutesFrom.get(0).toString())
                ,hoursFrom.get(1).toString().concat(minutesFrom.get(1).toString()),
                hoursFrom.get(2).toString().concat(minutesFrom.get(2).toString()),
                hoursFrom.get(3).toString().concat(minutesFrom.get(3).toString()),
                hoursFrom.get(4).toString().concat(minutesFrom.get(4).toString())};
        String[] allEndingTimes = {hoursTo.get(0).toString().concat(minutesTo.get(0).toString()),
                hoursTo.get(1).toString().concat(minutesTo.get(1).toString()),
                hoursTo.get(2).toString().concat(minutesTo.get(2).toString()),
                hoursTo.get(3).toString().concat(minutesTo.get(3).toString()),
                hoursTo.get(4).toString().concat(minutesTo.get(4).toString()),};
        String[] allResults = {units.get(0).toString(),units.get(1).toString(),
                units.get(2).toString(),units.get(3).toString(),units.get(4).toString()};

        for(int i= 0; i<allEndingTimes.length; i++) {
            if (!allStartingTimes[i].equals("") &&
                    !allEndingTimes[i].equals("") &&
                            !allResults[i].equals("")) ;
            {
                DateFormat sdf = new SimpleDateFormat("hh:mm");
                try {
                    long tEnd = sdf.parse(allEndingTimes[i]).getTime();
                    long tStart = sdf.parse(allStartingTimes[i]).getTime();
                    finalRes += (tEnd - tStart) * Long.parseLong(allResults[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        finalRes+=0.05;
        if(p.getDailyUnit()<finalRes)
        { ans.setText("מינון יומי בטווח הנורמה");
            ans.setTextColor(Color.GREEN);}
        else {
            ans.setText("שים לב! מינון יומי מתחת לטווח הנורמה");
            ans.setTextColor(Color.RED);
        }

        result.setText(String.valueOf(finalRes));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }
}

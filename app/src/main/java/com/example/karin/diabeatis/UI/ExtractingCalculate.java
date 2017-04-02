package com.example.karin.diabeatis.UI;

import android.app.Application;
import android.app.Fragment;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.Person;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtractingCalculate extends Fragment implements View.OnClickListener{

    private Application activity;
    private  View v;
    private Person p;
    private EditText fromHour1,toHour1;
    private EditText fromHour2,toHour2;
    private EditText fromHour3,toHour3;
    private EditText fromHour4,toHour4;
    private EditText fromHour5,toHour5;
    private EditText res1,res2,res3,res4,res5;
    private TextView txtName;
    private TextView result;
    private TextView ans;
    private Button calc,del;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_extracting_calculate, container, false);
        activity = this.getActivity().getApplication();
        p = (Person) getArguments().getSerializable("person");
        fromHour1 = (EditText)v.findViewById(R.id.hourFrom1);
        fromHour2 = (EditText)v.findViewById(R.id.hourFrom2);
        fromHour3 = (EditText)v.findViewById(R.id.hourFrom3);
        fromHour4 = (EditText)v.findViewById(R.id.hourFrom4);
        fromHour5 = (EditText)v.findViewById(R.id.hourFrom5);
        toHour1 = (EditText)v.findViewById(R.id.hourTo1);
        toHour2 = (EditText)v.findViewById(R.id.hourTo2);
        toHour3 = (EditText)v.findViewById(R.id.hourTo3);
        toHour4 = (EditText)v.findViewById(R.id.hourTo4);
        toHour5 = (EditText)v.findViewById(R.id.hourTo5);
        res1 = (EditText)v.findViewById(R.id.result1);
        res2 = (EditText)v.findViewById(R.id.result2);
        res3 = (EditText)v.findViewById(R.id.result3);
        res4 = (EditText)v.findViewById(R.id.result4);
        res5 = (EditText)v.findViewById(R.id.result5);
        result = (TextView)v.findViewById(R.id.txtFinalE);
        txtName = (TextView)v.findViewById(R.id.txtNameE);
        txtName.setText(p.getName());
        ans = (TextView)v.findViewById(R.id.txtAns);
        calc = (Button)v.findViewById(R.id.btnCalc);
        del = (Button)v.findViewById(R.id.btnDelete);
        calc.setOnClickListener(this);
        del.setOnClickListener(this);

        return v;
    }

        @Override
    public void onClick(View v) {
            int id = v.getId();
            switch (id)
            {
                case R.id.btnDelete:
                {
                    fromHour1.setText("");
                    fromHour2.setText("");
                    fromHour3.setText("");
                    fromHour4.setText("");
                    fromHour5.setText("");
                    toHour1.setText("");
                    toHour2.setText("");
                    toHour3.setText("");
                    toHour4.setText("");
                    toHour5.setText("");
                    res1.setText("");
                    res2.setText("");
                    res3.setText("");
                    res4.setText("");
                    res5.setText("");
                    result.setText("");
                    break;
                }
                case R.id.btnCalc:
                {
                    calculateDailyExtraction();
                    break;
                }
            }
    }

    public void calculateDailyExtraction()
    {
        double finalRes = 0;
        EditText[] allStartingTimes = {fromHour1,fromHour2,fromHour3,fromHour4,fromHour4};
        EditText[] allEndingTimes = {toHour1,toHour2,toHour3,toHour4,toHour5};
        EditText[] allResults = {res1,res2,res3,res4,res5};

        for(int i= 0; i<allEndingTimes.length; i++) {
            if (!String.valueOf(allStartingTimes[i].getText()).equals("") &&
                    !String.valueOf(allEndingTimes[i].getText()).equals("") &&
                            !String.valueOf(allResults[i].getText()).equals("")) ;
            {
                DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                try {
                    long tEnd = sdf.parse(String.valueOf(allEndingTimes[i].getText())).getTime();
                    long tStart = sdf.parse(String.valueOf(allStartingTimes[i].getText())).getTime();
                    finalRes += (tEnd - tStart) * Long.parseLong(String.valueOf(res1.getText()));
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
}

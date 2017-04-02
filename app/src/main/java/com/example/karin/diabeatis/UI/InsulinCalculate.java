package com.example.karin.diabeatis.UI;

import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.Person;

public class InsulinCalculate extends Fragment implements View.OnClickListener{

    private EditText res1,res2,res3,res4,res5;
    private int unit1,unit2,unit3,unit4,unit5;
    private TextView finalResult,name;
    private double result = 0;
    private Button btnCalc,btnDelete;
    private TextView answer;
    private Person p;
    private Application activity;
    private  View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.activity_insulin_calculate, container, false);
        activity = this.getActivity().getApplication();
        res1 = (EditText)v.findViewById(R.id.res1);
        res2 = (EditText)v.findViewById(R.id.res2);
        res3 = (EditText)v.findViewById(R.id.res3);
        res4 = (EditText)v.findViewById(R.id.res4);
        res5 = (EditText)v.findViewById(R.id.res5);
        p = (Person) getArguments().getSerializable("person");
        name = (TextView)v.findViewById(R.id.txtNameE);
        name.setText(p.getName());
        btnCalc = (Button)v.findViewById(R.id.btnOk);
        btnCalc.setOnClickListener(this);
        btnDelete = (Button)v.findViewById(R.id.btnDel);
        btnDelete.setOnClickListener(this);
        answer = (TextView)v.findViewById(R.id.txtAnswer);

        return v;
    }

    public void calculateDailyInjection()
    {
        String content = String.valueOf(res1.getText());
        if(!content.equals(""))
            unit1 = Integer.parseInt(content);
        else
            unit1 = 0;
        content = String.valueOf(res2.getText());
        if(!content.equals(""))
            unit2 = Integer.parseInt(content);
        else
            unit2 = 0;
        content = String.valueOf(res3.getText());
        if(!content.equals(""))
            unit3 = Integer.parseInt(content);
        else
            unit3 = 0;
        content = String.valueOf(res4.getText());
        if(!content.equals(""))
            unit4 = Integer.parseInt(content);
        else
            unit4 = 0;
        content = String.valueOf(res5.getText());
        if(!content.equals(""))
            unit5 = Integer.parseInt(content);
        else
            unit5 = 0;

        result = (unit1+unit2+unit3+unit4+unit5)*0.4;
        finalResult = (TextView)v.findViewById(R.id.txtFinal);
        finalResult.setText(String.valueOf(result));
        if(p.getDailyUnit()<result)
        { answer.setText("מינון יומי בטווח הנורמה");
            answer.setTextColor(Color.GREEN);}
        else {
            answer.setText("שים לב! מינון יומי מתחת לטווח הנורמה");
            answer.setTextColor(Color.RED);
    }
        p.setTotalCheck(p.getTotalCheck()+1);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.btnOk: {
                calculateDailyInjection();
                break;
            }
            case R.id.btnDel: {
                res1.setText("");
                res2.setText("");
                res3.setText("");
                res4.setText("");
                res5.setText("");
                finalResult.setText("");
                answer.setText("");
                break;
            }
        }
    }
}

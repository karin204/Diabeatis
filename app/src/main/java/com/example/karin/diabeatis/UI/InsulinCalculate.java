package com.example.karin.diabeatis.UI;

import android.app.Application;
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
        name = (TextView)v.findViewById(R.id.txtName);
        name.setText(p.getName());
        btnCalc = (Button)v.findViewById(R.id.btnOk);
        btnCalc.setOnClickListener(this);
        btnDelete = (Button)v.findViewById(R.id.btnDel);
        btnDelete.setOnClickListener(this);

        return v;
    }

    public void calculateDailyInjection()
    {
        unit1 = Integer.parseInt(String.valueOf(res1.getText()));
        unit2 = Integer.parseInt(String.valueOf(res2.getText()));
        unit3 = Integer.parseInt(String.valueOf(res3.getText()));
        unit4 = Integer.parseInt(String.valueOf(res4.getText()));
        unit5 = Integer.parseInt(String.valueOf(res5.getText()));

        result = (unit1+unit2+unit3+unit4+unit5)*0.4;
        finalResult = (TextView)v.findViewById(R.id.txtFinal);
        finalResult.setText(String.valueOf(result));
        p.setTotalCheck(p.getTotalCheck()+1);
        //SharedPreferences.Editor scoresEditor = getSharedPreferences("injections", MODE_PRIVATE).edit();
        //scoresEditor.putString(p.getName(), String.valueOf(result));
        //need to save multiple results not only one


    }

/*
    public void addTask(Task t) {
    if (null == currentTasks) {
    currentTasks = new ArrayList<task>();
  }
  currentTasks.add(t);

  // save the task list to preference
  SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
  Editor editor = prefs.edit();
  try {
    editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));
  } catch (IOException e) {
    e.printStackTrace();
  }
  editor.commit();
}

*/

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
                break;
            }
        }
    }
}

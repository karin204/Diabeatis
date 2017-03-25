package com.example.karin.diabeatis.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.Person;

/**
 * Created by Avi on 25/03/2017.
 */

public class StartPage extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = StartPage.class.getSimpleName();
    private EditText fNameText;
    private EditText lNameText;
    private EditText ageTextField;
    private EditText weightTextField;
    private EditText heightTextField;
    private RadioGroup radioGroupField;
    private EditText phoneText;
    private Button sendBtn;
    private String [] errorMsg = {"השדות חייבים להיות מלאים", "מספר טלפון לא תקין", "גיל/גובה/משקל יכול להכיל רק מספרים"};
    private SharedPreferences.Editor userEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        SharedPreferences userPref = getSharedPreferences("user", MODE_PRIVATE);
        if(userPref.contains("initialize"))
        {
            Person p = new Person(userPref.getString("name",""), userPref.getInt("age",0), userPref.getFloat("height",0), userPref.getFloat("weight",0),
                    userPref.getInt("dbType",2), userPref.getString("phone",""));

            final Intent intent = new Intent(this, MainPage.class);
            intent.putExtra("person",p);
            startActivity(intent);
            finish();
        }

        else {
            userEditor = getSharedPreferences("user", MODE_PRIVATE).edit();
            fNameText = (EditText) findViewById(R.id.fName);
            lNameText = (EditText) findViewById(R.id.lName);
            ageTextField = (EditText) findViewById(R.id.age);
            weightTextField = (EditText) findViewById(R.id.weight);
            heightTextField = (EditText) findViewById(R.id.height);
            radioGroupField = (RadioGroup) findViewById(R.id.radioGroup);
            phoneText = (EditText) findViewById(R.id.phone);
            sendBtn = (Button) findViewById(R.id.sendBtn);
            sendBtn.setOnClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v)
    {
        String fName = fNameText.getText().toString().trim();
        String lName = lNameText.getText().toString().trim();
        String ageText = ageTextField.getText().toString().trim();
        int age;
        String weightText = weightTextField.getText().toString().trim();
        double weight;
        String heightText = heightTextField.getText().toString().trim();
        double height;
        String dbTypeText;;
        int dbType;
        String phone = phoneText.getText().toString().trim();

        //check the radio button
        int radioButtonID = radioGroupField.getCheckedRadioButtonId();
        View radioButton = radioGroupField.findViewById(radioButtonID);
        int idx = radioGroupField.indexOfChild(radioButton);
        RadioButton r = (RadioButton) radioGroupField.getChildAt(idx);
        dbTypeText = r.getText().toString();

        if(fName.isEmpty() || lName.isEmpty() || weightText.isEmpty() || heightText.isEmpty() || phone.isEmpty())
            buildAlertMessage(errorMsg[0]);

        else if(phone.length()<10 || !tryParseInt(phone))
            buildAlertMessage(errorMsg[1]);

        else if(!tryParseDouble(weightText) || !tryParseDouble(heightText) || !tryParseInt(ageText))
            buildAlertMessage(errorMsg[2]);

        else
        {
            weight = Double.parseDouble(weightText);
            height = Double.parseDouble(heightText);
            dbType = Integer.parseInt(dbTypeText);
            age = Integer.parseInt(ageText);
            Person p = buildNewPerson(fName, age, height, weight, dbType, phone);

            final Intent intent = new Intent(this, MainPage.class);
            intent.putExtra("person",p);
            startActivity(intent);
            finish();
        }
    }

    private void buildAlertMessage(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean tryParseDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Person buildNewPerson(String fName, int age, double height, double weight, int dbType, String phone)
    {
        userEditor.putString("name", fName);
        userEditor.putInt("age", age);
        userEditor.putFloat("height", (float) height);
        userEditor.putFloat("weight", (float) weight);
        userEditor.putInt("dbType", dbType);
        userEditor.putString("phone", phone);
        userEditor.putBoolean("initialize", true);
        userEditor.apply();

        Person p = new Person(fName, age, height, weight, dbType, phone);
        return p;
    }
}

package com.example.karin.diabeatis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class startPage extends AppCompatActivity {

    private Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHelp = (Button)findViewById(R.id.btnHelp);

    }
}

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
import android.widget.TextView;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.logic.Check;
import com.example.karin.diabeatis.logic.CheckDbHandler;
import com.example.karin.diabeatis.logic.Person;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class HistoryDisplay extends Fragment implements View.OnClickListener
{
    private Application activity;
    private Person p;
    private TextView txtName;
    private EditText newCheck;
    private Button showGraph;
    private Button addTest;
    private CheckDbHandler checkDbHandler;
    private GraphView graph;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_history_display, container, false);
        activity = this.getActivity().getApplication();
        txtName = (TextView) v.findViewById(R.id.txtNameE);
        p = (Person) getArguments().getSerializable("person");
        txtName.setText(p.getName());
        checkDbHandler = CheckDbHandler.getInstance(v.getContext());
        newCheck = (EditText) v.findViewById(R.id.check);
        addTest = (Button)v.findViewById(R.id.btnAdd);
        addTest.setOnClickListener(this);
        showGraph = (Button) v.findViewById(R.id.btnShow);
        showGraph.setOnClickListener(this);
        graph = (GraphView) v.findViewById(R.id.graph);

        return v;

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        ArrayList<Check> checks  = new ArrayList<>();

        switch(id)
        {
            case R.id.btnAdd:
            {
                if(!tryParseDouble(newCheck.getText().toString()) )
                    buildAlertMessage("בדיקה חייבת להיות בעלת ערך מספרי");
                insertCheck();
                break;
            }
            case R.id.btnShow:
            {
                showChecksHistory();
                break;
            }
        }

    }


    public void insertCheck()
    {
        String ch = newCheck.getText().toString();
        newCheck.setText("");
        Check curCheck = new Check(p.getName(),Double.parseDouble(ch));
        checkDbHandler.insertCheck(curCheck);
    }

    public void showChecksHistory()
    {
        ArrayList<Check> checksHistory = new ArrayList<>();
        checksHistory = checkDbHandler.getAllChecks();
        if(checksHistory.size() == 0)
            buildAlertMessage("לא קיימת הסטוריית בדיקות");
        DataPoint[] pointsData = new DataPoint[checksHistory.size()];
        for (int i = 0; i<checksHistory.size(); i++)
                pointsData[i] = new DataPoint(i,checksHistory.get(i).getCheck());
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(pointsData);
        graph.addSeries(series);
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

    private boolean tryParseDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

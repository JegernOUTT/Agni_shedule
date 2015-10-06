package com.example.hellb.agni.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.hellb.agni.R;
import com.example.hellb.agni.serializible.SerializableData;

import java.util.Collection;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Runnable {

    private Spinner spFaculty, spGroup;
    private ArrayAdapter<String> arrayAdapterFac, arrayAdapterGr;
    private Object facultyResult, groupResult;
    private EditText editText;
    private SerializableData serializableData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spFaculty = (Spinner) findViewById(R.id.spFaculty);
        spGroup = (Spinner) findViewById(R.id.spGroup);

        spGroup.setEnabled(false);

        spFaculty.setOnItemSelectedListener(this);
        spGroup.setOnItemSelectedListener(this);

        serializableData = SerializableData.getInstance();

        new Thread(this).start();
    }

    private void loadSpinners()
    {
        Collection<String> collection = serializableData.getScheduleInputParams().values();
        String [] arr = new String[collection.size()];
        Iterator<String> stringIterator = collection.iterator();
        for (int i = 0; i < collection.size(); ++i)
        {
            arr[i] = stringIterator.next();
        }
        arrayAdapterFac = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, arr);
        spFaculty.setAdapter(arrayAdapterFac);
        spFaculty.setPrompt("Выберите факультет...");

        spGroup.setEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter().equals(arrayAdapterFac))
        {

        }
        else if (parent.getAdapter().equals(arrayAdapterGr))
        {
            groupResult = parent.getItemAtPosition(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void run() {
        while (true)
        {
            if (serializableData.isRegisterParamReady)
            {
                loadSpinners();
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.example.hellb.agni.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hellb.agni.R;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spFaculty, spGroup;
    private ArrayAdapter<String> arrayAdapterFac, arrayAdapterGr;
    Object facultyResult, groupResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spFaculty = (Spinner) findViewById(R.id.spFaculty);
        spGroup = (Spinner) findViewById(R.id.spGroup);
        spGroup.setEnabled(false);

        spFaculty.setOnItemSelectedListener(this);
        spGroup.setOnItemSelectedListener(this);
//2
        loadSpinners();
    }

    private void loadSpinners()
    {
        if ((spFaculty != null) && (spGroup != null))
        {
            arrayAdapterFac = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                    new String[]{"ФЭА", "ФЭУ", "ФНГ", "ФИМ"});
            spFaculty.setAdapter(arrayAdapterFac);
            spFaculty.setPrompt("Выберите факультет...");

            spGroup.setEnabled(true);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter().equals(arrayAdapterFac))
        {
            new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                    arrayAdapterGr = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                            new String[]{"39-61", "34-45", "314-4", "1234"});
                    spGroup.setAdapter(arrayAdapterGr);
                    spGroup.setPrompt("Выберите факультет...");
                }
            }).run();

            /*
            * Загрузка групп
            * */
            facultyResult = parent.getItemAtPosition(position);
            Toast.makeText(LoginActivity.this, facultyResult.toString(), Toast.LENGTH_SHORT).show();
        }
        else if (parent.getAdapter().equals(arrayAdapterGr))
        {
            groupResult = parent.getItemAtPosition(position);
            Toast.makeText(LoginActivity.this, groupResult.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

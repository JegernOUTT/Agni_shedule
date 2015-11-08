package com.example.hellb.agni.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hellb.agni.DataSerializeController;
import com.example.hellb.agni.DataWebController;
import com.example.hellb.agni.R;
import com.example.hellb.agni.serializible.CurrentSettings;

import java.io.FileReader;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity
        implements Observer {
    private ProgressBar progressBar;
    private TextView tvLoadInformation;

    private static String currentSettingsFileName = "currentSettings.dat";
    private static String modelFileName = "model.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLoadInformation = (TextView) findViewById(R.id.tvLoadInformation);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);

        loadModel();
        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.colorAccent));
    }

    public void setStatusBarColor(View statusBar, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int actionBarHeight = getActionBarHeight();
            int statusBarHeight = getStatusBarHeight();
            //action bar height
            statusBar.getLayoutParams().height = actionBarHeight + statusBarHeight;
            statusBar.setBackgroundColor(color);
        }
    }

    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void loadCurrentSettings() {
        boolean isThereFile = false;

        try {
            FileReader reader = new FileReader(getApplicationContext().getFilesDir().toString() + '/' + currentSettingsFileName);
            reader.close();
            isThereFile = true;
        } catch (Exception e) {
            isThereFile = false;
        }

        if (!isThereFile) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            //progressBar.setVisibility(View.GONE);
        }
        else
        {
            DataSerializeController.getInstance(getApplicationContext()).deserializeCurrentSettings(this);
        }
    }

    private void loadModel() {
        boolean isThereFile = false;

        try {
            FileReader reader = new FileReader(getApplicationContext().getFilesDir().toString() + '/' + modelFileName);
            reader.close();
            isThereFile = true;
        } catch (Exception e) {
            isThereFile = false;
        }

        if (!isThereFile) {
            DataWebController.getInstance(getApplicationContext()).downloadModelToCurrentSettings(this);
            tvLoadInformation.setText("Загрузка справочников с сайта. Загрузка может быть долгой. Пожалуйста, подождите");
        }
        else
        {
            DataSerializeController.getInstance(getApplicationContext()).deserializeModel(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void update(Observable observable, Object data) {
        String inf = (String) data;

        if (inf.equals("downloadScheduleToCurrentSettings")){
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        }
        else if (inf.equals("downloadModelToCurrentSettings")){
            DataSerializeController.getInstance(getApplicationContext()).serializeModel(this);
            loadCurrentSettings();
        }
        else if (inf.equals("deserializeCurrentSettings")){
            if (CurrentSettings.getInstance().isLoaded) {
                DataWebController.getInstance(getApplicationContext()).downloadScheduleToCurrentSettings(MainActivity.this);
                tvLoadInformation.setText("Загрузка расписания");
            }
            else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
        else if (inf.equals("deserializeModel")){
            loadCurrentSettings();
        }
    }
}

package com.example.hellb.agni.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.hellb.agni.DataSerializeController;
import com.example.hellb.agni.DataWebController;
import com.example.hellb.agni.R;

import java.io.FileReader;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Observer {
    private ProgressBar progressBar;

    private static String currentSettingsFileName = "currentSettings.dat";
    private static String modelFileName = "model.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationCreate();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        loadModel();
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
        }
        else
        {
            DataSerializeController.getInstance(getApplicationContext()).deserializeModel(this);
        }
    }

    private void NavigationCreate() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_enter) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_schedule) {
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void update(Observable observable, Object data) {
        String inf = (String) data;

        if (inf.equals("downloadScheduleToCurrentSettings")){
            progressBar.setVisibility(View.GONE);

            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        }
        else if (inf.equals("downloadModelToCurrentSettings")){
            progressBar.setVisibility(View.GONE);

            DataSerializeController.getInstance(getApplicationContext()).serializeModel(this);
            loadCurrentSettings();
        }
        else if (inf.equals("deserializeCurrentSettings")){
            DataWebController.getInstance(getApplicationContext()).downloadScheduleToCurrentSettings(MainActivity.this);
        }
        else if (inf.equals("deserializeModel")){
            loadCurrentSettings();
        }
    }
}

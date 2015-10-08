package com.example.hellb.agni.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.hellb.agni.DataGetStack;
import com.example.hellb.agni.R;
import com.example.hellb.agni.serializible.SerializableScheduleData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressBar progressBar;
    static String fileName = "settings.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        NavigationCreate();

        loadModel();
    }

    private void loadModel() {
        boolean isThereFile = false;

        try {
            FileReader reader = new FileReader(getApplicationContext().getFilesDir().toString() + '/' + fileName);
            reader.close();
            isThereFile = true;
        } catch (Exception e) {
            isThereFile = false;
        }

        if (!isThereFile) {
            DataGetStack.getInstance(10, getApplicationContext())
                    .addTask(SerializableScheduleData.getInstance());

            new AsyncTask<Boolean, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            try
                            {
                                FileOutputStream fos = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
                                ObjectOutputStream os = new ObjectOutputStream(fos);
                                os.writeObject(SerializableScheduleData.getInstance());
                                os.close();
                                fos.close();
                            }
                            catch (Exception exception)
                            {
                                Log.d("Exception: ", exception.getMessage());
                            }

                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                protected Void doInBackground(Boolean... params) {
                    while (true)
                    {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (DataGetStack.getInstance().isReady())
                        {
                            SerializableScheduleData serializableScheduleData = SerializableScheduleData.getInstance();
                            serializableScheduleData.isLoaded();
                            break;
                        }
                    }
                    return null;
                }
            }
                    .execute();
        }
        else
        {
            new AsyncTask<Boolean, Void, Void>() {
                @Override
                protected Void doInBackground(Boolean... params) {
                    FileInputStream fis = null;
                    try {
                        fis = getApplicationContext().openFileInput(fileName);
                        ObjectInputStream is = new ObjectInputStream(fis);
                        SerializableScheduleData.setInstance((SerializableScheduleData) is.readObject());
                        is.close();
                        fis.close();
                    } catch (Exception exception) {
                        Log.d("Exception: ", exception.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressBar.setVisibility(View.GONE);
                }
            }
                    .execute();

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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

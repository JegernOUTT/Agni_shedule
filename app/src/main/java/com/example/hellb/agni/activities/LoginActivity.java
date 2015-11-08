package com.example.hellb.agni.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hellb.agni.DataSerializeController;
import com.example.hellb.agni.DataWebController;
import com.example.hellb.agni.R;
import com.example.hellb.agni.serializible.CurrentSettings;
import com.example.hellb.agni.serializible.scheduleData.Course;
import com.example.hellb.agni.serializible.scheduleData.Faculty;
import com.example.hellb.agni.serializible.scheduleData.Group;
import com.example.hellb.agni.serializible.scheduleData.SerializableScheduleData;
import com.example.hellb.agni.serializible.scheduleData.Week;
import com.example.hellb.agni.serializible.scheduleEnums.HalfGroup;

import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener, Observer, SwipeRefreshLayout.OnRefreshListener {

    static String currentSettingsFileName = "currentSettings.dat";
    private volatile boolean isLoaded;
    private Spinner spFaculty, spGroup, spCourse, spHalfGroup;
    private ArrayAdapter<String> arrayAdapterFac, arrayAdapterCourse, arrayAdapterGr,
            arrayAdapterHalfGroup;
    private SerializableScheduleData serializableScheduleData;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNavFac, tvNavGroup;
    private NavigationView navigationView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NavigationCreate();
        isLoaded = false;

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutRegister);

        tvNavFac = (TextView) findViewById(R.id.tvNavFaculty);
        tvNavGroup = (TextView) findViewById(R.id.tvNavGroup);

        spFaculty = (Spinner) findViewById(R.id.spFaculty);
        spGroup = (Spinner) findViewById(R.id.spGroup);
        spCourse = (Spinner) findViewById(R.id.spCourse);
        spHalfGroup = (Spinner) findViewById(R.id.spHalfGroup);

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.GONE);

        spFaculty.setEnabled(false);
        spGroup.setEnabled(false);
        spCourse.setEnabled(false);
        spHalfGroup.setEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(this);

        spFaculty.setOnItemSelectedListener(this);
        spCourse.setOnItemSelectedListener(this);
        spGroup.setOnItemSelectedListener(this);
        spHalfGroup.setOnItemSelectedListener(this);

        serializableScheduleData = SerializableScheduleData.getInstance();

        loadSpinnerFaculty();
        navigationBarLoad();
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setCheckedItem(R.id.nav_enter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (! CurrentSettings.getInstance().isLoaded){
            navigationView.setEnabled(false);
        }
    }

    private void NavigationCreate() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Загрузка расписания", Snackbar.LENGTH_LONG)
                        .setAction("Загрузка расписания", null).show();
                btnSaveClick(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadSpinnerFaculty() {
        String[] arr = serializableScheduleData.getStringRepresentationFaculties();

        arrayAdapterFac = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_layout, arr);
        spFaculty.setAdapter(arrayAdapterFac);
        spFaculty.setPrompt("Выберите факультет");

        spFaculty.setEnabled(true);

        if (!isLoaded)
        {
            if (CurrentSettings.getInstance().isLoaded) {
                for (int i = 0; i < arrayAdapterFac.getCount(); ++i) {
                    if (CurrentSettings.getInstance().faculty.toString().equals((String) arrayAdapterFac.getItem(i))) {
                        spFaculty.setSelection(i, true);
                    }
                }
            }
        }
    }

    private void loadSpinnerCourse(Faculty faculty) {
        String[] arr =  faculty.getStringRepresentationCourses();

        arrayAdapterCourse = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_layout, arr);
        spCourse.setAdapter(arrayAdapterCourse);
        spCourse.setPrompt("Выберите курс");

        spCourse.setEnabled(true);

        if (!isLoaded)
        {
            if (CurrentSettings.getInstance().isLoaded) {
                for(int i = 0; i < arrayAdapterCourse.getCount(); ++i)
                {
                    if (CurrentSettings.getInstance().course.toString().equals((String) arrayAdapterCourse.getItem(i)))
                    {
                        spCourse.setSelection(i, true);
                    }
                }
            }
        }
    }

    private void loadSpinnerGroup(Course course) {
        String[] arr = course.getStringRepresentationGroups();
        arrayAdapterGr = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_layout, arr);
        spGroup.setAdapter(arrayAdapterGr);
        spGroup.setPrompt("Выберите группу");

        spGroup.setEnabled(true);

        if (!isLoaded)
        {
            if (CurrentSettings.getInstance().isLoaded) {
                for(int i = 0; i < arrayAdapterGr.getCount(); ++i)
                {
                    if (CurrentSettings.getInstance().group.toString().equals((String) arrayAdapterGr.getItem(i)))
                    {
                        spGroup.setSelection(i, true);
                    }
                }
            }
        }
    }

    private void loadSpinnerHalfGroup() {
        String[] arr = {"Первая подгруппа", "Вторая подгруппа", "Все подгруппы"};
        arrayAdapterHalfGroup = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_layout, arr);
        spHalfGroup.setAdapter(arrayAdapterHalfGroup);
        spHalfGroup.setPrompt("Выберите подгруппу");

        spHalfGroup.setEnabled(true);

        if (!isLoaded)
        {
            if (CurrentSettings.getInstance().isLoaded) {
                for(int i = 0; i < arrayAdapterHalfGroup.getCount(); ++i)
                {
                    if (CurrentSettings.getInstance().halfGroup.equals(HalfGroup.FIRST_HALF_GROUP))
                    {
                        spHalfGroup.setSelection(0, true);
                    }
                    else if (CurrentSettings.getInstance().halfGroup.equals(HalfGroup.SECOND_HALF_GROUP))
                    {
                        spHalfGroup.setSelection(1, true);
                    }
                    else if (CurrentSettings.getInstance().halfGroup.equals(HalfGroup.COMMON_HALF_GROUP))
                    {
                        spHalfGroup.setSelection(2, true);
                    }
                }
            }
        }
    }

    public void btnSaveClick(View view) {
        navigationView.setEnabled(true);

        if (spFaculty.getSelectedItem() == null ||
                spCourse.getSelectedItem().toString().equals("Нет данных") ||
                spGroup.getSelectedItem().toString().equals("Нет данных") ||
                spHalfGroup.getSelectedItem().toString().equals("Нет данных"))
        {
            Toast.makeText(getApplicationContext(), "Вы должны выбрать все параметры", Toast.LENGTH_LONG).show();
        }
        else
        {
            CurrentSettings.getInstance().faculty = (Faculty)
                    SerializableScheduleData.getInstance().getObjectByString((String) spFaculty.getSelectedItem());
            CurrentSettings.getInstance().course = (Course)
                    CurrentSettings.getInstance().faculty.getObjectByString((String) spCourse.getSelectedItem());
            CurrentSettings.getInstance().group = (Group)
                    CurrentSettings.getInstance().course.getObjectByString((String) spGroup.getSelectedItem());

            CurrentSettings.getInstance().halfGroup = (HalfGroup)HalfGroup.values()[spHalfGroup.getSelectedItemPosition()];

            CurrentSettings.getInstance().isLoaded = true;

            for (Week week: CurrentSettings.getInstance().group.getWeeks())
            {
                if (week.isCurrent)
                    CurrentSettings.getInstance().week = week;
            }

            DataSerializeController.getInstance(getApplicationContext()).serializeCurrentSettings(this);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case R.id.spFaculty:
                for (Faculty faculty: serializableScheduleData.getFaculties())
                {
                    if (faculty.toString().equals(spFaculty.getSelectedItem()))
                    {
                        loadSpinnerCourse(faculty);
                    }
                }

                break;
            case R.id.spCourse:
                for (Faculty faculty: serializableScheduleData.getFaculties())
                {
                    if (faculty.toString().equals(spFaculty.getSelectedItem()))
                    {
                        for (Course course: faculty.getCourses())
                        {
                            if (course.toString().equals(spCourse.getSelectedItem()))
                            {
                                loadSpinnerGroup(course);
                            }
                        }
                    }
                }

            case R.id.spGroup:
                loadSpinnerHalfGroup();

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()) {
            case R.id.spFaculty:

                break;

            case R.id.spCourse:

                break;
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

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_enter) {

        }
        else if (id == R.id.nav_schedule) {
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_contact)
        {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void update(Observable observable, Object data) {
        String inf = (String) data;

        if (inf.equals("serializeCurrentSettings")){
            CurrentSettings.getInstance().week.schedule.clearLessons();
            DataWebController.getInstance(getApplicationContext()).downloadScheduleToCurrentSettings(this);
        }
        else if (inf.equals("downloadScheduleToCurrentSettings")){
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
            progressBar.setVisibility(View.GONE);
        }
        else if (inf.equals("downloadModelToCurrentSettings")){
            DataSerializeController.getInstance(getApplicationContext()).serializeModel(this);
        }
        else if (inf.equals("serializeModel")){
            loadSpinnerFaculty();
            navigationBarLoad();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void navigationBarLoad() {
        if (CurrentSettings.getInstance().isLoaded)
        {
            tvNavFac.setText("Факультет: " + CurrentSettings.getInstance().faculty.toString());
            tvNavGroup.setText("Группа: " + CurrentSettings.getInstance().group.toString());
        }
    }

    @Override
    public void onRefresh() {
        SerializableScheduleData.getInstance().clear();

        DataWebController.getInstance(getApplicationContext()).downloadModelToCurrentSettings(this);
    }
}

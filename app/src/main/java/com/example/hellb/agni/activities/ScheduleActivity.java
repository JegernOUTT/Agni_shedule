package com.example.hellb.agni.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hellb.agni.R;
import com.example.hellb.agni.adapters.ScheduleAdapter;
import com.example.hellb.agni.serializible.CurrentSettings;
import com.example.hellb.agni.serializible.scheduleData.Lesson;
import com.example.hellb.agni.serializible.scheduleData.Schedule;
import com.example.hellb.agni.serializible.scheduleEnums.DaysOfWeek;
import com.example.hellb.agni.serializible.scheduleEnums.HalfGroup;

import java.util.ArrayList;
import java.util.EnumMap;

public class ScheduleActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private TextView tvNavFac, tvNavGroup;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private NavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setCheckedItem(R.id.nav_schedule);
        navigationBarLoad();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        tvNavFac = (TextView) findViewById(R.id.tvNavFaculty);
        tvNavGroup = (TextView) findViewById(R.id.tvNavGroup);

        notificationCreate();
    }

    private void notificationCreate() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_schedule) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public Schedule currentSchedule;
        EnumMap<DaysOfWeek, String> map;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            map = new EnumMap<DaysOfWeek, String>(DaysOfWeek.class);
            currentSchedule = CurrentSettings.getInstance().week.schedule;

            ArrayList<Pair<DaysOfWeek, String>> daysPairs = new ArrayList<Pair<DaysOfWeek, String>>();

            for (Lesson lesson: currentSchedule.getLessons())
            {
                map.put(lesson.getCurrentDay().second, getStringRepresentationOfDay(lesson.getCurrentDay().second));
            }
        }

        private String getStringRepresentationOfDay(DaysOfWeek daysOfWeek) {
            if (daysOfWeek.equals(DaysOfWeek.MONDAY)){
                return "ПН";
            }
            else if (daysOfWeek.equals(DaysOfWeek.TUESDAY)){
                return "ВТ";
            }
            else if (daysOfWeek.equals(DaysOfWeek.WEDNESDAY)){
                return "СР";
            }
            else if (daysOfWeek.equals(DaysOfWeek.THURSDAY)){
                return "ЧТ";
            }
            else if (daysOfWeek.equals(DaysOfWeek.FRIDAY)){
                return "ПТ";
            }
            else if (daysOfWeek.equals(DaysOfWeek.SATURDAY)){
                return "СБ";
            }
            else if (daysOfWeek.equals(DaysOfWeek.SUNDAY)){
                return "ВС";
            }
            else return "";
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Object object = map.keySet().toArray()[position];
            return PlaceholderFragment.newInstance((DaysOfWeek)object);
        }

        @Override
        public int getCount() {
            return map.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Object[] arr = map.values().toArray();
            return (String) arr[position];
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION = "section";
        private ProgressBar progressBar;
        //private ListView listView;
        private RecyclerView recyclerView;
        private DaysOfWeek currentDay;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(DaysOfWeek sectionObject) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION, sectionObject.name());
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
            //listView = (ListView)rootView.findViewById(R.id.listView);
            recyclerView = (RecyclerView)rootView.findViewById(R.id.listView);
            progressBar = (ProgressBar)rootView.findViewById((R.id.progressBar2));

            for (DaysOfWeek value: DaysOfWeek.values())
            {
                if ((getArguments().getString(ARG_SECTION)).equals(value.name()))
                {
                    currentDay = value;
                }
            }

            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();

            scheduleOnFragment(recyclerView, currentDay, getContext());
        }

        private void scheduleOnFragment(RecyclerView recyclerView1, DaysOfWeek currentDay, Context context) {
            if (currentDay != null)
            {
                ArrayList<Lesson> lessons = new ArrayList<Lesson>();
                lessons.clear();
                for (Lesson lesson: CurrentSettings.getInstance().week.schedule.getLessons())
                {
                    if (lesson.getCurrentDay().second.compareTo(currentDay) == 0 &&
                            (lesson.getCurrentHalfGroup().second.equals(CurrentSettings.getInstance().halfGroup) ||
                                    (lesson.getCurrentHalfGroup().second.equals(HalfGroup.COMMON_HALF_GROUP)) ||
                            (CurrentSettings.getInstance().halfGroup.equals(HalfGroup.COMMON_HALF_GROUP))))
                    {
                        lessons.add(lesson);
                    }
                }

                ScheduleAdapter scheduleAdapter = new ScheduleAdapter(context, lessons);
                recyclerView1.setAdapter(scheduleAdapter);
                recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }
    }

    private void navigationBarLoad() {
        if (CurrentSettings.getInstance().isLoaded)
        {
            tvNavFac.setText("Факультет: " + CurrentSettings.getInstance().faculty.toString());
            tvNavGroup.setText("Группа: " + CurrentSettings.getInstance().group.toString());
        }
    }
}



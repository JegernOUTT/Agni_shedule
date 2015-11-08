package com.example.hellb.agni.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.example.hellb.agni.DataWebController;
import com.example.hellb.agni.R;
import com.example.hellb.agni.adapters.ScheduleAdapter;
import com.example.hellb.agni.serializible.CurrentSettings;
import com.example.hellb.agni.serializible.scheduleData.Lesson;
import com.example.hellb.agni.serializible.scheduleData.Schedule;
import com.example.hellb.agni.serializible.scheduleData.Week;
import com.example.hellb.agni.serializible.scheduleEnums.DaysOfWeek;
import com.example.hellb.agni.serializible.scheduleEnums.HalfGroup;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Observable;
import java.util.Observer;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

import static android.support.design.widget.TabLayout.*;

public class ScheduleActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, OnTabSelectedListener {

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
    private TabLayout tabLayoutWeek;

    @Override
    protected void onStart() {
        super.onStart();

        notificationCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        weekLoad();
        navigationBarLoad();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                for (int i = 0; i < tabLayoutWeek.getTabCount(); ++i)
                {
                    if (((Week) tabLayoutWeek.getTabAt(i).getTag()).equals(CurrentSettings.getInstance().week))
                    {
                        int right = ((ViewGroup) tabLayoutWeek.getChildAt(0)).getChildAt(i).getRight();
                        tabLayoutWeek.smoothScrollTo(right, 0);
                        tabLayoutWeek.getTabAt(i).select();
                    }
                }

            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        tvNavFac = (TextView) findViewById(R.id.tvNavFaculty);
        tvNavGroup = (TextView) findViewById(R.id.tvNavGroup);

        //notificationCreate();
    }

    private void notificationCreate() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_days);
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.setTabGravity(GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayoutWeek = (TabLayout) findViewById(R.id.tabs_week);
        tabLayoutWeek.setTabMode(MODE_SCROLLABLE);
        tabLayoutWeek.setTabGravity(GRAVITY_CENTER);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void weekLoad() {
        for (Week week: CurrentSettings.getInstance().group.getWeeks())
        {
            Tab tab = tabLayoutWeek.newTab()
                    .setText(week.toString())
                    .setTag(week);
            tabLayoutWeek.addTab(tab);
        }
        tabLayoutWeek.setOnTabSelectedListener(this);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(Tab tab) {
        Week week = (Week) tab.getTag();
        if (!week.equals(CurrentSettings.getInstance().week))
        {
            CurrentSettings.getInstance().week = week;

            if (!week.schedule.isReady())
            {
                DataWebController.getInstance(getApplicationContext()).downloadScheduleToCurrentSettings(new Observer() {
                    @Override
                    public void update(Observable observable, Object data) {
                        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                        mViewPager = (ViewPager) findViewById(R.id.container);
                        mViewPager.setAdapter(mSectionsPagerAdapter);

                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_days);
                        tabLayout.setTabMode(MODE_FIXED);
                        tabLayout.setTabGravity(GRAVITY_FILL);
                        tabLayout.setupWithViewPager(mViewPager);
                    }
                });
            } else
            {
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_days);
                tabLayout.setTabMode(MODE_FIXED);
                tabLayout.setTabGravity(GRAVITY_FILL);
                tabLayout.setupWithViewPager(mViewPager);
            }
        }
    }

    @Override
    public void onTabUnselected(Tab tab) {

    }

    @Override
    public void onTabReselected(Tab tab) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        public Schedule currentSchedule;

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

        }

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
    public static class PlaceholderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
            Observer{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION = "section";
        private SwipeRefreshLayout swipeRefreshLayout;
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
            recyclerView = (RecyclerView)rootView.findViewById(R.id.listView);
            swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setDistanceToTriggerSync(350);

            final LinearLayoutManager layoutParams = new LinearLayoutManager(getContext().getApplicationContext());
            recyclerView.setLayoutManager(layoutParams);
            recyclerView.clearOnScrollListeners();
            recyclerView.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    swipeRefreshLayout.setEnabled(layoutParams.findFirstCompletelyVisibleItemPosition() == 0);
                }
            });


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
            }

            goToCurrent();
        }

        private void goToCurrent() {
            //Select current lesson
        }

        @Override
        public void onRefresh() {
            CurrentSettings.getInstance().week.schedule.clearLessons();
            DataWebController.getInstance(getContext()).downloadScheduleToCurrentSettings(this);
        }

        @Override
        public void update(Observable observable, Object data) {
            String inf = (String) data;

            if (inf.equals("downloadScheduleToCurrentSettings")){
                scheduleOnFragment(recyclerView, currentDay, getContext());
                swipeRefreshLayout.setRefreshing(false);
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



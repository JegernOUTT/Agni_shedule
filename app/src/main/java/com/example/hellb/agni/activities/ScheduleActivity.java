package com.example.hellb.agni.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hellb.agni.DataGetStack;
import com.example.hellb.agni.R;
import com.example.hellb.agni.adapters.ScheduleAdapter;
import com.example.hellb.agni.serializible.CurrentSettings;
import com.example.hellb.agni.serializible.scheduleData.Lesson;
import com.example.hellb.agni.serializible.scheduleData.Schedule;
import com.example.hellb.agni.serializible.scheduleEnums.DaysOfWeek;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        if (! CurrentSettings.getInstance().isLoaded)
        {
            Toast.makeText(getApplicationContext(), "Заполните входные данные", Toast.LENGTH_LONG).show();
        }
        else
        {
            loadSchedule();
        }
        notificationCreate();
    }

    private void loadSchedule() {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DataGetStack.getInstance(1, getApplicationContext()).
                        addTask(CurrentSettings.getInstance().week.schedule);
                return null;
            }
        }.execute();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public Schedule currentSchedule;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            currentSchedule = CurrentSettings.getInstance().week.schedule;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ПН";
                case 1:
                    return "ВТ";
                case 2:
                    return "СР";
                case 3:
                    return "ЧТ";
                case 4:
                    return "ПТ";
            }
            return null;
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
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ProgressBar progressBar;
        private ListView listView;
        private DaysOfWeek currentDay;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
            listView = (ListView)rootView.findViewById(R.id.listView);
            progressBar = (ProgressBar)rootView.findViewById((R.id.progressBar2));
            progressBar.setVisibility(View.VISIBLE);

            int i = 0;
            for (DaysOfWeek value: DaysOfWeek.values())
            {
                if ((getArguments().getInt(ARG_SECTION_NUMBER) - 1) == i)
                {
                    currentDay = value;
                }
                ++i;
            }

            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    while (!DataGetStack.getInstance().isReady())
                    {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    CurrentSettings asd = CurrentSettings.getInstance();
                    progressBar.setVisibility(View.GONE);
                    scheduleOnFragment(listView, currentDay, getContext());
                }
            }.execute();
        }

        private void scheduleOnFragment(ListView listView, DaysOfWeek currentDay, Context context) {
            if (currentDay != null)
            {
                ArrayList<Lesson> lessons = new ArrayList<Lesson>();
                for (Lesson lesson: CurrentSettings.getInstance().week.schedule.getLessons())
                {
                    if (lesson.getCurrentDay().second.compareTo(currentDay) == 0)
                    {
                        lessons.add(lesson);
                    }
                }

                ScheduleAdapter scheduleAdapter = new ScheduleAdapter(context, lessons);

                listView.setAdapter(scheduleAdapter);
                listView.setVisibility(View.VISIBLE);
            }
        }
    }
}

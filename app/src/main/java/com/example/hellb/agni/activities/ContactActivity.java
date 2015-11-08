package com.example.hellb.agni.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hellb.agni.R;

public class ContactActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView tvVk;
    private TextView tvUrl;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        tvVk = (TextView) findViewById(R.id.tvVK);
        tvUrl = (TextView) findViewById(R.id.tvUrl);
        navigationView = (NavigationView) findViewById(R.id.nav_view_contact);

        tvVk.setClickable(true);
        tvVk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://vk.com/agni_rt"));
                startActivity(i);
            }
        });

        tvUrl.setClickable(true);
        tvUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, "Hellboysis666@gmail.com");
                i.putExtra(Intent.EXTRA_SUBJECT, "Issues about AGNI Raspisanie");
                i.putExtra(Intent.EXTRA_TEXT, "");

                startActivity(Intent.createChooser(i, "Send Email"));
            }
        });

        notificationCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setCheckedItem(R.id.nav_contact);
    }

    private void notificationCreate() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_contact);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_contact);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_contact);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_enter) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_schedule) {
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_contact) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_contact);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

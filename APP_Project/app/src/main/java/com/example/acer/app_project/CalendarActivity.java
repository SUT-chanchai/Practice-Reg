package com.example.acer.app_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        WebView view = (WebView) this.findViewById(R.id.web);
        view.setWebViewClient(new WebViewClient());
        SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
        view.loadUrl("http://it2.sut.ac.th/script259g5/app/cal.php?namegruop="+sp.getString("id_group", ""));
        view.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent navigationDrawerIntent = new Intent(this, HomeActivity.class);
            startActivity(navigationDrawerIntent);
        } else if (id == R.id.nav_gallery) {
            Intent navigationDrawerIntent = new Intent(this, MemberActivity.class);
            startActivity(navigationDrawerIntent);
        } else if (id == R.id.nav_slideshow) {
            Intent navigationDrawerIntent = new Intent(this, StayActivity.class);
            startActivity(navigationDrawerIntent);
        } else if (id == R.id.nav_manage) {
            Intent navigationDrawerIntent = new Intent(this, CanteenActivity.class);
            startActivity(navigationDrawerIntent);
        }else if (id == R.id.nav_date) {
            Intent navigationDrawerIntent = new Intent(this, CalendarActivity.class);
            startActivity(navigationDrawerIntent);
        }else if (id == R.id.nav_share) {
            SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("check_login", 0);
            editor.commit();
            Intent navigationDrawerIntent = new Intent(this, MainActivity.class);
            startActivity(navigationDrawerIntent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

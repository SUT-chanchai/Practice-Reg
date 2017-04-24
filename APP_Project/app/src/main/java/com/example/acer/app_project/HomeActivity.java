package com.example.acer.app_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
int id_check;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        id_check = 1;
        SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
        syncgetHttp http = new syncgetHttp();
        http.SetUrl("http://it2.sut.ac.th/script259g5/App/select.php?namegruop=" + sp.getString("id_group", ""));
        http.execute();


        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_check = 2;
//                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
//                Uri number = Uri.parse("tel:5551234");
//                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
//                startActivity(callIntent);
                SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
                syncgetHttp http = new syncgetHttp();
                http.SetUrl("http://it2.sut.ac.th/script259g5/App/select.php?namegruop=" + sp.getString("id_group", ""));
                http.execute();

            }
        });


    }

    public void loadmap(View view) {
        id_check = 0;
        SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
        syncgetHttp http = new syncgetHttp();
        http.SetUrl("http://it2.sut.ac.th/script259g5/App/map.php?namegruop=" + sp.getString("id_group", ""));
        http.execute();
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
        getMenuInflater().inflate(R.menu.home, menu);
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

    public class syncgetHttp extends AsyncTask<Void, Void, String> {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request;

        protected void SetUrl(String url) {
            request = builder.url(url).build();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Not Success - code : " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error - " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(id_check==0) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + s.toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }else if(id_check==2){
                String data = s;
                String[] items = data.split(",");
                Uri number = Uri.parse("tel:"+items[4]);
                 Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                 startActivity(callIntent);

            }else{
                String data = s;
                String[] items = data.split(",");

                TextView t1 = (TextView)findViewById(R.id.textView3);
                TextView t2 = (TextView)findViewById(R.id.textView6);
                TextView t3 = (TextView)findViewById(R.id.textView8);
                TextView t4 = (TextView)findViewById(R.id.textView11);
                TextView t5 = (TextView)findViewById(R.id.textView16);
                //TextView t6 = (TextView)findViewById(R.id.textView19);
                t1.setText(items[0]);
                t2.setText(items[2]);
                t3.setText(items[1]);
                t4.setText(items[3]);
                t5.setText(items[4]);
                //t6.setText(items[5]);

                button = (Button) findViewById(R.id.button);
                button.setText("นำทางไป"+items[5]);

            }
        }
    }
}

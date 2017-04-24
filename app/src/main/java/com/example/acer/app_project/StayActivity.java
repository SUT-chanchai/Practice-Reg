package com.example.acer.app_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    int position_memder = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ListView listViewMovies = (ListView)findViewById(R.id.listView1);
        SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
        String url = "http://it2.sut.ac.th/script259g5/App/show_stay.php?namegruop="+sp.getString("id_group", "");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONArray data = new JSONArray(getJSON(url,params));


            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();
                map.put("id-m", String.valueOf(i+1));
                map.put("lg_name", c.getString("lg_name"));
                map.put("sex", c.getString("sex"));
                map.put("num", c.getString("num"));
                MyArrList.add(map);

            }


            SimpleAdapter simpleAdapterData;
            simpleAdapterData = new SimpleAdapter(StayActivity.this, MyArrList, R.layout.acivity_column2,
                    new String[] {"id-m", "lg_name"}, new int[] {R.id.articleID, R.id.articleTitle});
            listViewMovies.setAdapter(simpleAdapterData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        listViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                AlertDialog.Builder adb = new AlertDialog.Builder(
                        StayActivity.this);

                adb.setTitle("ข้อมูลที่พัก");
                adb.setMessage("ชื่อที่พัก: " + MyArrList.get(position).get("lg_name") + "\n"
                        + "ประเภท: " + MyArrList.get(position).get("sex") + "\n"
                        + "จำนวน: " +  MyArrList.get(position).get("num") + " คน\n"
                );
                adb.setPositiveButton("กลับ", null);
                adb.show();
            }
        });

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
        getMenuInflater().inflate(R.menu.stay, menu);
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

    public String getJSON(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader_buffer = new BufferedReader
                        (new InputStreamReader(content));

                String line;
                while ((line = reader_buffer.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download file..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

}

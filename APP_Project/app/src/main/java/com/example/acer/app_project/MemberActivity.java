package com.example.acer.app_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MemberActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    int position_memder = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
                Intent navigationDrawerIntent = new Intent(MemberActivity.this, AddMemberActivity.class);
                startActivity(navigationDrawerIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

         ListView listViewMovies = (ListView)findViewById(R.id.listView1);
        SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
        String url = "http://it2.sut.ac.th/script259g5/App/show1.php?namegruop="+sp.getString("id_group", "");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONArray data = new JSONArray(getJSON(url,params));


            HashMap<String, String> map;
            String disease,note;
            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();
                map.put("id-m", String.valueOf(i+1));
                map.put("phone", c.getString("phone"));
                map.put("name", c.getString("name"));
                map.put("e_mail", c.getString("e_mail"));
                if( c.getString("disease").trim().equals("")){
                    disease = "     -";
                }else{
                    disease = c.getString("disease");
                }

                if( c.getString("note").trim().equals("")){
                    note = "    -";
                }else{
                    note = c.getString("note");
                }

                map.put("disease", disease);
                map.put("note", note);
                map.put("id", c.getString("id"));
                MyArrList.add(map);

            }


            SimpleAdapter simpleAdapterData;
            simpleAdapterData = new SimpleAdapter(MemberActivity.this, MyArrList, R.layout.acivity_column,
                    new String[] {"id-m", "name"}, new int[] {R.id.articleID, R.id.articleTitle});
            listViewMovies.setAdapter(simpleAdapterData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        listViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                AlertDialog.Builder adb = new AlertDialog.Builder(
                        MemberActivity.this);
                position_memder = position;
                adb.setTitle("ข้อมูลสมาชิก");
                adb.setMessage("ชื่อ: " + MyArrList.get(position).get("name") + "\n"
                        + "เบอร์โทร: " + MyArrList.get(position).get("phone") + "\n"
                        + "อีเมล: " +  MyArrList.get(position).get("e_mail") + "\n"
                        + "โรคประจำตัว: " + MyArrList.get(position).get("disease") + "\n"
                        + "หมายเหตุ: " + MyArrList.get(position).get("note") + "\n"
                );
                adb.setPositiveButton("ลบ",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(MemberActivity.this);
                                builder.setMessage("คุณต้องการลบข้อมูลหรือไม่?");
                                builder.setPositiveButton("ลบ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        syncgetHttp http = new syncgetHttp ();
                                        http.SetUrl("http://it2.sut.ac.th/script259g5/app/delect.php?id="+MyArrList.get(position_memder).get("id"));
                                        http.execute();
                                    }
                                });
                                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dialog.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        });

                adb.setNeutralButton("กลับ",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        });

                adb.setNegativeButton("แก้ไข",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Intent navigationDrawerIntent = new Intent(MemberActivity.this, UpdateActivity.class);
                                navigationDrawerIntent.putExtra("name", MyArrList.get(position_memder).get("name"));
                                navigationDrawerIntent.putExtra("phone", MyArrList.get(position_memder).get("phone"));
                                navigationDrawerIntent.putExtra("e_mail", MyArrList.get(position_memder).get("e_mail"));
                                navigationDrawerIntent.putExtra("disease", MyArrList.get(position_memder).get("disease"));
                                navigationDrawerIntent.putExtra("note", MyArrList.get(position_memder).get("note"));
                                navigationDrawerIntent.putExtra("id", MyArrList.get(position_memder).get("id"));
                                startActivity(navigationDrawerIntent);
                                //Toast.makeText(getApplicationContext(),  String.format(" %d ", position_memder), Toast.LENGTH_LONG).show();
                            }
                        });


                AlertDialog alert = adb.create();
                alert.show();
            }
        });



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
        getMenuInflater().inflate(R.menu.member, menu);
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
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(MemberActivity.this);
            LayoutInflater inflater = getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog_custom, null);
            builder.setView(view);

            final EditText username = (EditText) view.findViewById(R.id.username);


            builder.setPositiveButton("ค้นหา", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                    MyArrList.clear();
                    ListView listViewMovies = (ListView)findViewById(R.id.listView1);
                    SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
                    String url = "http://it2.sut.ac.th/script259g5/app/search.php?namegruop="+sp.getString("id_group", "")+"&name="+username.getText();

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    try {
                        JSONArray data = new JSONArray(getJSON(url,params));


                        HashMap<String, String> map;
                        String disease,note;
                        for(int i = 0; i < data.length(); i++){
                            JSONObject c = data.getJSONObject(i);

                            map = new HashMap<String, String>();
                            map.put("id-m", String.valueOf(i+1));
                            map.put("phone", c.getString("phone"));
                            map.put("name", c.getString("name"));
                            map.put("e_mail", c.getString("e_mail"));
                            if( c.getString("disease").trim().equals("")){
                                disease = "     -";
                            }else{
                                disease = c.getString("disease");
                            }

                            if( c.getString("note").trim().equals("")){
                                note = "    -";
                            }else{
                                note = c.getString("note");
                            }

                            map.put("disease", disease);
                            map.put("note", note);
                            map.put("id", c.getString("id"));
                            MyArrList.add(map);

                        }


                        SimpleAdapter simpleAdapterData;
                        simpleAdapterData = new SimpleAdapter(MemberActivity.this, MyArrList, R.layout.acivity_column,
                                new String[] {"id-m", "name"}, new int[] {R.id.articleID, R.id.articleTitle});
                        listViewMovies.setAdapter(simpleAdapterData);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }



                }
            });
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();


        }else if(id == R.id.action_settings1){
            Intent navigationDrawerIntent = new Intent(MemberActivity.this, MemberActivity.class);
            startActivity(navigationDrawerIntent);
            finish();
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

    public class syncgetHttp extends AsyncTask<Void,Void,String> {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request;
        protected void SetUrl(String url){
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
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            Intent navigationDrawerIntent = new Intent(MemberActivity.this, MemberActivity.class);
            startActivity(navigationDrawerIntent);
            finish();
        }
    }
}

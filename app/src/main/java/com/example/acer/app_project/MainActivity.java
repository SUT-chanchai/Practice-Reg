package com.example.acer.app_project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String check;
    Button button;
    int check_login;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        button = (Button) this.findViewById(R.id.button_login1);
        final Activity activity = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
        check_login = sp.getInt("check_login", 0);
        if(check_login==1){
            syncgetHttp http = new syncgetHttp();
            http.SetUrl("http://it2.sut.ac.th/script259g5/App/login_group.php?namegruop=" + sp.getString("id_group", ""));
            http.execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
               // Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("id_group", result.getContents());
                editor.commit();
                syncgetHttp http = new syncgetHttp();
                http.SetUrl("http://it2.sut.ac.th/script259g5/App/login_group.php?namegruop=" + result.getContents());
                http.execute();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void load(View view) {
        TextView t1 = (TextView) findViewById(R.id.username);
        SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("id_group", t1.getText().toString());
        editor.commit();
        syncgetHttp http = new syncgetHttp();
        http.SetUrl("http://it2.sut.ac.th/script259g5/App/login_group.php?namegruop=" + t1.getText());
        http.execute();
    }

    public class syncgetHttp extends AsyncTask<Void,Void,String> {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request;
        protected void SetUrl(String url){
            request = builder.url(url).build();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMax(100);
            progressDialog.setMessage("loading....");
            progressDialog.setTitle("Practice Reg");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();

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
            int check2;
            check = s;
            check = check.trim();
            check2 = Integer.parseInt(check);
            if (check2 == 0) {
                Toast.makeText(getApplicationContext(), "ID not true", Toast.LENGTH_LONG).show();
            } else if (check2 == 1) {
                SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("check_login", 1);
                editor.commit();
                //Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
                Intent navigationDrawerIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(navigationDrawerIntent);
                finish();
            }
            progressDialog.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}

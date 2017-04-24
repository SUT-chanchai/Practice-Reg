package com.example.acer.app_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        this.setTitle("เพิ่มสมาชิก");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public void add(View view) {
        EditText name = (EditText) findViewById(R.id.editText2);
        EditText phone = (EditText) findViewById(R.id.editText3);
        EditText d = (EditText) findViewById(R.id.editText44);
        EditText n = (EditText) findViewById(R.id.editText9);
        EditText email = (EditText) findViewById(R.id.editText7);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        SharedPreferences sp = getSharedPreferences("PREF_NAME", 0);
        syncgetHttp http = new syncgetHttp ();
        http.SetUrl("http://it2.sut.ac.th/script259g5/App/add.php?namegruop="+sp.getString("id_group", "")+"&sex="+spin.getSelectedItem().toString()+"&name="+name.getText()+"&phone="
                +phone.getText()+"&d="+d.getText()+"&email="+email.getText()+"&n="+n.getText());
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
            Intent navigationDrawerIntent = new Intent(AddMemberActivity.this, MemberActivity.class);
            startActivity(navigationDrawerIntent);
        }
    }
}

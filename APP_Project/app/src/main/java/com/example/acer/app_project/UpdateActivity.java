package com.example.acer.app_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        this.setTitle("แก้ไขข้อมูลสมาชิก");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.insert_data();
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    protected void insert_data(){
        Intent intent = getIntent();
        EditText name_T = (EditText) findViewById(R.id.editText2);
        EditText phone = (EditText) findViewById(R.id.editText3);
        EditText d = (EditText) findViewById(R.id.editText44);
        EditText n = (EditText) findViewById(R.id.editText9);
        EditText email = (EditText) findViewById(R.id.editText7);
        String name = intent.getStringExtra("name");
        String phone_s = intent.getStringExtra("phone");
        String email_s = intent.getStringExtra("e_mail");
        String d_s = intent.getStringExtra("disease");
        String n_s = intent.getStringExtra("note");
        name_T.setText(name);
        phone.setText(phone_s);
        email.setText(email_s);
        d.setText(d_s);
        n.setText(n_s);

    }

    public void update(View view) {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        EditText name_T = (EditText) findViewById(R.id.editText2);
        EditText phone = (EditText) findViewById(R.id.editText3);
        EditText d = (EditText) findViewById(R.id.editText44);
        EditText n = (EditText) findViewById(R.id.editText9);
        EditText email = (EditText) findViewById(R.id.editText7);
        syncgetHttp http = new syncgetHttp ();
        http.SetUrl("http://it2.sut.ac.th/script259g5/app/update.php?name="+name_T.getText()+"&phone="+phone.getText()+"&e_mail="+email.getText()+"&disease="+d.getText()+"&note="+n.getText()+"&id="+id);
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
            //Log.d("Awesome Tag", s);
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            Intent navigationDrawerIntent = new Intent(UpdateActivity.this, MemberActivity.class);
            startActivity(navigationDrawerIntent);
            finish();
        }
    }
}

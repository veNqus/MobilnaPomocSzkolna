package com.example.kacperp.zarzadzaniezadaniami;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cz.msebera.android.httpclient.Header;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import static android.R.attr.duration;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void Zaloguj(View viev) {
        //Intent StartNewActivity = new Intent(this,NastepnaAktywnosc.class);
        //startActivity(StartNewActivity);
        EditText Email = (EditText) findViewById(R.id.EmailText);
        EditText Haslo = (EditText) findViewById(R.id.PasswdText);
        RequestParams rp = new RequestParams();
        rp.put("Email", Email.getText().toString());
        rp.put("Haslo", Haslo.getText().toString());
        String connString = "http://192.168.0.11:3466/api/Login";
        final AsyncHttpClient client = new AsyncHttpClient();

        client.post(connString,rp, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String responseString = new String(response);
                if (responseString.contains("error")) {
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "Błędne hasło lub email użytkownika", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    UdaneLogowanie(responseString);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Bład połaczenia: "+ statusCode+" "+e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    public void UdaneLogowanie(String id)
    {
        String ID = id.replace("\"", "");
        SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("idusera", ID);
        editor.commit();

        Intent StartNewActivity = new Intent(this,PoZalogowaniu.class);
        startActivity(StartNewActivity);
    }
}

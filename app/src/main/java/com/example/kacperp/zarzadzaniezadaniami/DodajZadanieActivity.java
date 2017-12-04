package com.example.kacperp.zarzadzaniezadaniami;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DodajZadanieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_zadanie);

        SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
        String IdUsera = mPrefs.getString("idusera", "");

        final List<Zajecia> Zajecia = new ArrayList<Zajecia>();
        String connString = "http://192.168.0.11:3466/api/zajecia/?idUser="+IdUsera;
        final AsyncHttpClient client = new AsyncHttpClient();

        client.get(connString, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String responseString = new String(response);
                try {
                    JSONArray responeJSON = new JSONArray(responseString);
                    for (int i = 0; i < responeJSON.length(); i++) {

                        Zajecia zaj = new Zajecia();
                        JSONObject jsonobject = responeJSON.getJSONObject(i);

                        zaj.Id = jsonobject.getInt("Id");
                        zaj.Nazwa = jsonobject.getString("Nazwa");

                        Zajecia.add(zaj);
                    }



                    String[] listItems = new String[Zajecia.size()];

                    for(int i = 0; i < Zajecia.size(); i++){
                        Zajecia zaj = Zajecia.get(i);
                        listItems[i] = zaj.Nazwa;
                    }


                    final Spinner ListaPrzedmiotów = (Spinner) findViewById(R.id.spinner);
                    ArrayList<String> WszystkieZajecia = new ArrayList<String>();
                    WszystkieZajecia.addAll( Arrays.asList(listItems) );
                    Context context = getApplicationContext();

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,WszystkieZajecia);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ListaPrzedmiotów.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}

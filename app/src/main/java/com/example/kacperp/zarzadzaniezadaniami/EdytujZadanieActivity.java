package com.example.kacperp.zarzadzaniezadaniami;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class EdytujZadanieActivity extends AppCompatActivity {

    final List<Zajecia> Zajecia = new ArrayList<Zajecia>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edytuj_zadanie);

        SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
        String IdUsera = mPrefs.getString("idusera", "");


        String connString = "http://192.168.0.11:3466/api/zajecia/?idUser=" + IdUsera;
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
                    Zadania zad = (Zadania)getIntent().getSerializableExtra("Zadanie");
                    int PosInSpinner=0;
                    for (int i = 0; i < Zajecia.size(); i++) {
                        Zajecia zaj = Zajecia.get(i);
                        listItems[i] = zaj.Nazwa;
                        if(zaj.Nazwa == zad.NazwaZajec)
                        {
                            PosInSpinner=i;
                        }
                    }


                    final Spinner ListaPrzedmiotów = (Spinner) findViewById(R.id.spinner);
                    ArrayList<String> WszystkieZajecia = new ArrayList<String>();
                    WszystkieZajecia.addAll(Arrays.asList(listItems));
                    Context context = getApplicationContext();

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, WszystkieZajecia);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ListaPrzedmiotów.setAdapter(adapter);


                    EditText Temat = (EditText) findViewById(R.id.Temat);
                    EditText Szczególy = (EditText) findViewById(R.id.Szczególy);
                    EditText Termin = (EditText) findViewById(R.id.Termin);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    String DateInString = dateFormat.format(zad.Termin);
                    Temat.setText(zad.Temat);
                    Szczególy.setText(zad.Informacje);
                    Termin.setText(DateInString);
                    ListaPrzedmiotów.setSelection(PosInSpinner);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void EdytujZadanie(View view) {
        try {
            SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
            String IdUsera = mPrefs.getString("idusera", "");
            Zadania zad = (Zadania)getIntent().getSerializableExtra("Zadanie");

            Spinner ListaPrzedmiotów = (Spinner) findViewById(R.id.spinner);
            EditText Temat = (EditText) findViewById(R.id.Temat);
            EditText Szczególy = (EditText) findViewById(R.id.Szczególy);
            EditText Termin = (EditText) findViewById(R.id.Termin);
            if (Termin.getText().toString().length() == 10) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date Term = dateFormat.parse(Termin.getText().toString());
                String DateInString = dateFormat.format(Term);


                String NazwaPrzedmiotu = (String) ListaPrzedmiotów.getSelectedItem();
                int IdPrzedmiotu = 0;

                for (Zajecia zaj : Zajecia) {
                    if (zaj.Nazwa == NazwaPrzedmiotu) {
                        IdPrzedmiotu = zaj.Id;
                    }
                }

                RequestParams rp = new RequestParams();
                rp.put("Temat", Temat.getText().toString());
                rp.put("Szczególy", Szczególy.getText().toString());
                rp.put("Termin", DateInString);
                rp.put("IdPrzedmiotu", IdPrzedmiotu);
                rp.put("IdUzytkownika", IdUsera);
                rp.put("CzyZrobione", false);
                String connString = "http://192.168.0.11:3466/api/zadania/"+zad.Id;
                final AsyncHttpClient client = new AsyncHttpClient();

                client.put(connString, rp, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                        String responseString = new String(response);
                        if (responseString.contains("OK")) {
                            Context context = getApplicationContext();
                            Toast toast = Toast.makeText(context, "Zadanie zedytowane, wróć na stronę główną by wczytać zmiany", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            Context context = getApplicationContext();
                            Toast toast = Toast.makeText(context, "Błąd przy edycji", Toast.LENGTH_SHORT);
                            toast.show();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context, "Bład połaczenia: " + statusCode + " " + e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();
                    }

                });
            } else {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Błedny format daty", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

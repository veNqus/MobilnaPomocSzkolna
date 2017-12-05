package com.example.kacperp.zarzadzaniezadaniami;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class ZadaniaSzczegolyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zadania_szczegoly);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Zadania zad = (Zadania)getIntent().getSerializableExtra("Zadanie");

        TextView Temat =(TextView) findViewById(R.id.Temat);
        TextView Przedmiot =(TextView) findViewById(R.id.Przedmiot);
        TextView Termin =(TextView) findViewById(R.id.Termin);
        TextView DataDodania =(TextView) findViewById(R.id.Data);
        TextView Info =(TextView) findViewById(R.id.Szczegoly);

        Temat.setText(zad.Temat);
        Przedmiot.setText(zad.NazwaZajec);

        String termin = DateFormat.format("dd.MM.yyyy", zad.Termin).toString();
        Termin.setText(termin);

        String data = DateFormat.format("dd.MM.yyyy", zad.DataDodania).toString();
        DataDodania.setText(data);

        Info.setText(" "+zad.Informacje);
    }

    public void OznaczJakoZrobione(View view)
    {
        Zadania zad = (Zadania)getIntent().getSerializableExtra("Zadanie");
        RequestParams rp = new RequestParams();
        rp.put("CzyZrobione", true);
        String connString = "http://192.168.0.11:3466/api/zadania/"+zad.Id;
        final AsyncHttpClient client = new AsyncHttpClient();

        client.put(connString, rp, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String responseString = new String(response);
                if (responseString.contains("OK")) {
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "Zadanie zedytowane", Toast.LENGTH_SHORT);
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
    }

    public void Edytuj(View view)
    {
        Zadania zad = (Zadania)getIntent().getSerializableExtra("Zadanie");
        Intent intent = new Intent(ZadaniaSzczegolyActivity.this, EdytujZadanieActivity.class);
        intent.putExtra("Zadanie", zad);
        startActivity(intent);
    }
}

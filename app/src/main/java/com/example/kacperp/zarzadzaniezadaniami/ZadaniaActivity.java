package com.example.kacperp.zarzadzaniezadaniami;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ZadaniaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zadania);

        SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
        String IdUsera = mPrefs.getString("idusera", "");

        final List<Zadania> ZadaniaZApi = new ArrayList<Zadania>();
        String connString = "http://192.168.0.11:3466/api/zadania/?idUser="+IdUsera;
        final AsyncHttpClient client = new AsyncHttpClient();

        client.get(connString, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String responseString = new String(response);
                try {
                    JSONArray responeJSON = new JSONArray(responseString);
                    for (int i = 0; i < responeJSON.length(); i++) {
                        Zadania zad = new Zadania();
                        JSONObject jsonobject = responeJSON.getJSONObject(i);

                        zad.Id = jsonobject.getInt("Id");

                        zad.CzyZrobione = jsonobject.getBoolean("CzyZrobione");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        String DataDodaniaInString = jsonobject.getString("DataDodania");
                        DataDodaniaInString = DataDodaniaInString.substring(0,10);
                        Date DataDodania = dateFormat.parse(DataDodaniaInString);
                        zad.DataDodania = DataDodania;

                        zad.IdUzytkownika = jsonobject.getString("IdUzytkownika");

                        zad.Informacje = jsonobject.getString("Informacje");

                        zad.NazwaZajec = jsonobject.getString("NazwaZajec");

                        zad.Temat = jsonobject.getString("Temat");

                        String TerminInString = jsonobject.getString("Termin");
                        TerminInString = TerminInString.substring(0,10);
                        Date Termin = dateFormat.parse(TerminInString);
                        zad.Termin = Termin;

                        ZadaniaZApi.add(zad);
                    }
                    int i = 1;
                    final Context context = getApplicationContext();
                    TableLayout Tablica = (TableLayout) findViewById(R.id.TablicaZadan);

                    for (Zadania zad : ZadaniaZApi)
                    {
                        Date term = zad.Termin;

                        String pattern = "dd-MM-yyyy";
                        String CurrentDateInString =new SimpleDateFormat(pattern).format(new Date());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date CurrentDate = dateFormat.parse(CurrentDateInString);
                        final Zadania ZadToPass = zad;

                        if(CurrentDate.compareTo(term) <= 0) {

                            TableRow Rzad = new TableRow(context);

                            TextView Temat = new TextView(context);
                            Temat.setText(zad.Temat);
                            Temat.setGravity(Gravity.CENTER);
                            Temat.isClickable();

                            TextView Przedmiot = new TextView(context);
                            Przedmiot.setText(zad.NazwaZajec);
                            Przedmiot.setGravity(Gravity.CENTER);


                            TextView Termin = new TextView(context);
                            String termin = DateFormat.format("dd.MM.yyyy", term).toString();
                            Termin.setText(termin);
                            Termin.setGravity(Gravity.CENTER);

                            TextView info = new TextView(context);
                            info.setText(" INFO ");
                            info.setTextColor(Color.RED);
                            info.setGravity(Gravity.CENTER);
                            info.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(ZadaniaActivity.this, ZadaniaSzczegolyActivity.class);
                                    intent.putExtra("Zadanie", ZadToPass);
                                    startActivity(intent);

                                }
                            });


                            Rzad.addView(Temat);
                            Rzad.addView(Przedmiot);
                            Rzad.addView(Termin);
                            Rzad.addView(info);
                            Tablica.addView(Rzad, i);
                            i++;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

            }
        });
    }

    public void DodajZadania(View view)
    {
        Intent intent = new Intent(ZadaniaActivity.this, DodajZadanieActivity.class);
        startActivity(intent);
    }
}

package com.example.kacperp.zarzadzaniezadaniami;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ZadaniaSzczegolyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zadania_szczegoly);


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
}

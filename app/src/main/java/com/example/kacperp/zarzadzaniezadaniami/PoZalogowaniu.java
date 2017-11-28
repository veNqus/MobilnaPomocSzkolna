package com.example.kacperp.zarzadzaniezadaniami;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PoZalogowaniu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po_zalogowaniu);



      /*

        SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
        String IdUsera = mPrefs.getString("idusera", "");

        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "IDUsera = " + IdUsera, Toast.LENGTH_SHORT);
        toast.show();

      */
    }

    protected void ZajeciaBtnClick(View view)
    {
        Intent StartNewActivity = new Intent(this,ZajeciaActivity.class);
        startActivity(StartNewActivity);
    }

    protected void ZadaniaBtnClick(View view)
    {
        Intent StartNewActivity = new Intent(this,ZadaniaActivity.class);
        startActivity(StartNewActivity);
    }
}

package com.example.cashupnd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Nav extends AppCompatActivity {

    private ConexionSQLite bd;
    TextView nombre;
    TextView numero;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);
        nombre = findViewById(R.id.nombre);
        numero = findViewById(R.id.number);
        bd = new ConexionSQLite(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = prefs.getString("user","nulo");

        nombre.setText(bd.getNombreApellidos(username));
        numero.setText(bd.getNumero(username));



    }
}
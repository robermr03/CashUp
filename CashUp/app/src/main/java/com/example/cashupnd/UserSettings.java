package com.example.cashupnd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import es.dmoral.toasty.Toasty;

public class UserSettings extends AppCompatActivity {
    EditText et_nuevaContrasena;
    Button cambiarContrasena;
    Button cerrarSesion;
    Button borrarCuenta;
    private ConexionSQLite bd;
    boolean bool;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bd = new ConexionSQLite(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        et_nuevaContrasena = findViewById(R.id.et_nuevaContrasena);
        cambiarContrasena = findViewById(R.id.camCon);
        cerrarSesion = findViewById(R.id.cerSes);
        borrarCuenta = findViewById(R.id.borCue);




        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettings.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
        borrarCuenta.setOnClickListener(new View.OnClickListener() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String username = prefs.getString("user","nulo");
            @Override
            public void onClick(View view) {
                bd.BorrarUsuario(username);
                Toasty.success(getApplicationContext(),"Usuario borrado correctamente", Toast.LENGTH_SHORT, true).show();
                Intent intent = new Intent(UserSettings.this,Login.class);
                startActivity(intent);
                }
        });
        cambiarContrasena.setOnClickListener(new View.OnClickListener() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String username = prefs.getString("user","nulo");
            @Override
            public void onClick(View v) {
                try {
                    bd.cambiarContrasena(username,et_nuevaContrasena.getText().toString());
                    Toasty.success(getApplicationContext(),"Contraseña cambiada correctamente", Toast.LENGTH_SHORT, true).show();
                }catch (Exception e){
                    e.toString();
                    Toasty.error(getApplicationContext(),"Error al cambiar contraseña", Toast.LENGTH_SHORT, true).show();
                }


            }
        });
    }
}
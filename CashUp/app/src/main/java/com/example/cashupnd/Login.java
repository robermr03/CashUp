package com.example.cashupnd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashupnd.ui.inicio.InicioFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {

    TextView registerTextClick;
    private ConexionSQLite bd;
    ArrayList<Usuario> usuariosbd;
    ArrayList<Integer> idsUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button botonLogin = findViewById(R.id.buttonLogin);
        registerTextClick = findViewById(R.id.clickTextRegister);
        EditText user = findViewById(R.id.textoUsuario);
        EditText passwd = findViewById(R.id.textoContrasena);

        bd = new ConexionSQLite(this);

        try {
            idsUsuarios = bd.obtenerIds();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            usuariosbd = bd.obtenerTodosUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("USUARIOBDD",String.valueOf(idsUsuarios));

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        registerTextClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent inRegister = new Intent(Login.this, Register.class);
                    startActivity(inRegister);
                    finish();
            }
        });



        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String password = passwd.getText().toString();
                Boolean checkuserpass = bd.checkusernamepassword(username,password);
                if (checkuserpass==true){
                    Toasty.success(getApplicationContext(), "Sesión iniciada correctamente", Toast.LENGTH_SHORT, true).show();
                    Intent in = new Intent(Login.this, MainActivity.class);
                    in.putExtra("username", username);
                    startActivity(in);
                    finish();
                }else{
                    user.setText("");
                    passwd.setText("");
                    Toasty.error(getApplicationContext(), "Creedenciales incorrectas", Toast.LENGTH_SHORT, true).show();
                }
            }

        });

    }

    @Override
    public void onBackPressed() {

    }
}
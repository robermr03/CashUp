package com.example.cashupnd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Register extends AppCompatActivity {
    EditText etNombre;
    EditText etApellidos;
    EditText etUsuario;
    EditText etContrasena;
    EditText etRepContrasena;
    TextView loginTextClick;
    Button registerButton;
    ArrayList<Integer> idsUsuarios;
    private ConexionSQLite bd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombre = findViewById(R.id.et_registerNombre);
        etApellidos = findViewById(R.id.et_registerApellidos);
        etUsuario = findViewById(R.id.et_registerUsuario);
        etContrasena = findViewById(R.id.et_registerContrasena);
        etRepContrasena = findViewById(R.id.et_registerRepContrasena);
        registerButton = findViewById(R.id.buttonRegister);
        loginTextClick = findViewById(R.id.clickTextInic);

        bd = new ConexionSQLite(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loginTextClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inLogin = new Intent(Register.this, Login.class);
                startActivity(inLogin);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idsUsuarios = bd.obtenerIds();
                int id = idsUsuarios.get(idsUsuarios.size()-1) + 1;


                if (!etNombre.getText().toString().equals("") && !etApellidos.getText().toString().equals("") && !etUsuario.getText().toString().equals("") && !etContrasena.getText().toString().equals("")
                && !etRepContrasena.getText().toString().equals("")){
                    if (etContrasena.getText().toString().equals(etRepContrasena.getText().toString())){
                        String nombre = etNombre.getText().toString();
                        String apellidos = etApellidos.getText().toString();
                        String usuario = etUsuario.getText().toString();
                        String contrasena = etContrasena.getText().toString();

                        ArrayList<String> usernames = bd.obtenerUsuarios();
                        int comprobarDispo=0;
                        for (int i =0; i<usernames.size();i++) {
                            if (usernames.get(i)!=null){
                                if (usernames.get(i).equals(usuario)) {
                                    comprobarDispo++;
                                }
                            }
                            else{
                                comprobarDispo=0;
                            }
                        }

                        if (comprobarDispo == 0){
                            try {
                                bd.insertarUsuario(id,nombre,apellidos,usuario,contrasena,1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toasty.info(getApplicationContext(),"Usuario creado correctamente", Toasty.LENGTH_SHORT).show();
                            etNombre.setText("");
                            etApellidos.setText("");
                            etUsuario.setText("");
                            etContrasena.setText("");
                            etRepContrasena.setText("");
                        }else{
                            Toasty.error(getApplicationContext(),"Usuario no disponible", Toasty.LENGTH_LONG, true ).show();
                            etUsuario.setText("");
                        }


                    }else{
                        Toasty.error(getApplicationContext(),"La contraseña no coincide", Toast.LENGTH_SHORT, true).show();
                    }
                }else{
                    Toasty.error(getApplicationContext(),"Rellene todos los campos", Toast.LENGTH_SHORT, true).show();
                }



            }
        });


    }

    @Override
    public void onBackPressed() {

    }
}
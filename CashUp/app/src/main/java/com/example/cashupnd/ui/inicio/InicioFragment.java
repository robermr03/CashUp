package com.example.cashupnd.ui.inicio;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashupnd.ConexionSQLite;
import com.example.cashupnd.MainActivity;
import com.example.cashupnd.R;

public class InicioFragment extends Fragment implements View.OnClickListener{

    private Button boton;
    private String username;
    private TextView tvUsername;
    private ConexionSQLite bd;
    private TextView personname;
    private TextView code;
    private TextView cvv;
    private TextView caducidad;

    public static InicioFragment newInstance() {
        return new InicioFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = new ConexionSQLite(getActivity().getApplicationContext());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        username = prefs.getString("user","nulo");

    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ;
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        tvUsername = view.findViewById(R.id.tv2);
        personname = view.findViewById(R.id.personname);
        code = view.findViewById(R.id.code);
        cvv = view.findViewById(R.id.cvv);
        caducidad = view.findViewById(R.id.caducidad);
        tvUsername.setText("Bienvenido " + username + "!");

        personname.setText(bd.getNombreApellidos(username));
        code.setText(bd.getNumero(username));
        cvv.setText(bd.getCvv(username));
        caducidad.setText(bd.getCaducidad(username));

        return view;
    }


    @Override
    public void onClick(View view) {
    }


}
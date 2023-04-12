package com.example.cashupnd.ui.transferencias;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashupnd.ConexionSQLite;
import com.example.cashupnd.FriendList;
import com.example.cashupnd.Login;
import com.example.cashupnd.MainActivity;
import com.example.cashupnd.R;

import es.dmoral.toasty.Toasty;

public class TransferenciasFragment extends Fragment implements View.OnClickListener{


    private Button Enviar, Users;
    private EditText DineroEnviar, IbanDestinatario;
    private TextView Dinero;
    private ImageView Imagen;
    String username;
    String userEnviar;
    ConexionSQLite bd;
    View v;

    public static TransferenciasFragment newInstance() {
        return new TransferenciasFragment();
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        bd = new ConexionSQLite(getActivity().getApplicationContext());
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        username = prefs.getString("user", "nulo");
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_transferencias, container, false);
        super.onCreate(savedInstanceState);
        Imagen = v.findViewById(R.id.Imagen);
        Enviar = v.findViewById(R.id.Enviar);
        Enviar.setOnClickListener(this);
        DineroEnviar = (EditText) v.findViewById(R.id.DineroEnviar);
        IbanDestinatario = v.findViewById(R.id.IbanDestinatario);
        Dinero= v.findViewById(R.id.Dinero);
        Dinero.setText(String.valueOf(bd.ObtenerSaldo(username)));

        Users = v.findViewById(R.id.buttonUsers);
        Users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity().getApplicationContext(), FriendList.class);
                startActivity(in);
            }
        });
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Enviar:
                int dineroAEnviar = 0;
                double dineroSaldo = Double.parseDouble(Dinero.getText().toString());
                String res = "0";
                if (Enviar.isClickable()) {
                    if (DineroEnviar.getText().toString().equals("")){
                        dineroAEnviar = 0;
                    }else{
                        dineroAEnviar = Integer.parseInt(DineroEnviar.getText().toString());
                    }
                    if (dineroAEnviar==0){
                        Toasty.error(getActivity().getApplicationContext(),"Introduzca el importe", Toasty.LENGTH_SHORT).show();
                    }else{
                        if(IbanDestinatario.getText().toString().equals("")){
                            Toasty.error(getActivity().getApplicationContext(),"Introduzca el nombre de usuario",
                                    Toasty.LENGTH_SHORT).show();
                        }else{
                            if ((dineroSaldo - dineroAEnviar) <0){
                                Dinero.setTextColor(Color.parseColor("#FF0000"));
                            }
                            if(username.equals(IbanDestinatario.getText().toString())){
                                Toasty.error(getActivity().getApplicationContext(),"No puedes enviar dinero a tu cuenta",
                                        Toasty.LENGTH_SHORT).show();

                            }else{
                                res =String.valueOf(dineroSaldo - dineroAEnviar);
                                try {
                                    bd.realizarTransferencia(dineroAEnviar,username,IbanDestinatario.getText().toString());
                                    Toasty.success(getActivity().getApplicationContext(),"Transferencia realizada correctamente", Toasty.LENGTH_SHORT).show();
                                    DineroEnviar.setText("");
                                    IbanDestinatario.setText("");
                                    Dinero.setText(String.valueOf(bd.ObtenerSaldo(username)));
                                }catch(Exception e){
                                    e.getStackTrace();
                                    Toasty.error(getActivity().getApplicationContext(),"Error al realizar transferencia" + e.toString(), Toasty.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }



                }

        }
    }

}



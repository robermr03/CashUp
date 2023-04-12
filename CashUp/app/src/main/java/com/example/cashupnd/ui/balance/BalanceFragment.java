package com.example.cashupnd.ui.balance;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cashupnd.ConexionSQLite;
import com.example.cashupnd.R;

public class BalanceFragment extends Fragment {


    private ConexionSQLite bd;
    private String username;
    private TextView dinero;

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = new ConexionSQLite(getActivity().getApplicationContext());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        username = prefs.getString("user","nulo");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        dinero = view.findViewById(R.id.dinero);
        dinero.setText(String.valueOf(bd.ObtenerSaldo(username)) + " €");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }
}
  


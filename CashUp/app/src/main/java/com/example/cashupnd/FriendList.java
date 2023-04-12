package com.example.cashupnd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FriendList extends AppCompatActivity {


    List<ListElement> elements;
    ConexionSQLite bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        bd = new ConexionSQLite(this);

        ArrayList<String> usuarios = bd.obtenerUsuarios();

        elements = new ArrayList<>();
        for (int i = 0; i<usuarios.size();i++){
            elements.add(new ListElement(bd.getNombreApellidos(usuarios.get(i)),bd.getNumero(usuarios.get(i)),"@" + usuarios.get(i)));
        }
        Log.i("COUNTUSERS", elements.toString());

        ListAdapter listAdapter = new ListAdapter(elements,this);
        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    public void init() throws Exception {


    }
}
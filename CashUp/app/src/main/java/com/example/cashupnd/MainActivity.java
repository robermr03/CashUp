package com.example.cashupnd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashupnd.ui.inicio.InicioFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.cashupnd.databinding.ActivityMainBinding;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private ConexionSQLite bd;
    public static final String KEY = "text";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public String username;
    NavigationView navigationView;
    private int cont=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cont++;
        super.onCreate(savedInstanceState);

        username = getIntent().getStringExtra("username");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user",username);
        editor.commit();



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);



        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Boolean prefBalance= prefs.getBoolean("prefBalance",true);
        Boolean prefTrans= prefs.getBoolean("prefTrans",true);

        if (!prefBalance){
            navigationView.getMenu().findItem(R.id.nav_balance).setVisible(false);
        }
        if (!prefTrans){
            navigationView.getMenu().findItem(R.id.nav_transferencias).setVisible(false);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        TextView nombre;
        TextView numero;
        nombre = findViewById(R.id.nombre);
        numero = findViewById(R.id.number);
        bd = new ConexionSQLite(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = prefs.getString("user","nulo");

        nombre.setText(bd.getNombreApellidos(username));
        numero.setText(bd.getNumero(username));
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.settings:
                Intent settings = new Intent(MainActivity.this,Settings.class);
                settings.putExtra("username", username);
                startActivity(settings);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_balance).setVisible(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
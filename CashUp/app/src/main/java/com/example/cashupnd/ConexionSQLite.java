package com.example.cashupnd;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.xml.transform.Result;

public class ConexionSQLite extends SQLiteOpenHelper {

    private Context contexto;
    private final String SQLCREATE = "CREATE TABLE Usuarios (id INTEGER, nombre TEXT, apellidos TEXT,usuario TEXT unique, contrasena TEXT, balance REAL, numero TEXT, cvv INTEGER, caducidad DATE)";
    private final String SQLDROP = "DROP TABLE IF EXISTS Usuarios";

    private SQLiteDatabase bd = null;

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "DBUsuarios.db";

    public ConexionSQLite(Context contexto){
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
        this.contexto=contexto;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLCREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLDROP);
        db.execSQL(SQLCREATE);
    }

    //public void cerrarBD(){
      //  if (bd != null){
        //    bd.close();
        //}
    //}
    public void insertarUsuario(int id, String nombre, String apellidos, String usuario, String contrasena, double balance) throws Exception {
        bd = getWritableDatabase();

        if (bd!= null){

            long newRowId;
            try {

                String numero = "";
                Random r = new Random();
                for (int i=0; i<4 ; i++){
                    int low = 1000;
                    int high = 9999;
                    int result = r.nextInt(high-low) + low;
                    numero = numero + " " +  String.valueOf(result);
                }
                int low = 100;
                int high = 999;
                int cvv = r.nextInt(high-low) + low;
                String numeroFinal =numero.substring(1);

                ContentValues values = new ContentValues();
                values.put("id", id);
                values.put("nombre", nombre);
                values.put("apellidos", apellidos);
                values.put("usuario", usuario);
                values.put("contrasena", contrasena);
                values.put("balance", balance);
                values.put("numero", numeroFinal);
                values.put("cvv", cvv);
                values.put("caducidad", getFechaRandom());

                newRowId = bd.insertWithOnConflict("Usuarios", "", values,SQLiteDatabase.CONFLICT_FAIL);
                bd.close();
            }catch (Exception e){
                throw new Exception(e.toString());
            }
        }
    }
    public ArrayList<Usuario> obtenerTodosUsuarios() throws Exception{
        ArrayList<Usuario> usuarios = new ArrayList<>();
        bd = getReadableDatabase();

        try {
            String sortOrder = "id ASC";
            Cursor c = bd.query(
                    "Usuarios",
                    null,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );
            c.moveToFirst();
            if (c.getCount()>0){
                do {
                    Usuario usuarioAdd = null;
                    int id = c.getInt(0);
                    String nombre = c.getString(1);
                    String apellidos = c.getString(2);
                    String usuario = c.getString(3);
                    String contrasena = c.getString(4);
                    double balance = c.getDouble(5);
                    String numero = c.getString(6);
                    usuarioAdd.setId(id);
                    usuarioAdd.setNombre(nombre);
                    usuarioAdd.setApellidos(apellidos);
                    usuarioAdd.setUsuario(usuario);
                    usuarioAdd.setUsuario(contrasena);
                    usuarioAdd.setBalance(balance);
                    usuarioAdd.setNumero(numero);
                    usuarios.add(usuarioAdd);
                }while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    @SuppressLint("Range")
    public ArrayList<Integer> obtenerIds(){
        ArrayList<Integer> Ids = new ArrayList<>();
        bd = getReadableDatabase();

        Cursor c = bd.query("Usuarios", new String[]{"id"},null,null,null,null,"id ASC");

        if (c!=null){
            c.moveToFirst();
                do {
                    @SuppressLint("Range")
                    int id;
                    if (c.getCount()!=0) {
                        id = c.getInt(c.getColumnIndex("id"));
                    } else {
                        id=0;
                    }
                    Ids.add(id);
                }while(c.moveToNext());
        }
        c.close();
        //cerrarBD();
        return Ids;
        }
    public Boolean checkusernamepassword(String usuario, String contrasena){
        bd = this.getWritableDatabase();
        Cursor cursor = bd.rawQuery("select * from Usuarios where usuario = ? and contrasena = ?", new String[]{usuario,contrasena});
        if (cursor.getCount()>0){
            return true;
        }else
            return false;
    }
    @SuppressLint("Range")
    public int obtenerId(String usuario){
        int id=0;
        bd = this.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT id FROM Usuarios where usuario=?", new String[]{usuario});
        if (cursor!=null){
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }
        return id;
    }
    public void BorrarUsuario(String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete("Usuarios", "usuario=?", new String[]{username});
        db.close();
    }
    public void cambiarContrasena(String usuario, String contrasena){
        bd = this.getWritableDatabase();
        bd.execSQL("UPDATE Usuarios SET contrasena =? where usuario =?",new String[]{contrasena,usuario});
        bd.close();
    }
    @SuppressLint("Range")
    public double ObtenerSaldo(String usuario) {
        bd=this.getReadableDatabase();
        Cursor c = bd.rawQuery("select balance from Usuarios where usuario=?", new String[]{usuario});
        double resultado = 0;
        if(c!=null){
            c.moveToFirst();
            resultado = c.getDouble(c.getColumnIndex("balance"));
        }
        return resultado;
    }

    public void realizarTransferencia(double cantidad, String envio, String recibo){
        bd = this.getWritableDatabase();
        double cantidadPreEnvio = ObtenerSaldo(envio);
        double cantidadPreRecibo = ObtenerSaldo(recibo);
        double cantidadTotalEnvio = cantidadPreEnvio-cantidad;
        double cantidadTotalRecibo = cantidadPreRecibo+cantidad;
        bd.execSQL("UPDATE Usuarios SET balance =? where usuario =?",new String[]{String.valueOf(cantidadTotalEnvio),envio});
        bd.execSQL("UPDATE Usuarios SET balance =? where usuario =?",new String[]{String.valueOf(cantidadTotalRecibo),recibo});
        bd.close();
    }
    @SuppressLint("Range")
    public String getNombreApellidos(String username){
        String nombreApellidos = null;
        bd=this.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM Usuarios WHERE usuario = '"+username+"'", null);
        if(c!=null){
            c.moveToFirst();
            nombreApellidos = (c.getString(c.getColumnIndex("nombre")) + " " + c.getString(c.getColumnIndex("apellidos")));
        }
        return nombreApellidos;
    }
    @SuppressLint("Range")
    public String getNumero(String username){
        String numero = "";
        bd=this.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM Usuarios WHERE usuario = '"+username+"'", null);
        if(c!=null){
            c.moveToFirst();
            numero = (c.getString(c.getColumnIndex("numero")));
        }
        return  numero;
    }
    @SuppressLint("Range")
    public ArrayList<String> obtenerUsuarios(){
        ArrayList<String> usernames= new ArrayList<>();
        bd=this.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT usuario FROM Usuarios", null);
        Log.i("PRUEBA", String.valueOf(c.getCount()));
        if (c!=null){
            c.moveToFirst();
            do {
                @SuppressLint("Range")
                String username;
                if (c.getCount()!=0) {
                    username = c.getString(c.getColumnIndex("usuario"));
                } else {
                    username = null;
                }
                usernames.add(username);
            }while(c.moveToNext());
        }else{

        }

        return usernames;
    }
    public static String getFechaRandom() {
        final java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd/MM/yyyy");

        Random r = new Random();
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(java.util.Calendar.YEAR, Math.abs(r.nextInt(2035 - 2022) + 2022));
        c.set(java.util.Calendar.MONTH, Math.abs(r.nextInt()) % 12);
        if (c.MONTH == 2) {
            c.set(java.util.Calendar.DAY_OF_MONTH, Math.abs(r.nextInt()) % 28);
        }else if (c.MONTH == 1 || c.MONTH == 3 || c.MONTH == 5 || c.MONTH == 7 || c.MONTH == 8 || c.MONTH == 10 || c.MONTH == 12) {
            c.set(java.util.Calendar.DAY_OF_MONTH, Math.abs(r.nextInt()) % 31);
        }else {
            c.set(java.util.Calendar.DAY_OF_MONTH, Math.abs(r.nextInt()) % 30);
        }

        c.setLenient(true);
        return DATE_FORMAT.format(c.getTime());

    }
    @SuppressLint("Range")
    public String getCvv(String username){
        String cvv = "";
        bd=this.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM Usuarios WHERE usuario = '"+username+"'", null);
        if(c!=null){
            c.moveToFirst();
            cvv = (c.getString(c.getColumnIndex("cvv")));
        }
        return  cvv;
    }
    @SuppressLint("Range")
    public String getCaducidad(String username){
        final java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String caducidad = "";
        bd=this.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM Usuarios WHERE usuario = '"+username+"'", null);
        if(c!=null){
            c.moveToFirst();
            caducidad = (c.getString(c.getColumnIndex("caducidad")));
        }
        return caducidad;
    }
    }





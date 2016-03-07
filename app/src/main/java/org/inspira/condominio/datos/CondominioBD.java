package org.inspira.condominio.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * adb -d logcat com.example.example:I *:S
 */
public class CondominioBD extends SQLiteOpenHelper {

    public CondominioBD(Context context){
        super(context, "Condominio", null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        dataBase.execSQL("create table Usuario(" +
                "email TEXT NOT NULL PRIMARY KEY," +
                "nickname TEXT NOT NULL," +
                "dateOfBirth long not null" +
                ")");
        dataBase.execSQL("create table InformacionAdmin(" +
                "email text not null primary key," +
                "sello text not null" +
                ")");
        dataBase.execSQL("create table Convocatoria(" +
                "idConvocatoria INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "Asunto TEXT NOT NULL," +
                "Condominio TEXT," +
                "Ubicacion TEXT," +
                "Ubicacion_Interna TEXT," +
                "Firma TEXT," +
                "Fecha_de_Inicio LONG," +
                "email TEXT NOT NULL," +
                "FOREIGN KEY (email) REFERENCES Usuario(email)" +
                ")");
        dataBase.execSQL("create table Punto_OdD(" +
                "idPunto_OdD INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Descripcion TEXT NOT NULL," +
                "idConvocatoria INTEGER NOT NULL," +
                "FOREIGN KEY(idConvocatoria) REFERENCES Convocatoria(idConvocatoria)" +
                ")");
        dataBase.execSQL("create table Razon_de_Ingreso(" +
                "idRazon_de_Ingreso INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "Razon_de_Ingreso TEXT NOT NULL" +
                ")");
        dataBase.execSQL("create table Concepto_de_Ingreso(" +
                "idConcepto_de_Ingreso integer not null primary key autoincrement," +
                "Concepto_de_Ingreso text not null" +
                ")");
        dataBase.execSQL("create table Ingreso(" +
                "idIngreso INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "idRazon_de_Ingreso INTEGER NOT NULL," +
                "idConcepto_de_Ingreso integer NOT NULL," +
                "monto float not null," +
                "nombre text not null," +
                "departamento text," +
                "fecha text not null," +
                "email text not null," +
                "foreign key(idRazon_de_Ingreso) references Razon_de_Ingreso(idRazon_de_Ingreso)," +
                "foreign key(idConcepto_de_Ingreso) references Concepto_de_Ingreso(idConcepto_de_Ingreso)," +
                "foreign key(email) references InformacionAdmin(email)" +
                ")");
        dataBase.execSQL("create table Razon_de_Egreso(" +
                "idRazon_de_Egreso integer not null primary key autoincrement," +
                "Razon_de_Egreso text not null" +
                ")");
        dataBase.execSQL("create table Egreso(" +
                "idEgreso INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "idRazon_de_Egreso integer not null," +
                "monto float not null," +
                "favorecido text not null," +  // Debe ser un string que venga del server de un habitante registrado.
                "fecha long not null," +
                "email text not null," +
                "foreign key(idRazon_de_Egreso) references Razon_de_Egreso(idRazon_de_Egreso)," +
                "foreign key(email) references InformacionAdmin(email)" +
                ")");
    }

    /***
     *
     * Zona de inserts
     *
     ***********************/

    public void agregarUsuario(Usuario usuario){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", usuario.getEmail());
        values.put("nickname", usuario.getNickname());
        values.put("dateOfBirth", usuario.getDateOfBirth());
        db.insert("Usuario", "---", values);
        db.close();
    }

    public void agregaSello(String email, String sello){
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("sello", sello);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("InformacionAdmin", "---", values);
        db.close();
    }

    public int insertaConvocatoria(Convocatoria conv, String email){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idConvocatoria", conv.getId());
        values.put("Asunto", conv.getAsunto());
        values.put("Condominio", conv.getCondominio());
        values.put("Ubicacion", conv.getUbicacion());
        values.put("Ubicacion_Interna", conv.getUbicacionInterna());
        values.put("Firma", conv.getFirma());
        values.put("Fecha_de_Inicio", conv.getFechaInicio());
        values.put("email", email);
        db.insert("Convocatoria", "---", values);
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select last_insert_rowid() v", null);
        int id = -1;
        if(c.moveToNext()){
            id = c.getInt(0);
        }
        db.close();
        return id;
    }

    public void insertaPuntoOdD(PuntoOdD punto){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Descripcion", punto.getDescripcion());
        values.put("idConvocatoria", punto.getIdConvocatoria());
        db.insert("Punto_OdD", "---", values);
        db.close();
    }

    /*************************
     *
     *      Zona de selecciones
     *
     ******************************/
    public Convocatoria[] obtenerConvocatorias(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from Convocatoria", null);
        List<Convocatoria> convocatorias = new ArrayList<>();
        while(c.moveToNext()){
            Convocatoria convocatoria = new Convocatoria(c.getInt(c.getColumnIndex("idConvocatoria")));
            convocatoria.setAsunto(c.getString(c.getColumnIndex("Asunto")));
            convocatoria.setCondominio(c.getString(c.getColumnIndex("Condominio")));
            convocatoria.setUbicacion(c.getString(c.getColumnIndex("Ubicacion")));
            convocatoria.setUbicacionInterna(c.getString(c.getColumnIndex("Ubicacion_Interna")));
            convocatoria.setFirma(c.getString(c.getColumnIndex("Firma")));
            convocatoria.setFechaInicio(c.getLong(c.getColumnIndex("Fecha_de_Inicio")));
            Cursor c2 = db.rawQuery("select idPunto_OdD,Descripcion from Punto_OdD where idConvocatoria = CAST(? as INTEGER)",
                    new String[]{String.valueOf(convocatoria.getId())});
            List<PuntoOdD> puntos = new ArrayList<>();
            while(c2.moveToNext()) {
                PuntoOdD punto = new PuntoOdD(c2.getInt(c2.getColumnIndex("idPunto_OdD")));
                punto.setDescripcion(c2.getString(c2.getColumnIndex("Descripcion")));
                punto.setIdConvocatoria(convocatoria.getId());
                puntos.add(punto);
            }
            c2.close();
            convocatoria.setPuntos(puntos);
            convocatorias.add(convocatoria);
        }
        c.close();
        db.close();
        return convocatorias.toArray(new Convocatoria[0]);
    }

    /***
     *
     * Zona de Revisiones
     *
     **********************/


    public boolean revisarExistenciaDeUsuarios(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from Usuario", null);
        boolean exists = c.moveToNext();
        c.close();
        return exists;
    }

    public boolean revisaExistenciaDeSello(String email){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select sello from InformacionAdmin where email = ?", new String[]{email});
        boolean resultado = c.moveToNext();
        c.close();
        db.close();
        return resultado;
    }
}
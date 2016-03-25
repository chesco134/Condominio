package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.datos.PuntoOdD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 21/03/16.
 */
public class AccionesTablaConvocatoria {

    public static void agregaConvocatoria(Context context, Convocatoria convocatoria){
        ContentValues values = new ContentValues();
        values.put("idConvocatoria", convocatoria.getId());
        values.put("Asunto", convocatoria.getAsunto());
        values.put("Fecha_de_Inicio", convocatoria.getFechaInicio());
        values.put("Ubicacion_Interna", convocatoria.getUbicacionInterna());
        values.put("firma", convocatoria.getFirma());
        values.put("email", convocatoria.getEmail());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Convocatoria", "---", values);
        db.close();
    }

    public static void agregaPuntoOdD(Context context, PuntoOdD punto){
        ContentValues values = new ContentValues();
        values.put("Descripcion", punto.getDescripcion());
        values.put("idConvocatoria", punto.getIdConvocatoria());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Punto_OdD", "---", values);
        db.close();
    }

    public static Convocatoria obtenerConvocatoria(Context context, int idConvocatoria){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Convocatoria where idConvocatoria = CAST(? as INTEGER)", new String[]{String.valueOf(idConvocatoria)});
        Convocatoria convocatoria;
        if(c.moveToFirst()){
            convocatoria = new Convocatoria(idConvocatoria);
            convocatoria.setAsunto(c.getString(c.getColumnIndex("Asunto")));
            convocatoria.setFechaInicio(c.getLong(c.getColumnIndex("Fecha_de_Inicio")));
            convocatoria.setUbicacionInterna(c.getString(c.getColumnIndex("Ubicacion_Interna")));
            convocatoria.setEmail(c.getString(c.getColumnIndex("email")));
        }else{
            convocatoria = null;
        }
        db.close();
        c.close();
        return convocatoria;
    }

    public static PuntoOdD[] obtenerPuntosOdD(Context context, int idConvocatoria){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Punto_OdD where idConvocatoria = CAST(? as INTEGER)", new String[]{String.valueOf(idConvocatoria)});
        List<PuntoOdD> puntos = new ArrayList<>();
        PuntoOdD punto;
        while(c.moveToNext()){
            punto = new PuntoOdD(c.getInt(c.getColumnIndex("idPunto_OdD")));
            punto.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
            punto.setIdConvocatoria(idConvocatoria);
            puntos.add(punto);
            Log.d("DB", "Added convocatoria: " + idConvocatoria + " -- " + punto.getDescripcion());
        }
        c.close();
        db.close();
        return puntos.toArray(new PuntoOdD[0]);
    }
}

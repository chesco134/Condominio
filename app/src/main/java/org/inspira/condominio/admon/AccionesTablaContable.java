package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.inspira.condominio.datos.ConceptoDeIngreso;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Egreso;
import org.inspira.condominio.datos.Ingreso;
import org.inspira.condominio.datos.RazonDeEgreso;
import org.inspira.condominio.datos.RazonDeIngreso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 9/04/16.
 */
public class AccionesTablaContable {

    public static List<String> obtenerRazonesDeIngreso(Context context){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select Razon_de_Ingreso from Razon_de_Ingreso", null);
        List<String> razonesDeIngreso = new ArrayList<>();
        while(c.moveToNext())
            razonesDeIngreso.add(c.getString(0));
        c.close();
        db.close();
        return  razonesDeIngreso;
    }

    public static void agregarRazonDeIngreso(Context context, RazonDeIngreso razonDeIngreso){
        ContentValues values = new ContentValues();
        values.put("idRazon_de_Ingreso", razonDeIngreso.getId());
        values.put("Razon_de_Ingreso", razonDeIngreso.getRazonDeIngreso());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Razon_de_Ingreso", "---", values);
        db.close();
    }

    public static List<String> obtenerConceptosDeIngreso(Context context){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select Concepto_de_Ingreso from Concepto_de_Ingreso", null);
        List<String> conceptosDeIngreso = new ArrayList<>();
        while(c.moveToNext())
            conceptosDeIngreso.add(c.getString(0));
        c.close();
        db.close();
        return  conceptosDeIngreso;
    }

    public static void agregarConceptoDeIngreso(Context context, ConceptoDeIngreso conceptoDeIngreso){
        ContentValues values = new ContentValues();
        values.put("idConcepto_de_Ingreso", conceptoDeIngreso.getId());
        values.put("Concepto_de_Ingreso", conceptoDeIngreso.getConceptoDeIngreso());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Concepto_de_Ingreso", "---", values);
        db.close();
    }

    public static List<String> obtenerRazonesDeEgreso(Context context){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select Razon_de_Egreso from Razon_de_Egreso", null);
        List<String> razonesDeEgreso = new ArrayList<>();
        while(c.moveToNext())
            razonesDeEgreso.add(c.getString(0));
        c.close();
        db.close();
        return  razonesDeEgreso;
    }

    public static void agregarRazonDeEgreso(Context context, RazonDeEgreso razonDeEgreso) {
        ContentValues values = new ContentValues();
        values.put("idRazon_de_Egreso", razonDeEgreso.getId());
        values.put("Razon_de_Egreso", razonDeEgreso.getRazonDeEgreso());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Razon_de_Egreso", "---", values);
        db.close();
    }

    public static boolean comprobarExistenciaDeTexto(Context context, String column, String value, String table){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + table + " where " + column + " like ?",
                new String[]{value});
        c.moveToFirst();
        boolean existe = c.getInt(0) > 0;
        c.close();
        db.close();
        return existe;
    }

    public static int obtenerIdConceptoDeIngreso(Context context, String concepto) {
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idConcepto_de_Ingreso from Concepto_de_Ingreso where Concepto_de_Ingreso like ?",
                new String[]{concepto});
        int id = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return id;
    }

    public static int obtenerIdRazonDeEgreso(Context context, String razon) {
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idRazon_de_Egreso from Razon_de_Egreso where Razon_de_Egreso like ?",
                new String[]{razon});
        int id = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return id;
    }

    public static int obtenerIdRazonDeIngreso(Context context, String razon) {
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idRazon_de_Ingreso from Razon_de_Ingreso where Razon_de_Ingreso like ?",
                new String[]{razon});
        int id = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return id;
    }

    public static void agregarIngreso(Context context, Ingreso ingreso) {
        ContentValues values = new ContentValues();
        values.put("idIngreso", ingreso.getId());
        values.put("idRazon_de_Ingreso", ingreso.getRazonDeIngreso().getId());
        values.put("idConcepto_de_Ingreso", ingreso.getConceptoDeIngreso().getId());
        values.put("idHabitante", ingreso.getIdHabitante());
        values.put("departamento", ingreso.getDepartamento());
        values.put("monto", ingreso.getMonto());
        values.put("fecha", ingreso.getFecha());
        values.put("es_extraordinario", ingreso.isExtraordinario() ? 1 : 0);
        values.put("email", ingreso.getEmail());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Razon_de_Ingreso", "---", values);
        db.close();
    }

    public static void agregarEgreso(Context context, Egreso egreso){
        ContentValues values = new ContentValues();
        values.put("idEgreso", egreso.getId());
        values.put("idRazon_de_Egreso", egreso.getIdRazonDeEgreso());
        values.put("favorecido", egreso.getFavorecido());
        values.put("monto", egreso.getMonto());
        values.put("fecha", egreso.getFecha());
        values.put("es_extraordinario", egreso.isEsExtraordinario());
        values.put("email", egreso.getEmail());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Egreso", "---", values);
        db.close();
    }
}

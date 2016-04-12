package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.inspira.condominio.datos.ConceptoDeIngreso;
import org.inspira.condominio.datos.CondominioBD;
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
}

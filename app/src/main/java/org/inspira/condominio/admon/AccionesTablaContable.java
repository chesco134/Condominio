package org.inspira.condominio.admon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.inspira.condominio.datos.CondominioBD;

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
}

package org.inspira.condominio.actividades;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Persona;

/**
 * Created by jcapiz on 4/04/16.
 */
public class ActualizaNombreEnBD {

    public static void actualizacion(Context context, Persona persona){
        String[] fields = persona.getClass().getName().split("\\.");
        String lastName = fields[fields.length-1];
        ContentValues values = new ContentValues();
        values.put("nombres", persona.getNombres());
        values.put("ap_paterno", persona.getApPaterno());
        values.put("ap_materno", persona.getApMaterno());
        Log.d("ActualizaNombre", lastName + ", id" + lastName + ", " + persona.getId());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.update(lastName, values, "id" + lastName + " = CAST(? as INTEGER)",
                new String[]{String.valueOf(persona.getId())});
        db.close();
    }
}

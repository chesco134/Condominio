package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Torre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 17/03/16.
 */
public class AccionesTablaTorre {

    public static int agregarTorre(Context context, Torre torre){
        ContentValues values = new ContentValues();
        values.put("idTorre", torre.getId());
        values.put("nombre", torre.getNombre());
        values.put("posee_elevador", torre.isPoseeElevador());
        values.put("cantidad_de_pisos", torre.getCantidadDePisos());
        values.put("cantidad_de_focos", torre.getCantidadDeFocos());
        values.put("cantidad_de_departamentos", torre.getCantidadDeDepartamentos());
        values.put("idAdministracion", torre.getIdAdministracion());
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase writable = condominioBD.getWritableDatabase();
        writable.insert("Torre", "---", values);
        SQLiteDatabase readable = condominioBD.getReadableDatabase();
        Cursor c = readable.rawQuery("select last_insert_rowid()", null);
        int idTorre = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        readable.close();
        writable.close();
        return idTorre;
    }

    public static Torre[] obtenerTorres(Context context, int idAdministracion){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Torre where idAdministracion = CAST(? as INTEGER)",
                new String[]{String.valueOf(idAdministracion)});
        List<Torre> torres = new ArrayList<>();
        Torre torre;
        while(c.moveToNext()){
            torre = new Torre(c.getInt(c.getColumnIndex("idTorre")));
            torre.setCantidadDeDepartamentos(c.getInt(c.getColumnIndex("cantidad_de_departamentos")));
            torre.setCantidadDeFocos(c.getInt(c.getColumnIndex("cantidad_de_focos")));
            torre.setCantidadDePisos(c.getInt(c.getColumnIndex("cantidad_de_pisos")));
            torre.setNombre(c.getString(c.getColumnIndex("nombre")));
            torre.setPoseeElevador(c.getInt(c.getColumnIndex("posee_elevador")) != 0);
            torre.setIdAdministracion(idAdministracion);
            torres.add(torre);
        }
        c.close();
        db.close();
        return torres.toArray(new Torre[0]);
    }

    public static int obtenerIdTorre(Context context, String nombre){
        Log.d("Torre", "No context? " + (context == null));
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idTorre from Torre where nombre like ?", new String[]{nombre});
        int idTorre =  c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return idTorre;
    }

    public static boolean existenTorres(Context context) {
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from Torre where idAdministracion = CAST(? as INTEGER)", new String[]{String.valueOf(ProveedorDeRecursos.obtenerIdAdministracion(context))});
        boolean hayTorres = false;
        if(c.moveToFirst()){
            hayTorres = c.getInt(0) > 0;
        }
        c.close();
        db.close();
        return hayTorres;
    }

    public static void actualizaTorre(Context context, Torre torre){
        ContentValues values = new ContentValues();
        values.put("nombre", torre.getNombre());
        values.put("cantidad_de_pisos", torre.getCantidadDePisos());
        values.put("cantidad_de_focos", torre.getCantidadDeFocos());
        values.put("cantidad_de_departamentos", torre.getCantidadDeDepartamentos());
        values.put("posee_elevador", torre.isPoseeElevador());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.update("Torre", values, "idTorre = CAST(? as INTEGER)", new String[]{String.valueOf(torre.getId())});
        db.close();
    }

    public static void removerTorre(Context context, int idTorre) {
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        SQLiteDatabase db2 = new CondominioBD(context).getReadableDatabase();
        Cursor c = db2.rawQuery("select idHabitante from Habitante where idTorre = CAST(? as INTEGER)",
                new String[]{String.valueOf(idTorre)});
        String idHabitante;
        while(c.moveToNext()){
            idHabitante = String.valueOf(c.getInt(0));
            db.delete("Contacto_Habitante", "idHabitante = CAST(? as INTEGER)",
                    new String[]{idHabitante});
            db.delete("Propietario_de_Departamento", "idHabitante = CAST(? as INTEGER)",
                    new String[]{idHabitante});
            db.delete("Habitante", "idHabitante = CAST(? as INTEGER)",
                    new String[]{idHabitante});
        }
        c.close();
        db.delete("Torre", "idTorre = CAST(? as INTEGER)", new String[]{String.valueOf(idTorre)});
        db.close();
    }

    public static Torre obtenerTorre(Context context, int idTorre) {
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Torre where idTorre = CAST(? as INTEGER)", new String[]{String.valueOf(idTorre)});
        Torre torre;
        if(c.moveToFirst()){
            torre = new Torre(idTorre);
            torre.setCantidadDeDepartamentos(c.getInt(c.getColumnIndex("cantidad_de_departamentos")));
            torre.setCantidadDeFocos(c.getInt(c.getColumnIndex("cantidad_de_focos")));
            torre.setCantidadDePisos(c.getInt(c.getColumnIndex("cantidad_de_pisos")));
            torre.setNombre(c.getString(c.getColumnIndex("nombre")));
            torre.setPoseeElevador(c.getInt(c.getColumnIndex("posee_elevador")) != 0);
            torre.setIdAdministracion(c.getInt(c.getColumnIndex("idAdministracion")));
        }else{
            torre = null;
        }
        c.close();
        db.close();
        return torre;
    }
}

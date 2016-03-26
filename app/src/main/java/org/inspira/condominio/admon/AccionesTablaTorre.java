package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
            //torre.setcondominio(condominio);
            torres.add(torre);
        }
        c.close();
        db.close();
        return torres.toArray(new Torre[1]);
    }

    public static int obtenerIdTorre(Context context, String nombre){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idTorre from Torre where nombre like ?", new String[]{nombre});
        int idTorre = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return idTorre;
    }

    public static boolean existenTorres(Context context) {
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idTorre from Torre where idAdministracion = CAST(? as INTEGER)", new String[]{String.valueOf(ProveedorDeRecursos.obtenerIdAdministracion(context))});
        boolean hayTorres = c.moveToFirst();
        c.close();
        db.close();
        return hayTorres;
    }
}

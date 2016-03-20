package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.inspira.condominio.datos.Condominio;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.TipoDeCondominio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 17/03/16.
 */
public class AccionesTablaCondominio {

    public static TipoDeCondominio[] obtenerTiposDeCondominio(Context context){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Tipo_de_Condominio", null);
        List<TipoDeCondominio> tiposDeCondominio = new ArrayList<>();
        TipoDeCondominio tipoDeCondominio;
        while(c.moveToNext()){
            tipoDeCondominio = new TipoDeCondominio(c.getInt(c.getColumnIndex("idTipo_de_Condominio")));
            tipoDeCondominio.setDescripcion(c.getString(c.getColumnIndex("descripcion")));
            tiposDeCondominio.add(tipoDeCondominio);
        }
        c.close();
        db.close();
        return tiposDeCondominio.toArray(new TipoDeCondominio[1]);
    }

    public static int obtenerIdTipoDeCondominio(Context context, String descripcion){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idTipo_de_Condominio from Tipo_de_Condominio where descripcion like ?", new String[]{descripcion});
        int idTipoDeCondominio = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return idTipoDeCondominio;
    }

    public static int agregarCondominio(Context context, Condominio condominio){
        ContentValues values = new ContentValues();
        values.put("direccion", condominio.getDireccion());
        values.put("edad", condominio.getEdad());
        values.put("idTipo_de_Condominio", condominio.getTipoDeCondominio().getId());
        values.put("inmoviliaria", condominio.getInmoviliaria());
        values.put("posee_sala_de_juntas", condominio.isPoseeSalaDeJuntas());
        values.put("posee_gym", condominio.isPoseeGym());
        values.put("posee_espacio_recreativo", condominio.isPoseeEspacioRecreativo());
        values.put("posee_espacio_cultural", condominio.isPoseeEspacioCultural());
        values.put("posee_oficinas_administrativas", condominio.isPoseeOficinasAdministrativas());
        values.put("posee_alarma_sismica", condominio.isPoseeAlarmaSismica());
        values.put("cantidad_de_lugares_estacionamiento", condominio.getCantidadDeLugaresEstacionamiento());
        values.put("cantidad_de_lugares_estacionamiento_visitas", condominio.getCantidadDeLugaresEstacionamientoVisitas());
        values.put("costo_aproximado", condominio.getCostoAproximadoPorUnidadPrivativa());
        values.put("posee_visterna_agua_pluvial", condominio.isPoseeCisternaAguaPluvial());
        values.put("capacidad_cisterna", condominio.getCapacidadDeCisterna());
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getWritableDatabase();
        db.insert("Condominio", "---", values);
        db = condominioBD.getReadableDatabase();
        Cursor c = db.rawQuery("select last_insert_rowid()", null);
        int idCondominio = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return idCondominio;
    }

    public static Condominio obtenerCondominio(Context context, int idCondominio){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Condominio where idCondominio = CAST(? as INTEGER)",
                new String[]{String.valueOf(idCondominio)});
        Condominio condominio;
        int idTipoDeCondominio;
        String descripcion;
        TipoDeCondominio tipoDeCondominio;
        if(c.moveToFirst()){
            condominio = new Condominio(idCondominio);
            condominio.setCantidadDeLugaresEstacionamiento(c.getInt(c.getColumnIndex("cantidad_de_lugares_estacionamiento")));
            condominio.setCantidadDeLugaresEstacionamientoVisitas(c.getInt(c.getColumnIndex("cantidad_de_lugares_estacionamiento_visitas")));
            condominio.setCapacidadDeCisterna(c.getFloat(c.getColumnIndex("capacidad_de_cisterna")));
            condominio.setDireccion(c.getString(c.getColumnIndex("direccion")));
            condominio.setEdad(c.getInt(c.getColumnIndex("edad")));
            condominio.setInmoviliaria(c.getString(c.getColumnIndex("inmoviliaria")));
            condominio.setPoseeAlarmaSismica(c.getInt(c.getColumnIndex("posee_alarma_sismica")) != 0);
            condominio.setPoseeCisternaAguaPluvial(c.getInt(c.getColumnIndex("posee_cisterna_agua_pluvial")) != 0);
            condominio.setPoseeEspacioCultural(c.getInt(c.getColumnIndex("posee_espacio_cultural")) != 0);
            condominio.setPoseeEspacioRecreativo(c.getInt(c.getColumnIndex("posee_espacio_recreativo")) != 0);
            condominio.setPoseeGym(c.getInt(c.getColumnIndex("posee_gym")) != 0);
            condominio.setPoseeOficinasAdministrativas(c.getInt(c.getColumnIndex("posee_oficinas_administrativas")) != 0);
            condominio.setPoseeSalaDeJuntas(c.getInt(c.getColumnIndex("posee_sala_de_juntas")) != 0);
            condominio.setCostoAproximadoPorUnidadPrivativa(c.getFloat(c.getColumnIndex("costo_aproximado")));
            idTipoDeCondominio = c.getInt(c.getColumnIndex("idTipo_de_Condominio"));
            Cursor x = db.rawQuery("select descripcion from Tipo_de_Condominio where idTipo_de_Condominio = CAST(? as INTEGER)", new String[]{String.valueOf(idTipoDeCondominio)});
            x.moveToFirst();
            descripcion = x.getString(0);
            x.close();
            tipoDeCondominio = new TipoDeCondominio(idTipoDeCondominio);
            tipoDeCondominio.setDescripcion(descripcion);
            condominio.setTipoDeCondominio(tipoDeCondominio);
        }else{
            condominio = null;
        }
        c.close();
        db.close();
        return condominio;
    }

    public static boolean consultaCondominio(Context context, int idCondominio){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from Condominio where idCondominio = CAST(? as INTEGER)", new String[]{String.valueOf(idCondominio)});
        c.moveToFirst();
        boolean resultado = c.getInt(0) > 0;
        c.close();
        db.close();
        return resultado;
    }
}

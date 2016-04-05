package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.ContactoTrabajador;
import org.inspira.condominio.datos.Trabajador;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 28/03/16.
 */
public class AccionesTablaTrabajador {

    public static int obtenerIdTipoDeTrabajador(Context context, String tipo_de_trabajador){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idTipo_de_Trabajador from Tipo_de_Trabajador where tipo_de_trabajador like ?", new String[]{tipo_de_trabajador});
        int idTipoDeTrabajador = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return idTipoDeTrabajador;
    }

    public static String obtenerTipoDeTrabajador(Context context, int idTipoDeTrabajador){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select tipo_de_trabajador from Tipo_de_Trabajador where idTipo_de_Trabajador = CAST(? as INTEGER)", new String[]{String.valueOf(idTipoDeTrabajador)});
        String tipoDeTrabajador = c.moveToFirst() ? c.getString(0) : null;
        c.close();
        db.close();
        return tipoDeTrabajador;
    }

    public static void agregarTrabajador(Context context, Trabajador trabajador){
        ContentValues values = new ContentValues();
        values.put("idTrabajador", trabajador.getId());
        values.put("nombres", trabajador.getNombres());
        values.put("ap_paterno", trabajador.getApPaterno());
        values.put("ap_materno", trabajador.getApMaterno());
        values.put("salario", trabajador.getSalario());
        values.put("genero", trabajador.isGenero() ? 1 : 0); // TRUE = MASCULINO.
        values.put("posee_seguro_social", trabajador.isPoseeSeguroSocial() ? 1 : 0);
        values.put("idTipo_de_Trabajador", trabajador.getIdTipoDeTrabajador());
        values.put("idAdministracion", trabajador.getIdAdministracion());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Trabajador", "---", values);
        db.close();
    }

    public static Trabajador[] obtenerTrabajadores(Context context, int idTipoDeTrabajador){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Trabajador where idTipo_de_Trabajador = CAST(? as INTEGER)", new String[]{String.valueOf(idTipoDeTrabajador)});
        List<Trabajador> trabajadores = new ArrayList<>();
        Trabajador trabajador;
        while(c.moveToNext()){
            trabajador = new Trabajador(c.getInt(c.getColumnIndex("idTrabajador")));
            trabajador.setNombres(c.getString(c.getColumnIndex("nombres")));
            trabajador.setApPaterno(c.getString(c.getColumnIndex("ap_paterno")));
            trabajador.setApMaterno(c.getString(c.getColumnIndex("ap_materno")));
            trabajador.setSalario(c.getFloat(c.getColumnIndex("salario")));
            trabajador.setGenero(c.getInt(c.getColumnIndex("genero")) == 1); // 1 es MASCULINO.
            trabajador.setIdAdministracion(c.getInt(c.getColumnIndex("idAdministracion")));
            trabajador.setPoseeSeguroSocial(c.getInt(c.getColumnIndex("posee_seguro_social")) != 0);
            trabajador.setIdTipoDeTrabajador(idTipoDeTrabajador);
            trabajadores.add(trabajador);
        }
        c.close();
        db.close();
        return trabajadores.toArray(new Trabajador[0]);
    }

    public static Trabajador obtenerTrabajador(Context context, int idTrabajador){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Trabajador where idTrabajador = CAST(? as INTEGER)", new String[]{String.valueOf(idTrabajador)});
        Trabajador trabajador;
        if(c.moveToFirst()){
            trabajador = new Trabajador(idTrabajador);
            trabajador.setNombres(c.getString(c.getColumnIndex("nombres")));
            trabajador.setApPaterno(c.getString(c.getColumnIndex("ap_paterno")));
            trabajador.setApMaterno(c.getString(c.getColumnIndex("ap_materno")));
            trabajador.setSalario(c.getFloat(c.getColumnIndex("salario")));
            trabajador.setGenero(c.getInt(c.getColumnIndex("genero")) == 1);
            trabajador.setIdAdministracion(c.getInt(c.getColumnIndex("idAdministracion")));
            trabajador.setPoseeSeguroSocial(c.getInt(c.getColumnIndex("posee_seguro_social")) != 0);
            trabajador.setIdTipoDeTrabajador(c.getInt(c.getColumnIndex("idTipo_de_Trabajador")));
        }else
            trabajador = null;
        c.close();
        db.close();
        return trabajador;
    }

    public static void removerTrabajador(Context context, int idTrabajador){
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.delete("Contacto_Trabajador", "idTrabajador = CAST(? as INTEGER)", new String[]{String.valueOf(idTrabajador)});
        db.delete("Trabajador", "idTrabajador = CAST(? as INTEGER)", new String[]{String.valueOf(idTrabajador)});
        db.close();
    }

    public static void actualizacionDeCampo(Context context, String key, String value, int idTrabajador){
        ContentValues values = new ContentValues();
        values.put(key, value);
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        int rowsAffected = db.update("Trabajador", values, "idTrabajador = CAST(? as INTEGER)",
                new String[]{String.valueOf(idTrabajador)});
        Log.d("Shukkaku", "Rows affected: " + rowsAffected);
        db.close();
    }

    public static ContactoTrabajador[] obtenerListaDeContactos(Context context, int idTrabajador){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Contacto_Trabajador where idTrabajador = CAST(? as INTEGER)",
                new String[]{String.valueOf(idTrabajador)});
        List<ContactoTrabajador> contactos = new ArrayList<>();
        ContactoTrabajador contacto;
        while(c.moveToNext()){
            contacto = new ContactoTrabajador(c.getInt(c.getColumnIndex("idContacto_Trabajador")));
            contacto.setContacto(c.getString(c.getColumnIndex("contacto")));
            contacto.setIdTrabajador(idTrabajador);
            contactos.add(contacto);
        }
        c.close();
        db.close();
        return contactos.toArray(new ContactoTrabajador[0]);
    }

    public static void agregarContactoTrabajador(Context context, ContactoTrabajador contacto){
        ContentValues values = new ContentValues();
        values.put("contacto", contacto.getContacto());
        values.put("idTrabajador", contacto.getIdTrabajador());
        values.put("idContacto_Trabajador", contacto.getId());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Contacto_Trabajador", "---", values);
        db.close();
    }
}

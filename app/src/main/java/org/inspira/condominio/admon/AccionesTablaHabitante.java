package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admin.habitantes.ResumenHabitante;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.ContactoAdministracion;
import org.inspira.condominio.datos.ContactoHabitante;
import org.inspira.condominio.datos.Habitante;
import org.inspira.condominio.datos.PropietarioDeDepartamento;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 25/03/16.
 */
public class AccionesTablaHabitante {

    public static void agregarHabitante(Context context, Habitante habitante){
        ContentValues values = new ContentValues();
        values.put("idHabitante", habitante.getId());
        values.put("nombres", habitante.getNombres());
        values.put("ap_paterno", habitante.getApPaterno());
        values.put("ap_materno", habitante.getApMaterno());
        values.put("nombre_departamento", habitante.getNombreDepartamento());
        values.put("genero", habitante.isGenero());
        values.put("idTorre", habitante.getIdTorre());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Habitante", "---", values);
        db.close();
    }

    public static void agregarHabitantePropietario(Context context, PropietarioDeDepartamento p){
        ContentValues values = new ContentValues();
        values.put("idHabitante", p.getIdHabitante());
        values.put("posee_seguro", p.isPoseeSeguro());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Propietario_de_Departamento", "---", values);
        db.close();
    }

    public static PropietarioDeDepartamento[] propietariosDeDepartamento(Context context){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Propietario_de_Departamento", null);
        List<PropietarioDeDepartamento> propietarios = new ArrayList<>();
        PropietarioDeDepartamento propietario;
        while(c.moveToNext()){
            propietario = new PropietarioDeDepartamento();
            propietario.setIdHabitante(c.getInt(c.getColumnIndex("idHabitante")));
            propietario.setPoseeSeguro(c.getInt(c.getColumnIndex("posee_seguro")) != 0);
            propietarios.add(propietario);
        }
        c.close();
        db.close();
        return propietarios.toArray(new PropietarioDeDepartamento[0]);
    }

    public static boolean esPropietario(Context context, int idHabitante){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from Propietario_de_Departamento where idHabitante = CAST(? as INTEGER)", new String[]{String.valueOf(idHabitante)});
        boolean esPropietario = c.moveToFirst() && c.getInt(0) > 0;
        c.close();
        db.close();
        return esPropietario;
    }

    public static Habitante[] obtenerHabitantes(Context context, int idTorre){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Habitante where idTorre = CAST(? as INTEGER) order by ap_paterno", new String[]{String.valueOf(idTorre)});
        List<Habitante> habitantes = new ArrayList<>();
        Habitante habitante;
        while(c.moveToNext()){
            habitante = new Habitante(c.getInt(c.getColumnIndex("idHabitante")));
            habitante.setNombres(c.getString(c.getColumnIndex("nombres")));
            habitante.setApPaterno(c.getString(c.getColumnIndex("ap_paterno")));
            habitante.setApMaterno(c.getString(c.getColumnIndex("ap_materno")));
            habitante.setNombreDepartamento(c.getString(c.getColumnIndex("nombre_departamento")));
            habitante.setGenero(c.getInt(c.getColumnIndex("genero")) != 0);
            habitante.setIdTorre(idTorre);
            habitantes.add(habitante);
        }
        c.close();
        db.close();
        return habitantes.toArray(new Habitante[]{});
    }

    public static Habitante[] obtenerHabitantes(Context context){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from " +
                "Habitante join " +
                "(select idTorre from Torre join Usuario using(idAdministracion) where email like ?) a " +
                "using(idTorre) order by ap_paterno", new String[]{ProveedorDeRecursos.obtenerEmail(context)});
        List<Habitante> habitantes = new ArrayList<>();
        Habitante habitante;
        while(c.moveToNext()){
            habitante = new Habitante(c.getInt(c.getColumnIndex("idHabitante")));
            habitante.setNombres(c.getString(c.getColumnIndex("nombres")));
            habitante.setApPaterno(c.getString(c.getColumnIndex("ap_paterno")));
            habitante.setApMaterno(c.getString(c.getColumnIndex("ap_materno")));
            habitante.setNombreDepartamento(c.getString(c.getColumnIndex("nombre_departamento")));
            habitante.setGenero(c.getInt(c.getColumnIndex("genero")) != 0);
            habitante.setIdTorre(c.getInt(c.getColumnIndex("idTorre")));
            habitantes.add(habitante);
        }
        c.close();
        db.close();
        return habitantes.toArray(new Habitante[]{});
    }

    public static void agregarContactoHabitante(Context context, ContactoHabitante contacto){
        ContentValues values = new ContentValues();
        values.put("idHabitante", contacto.getIdHabitante());
        values.put("contacto", contacto.getContacto());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Contacto_Habitante", "---", values);
        db.close();
    }

    public static void removerHabitante(Context context, int idHabitante){
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.delete("Contacto_Habitante", "idHabitante = CAST(? as INTEGER)", new String[]{String.valueOf(idHabitante)});
        db.delete("Habitante", "idHabitante = CAST(? as INTEGER)", new String[]{String.valueOf(idHabitante)});
        db.close();
    }

    public static void actualizarNombre(Context context, int idHabitante, String nombres, String apPaterno, String apMaterno) {
        ContentValues values = new ContentValues();
        values.put("nombres", nombres);
        values.put("ap_paterno", apPaterno);
        values.put("ap_materno", apMaterno);
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getWritableDatabase();
        db.update("Habitante", values, "idHabitante = CAST(? as INTEGER)", new String[]{String.valueOf(idHabitante)});
        db.close();
        condominioBD.close();
    }

    public static void actualizaCampo(Context context, int id, String key, String value) {
        Log.d("Bazoo", "Monster interaction: " + value);
        ContentValues values = new ContentValues();
        values.put(key, value);
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getWritableDatabase();
        db.update("Habitante", values, "idHabitante = CAST(? as INTEGER)",
                new String[]{String.valueOf(id)});
        db.close();
        condominioBD.close();
    }

    public static ContactoHabitante[] obtenerListaDeContactos(Context context, int idHabitante) {
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getReadableDatabase();
        Cursor c = db.rawQuery("select * from Contacto_Habitante where idHabitante = CAST(? as INTEGER)"
                , new String[]{String.valueOf(idHabitante)});
        List<ContactoHabitante> contactos = new ArrayList<ContactoHabitante>();
        ContactoHabitante contactoHabitante;
        while(c.moveToNext()){
            contactoHabitante = new ContactoHabitante(c.getInt(c.getColumnIndex("idContacto_Habitante")));
            contactoHabitante.setContacto(c.getString(c.getColumnIndex("contacto")));
            contactoHabitante.setIdHabitante(idHabitante);
            contactos.add(contactoHabitante);
        }
        c.close();
        db.close();
        condominioBD.close();
        return contactos.toArray(new ContactoHabitante[0]);
    }

    public static void actualizaEstadoDeSeguroDePropietario(Context context, boolean poseeSeguro, int idHabitante){
        ContentValues values = new ContentValues();
        values.put("posee_seguro", poseeSeguro ? 1 : 0);
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getWritableDatabase();
        db.update("Propietario_de_Departamento", values, "idHabitante = CAST(? as INTEGER)",
                new String[]{String.valueOf(idHabitante)});
        db.close();
        condominioBD.close();
    }

    public static void quitaPropietarioDeDepartamento(Context context, int idHabitante){
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getWritableDatabase();
        db.delete("Propietario_de_Departamento", "idHabitante = CAST(? as INTEGER)",
                new String[]{String.valueOf(idHabitante)});
        db.close();
        condominioBD.close();
    }

    public static boolean poseeSeguro(Context context, int id) {
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getReadableDatabase();
        Cursor c = db.rawQuery("select posee_seguro from Propietario_de_Departamento " +
                "where idHabitante = CAST(? as INTEGER)",
                new String[]{String.valueOf(id)});
        c.moveToFirst();
        int poseeSeguro = c.getInt(0);
        c.close();
        db.close();
        condominioBD.close();
        return poseeSeguro != 0;
    }

    public static Habitante obtenerHabitante(Context context, int id) {
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getReadableDatabase();
        Cursor c = db.rawQuery("select * from Habitante where idHabitante = CAST(? as INTEGER)",
                new String[]{String.valueOf(id)});
        Habitante habitante;
        if(c.moveToFirst()) {
            habitante = new Habitante(id);
            habitante.setNombreDepartamento(c.getString(c.getColumnIndex("nombre_departamento")));
            habitante.setIdTorre(c.getInt(c.getColumnIndex("idTorre")));
            habitante.setGenero(c.getInt(c.getColumnIndex("genero")) != 0);
            habitante.setNombres(c.getString(c.getColumnIndex("nombres")));
            habitante.setApPaterno(c.getString(c.getColumnIndex("ap_paterno")));
            habitante.setApMaterno(c.getString(c.getColumnIndex("ap_materno")));
        }else
            habitante = null;
        c.close();
        db.close();
        condominioBD.close();
        return habitante;
    }

    public static int obtenerNumeroDeHabitantesEnTorre(Context context){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from Habitante where idTorre = CAST(? as INTEGER)",
                new String[]{String.valueOf(ProveedorDeRecursos.obtenerIdTorreActual(context))});
        int cantidadDeHabitantes = c.moveToNext() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return cantidadDeHabitantes;
    }
}

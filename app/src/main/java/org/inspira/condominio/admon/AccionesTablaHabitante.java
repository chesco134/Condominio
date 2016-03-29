package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.datos.CondominioBD;
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

    public static ContactoHabitante[] obtenerContactoHabitante(Context context, int idHabitante){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idContacto_Habitante, contacto from Contacto_Habitante where idHabitante = CAST(? as INTEGER)", new String[]{String.valueOf(idHabitante)});
        List<ContactoHabitante> contactos = new ArrayList<>();
        ContactoHabitante contacto;
        while(c.moveToNext()) {
            contacto = new ContactoHabitante(c.getInt(c.getColumnIndex("idContacto_Habitante")));
            contacto.setContacto(c.getString(c.getColumnIndex("contacto")));
            contacto.setIdHabitante(idHabitante);
            contactos.add(contacto);
        }
        c.close();
        db.close();
        return contactos.toArray(new ContactoHabitante[]{});
    }

    public static void removerHabitante(Context context, int idHabitante){
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.delete("Contacto_Habitante", "idHabitante = CAST(? as INTEGER)", new String[]{String.valueOf(idHabitante)});
        db.delete("Habitante", "idHabitante = CAST(? as INTEGER)", new String[]{String.valueOf(idHabitante)});
        db.close();
    }
}

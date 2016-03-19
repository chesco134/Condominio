package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Escolaridad;
import org.inspira.condominio.datos.NombreDeUsuario;
import org.inspira.condominio.datos.TipoDeAdministrador;
import org.inspira.condominio.datos.Usuario;

/**
 * Created by jcapiz on 19/03/16.
 */
public class AccionesTablaUsuario {

    public static Escolaridad obtenerEscolaridad(Context context, String escolaridad){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idEscolaridad from Escolaridad where escolaridad like ?", new String[]{escolaridad});
        int idEscolaridad = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        Escolaridad escolaridad1 = new Escolaridad(idEscolaridad);
        escolaridad1.setEscolaridad(escolaridad);
        return escolaridad1;
    }

    public static TipoDeAdministrador obtenerTipoDeAdministrador(Context context, String tipoDeAdministrador){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idTipo_de_Administrador from Tipo_de_Administrador where tipo_de_administrador like ?", new String[]{tipoDeAdministrador});
        int idTipoDeAdministrador = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        TipoDeAdministrador tipoDeAdministrador1 = new TipoDeAdministrador(idTipoDeAdministrador);
        tipoDeAdministrador1.setDescripcion(tipoDeAdministrador);
        return tipoDeAdministrador1;
    }

    public static int agregaNombreDeUsuario(Context context, NombreDeUsuario nombreDeUsuario){
        ContentValues values = new ContentValues();
        values.put("nombres", nombreDeUsuario.getNombres());
        values.put("ap_paterno", nombreDeUsuario.getApPaterno());
        values.put("ap_materno", nombreDeUsuario.getApMaterno());
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getWritableDatabase();
        db.insert("Nombre_de_Usuario", "---", values);
        db = condominioBD.getReadableDatabase();
        Cursor c = db.rawQuery("select last_insert_rowid()", null);
        int idNombreDeUsuario = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return idNombreDeUsuario;
    }

    public static void agregaUsuario(Context context, Usuario usuario){
        ContentValues values = new ContentValues();
        values.put("email", usuario.getEmail());
        values.put("dateOfBirth", usuario.getDateOfBirth());
        values.put("remuneracion", usuario.getRemuneracion());
        values.put("idNombre_de_Usuario", usuario.getNombreDeUsuario().getId());
        values.put("idAdministracion", usuario.getAdministracion().getId());
        values.put("idEscolaridad", usuario.getEscolaridad().getId());
        values.put("idTipo_de_Administrador", usuario.getTipoDeAdministrador().getId());
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getWritableDatabase();
        db.insert("Usuario", "---", values);
        db.close();
    }

    public static void agregaUsuarioProfesionista(Context context, String email, String profesion){
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("profesion", profesion);
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Usuario_Profesionista", "---", values);
        db.close();
    }

    public static void agregaContactoUsuario(Context context, String email, String contacto){
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("telefono", contacto);
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Contacto_Usuario", "---", values);
        db.close();
    }

    public static NombreDeUsuario obtenerNombreDeUsuario(Context context, String nombres, String apPaterno, String apMaterno){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idNombre_de_Usuario from Nombre_de_Usuario where nombres like ? and ap_paterno like ? and ap_materno like ?", new String[]{nombres, apPaterno, apMaterno});
        NombreDeUsuario nombreDeUsuario;
        if(c.moveToFirst()){
            nombreDeUsuario = new NombreDeUsuario(c.getInt(0));
            nombreDeUsuario.setApPaterno(apPaterno);
            nombreDeUsuario.setApMaterno(apMaterno);
        }else
            nombreDeUsuario = null;
        c.close();
        db.close();
        return nombreDeUsuario;
    }
}

package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Escolaridad;
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

    public static Escolaridad obtenerEscolaridad(Context context, int idEscoladirar){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select escolaridad from Escolaridad where idEscolaridad = CAST(? as INTEGER)", new String[]{String.valueOf(idEscoladirar)});
        c.moveToFirst();
        Escolaridad escolaridad = new Escolaridad(idEscoladirar);
        escolaridad.setEscolaridad(c.getString(0));
        c.close();
        db.close();
        return escolaridad;
    }

    public static TipoDeAdministrador obtenerTipoDeAdministrador(Context context, String tipoDeAdministrador){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idTipo_de_Administrador from Tipo_de_Administrador where descripcion like ?", new String[]{tipoDeAdministrador});
        int idTipoDeAdministrador = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        TipoDeAdministrador tipoDeAdministrador1 = new TipoDeAdministrador(idTipoDeAdministrador);
        tipoDeAdministrador1.setDescripcion(tipoDeAdministrador);
        return tipoDeAdministrador1;
    }

    public static TipoDeAdministrador obtenerTipoDeAdministrador(Context context, int idTipoDeAdministrador){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select descripcion from Tipo_de_Administrador where idTipo_de_Administrador = CAST(? as INTEGER)", new String[]{String.valueOf(idTipoDeAdministrador)});
        c.moveToFirst();
        TipoDeAdministrador tipoDeAdministrador = new TipoDeAdministrador(idTipoDeAdministrador);
        tipoDeAdministrador.setDescripcion(c.getString(0));
        c.close();
        db.close();
        return tipoDeAdministrador;
    }

    public static void agregaUsuario(Context context, Usuario usuario){
        ContentValues values = new ContentValues();
        values.put("email", usuario.getEmail());
        values.put("nombres", usuario.getNombres());
        values.put("ap_paterno", usuario.getApPaterno());
        values.put("ap_materno", usuario.getApMaterno());
        values.put("dateOfBirth", usuario.getDateOfBirth());
        values.put("remuneracion", usuario.getRemuneracion());
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

    public static Usuario obtenerUsuario(Context context, String email){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Usuario where email like ?", new String[]{email});
        Usuario usuario;
        if(c.moveToNext()){
            usuario = new Usuario();
            usuario.setEmail(c.getString(c.getColumnIndex("email")));
            usuario.setNombres(c.getString(c.getColumnIndex("nombres")));
            usuario.setApPaterno(c.getString(c.getColumnIndex("ap_paterno")));
            usuario.setApMaterno(c.getColumnName(c.getColumnIndex("ap_materno")));
            usuario.setRemuneracion(c.getFloat(c.getColumnIndex("remuneracion")));
            usuario.setEscolaridad(obtenerEscolaridad(context, c.getInt(c.getColumnIndex("idEscolaridad"))));
            usuario.setTipoDeAdministrador(obtenerTipoDeAdministrador(context, c.getInt(c.getColumnIndex("idTipo_de_Administrador"))));
            usuario.setAdministracion(AccionesTablaAdministracion.obtenerAdministracion(context, c.getInt(c.getColumnIndex("idAdministracion"))));
            usuario.setDateOfBirth(c.getLong(c.getColumnIndex("dateOfBirth")));
        }else{
            usuario = null;
        }
        c.close();
        db.close();
        return usuario;
    }
}

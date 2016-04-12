package org.inspira.condominio.actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Editable;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.admin.CentralPoint;
import org.inspira.condominio.datos.CondominioBD;

/**
 * Created by jcapiz on 18/03/16.
 */
public class ProveedorDeRecursos {

    public static final int REGISTRO_DE_CONDOMINIO = 12;
    public static final int REGISTRO_DE_TORRE = 13;
    public static final int REGISTRO_DE_ADMINISTRACION = 14;
    public static final int REGISTRO_DE_USUARIO = 15;
    public static final int SOLICITAR_CONTENIDO_CONDOMINIO = 16;
    public static final int ACTUALIZACION_DE_NOMBRE = 17;
    public static final int REGISTRO_DE_HABITANTE = 18;
    public static final int REMOCION_DE_HABITANTES = 19;
    public static final int ACTUALIZAR_DATOS_TORRE = 20;
    public static final int REMOCION_DE_TORRES = 21;
    public static final int REGISTRO_DE_TRABAJADOR = 22;
    public static final int REMOCION_DE_TRABAJADORES = 23;
    public static final int ACTUALIZAR_DATOS_CONDOMINIO = 24;
    public static final int ACTUALIZAR_DATOS_ADMINISTRACION = 25;
    public static final int REGISTRO_CONTACTO = 26;
    public static final int ACTUALIZACION_DE_CONTACTO = 27;
    public static final int REMOCION_DE_CONTACTOS = 28;
    public static final int ACTUALIZAR_DATOS_HABITANTE = 29;
    public static final int ACTUALIZAR_NOMBRE = 30;
    public static final int REMOCION_DE_PROPIETARIO_DE_DEPARTAMENTO = 31;
    public static final int ACTUALIZACION_DE_PROPIETARIO_DE_DEPARTAMENTO = 32;
    public static final int REGISTRO_DE_PROPIETARIO = 33;
    public static final int ACTUALIZACION_DE_TRABAJADOR = 34;
    public static final int REGISTRO_DE_RAZON_O_CONCEPTO = 35;

    public static int obtenerColorDeError(Context context){
        return context.getResources().getColor(R.color.error);
    }

    public static int obtenerIdCondominio(Context context){
        return context.getSharedPreferences(Configuraciones.class.getName(), Context.MODE_PRIVATE)
                .getInt("idCondominio", -1);
    }

    public static int obtenerIdAdministracion(Context context){
        return context.getSharedPreferences(Configuraciones.class.getName(), Context.MODE_PRIVATE)
                .getInt("idAdministracion", -1);
    }

    public static String obtenerEmail(Context context){
        return context.getSharedPreferences(CentralPoint.class.getName(), Context.MODE_PRIVATE)
                .getString("email", "NaN");
    }

    public static String obtenerUsuario(Context context){
        return context.getSharedPreferences(CentralPoint.class.getName(), Context.MODE_PRIVATE)
                .getString("usuario", "NaN");
    }

    public static void guardaUsuario(Context context, String usuario){
        SharedPreferences.Editor editor = context.getSharedPreferences(CentralPoint.class.getName(), Context.MODE_PRIVATE)
                .edit();
        editor.putString("usuario", usuario);
        editor.apply();
    }

    public static void guardaRecursoInt(Context context, String nombre, int recurso){
        SharedPreferences.Editor editor = context.getSharedPreferences(Configuraciones.class.getName(), Context.MODE_PRIVATE)
                .edit();
        editor.putInt(nombre, recurso);
        editor.apply();
    }

    public static void guardaRecursoString(Context context, String nombre, String valor){
        SharedPreferences.Editor editor = context.getSharedPreferences(Configuraciones.class.getName(), Context.MODE_PRIVATE)
                .edit();
        editor.putString(nombre, valor);
        editor.apply();
    }

    public static int obtenerIdTorreActual(Context context) {
        return context.getSharedPreferences(Configuraciones.class.getName(), Context.MODE_PRIVATE)
                .getInt("idTorre", -1);
    }
}

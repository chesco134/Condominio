package org.inspira.condominio.actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.TextView;

import org.inspira.condominio.R;

/**
 * Created by jcapiz on 18/03/16.
 */
public class ProveedorDeRecursos {

    public static final int REGISTRO_DE_CONDOMINIO = 12;
    public static final int REGISTRO_DE_TORRE = 13;
    public static final int REGISTRO_DE_ADMINISTRACION = 14;
    public static final int REGISTRO_DE_USUARIO = 15;
    public static final int SOLICITAR_CONTENIDO_CONDOMINIO = 16;

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

    public static void guardaRecursoInt(Context context, String nombre, int recurso){
        SharedPreferences.Editor editor = context.getSharedPreferences(Configuraciones.class.getName(), Context.MODE_PRIVATE)
                .edit();
        editor.putInt(nombre, recurso);
        editor.apply();
    }

    public static String obtenerEmail(Context context){
        return context.getSharedPreferences(Configuraciones.class.getName(), Context.MODE_PRIVATE)
                .getString("email", "NaN");
    }
}

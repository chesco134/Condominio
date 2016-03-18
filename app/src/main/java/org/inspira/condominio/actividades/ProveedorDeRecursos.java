package org.inspira.condominio.actividades;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import org.inspira.condominio.R;

/**
 * Created by jcapiz on 18/03/16.
 */
public class ProveedorDeRecursos {

    public static int obtenerColorDeError(Context context){
        return context.getResources().getColor(R.color.error);
    }

    public static int obtenerIdCondominio(Context context){
        return context.getSharedPreferences(Configuraciones.class.getName(), Context.MODE_PRIVATE)
                .getInt("idCondominio", -1);
    }
}

package org.inspira.condominio.datos;

import android.os.Environment;

/**
 * Created by jcapiz.
 */
public class AlmacenamientoInterno {

    public static String obtenerRutaAlmacenamientoInterno(){
        return Environment.getExternalStorageDirectory() + "/Abbit";
    }
}

package org.inspira.condominio.datos;

import android.content.Context;
import android.os.Environment;

import org.inspira.condominio.R;

import java.io.File;

/**
 * Created by jcapiz.
 */
public class AlmacenamientoInterno {

    private Context context;

    public AlmacenamientoInterno(Context context) {
        this.context = context;
    }

    public static String obtenerRutaAlmacenamientoInterno(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public void crearDirectorio(){
        File f = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name).concat("/Convocatorias"));
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public void crearDirectorioContable(){
        File f = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name).concat("/Contable"));
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public String obtenerRutaDeAlmacenamiento(){
        return obtenerRutaAlmacenamientoInterno() + "/" + context.getString(R.string.app_name).concat("/Convocatorias");
    }

    public String obtenerRutaDeAlmacenamientoContable(){
        return obtenerRutaAlmacenamientoInterno() + "/" + context.getString(R.string.app_name).concat("/Contable");
    }
}

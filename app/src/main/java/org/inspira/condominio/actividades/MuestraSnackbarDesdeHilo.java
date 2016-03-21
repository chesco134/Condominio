package org.inspira.condominio.actividades;

import android.app.Activity;
import android.view.View;

import org.inspira.condominio.dialogos.ProveedorSnackBar;

/**
 * Created by jcapiz on 21/03/16.
 */
public class MuestraSnackbarDesdeHilo {
 public static void muestraMensaje(Activity activity, final View view, final String mensaje){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProveedorSnackBar
                        .muestraBarraDeBocados(view, mensaje);
            }
        });
    }
}

package org.inspira.condominio.actividades;

import android.app.Activity;
import android.view.View;

import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.dialogos.ProveedorToast;

/**
 * Created by jcapiz on 21/03/16.
 */
public class MuestraMensajeDesdeHilo {

    public static void muestraMensaje(Activity activity, final View view, final String mensaje){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProveedorSnackBar
                        .muestraBarraDeBocados(view, mensaje);
            }
        });
    }

    public static void muestraToast(final Activity activity, final String mensaje){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProveedorToast.showToast(activity, mensaje);
            }
        });
    }
}

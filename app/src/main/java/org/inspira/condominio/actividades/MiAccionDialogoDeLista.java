package org.inspira.condominio.actividades;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.inspira.condominio.dialogos.DialogoDeLista;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.networking.ContactoConServidor;

/**
 * Created by jcapiz on 30/03/16.
 */
public class MiAccionDialogoDeLista implements DialogoDeLista.AccionDialogoDeLista{

    private Context context;
    private String key;
    private TextView hostElement;
    private ColocaValorDesdeDialogo.FormatoDeMensaje fdm;
    private AccionCheckBox.ActualizacionDeCampo adc;

    public MiAccionDialogoDeLista(Context context, String key, TextView hostElement, ColocaValorDesdeDialogo.FormatoDeMensaje fdm, AccionCheckBox.ActualizacionDeCampo adc) {
        this.context = context;
        this.key = key;
        this.hostElement = hostElement;
        this.fdm = fdm;
        this.adc = adc;
    }

    @Override
    public void objetoSeleccionado(final String texto) {
        String contenidoDeMensaje = fdm.armarContenidoDeMensaje(key, String.valueOf(adc.obtenerId(texto)));
        ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
            @Override
            public void resultadoSatisfactorio(Thread t) {
                String respuesta = ((ContactoConServidor)t).getResponse();
                if(CompruebaCamposJSON.validaContenido(respuesta)){
                    adc.actualizaCampo(key, String.valueOf(adc.obtenerId(texto)));
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hostElement.setText(texto);
                            ProveedorSnackBar
                                    .muestraBarraDeBocados(hostElement, "Hecho");
                        }
                    });
                }else{
                    MuestraMensajeDesdeHilo
                            .muestraMensaje((AppCompatActivity)context, hostElement, "Servicio por el momento no disponible");
                }
            }

            @Override
            public void problemasDeConexion(Thread t) {}
        }, contenidoDeMensaje);
        contacto.start();
    }
}

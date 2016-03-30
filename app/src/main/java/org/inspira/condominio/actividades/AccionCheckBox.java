package org.inspira.condominio.actividades;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;

import org.inspira.condominio.admon.CompruebaCamposJSON;
import org.inspira.condominio.networking.ContactoConServidor;

/**
 * Created by jcapiz on 30/03/16.
 */
public class AccionCheckBox implements CompoundButton.OnCheckedChangeListener{

    private boolean allowToCheck;
    private Context context;
    private ColocaValorDesdeDialogo.FormatoDeMensaje fdm;
    private ActualizacionDeCampo acb;

    public interface ActualizacionDeCampo {
        void actualizaCampo(String key, String value);
        int obtenerId(String texto);
    }

    public AccionCheckBox(Context context, ColocaValorDesdeDialogo.FormatoDeMensaje fdm, ActualizacionDeCampo acb) {
        this.context = context;
        this.fdm = fdm;
        this.acb = acb;
        allowToCheck = true;
    }
    
    @Override
    public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
        if(allowToCheck) {
            String midString = buttonView.getText().toString().replace(' ', '_').replace('í','i');
            final String key = midString.contains("posee") ? midString : "posee_".concat(midString);
            final String value = isChecked ? "1" : "0";
            String content = fdm.armarContenidoDeMensaje(key, value);
            ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                @Override
                public void resultadoSatisfactorio(Thread t) {
                    String respuesta = ((ContactoConServidor) t).getResponse();
                    if (CompruebaCamposJSON.validaContenido(respuesta)) {
                        acb.actualizaCampo(key, value);
                        MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, buttonView, "Hecho");
                    } else {
                        MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, buttonView, "Hola ché");
                        allowToCheck = false;
                        buttonView.setChecked(!buttonView.isChecked());
                    }
                }

                @Override
                public void problemasDeConexion(Thread t) {
                    MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, buttonView, "Servicio por el momento no disponible");
                    allowToCheck = false;
                    buttonView.setChecked(!buttonView.isChecked());
                }
            }, content);
            contacto.start();
        }else
            allowToCheck = true;
    }
}

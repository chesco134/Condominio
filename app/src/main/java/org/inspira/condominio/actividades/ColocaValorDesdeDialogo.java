package org.inspira.condominio.actividades;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.inspira.condominio.admon.AccionesTablaCondominio;
import org.inspira.condominio.admon.CompruebaCamposJSON;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.networking.ContactoConServidor;

/**
 * Created by jcapiz on 30/03/16.
 */
public class ColocaValorDesdeDialogo implements View.OnClickListener{

    private int viewResLabel;
    private int viewResToChange;
    private int tipoDeEntradaDeTexto;
    private String nombreCampo;
    private String key;
    private Context context;
    private FormatoDeMensaje fdm;
    private AccionCheckBox.ActualizacionDeCampo adc;

    public interface FormatoDeMensaje{
        String armarContenidoDeMensaje(String key, String value);
    }

    public ColocaValorDesdeDialogo(Context context, int viewResLabel, int viewResToChange, int tipoDeEntradaDeTexto, String key, FormatoDeMensaje fdm, AccionCheckBox.ActualizacionDeCampo adc) {
        this.context = context;
        this.viewResLabel = viewResLabel;
        this.viewResToChange = viewResToChange;
        this.tipoDeEntradaDeTexto = tipoDeEntradaDeTexto;
        this.key = key;
        this.fdm = fdm;
        this.adc = adc;
    }

    public ColocaValorDesdeDialogo(Context context, String nombreCampo, int viewResToChange, int tipoDeEntradaDeTexto,  String key, FormatoDeMensaje fdm, AccionCheckBox.ActualizacionDeCampo adc) {
        this.context = context;
        this.nombreCampo = nombreCampo;
        this.viewResToChange = viewResToChange;
        this.tipoDeEntradaDeTexto = tipoDeEntradaDeTexto;
        this.key = key;
        this.fdm = fdm;
        this.adc = adc;
    }

    @Override
    public void onClick(View view){
        EntradaTexto et = new EntradaTexto();
        et.setTipoDeEntradaDeTexto(tipoDeEntradaDeTexto);
        final TextView viewToChange = (TextView)view.findViewById(viewResToChange);
        TextView viewLabel;
        String labelText;
        if( nombreCampo == null ) {
            viewLabel = (TextView) view.findViewById(viewResLabel);
            labelText = viewLabel.getText().toString();
        }else{
            labelText = nombreCampo;
        }
        String prevText = viewToChange.getText().toString().trim();
        Bundle args = new Bundle();
        args.putString("contenido", prevText);
        args.putString("mensaje", labelText);
        et.setArguments(args);
        et.setAccionDialogo(new EntradaTexto.AccionDialogo() {
            @Override
            public void accionPositiva(DialogFragment fragment) {
                final String nuevoTexto = ((EntradaTexto) fragment).getEntradaDeTexto().trim();
                if (!"".equals(nuevoTexto)) {
                    String contenidoMensaje = fdm.armarContenidoDeMensaje(key, nuevoTexto);
                    ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                        @Override
                        public void resultadoSatisfactorio(Thread t) {
                            String resultado = ((ContactoConServidor)t).getResponse();
                            adc.actualizaCampo(key, nuevoTexto);
                            if(CompruebaCamposJSON.validaContenido(resultado)){
                                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewToChange.setText(nuevoTexto);
                                    }
                                });
                                MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, viewToChange, "Hecho");
                            }
                        }

                        @Override
                        public void problemasDeConexion(Thread t) {
                            MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, viewToChange, "Servicio no disponible por el momento");
                        }
                    }, contenidoMensaje);
                    contacto.start();
                }
            }

            @Override
            public void accionNegativa(DialogFragment fragment) {
            }
        });
        et.show(((AppCompatActivity)context).getSupportFragmentManager(), "Modificar campo");
    }
}

package org.inspira.condominio.actividades;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 24/03/16.
 */
public class ActualizaTextoDesdeEntradaLibre implements View.OnClickListener, EntradaTexto.AccionDialogo{

    private int accionSolicitada;
    private String contenido;
    private String mensaje;
    private View view;
    private Context context;

    public ActualizaTextoDesdeEntradaLibre(Context context, int accionSolicitada, String contenido, String mensaje) {
        this.accionSolicitada = accionSolicitada;
        this.contenido = contenido;
        this.mensaje = mensaje;
        this.context = context;
    }

    @Override
    public void onClick(View view){
        this.view = view;
        Bundle args = new Bundle();
        args.putString("contenido", contenido);
        args.putString("mensaje", mensaje);
        EntradaTexto entradaTexto = new EntradaTexto();
        entradaTexto.setArguments(args);
        entradaTexto.setAccionDialogo(this);
        entradaTexto.show(((AppCompatActivity)context).getSupportFragmentManager(), "Cambiar texto");
    }

    @Override
    public void accionPositiva(DialogFragment fragment) {
        contenido = ((EntradaTexto)fragment).getEntradaDeTexto();
        if(!"".equals(contenido))
            actualizarInformacionRemota();
        else
            ProveedorSnackBar
                    .muestraBarraDeBocados(view, "Sin cambios");
    }

    @Override
    public void accionNegativa(DialogFragment fragment) {}

    private void actualizarInformacionRemota() {
        String cuerpoDeMensaje = armarContenidoDeMensaje(contenido);
        if(cuerpoDeMensaje != null){
            ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                @Override
                public void resultadoSatisfactorio(Thread t) {
                    String respuesta = ((ContactoConServidor)t).getResponse();
                    try{
                        JSONObject json = new JSONObject(respuesta);
                        if(json.getBoolean("content")) {
                            cambiarTexto();
                            ProveedorDeRecursos.guardaUsuario(context, contenido);
                            muestraMensaje("Hecho");
                        }else
                            muestraMensaje("Servicio por el momento no disponible");
                    }catch(JSONException e){ e.printStackTrace(); }
                }

                private void cambiarTexto() {
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) view).setText(contenido);
                        }
                    });
                }

                @Override
                public void problemasDeConexion(Thread t) {
                    muestraMensaje("Servicio temporalmente no disponible");
                }

                private void muestraMensaje(String mensaje) {
                    MuestraMensajeDesdeHilo.muestraMensaje(
                            (AppCompatActivity) context,
                            ActualizaTextoDesdeEntradaLibre.this.view,
                            mensaje);
                }
            }, cuerpoDeMensaje);
            contacto.start();
        }
    }

    private String armarContenidoDeMensaje(String contenido){
        String cuerpoDeMensaje = null;
        try{
            JSONObject json = new JSONObject();
            json.put("email", ProveedorDeRecursos.obtenerEmail(context));
            switch(accionSolicitada){
                case ProveedorDeRecursos.ACTUALIZACION_DE_NOMBRE:
                    json.put("action", ProveedorDeRecursos.ACTUALIZACION_DE_NOMBRE);
                    json.put("nickname", contenido);
                    break;
                default:
                    throw new JSONException("La acción no está soportada.");
            }
            cuerpoDeMensaje = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return cuerpoDeMensaje;
    }
}

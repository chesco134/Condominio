package org.inspira.condominio.actividades;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.inspira.condominio.admon.CompruebaCamposJSON;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jcapiz on 31/03/16 stated
 *
 */
public class ActualizarCampoDeContacto implements View.OnClickListener{

    private static final String ACCION = "action";
    private static final String TABLE = "table";
    private static final String COLUMN = "column";
    private static final String PK_VALUE = "pk_value";
    private int accion;
    private Context context;
    private TextView campoDeTexto;
    private boolean ocupado;
    private String tabla;
    private String columna;
    private int pkValue;
    private String entrada;

    public static ActualizarCampoDeContacto crearActualizarCampoDeContacto(Context context, Bundle bundle){
        List<String> missingTags = new ArrayList<>();
        if(!bundle.containsKey(ACCION))
            missingTags.add(ACCION);
        if(!bundle.containsKey(TABLE))
            missingTags.add(TABLE);
        if(!bundle.containsKey(COLUMN))
            missingTags.add(COLUMN);
        if(!bundle.containsKey(PK_VALUE))
            missingTags.add(PK_VALUE);
        if(missingTags.size() > 0){
            String mensaje = "Hacen falta los siguientes parámetros: ";
            String ultimoTag = missingTags.get(missingTags.size()-1);
            for(String tag : missingTags)
                if(!tag.equals(ultimoTag))
                    mensaje = mensaje.concat(tag).concat(", ");
                else
                    mensaje = mensaje.concat(tag);
            throw new IllegalArgumentException(mensaje);
        }else{
            if( context instanceof AppCompatActivity)
                return new ActualizarCampoDeContacto(context, bundle);
            else
                throw new ClassCastException();
        }
    }

    private ActualizarCampoDeContacto(Context context, Bundle bundle){
        ocupado = false;
        this.context = context;
        accion = bundle.getInt(ACCION);
        tabla = bundle.getString(TABLE);
        columna = bundle.getString(COLUMN);
        pkValue = bundle.getInt(PK_VALUE);
    }

    @Override
    public void onClick(View v){
        if(!ocupado) {
            ocupado = true;
            if (campoDeTexto == null)
                campoDeTexto = (TextView) v;
            iniciaDialogoDeEntradaSimple();
        }
    }

    private void iniciaDialogoDeEntradaSimple(){
        EntradaTexto entradaDeTexto = new EntradaTexto();
        Bundle args = new Bundle();
        args.putString("mensaje", "Modifique el campo");
        args.putString("contenido", campoDeTexto.getText().toString());
        entradaDeTexto.setArguments(args);
        entradaDeTexto.setAccionDialogo(accionEntradaDeTexto);
        entradaDeTexto.show(((AppCompatActivity)context).getSupportFragmentManager(), "Actualización");
    }

    private String armarContenidoDeMensaje(){
        String contenido = null;
        try{
            JSONObject json = new JSONObject();
            json.put(ACCION, accion);
            json.put(TABLE, tabla);
            json.put(COLUMN, columna);
            json.put("value", entrada);
            json.put(PK_VALUE, pkValue);
            contenido = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return contenido;
    }

    private void actualizaTextoDesdeHilo(){
        ((AppCompatActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                campoDeTexto.setText(entrada);
            }
        });
    }

    private void actualizarBaseDeDatos(){
        ContentValues values = new ContentValues();
        values.put(columna, entrada);
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getWritableDatabase();
        db.update(tabla,values, "id"+tabla+" = CAST(? as INTEGER)",
                new String[]{String.valueOf(pkValue)});
        db.close();
        condominioBD.close();
    }

    private final EntradaTexto.AccionDialogo accionEntradaDeTexto = new EntradaTexto.AccionDialogo() {

        @Override
        public void accionPositiva(DialogFragment fragment) {
            String respuesta = ((EntradaTexto)fragment).getEntradaDeTexto();
            if(!"".equals(respuesta)){
                entrada = respuesta;
                String contenidoDeMensaje = armarContenidoDeMensaje();
                ContactoConServidor contacto = new ContactoConServidor(validacion, contenidoDeMensaje);
                contacto.start();
            }
        }

        @Override
        public void accionNegativa(DialogFragment fragment) {}
    };

    private final ContactoConServidor.AccionesDeValidacionConServidor validacion = new ContactoConServidor.AccionesDeValidacionConServidor() {
        @Override
        public void resultadoSatisfactorio(Thread t) {
            String respuesta = ((ContactoConServidor)t).getResponse();
            if(CompruebaCamposJSON.validaContenido(respuesta)){
                actualizarBaseDeDatos();
                actualizaTextoDesdeHilo();
                MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, campoDeTexto, "Hecho");
            }else{
                MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, campoDeTexto, "Servicio temporalmente no disponible");
            }
            ocupado = false;
        }

        @Override
        public void problemasDeConexion(Thread t) {
            MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity) context, campoDeTexto, "Servicio por el momento inalcanzable");
            ocupado = false;
        }
    };

}

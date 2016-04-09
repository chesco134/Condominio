package org.inspira.condominio.actividades;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 30/03/16.
 */
public class InsertarElementoMultivalor extends DialogFragment {

    public static final java.lang.String ACCION = "accion";
    public static final java.lang.String FK_NAME = "fk_name";
    public static final java.lang.String FK_VALUE = "fk_value";
    //public static final java.lang.String PK_NAME = "pk_name";
    //public static final java.lang.String PK_VALUE = "pk_value";
    public static final java.lang.String TABLE_NAME = "table_name";
    public static final java.lang.String COLUMN_NAME = "column_name";
    public static final java.lang.String TITULO = "titulo";
    //public static final java.lang.String CURRENT_VALUE = "valor_actual";
    public static final java.lang.String INPUT_TYPE = "input_type";
    public static final String RECURSO_DE_CONTENEDOR = "content_res";
    private String tableName;
    private String columnName;
    private EditText entradaDeTexto;
    private String contenido;
    private String fkName;
    private int fkValue;
    private int action;
    private int recursoDeContenedor;
    private Context context;

    public static InsertarElementoMultivalor crearDialogo(Bundle args){
        InsertarElementoMultivalor dialogo = new InsertarElementoMultivalor();
        List<String> missingValues = new ArrayList<>();
        if(!args.containsKey(ACCION))
            missingValues.add(ACCION);
        if(!args.containsKey(TABLE_NAME))
            missingValues.add(TABLE_NAME);
        if(!args.containsKey(COLUMN_NAME))
            missingValues.add(COLUMN_NAME);
        if(!args.containsKey(FK_NAME))
            missingValues.add(FK_NAME);
        if(!args.containsKey(FK_VALUE))
            missingValues.add(FK_VALUE);
        if(!args.containsKey(TITULO))
            missingValues.add(TITULO);
        if(!args.containsKey(INPUT_TYPE))
            missingValues.add(INPUT_TYPE);
        if(!args.containsKey(RECURSO_DE_CONTENEDOR))
            missingValues.add(RECURSO_DE_CONTENEDOR);
        if(missingValues.size() > 0){
            String errorMessage = "No se han proporcionado los valores: ";
            String lastValue = missingValues.get(missingValues.size()-1);
            for(String key : missingValues)
                if(!key.equals(lastValue))
                    errorMessage = errorMessage.concat("\"" + key + "\",");
                else
                    errorMessage = errorMessage.concat("\"" + key + "\".");
            errorMessage = errorMessage.concat("\nPor favor colóquelos.");
            throw new IllegalArgumentException(errorMessage);
        }else {
            dialogo.setArguments(args);
            return dialogo;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        context = getContext();
        Bundle args = getArguments();
        action = args.getInt(ACCION);
        tableName = args.getString(TABLE_NAME);
        columnName = args.getString(COLUMN_NAME);
        fkName = args.getString(FK_NAME);
        fkValue = args.getInt(FK_VALUE);
        recursoDeContenedor = args.getInt(RECURSO_DE_CONTENEDOR);
        String titulo = args.getString(TITULO);
        int tipoDeEntradaTextual = args.getInt(INPUT_TYPE);
        RelativeLayout contenedor = (RelativeLayout)((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.entrada_de_texto, null, false);
        entradaDeTexto = (EditText) contenedor.findViewById(R.id.entrada_de_texto);
        entradaDeTexto.setInputType(tipoDeEntradaTextual);
        entradaDeTexto.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), contenedor.findViewById(R.id.piso_entrada_de_texto)));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder = builder.setTitle(titulo);
        builder = builder.setView(contenedor);
        builder = builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                contenido = entradaDeTexto.getText().toString().trim();
                if(!"".equals(contenido))
                    validarInformacionRemotamente();
            }
        });
        return builder.create();
    }

    private void validarInformacionRemotamente(){
        ContactoConServidor.AccionesDeValidacionConServidor validaciones = new ContactoConServidor.AccionesDeValidacionConServidor() {
            @Override
            public void resultadoSatisfactorio(Thread t) {
                String respuesta = ((ContactoConServidor)t).getResponse();
                if(CompruebaCamposJSON.validaContenido(respuesta)){
                    String pkValue = obtenerPKValue(respuesta);
                    agregarValor(pkValue, contenido);
                    MuestraMensajeDesdeHilo.muestraToast((AppCompatActivity)context, "Hecho");
                }else{
                    MuestraMensajeDesdeHilo.muestraToast((AppCompatActivity)context, "Saludos! muchos saludos para usted!! =)");
                }
            }

            private String obtenerPKValue(String respuesta) {
                String pkValue = null;
                try{
                    pkValue = new JSONObject(respuesta).getString("id");
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return pkValue;
            }

            @Override
            public void problemasDeConexion(Thread t) {
                MuestraMensajeDesdeHilo
                        .muestraToast((AppCompatActivity)context, "Servicio por el momento no disponible");
            }
        };
        String contenidoDeMensaje = armarContenidoDeMensaje();
        ContactoConServidor contacto = new ContactoConServidor(validaciones, contenidoDeMensaje);
        contacto.start();
    }

    private String armarContenidoDeMensaje(){
        String contenidoDeMensaje = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action", action);
            json.put("tabla", tableName);
            json.put("column_name", columnName);
            json.put("column_value", contenido);
            json.put("fk_name", fkName);
            json.put("fk_value", fkValue);
            contenidoDeMensaje = json.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return contenidoDeMensaje;
    }

    private void agregarValor(String pk_value, String valor){
        ContentValues values = new ContentValues();
        values.put("id".concat(tableName), pk_value);
        values.put(columnName, valor);
        values.put(fkName, fkValue);
        CondominioBD bdCondominio = new CondominioBD(context);
        SQLiteDatabase db = bdCondominio.getWritableDatabase();
        db.insert(tableName, "---", values);
        db.close();
        bdCondominio.close();
        colocarEtiquetaEnActividad(pk_value, valor);
    }

    private void colocarEtiquetaEnActividad(String pkValue, String texto){
        final LinearLayout container = (LinearLayout)((AppCompatActivity)context).findViewById(recursoDeContenedor);
        Bundle args = new Bundle();
        args.putInt("action", ProveedorDeRecursos.ACTUALIZACION_DE_CONTACTO);
        args.putString("table", tableName);
        args.putString("column", columnName);
        args.putInt("pk_value", Integer.parseInt(pkValue));
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final TextView nuevoTexto = (TextView) inflater.inflate(R.layout.entrada_simple_de_texto, container, false);
        nuevoTexto.setText(texto);
        nuevoTexto.setOnClickListener(ActualizarCampoDeContacto.crearActualizarCampoDeContacto(context, args));
        ((AppCompatActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                container.addView(nuevoTexto);
            }
        });
    }
}

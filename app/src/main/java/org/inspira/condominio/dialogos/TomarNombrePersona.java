package org.inspira.condominio.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.ActualizaNombreEnBD;
import org.inspira.condominio.actividades.EfectoDeEnfoque;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.actividades.CompruebaCamposJSON;
import org.inspira.condominio.datos.Persona;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 1/04/16.
 */
public class TomarNombrePersona extends DialogFragment {

    private static final String PERSONA = "persona";
    private Persona persona;
    private Context context;
    private EditText nombres;
    private EditText apPaterno;
    private EditText apMaterno;

    public static TomarNombrePersona obtenerTomarNombre(Bundle args){
        List<String> missingParams = new ArrayList<>();
        if (!args.containsKey(PERSONA))
            missingParams.add(PERSONA);
        if(missingParams.size() > 0){
            String sentence = "Hace falta proporcionar: ";
            String lastMissingParam = missingParams.get(missingParams.size()-1);
            for(String missingParam : missingParams)
                if (missingParam.equals(lastMissingParam))
                    sentence = sentence.concat(missingParam);
                else
                    sentence = sentence.concat(missingParam).concat(", ");
            throw new IllegalArgumentException(sentence);
        }else{
            TomarNombrePersona tomarNombrePersona = new TomarNombrePersona();
            tomarNombrePersona.setArguments(args);
            return tomarNombrePersona;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Bundle args = getArguments();
        persona = (Persona) args.getSerializable(PERSONA);
        context = getContext();
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.formato_registro_de_nombre, null);
        nombres = (EditText) root.findViewById(R.id.formato_registro_de_nombre_nombres);
        nombres.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), root.findViewById(R.id.formato_registro_de_nombre_piso_nombres)));
        apPaterno = (EditText) root.findViewById(R.id.formato_registro_de_nombre_ap_paterno);
        apPaterno.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), root.findViewById(R.id.formato_registro_de_nombre_piso_ap_paterno)));
        apMaterno = (EditText) root.findViewById(R.id.formato_registro_de_nombre_ap_materno);
        apMaterno.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), root.findViewById(R.id.formato_registro_de_nombre_piso_ap_materno)));
        nombres.setText(persona.getNombres());
        apPaterno.setText(persona.getApPaterno());
        apMaterno.setText(persona.getApMaterno());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder = builder.setTitle("Nombre completo del habitante");
        builder = builder.setView(root);
        builder = builder.setPositiveButton("Aceptar", positiveAction);
        builder = builder.setNegativeButton("Cancelar", negativeAction);
        return builder.create();
    }

    private final DialogInterface.OnClickListener positiveAction = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String nombres = TomarNombrePersona.this.nombres.getText().toString().trim();
            String apPaterno = TomarNombrePersona.this.apPaterno.getText().toString().trim();
            String apMaterno = TomarNombrePersona.this.apMaterno.getText().toString().trim();
            if(!"".equals(nombres) && !"".equals(apPaterno) && !"".equals(apMaterno)) {
                String contenidoDeMensaje = armarMensaje();
                enviaMensajeAlServidor(contenidoDeMensaje);
            }
        }
    };

    private void enviaMensajeAlServidor(String contenidoDeMensaje) {
        ContactoConServidor contacto = new ContactoConServidor(validacion, contenidoDeMensaje);
        contacto.start();
    }

    private String armarMensaje() {
        String contenidoDeMensaje = null;
        try{
            String table = persona.getClass().getSimpleName();
            String key = "id".concat(table);
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.ACTUALIZAR_NOMBRE);
            json.put("nombres", nombres.getText().toString().trim());
            json.put("ap_paterno", apPaterno.getText().toString().trim());
            json.put("ap_materno", apMaterno.getText().toString().trim());
            json.put("key", key);
            json.put("value", persona.getId());
            json.put("table", table);
            contenidoDeMensaje = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return contenidoDeMensaje;
    }

    private final ContactoConServidor.AccionesDeValidacionConServidor validacion = new ContactoConServidor.AccionesDeValidacionConServidor() {
        @Override
        public void resultadoSatisfactorio(Thread t) {
            String resultado = ((ContactoConServidor)t).getResponse();
            try {
                if(CompruebaCamposJSON.validaContenido(resultado)){
                    persona.setNombres(nombres.getText().toString().trim());
                    persona.setApPaterno(apPaterno.getText().toString().trim());
                    persona.setApMaterno(apMaterno.getText().toString().trim());
                    ActualizaNombreEnBD.actualizacion(context, persona);
                    String nuevoNombre = persona.getApPaterno() + " " +
                            persona.getApMaterno() + " " + persona.getNombres();
                    Method method = context.getClass().getMethod("actualizarNombre", String.class);
                    method.invoke(context, nuevoNombre);
                    MuestraMensajeDesdeHilo.muestraToast((AppCompatActivity) context,
                            "Hecho");
                }else{
                    MuestraMensajeDesdeHilo.muestraToast((AppCompatActivity)context,
                            "Servicio momentaneamente inalcansable");
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void problemasDeConexion(Thread t) {
            MuestraMensajeDesdeHilo.muestraToast((AppCompatActivity)context,
                    "Servicio por el momento no disponible");
        }
    };

    private final DialogInterface.OnClickListener negativeAction = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {}
    };
}

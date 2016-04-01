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
import org.inspira.condominio.actividades.EfectoDeEnfoque;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admin.habitantes.ResumenHabitante;
import org.inspira.condominio.admon.AccionesTablaHabitante;
import org.inspira.condominio.admon.CompruebaCamposJSON;
import org.inspira.condominio.datos.Habitante;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 1/04/16.
 */
public class TomarNombreHabitante extends DialogFragment {

    private static final String HABITANTE = "habitante";
    private static Habitante habitante;
    private Context context;
    private EditText nombres;
    private EditText apPaterno;
    private EditText apMaterno;

    public static TomarNombreHabitante obtenerTomarNombreHabitante(Bundle args){
        if(!args.containsKey(HABITANTE)){
            throw new IllegalArgumentException("Hace falta proporcionar un habitante");
        }else{
            TomarNombreHabitante tomarNombreHabitante = new TomarNombreHabitante();
            tomarNombreHabitante.setArguments(args);
            return tomarNombreHabitante;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        habitante = (Habitante) getArguments().getSerializable(HABITANTE);
        context = getContext();
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.formato_registro_de_nombre, null);
        nombres = (EditText) root.findViewById(R.id.formato_registro_de_nombre_nombres);
        nombres.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), root.findViewById(R.id.formato_registro_de_nombre_piso_nombres)));
        apPaterno = (EditText) root.findViewById(R.id.formato_registro_de_nombre_ap_paterno);
        apPaterno.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), root.findViewById(R.id.formato_registro_de_nombre_piso_ap_paterno)));
        apMaterno = (EditText) root.findViewById(R.id.formato_registro_de_nombre_ap_materno);
        apMaterno.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), root.findViewById(R.id.formato_registro_de_nombre_piso_ap_materno)));
        nombres.setText(habitante.getNombres());
        apPaterno.setText(habitante.getApPaterno());
        apMaterno.setText(habitante.getApMaterno());
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
            String nombres = TomarNombreHabitante.this.nombres.getText().toString().trim();
            String apPaterno = TomarNombreHabitante.this.apPaterno.getText().toString().trim();
            String apMaterno = TomarNombreHabitante.this.apMaterno.getText().toString().trim();
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
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.ACTUALIZAR_NOMBRE_HABITANTE);
            json.put("nombres", nombres.getText().toString().trim());
            json.put("ap_paterno", apPaterno.getText().toString().trim());
            json.put("ap_materno", apMaterno.getText().toString().trim());
            json.put("idHabitante", habitante.getId());
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
            if(CompruebaCamposJSON.validaContenido(resultado)){
                String nombres = TomarNombreHabitante.this.nombres.getText().toString().trim();
                String apPaterno = TomarNombreHabitante.this.apPaterno.getText().toString().trim();
                String apMaterno = TomarNombreHabitante.this.apMaterno.getText().toString().trim();
                AccionesTablaHabitante.actualizarNombre(context, habitante.getId(), nombres, apPaterno, apMaterno);
                String nuevoNombre = apPaterno + " " + apMaterno + " " + nombres;
                ((ResumenHabitante) context).actualizarNombre(nuevoNombre);
                MuestraMensajeDesdeHilo.muestraToast((AppCompatActivity)context,
                        "Hecho");
            }else{
                MuestraMensajeDesdeHilo.muestraToast((AppCompatActivity)context,
                        "Servicio momentaneamente inalcansable");
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

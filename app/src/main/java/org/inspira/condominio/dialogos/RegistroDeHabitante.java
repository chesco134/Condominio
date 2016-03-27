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
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.ActualizaTextoDesdeLista;
import org.inspira.condominio.actividades.EfectoDeEnfoque;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.actividades.Verificador;
import org.inspira.condominio.admin.habitantes.ControlDeHabitantes;
import org.inspira.condominio.admon.AccionesTablaHabitante;
import org.inspira.condominio.admon.AccionesTablaTorre;
import org.inspira.condominio.datos.Habitante;
import org.inspira.condominio.datos.Torre;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 26/03/16.
 */
public class RegistroDeHabitante extends DialogFragment {

    private EditText nombres;
    private EditText apPaterno;
    private EditText apMaterno;
    private EditText nombreDepartamento;
    private TextView torre;
    private Context context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        context = getContext();
        View rootView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.formato_registro_de_habitante, null, false);
        nombres = (EditText) rootView.findViewById(R.id.formato_registro_de_habitante_nombres);
        nombres.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(),rootView.findViewById(R.id.formato_registro_de_habitante_piso_nombres)));
        apPaterno = (EditText) rootView.findViewById(R.id.formato_registro_de_habitante_ap_paterno);
        apPaterno.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.formato_registro_de_habitante_piso_ap_paterno)));
        apMaterno = (EditText) rootView.findViewById(R.id.formato_registro_de_habitante_ap_materno);
        apMaterno.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.formato_registro_de_habitante_piso_ap_materno)));
        nombreDepartamento = (EditText) rootView.findViewById(R.id.formato_registro_de_habitante_nombre_departamento);
        nombreDepartamento.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.formato_registro_de_habitante_piso_nombre_departamento)));
        torre = (TextView) rootView.findViewById(R.id.formato_registro_de_habitante_torre);
        Torre[] torres = AccionesTablaTorre.obtenerTorres(getContext(), ProveedorDeRecursos.obtenerIdAdministracion(getContext()));
        String[] nombresTorre = new String[torres.length];
        int i=0;
        for(Torre torre : torres)
            nombresTorre[i++] = torre.getNombre();
        torre.setOnClickListener(new ActualizaTextoDesdeLista("Torre a la que pertenece", nombresTorre));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle(getArguments().getString("titulo"))
                .setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(validarInformacion()){
                            registrarHabitanteEnServidorRemoto();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .setView(rootView)
                .create();
    }

    private void registrarHabitanteEnServidorRemoto() {
        String contenidoMensaje = armarMensaje();
        ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
            @Override
            public void resultadoSatisfactorio(Thread t) {
                String respuesta = ((ContactoConServidor)t).getResponse();
                if(validaRespuesta(respuesta)){
                    guardaEnBaseDeDatos(obtenerIdDeHabitante(respuesta));
                    MuestraMensajeDesdeHilo.muestraToast((AppCompatActivity)context, "Hecho");
                }
            }

            private int obtenerIdDeHabitante(String respuesta) {
                int idHabitante = -1;
                try{
                    idHabitante = new JSONObject(respuesta).getInt("idHabitante");
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return idHabitante;
            }

            private boolean validaRespuesta(String respuesta) {
                boolean esValida = false;
                try{
                    esValida = new JSONObject(respuesta).getBoolean("content");
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return esValida;
            }

            @Override
            public void problemasDeConexion(Thread t) {
                MuestraMensajeDesdeHilo.muestraToast(getActivity(), "Servicio temporalmente no disponible");
            }
        }, contenidoMensaje);
        contacto.start();
    }

    private void guardaEnBaseDeDatos(int idHabitante) {
        Habitante habitante = new Habitante(idHabitante);
        habitante.setIdTorre(AccionesTablaTorre.obtenerIdTorre(context, torre.getText().toString()));
        habitante.setNombres(nombres.getText().toString().trim());
        habitante.setApPaterno(apPaterno.getText().toString().trim());
        habitante.setApMaterno(apMaterno.getText().toString().trim());
        habitante.setNombreDepartamento(nombreDepartamento.getText().toString().trim());
        AccionesTablaHabitante.agregarHabitante(context, habitante);
        if(habitante.getIdTorre() == ProveedorDeRecursos.obtenerIdTorreActual(context))
            ((ControlDeHabitantes)context).agregarHabitante(habitante);
    }

    private String armarMensaje() {
        String contenidoMensaje = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.REGISTRO_DE_HABITANTE);
            json.put("nombres", nombres.getText().toString().trim());
            json.put("ap_paterno", apPaterno.getText().toString().trim());
            json.put("ap_materno", apMaterno.getText().toString().trim());
            json.put("nombre_departamento", nombreDepartamento.getText().toString().trim());
            json.put("idTorre", AccionesTablaTorre.obtenerIdTorre(context, torre.getText().toString()));
            contenidoMensaje = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return contenidoMensaje;
    }

    private boolean validarInformacion() {
        boolean[] validacion = new boolean[5];
        validacion[0] = Verificador.marcaCampo(getContext(), nombres, Verificador.TEXTO);
        validacion[1] = Verificador.marcaCampo(getContext(), apPaterno, Verificador.TEXTO);
        validacion[2] = Verificador.marcaCampo(getContext(), apMaterno, Verificador.TEXTO);
        validacion[3] = Verificador.marcaCampo(getContext(), nombreDepartamento, Verificador.TEXTO);
        validacion[4] = Verificador.marcaCampo(getContext(), torre, Verificador.TEXTO);
        return validacion[0] && validacion[1] && validacion[2] && validacion[3] && validacion[4];
    }
}

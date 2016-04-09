package org.inspira.condominio.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.EfectoDeEnfoque;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.actividades.Verificador;
import org.inspira.condominio.admon.AccionesTablaTrabajador;
import org.inspira.condominio.actividades.CompruebaCamposJSON;
import org.inspira.condominio.datos.Trabajador;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 28/03/16.
 */
public class RegistroDeTrabajador extends DialogFragment {

    private EditText nombres;
    private EditText apPaterno;
    private EditText apMaterno;
    private EditText salario;
    private String tipoDeTrabajador;
    private CheckBox poseeSeguroSocial;
    private RadioGroup seleccionDeGenero;
    private Context context;
    private AccionRegistroDeTrabajador ardt;

    public void setArdt(AccionRegistroDeTrabajador ardt) {
        this.ardt = ardt;
    }

    public interface AccionRegistroDeTrabajador{
        void trabajadorRegistrado(Trabajador trabajador);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        context = getContext();
        View rootView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.formato_registro_de_trabajador, null, false);
        nombres = (EditText) rootView.findViewById(R.id.formato_registro_de_trabajador_nombres);
        nombres.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(),rootView.findViewById(R.id.formato_registro_de_trabajador_piso_nombres)));
        apPaterno = (EditText) rootView.findViewById(R.id.formato_registro_de_trabajador_ap_paterno);
        apPaterno.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.formato_registro_de_trabajador_piso_ap_paterno)));
        apMaterno = (EditText) rootView.findViewById(R.id.formato_registro_de_trabajador_ap_materno);
        apMaterno.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.formato_registro_de_trabajador_piso_ap_materno)));
        salario = (EditText) rootView.findViewById(R.id.formato_registro_de_trabajador_salario);
        salario.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.formato_registro_de_trabajador_piso_salario)));
        poseeSeguroSocial = (CheckBox) rootView.findViewById(R.id.formato_registro_de_trabajador_posee_seguro_social);
        seleccionDeGenero = (RadioGroup) rootView.findViewById(R.id.formato_registro_de_trabajador_generos);
        tipoDeTrabajador = getArguments().getString("tipo_de_trabajador");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                if(CompruebaCamposJSON.validaContenido(respuesta)){
                    guardaEnBaseDeDatos(obtenerIdDeTrabajador(respuesta));
                }
            }

            private int obtenerIdDeTrabajador(String respuesta) {
                int idTrabajador = -1;
                try{
                    idTrabajador = new JSONObject(respuesta).getInt("idTrabajador");
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return idTrabajador;
            }

            @Override
            public void problemasDeConexion(Thread t) {
                MuestraMensajeDesdeHilo.muestraToast(getActivity(), "Servicio temporalmente no disponible");
            }
        }, contenidoMensaje);
        contacto.start();
    }

    private void guardaEnBaseDeDatos(int idTrabajador) {
        Trabajador trabajador = new Trabajador(idTrabajador);
        trabajador.setNombres(nombres.getText().toString().trim());
        trabajador.setApPaterno(apPaterno.getText().toString().trim());
        trabajador.setApMaterno(apMaterno.getText().toString().trim());
        trabajador.setSalario(Float.parseFloat(salario.getText().toString()));
        trabajador.setPoseeSeguroSocial(poseeSeguroSocial.isChecked());
        trabajador.setGenero(seleccionDeGenero.getCheckedRadioButtonId() == R.id.formato_registro_de_trabajador_masculino);
        trabajador.setIdTipoDeTrabajador(AccionesTablaTrabajador.obtenerIdTipoDeTrabajador(context, tipoDeTrabajador));
        trabajador.setIdAdministracion(ProveedorDeRecursos.obtenerIdAdministracion(context));
        AccionesTablaTrabajador.agregarTrabajador(context, trabajador);
        ardt.trabajadorRegistrado(trabajador);
    }

    private String armarMensaje() {
        String contenidoMensaje = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.REGISTRO_DE_TRABAJADOR);
            json.put("nombres", nombres.getText().toString().trim());
            json.put("ap_paterno", apPaterno.getText().toString().trim());
            json.put("ap_materno", apMaterno.getText().toString().trim());
            json.put("salario", Float.parseFloat(salario.getText().toString()));
            json.put("idTipo_de_Trabajador", AccionesTablaTrabajador.obtenerIdTipoDeTrabajador(context, tipoDeTrabajador));
            json.put("genero", seleccionDeGenero.getCheckedRadioButtonId() == R.id.formato_registro_de_trabajador_masculino ? 1 : 0);
            json.put("posee_seguro_social", poseeSeguroSocial.isChecked() ? 1 : 0);
            json.put("idAdministracion", ProveedorDeRecursos.obtenerIdAdministracion(context));
            contenidoMensaje = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return contenidoMensaje;
    }

    private boolean validarInformacion() {
        boolean[] validacion = new boolean[4];
        validacion[0] = Verificador.marcaCampo(getContext(), nombres, Verificador.TEXTO);
        validacion[1] = Verificador.marcaCampo(getContext(), apPaterno, Verificador.TEXTO);
        validacion[2] = Verificador.marcaCampo(getContext(), apMaterno, Verificador.TEXTO);
        validacion[3] = Verificador.marcaCampo(getContext(), salario, Verificador.FLOTANTE);
        return validacion[0] && validacion[1] && validacion[2] && validacion[3];
    }
}

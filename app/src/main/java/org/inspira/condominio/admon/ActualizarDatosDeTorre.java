package org.inspira.condominio.admon;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.CompruebaCamposJSON;
import org.inspira.condominio.actividades.EfectoDeEnfoque;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.datos.Torre;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 26/03/16.
 */
public class ActualizarDatosDeTorre extends Fragment{

    private EditText nombre;
    private EditText numeroDePisos;
    private EditText numeroDeFocos;
    private EditText numeroDeDepartamentos;
    private SwitchCompat cuentaConElevador;
    private int idTorre;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.formulario_torre, parent, false);
        nombre = (EditText) rootView.findViewById(R.id.formulario_torre_nombre);
        numeroDePisos = (EditText) rootView.findViewById(R.id.formulario_torre_cantidad_de_pisos);
        numeroDeFocos = (EditText) rootView.findViewById(R.id.formulario_torre_cantidad_de_focos);
        numeroDeDepartamentos = (EditText) rootView.findViewById(R.id.formulario_torre_cantidad_de_departamentos);
        cuentaConElevador = (SwitchCompat) rootView.findViewById(R.id.formulario_torre_posee_elevador);
        nombre.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.formulario_torre_piso_nombre)));
        numeroDePisos.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.formulario_torre_piso_cantidad_de_pisos)));
        numeroDeFocos.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.formulario_torre_piso_cantidad_de_focos)));
        numeroDeDepartamentos.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.formulario_torre_piso_cantidad_de_departamentos)));
        Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
        nombre.setText(args.getString("nombre"));
        numeroDePisos.setText(args.getString("numero_de_pisos"));
        numeroDeFocos.setText(args.getString("numero_de_focos"));
        numeroDeDepartamentos.setText(args.getString("numero_de_departamentos"));
        cuentaConElevador.setChecked(args.getBoolean("posee_elevador"));
        idTorre = args.getInt("idTorre");
        rootView.findViewById(R.id.formulario_torre_hecho).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                    @Override
                    public void resultadoSatisfactorio(Thread t) {
                        String respuesta = ((ContactoConServidor) t).getResponse();
                        if (CompruebaCamposJSON.validaContenido(respuesta)) {
                            actualizarTorre();
                            MuestraMensajeDesdeHilo.muestraMensaje(getActivity(), nombre, "Hecho");
                        } else {
                            MuestraMensajeDesdeHilo.muestraMensaje(getActivity(), nombre, "Servicio por el momento no disponible");
                        }
                    }

                    @Override
                    public void problemasDeConexion(Thread t) {
                        MuestraMensajeDesdeHilo.muestraMensaje(getActivity(), nombre, "Servicio temporalmente inalcanzable");
                    }
                }, armaCuerpoDeMensaje());
                contacto.start();
            }
        });
        return rootView;
    }

    private void actualizarTorre() {
        Torre torre = new Torre(idTorre);
        torre.setNombre(nombre.getText().toString().trim());
        torre.setCantidadDePisos(Integer.parseInt(numeroDePisos.getText().toString()));
        torre.setCantidadDeFocos(Integer.parseInt(numeroDeFocos.getText().toString()));
        torre.setCantidadDeDepartamentos(Integer.parseInt(numeroDeDepartamentos.getText().toString()));
        torre.setPoseeElevador(cuentaConElevador.isChecked());
        AccionesTablaTorre.actualizaTorre(getContext(), torre);
    }

    private String armaCuerpoDeMensaje() {
        String cuerpoDeMensaje = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.ACTUALIZAR_DATOS_TORRE);
            json.put("nombre", nombre.getText().toString().trim());
            json.put("cantidad_de_pisos", Integer.parseInt(numeroDePisos.getText().toString()));
            json.put("cantidad_de_focos", Integer.parseInt(numeroDeFocos.getText().toString()));
            json.put("cantidad_de_departamentos", Integer.parseInt(numeroDeDepartamentos.getText().toString()));
            json.put("posee_elevador", cuentaConElevador.isChecked());
            json.put("idTorre", idTorre);
            cuerpoDeMensaje = json.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return cuerpoDeMensaje;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("nombre", nombre.getText().toString().trim());
        outState.putString("numero_de_pisos", numeroDePisos.getText().toString().trim());
        outState.putString("numero_de_focos", numeroDeFocos.getText().toString().trim());
        outState.putString("numero_de_departamentos", numeroDeDepartamentos.getText().toString().trim());
        outState.putBoolean("posee_elevador", cuentaConElevador.isChecked());
        outState.putInt("idTorre", idTorre);
    }

    @Override
    public void onResume(){
        super.onResume();
        ((AppCompatActivity)getContext()).getSupportActionBar().setTitle("Actualizar información");
    }
}

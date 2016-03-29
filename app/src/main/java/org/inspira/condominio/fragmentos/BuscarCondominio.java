package org.inspira.condominio.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.EfectoDeEnfoque;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admon.AccionesTablaCondominio;
import org.inspira.condominio.admon.NuevoCondominioActivity;
import org.inspira.condominio.datos.Administracion;
import org.inspira.condominio.datos.Condominio;
import org.inspira.condominio.datos.TipoDeCondominio;
import org.inspira.condominio.datos.Torre;
import org.inspira.condominio.dialogos.DialogoDeConsultaSimple;
import org.inspira.condominio.dialogos.DialogoDeLista;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jcapiz on 19/03/16.
 */
public class BuscarCondominio extends Fragment {

    private EditText direccion;
    private Condominio condominio;
    private Torre torre;
    private Administracion administracion;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.consulta_condominio, parent, false);
        direccion = (EditText)rootView.findViewById(R.id.consulta_condominio_direccion);
        direccion.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), rootView.findViewById(R.id.consulta_condominio_piso_direccion)));
        rootView.findViewById(R.id.consulta_condominio_boton_consulta)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!"".equals(direccion.getText().toString())){
                            ContactoConServidor contactoConServidor = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {

                                private Map<String, Integer> resultadosCondominios;

                                @Override
                                public void resultadoSatisfactorio(Thread t) {
                                    String respuesta = ((ContactoConServidor)t).getResponse();
                                    if(estadoDeLaRespuesta(respuesta)){
                                        String[] direcciones = obtenerDirecciones(respuesta);
                                        if(direcciones != null){
                                            muestraDialogoDeSeleccion(direcciones);
                                        }
                                    }else{
                                        muestraMensajeDesdeHilo("Servicio por el momento no disponible");
                                    }
                                }

                                private void muestraDialogoDeSeleccion(String[] direcciones) {
                                    DialogoDeLista dlista = new DialogoDeLista();
                                    dlista.setElementos(direcciones);
                                    dlista.setTitulo("Condominios");
                                    dlista.setAccion(new DialogoDeLista.AccionDialogoDeLista() {
                                        @Override
                                        public void objetoSeleccionado(String texto) {
                                            int idCondominio = resultadosCondominios.get(texto);
                                            if(AccionesTablaCondominio.consultaCondominio(getContext(), idCondominio)){
                                                ProveedorDeRecursos.guardaRecursoInt(getContext(), "idCondominio", idCondominio);
                                            }else{
                                                String contenido = armaContenidoPeticionDeCondominio();
                                                enviarSolicitudDeContenido(contenido);
                                            }
                                        }
                                    });
                                }

                                private void enviarSolicitudDeContenido(String contenido) {
                                    ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                                        @Override
                                        public void resultadoSatisfactorio(Thread t) {
                                            String respuesta = ((ContactoConServidor)t).getResponse();
                                            if(estadoDeLaRespuesta(respuesta)){
                                                armaCondominio(respuesta);
                                                AccionesTablaCondominio.agregarCondominio(getContext(), condominio);
                                            }else{
                                                muestraMensajeDesdeHilo("Servicio por el momento inaccesible");
                                            }
                                        }

                                        @Override
                                        public void problemasDeConexion(Thread t) {
                                            muestraMensajeDesdeHilo("Servicio temporalmente no disponible");
                                        }
                                    }, contenido);
                                    contacto.start();
                                }

                                private String armaContenidoPeticionDeCondominio() {
                                    String peticion = null;
                                    try{
                                        JSONObject json = new JSONObject();
                                        json.put("action", ProveedorDeRecursos.SOLICITAR_CONTENIDO_CONDOMINIO);
                                        json.put("idCondominio", ProveedorDeRecursos.obtenerIdCondominio(getContext()));
                                        peticion = json.toString();
                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                    return peticion;
                                }

                                private String[] obtenerDirecciones(String respuesta) {
                                    String[] direcciones = null;
                                    try{
                                        JSONObject json = new JSONObject(respuesta);
                                        JSONArray array = json.getJSONArray("content");
                                        List<String> elementos = new ArrayList<>();
                                        resultadosCondominios = new TreeMap<>();
                                        for(int i=0; i<array.length(); i++) {
                                            elementos.add(array.optJSONObject(i).getString("direccion"));
                                            resultadosCondominios.put(array.optJSONObject(i).getString("direccion"), array.optJSONObject(i).getInt("idCondominio"));
                                        }
                                        direcciones = elementos.toArray(new String[1]);
                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                    return direcciones;
                                }

                                private boolean estadoDeLaRespuesta(String respuesta) {
                                    boolean adecuado = true;
                                    try{
                                        adecuado = new JSONObject(respuesta).getBoolean("content");
                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                    return adecuado;
                                }

                                @Override
                                public void problemasDeConexion(Thread t) {
                                    muestraMensajeDesdeHilo("Servicio temporalmente no disponible");
                                }
                            }, armaMensajeDeConsulta());
//                            contactoConServidor.start();
                            ProveedorSnackBar
                                    .muestraBarraDeBocados(direccion, "En fase de pruebas...");
                        }else{
                            ProveedorSnackBar
                                    .muestraBarraDeBocados(direccion, "Necesitamos un código");
                        }
                    }
                });
        rootView.findViewById(R.id.consulta_condominio_boton_nuevo_registro)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(getActivity(), NuevoCondominioActivity.class), 321);
                        /****************************
                         * En el onResume de "Preparacion", debe revisarse la existencia de
                         * Condominio, Torres, Administración y Administrador, para tomar la acción
                         * que corresponda.
                         **************************************************************************/
                    }
                });
        if(savedInstanceState != null){
            direccion.setText(savedInstanceState.getString("direccion"));
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("direccion", direccion.getText().toString());
    }

    private void muestraMensajeDesdeHilo(final String mensaje){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProveedorSnackBar
                        .muestraBarraDeBocados(direccion, mensaje);
            }
        });
    }

    private String armaMensajeDeConsulta(){
        String mensaje = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action", 16);
            json.put("direccion", direccion.getText().toString().trim());
            mensaje = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return mensaje;
    }

    private void muestraDialogoDeConfirmacion(){
        DialogoDeConsultaSimple ddcs = new DialogoDeConsultaSimple();
        Bundle args = new Bundle();
        args.putString("mensaje", "¿Desea unirse al condominio proporcionado?");
        ddcs.setArguments(args);
        ddcs.setAgenteDeInteraccion(new DialogoDeConsultaSimple.AgenteDeInteraccionConResultado() {
            @Override
            public void clickSobreAccionPositiva(DialogFragment dialogo) {

            }

            @Override
            public void clickSobreAccionNegativa(DialogFragment dialogo) {

            }
        });
        ddcs.show(getActivity().getSupportFragmentManager(), "Confirmar");
    }

    private void armaCondominio(String contenido){
        try{
            JSONObject json = new JSONObject(contenido);
            condominio = new Condominio(json.getInt("idCondominio"));
            condominio.setCostoAproximadoPorUnidadPrivativa((float)json.getDouble("costo_por_unidad_privativa"));
            TipoDeCondominio tipoDeCondominio =
                    new TipoDeCondominio(AccionesTablaCondominio.obtenerIdTipoDeCondominio(getContext(), json.getString("tipo_de_condominio")));
            tipoDeCondominio.setDescripcion(json.getString("tipo_de_condominio"));
            condominio.setTipoDeCondominio(tipoDeCondominio);
            condominio.setCantidadDeLugaresEstacionamiento(json.getInt("cantidad_de_lugares_estacionamiento"));
            condominio.setCantidadDeLugaresEstacionamientoVisitas(json.getInt("cantidad_de_lugares_estacionamiento_visitas"));
            condominio.setCapacidadDeCisterna((float)json.getDouble("capacidad_cisterna"));
            condominio.setDireccion(json.getString("direccion"));
            condominio.setEdad(json.getInt("edad"));
            condominio.setInmoviliaria(json.getString("inmoviliaria"));
            condominio.setPoseeSalaDeJuntas(json.getBoolean("posee_sala_de_juntas"));
            condominio.setPoseeGym(json.getBoolean("posee_gym"));
            condominio.setPoseeEspacioRecreativo(json.getBoolean("posee_espacio_recreativo"));
            condominio.setPoseeEspacioCultural(json.getBoolean("posee_espacio_cultural"));
            condominio.setPoseeOficinasAdministrativas(json.getBoolean("posee_oficinas_administrativas"));
            condominio.setPoseeAlarmaSismica(json.getBoolean("posee_alarma_sismica"));
            condominio.setPoseeCisternaAguaPluvial(json.getBoolean("posee_cisterna_agua_pluvial"));
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void armaTorre(String contenido){
        try {
            JSONObject json = new JSONObject(contenido);
            torre = new Torre(json.getInt("idTorre"));
            torre.setIdAdministracion(ProveedorDeRecursos.obtenerIdAdministracion(getContext()));
            torre.setPoseeElevador(json.getBoolean("cuenta_con_elevador"));
            torre.setNombre(json.getString("nombre"));
            torre.setCantidadDePisos(json.getInt("cantidad_de_pisos"));
            torre.setCantidadDeDepartamentos(json.getInt("cantidad_de_departamentos"));
            torre.setCantidadDeFocos(json.getInt("cantidad_de_focos"));
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        getActivity().setResult(resultCode);
        getActivity().finish();
    }
}

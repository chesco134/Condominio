package org.inspira.condominio.admon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.CompruebaCamposJSON;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.datos.Torre;
import org.inspira.condominio.dialogos.DialogoDeLista;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.RemocionElementos;
import org.inspira.condominio.fragmentos.OrdenDelDia;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 26/03/16.
 */
public class FragmentoSelectorDeTorre extends Fragment {

    private static final int NUEVA_TORRE = 1;
    private ArrayAdapter<String> adapter;
    private TextView torreActual;
    private ListView listaDeTorres;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.administrador_de_torres, parent, false);
        listaDeTorres = (ListView) rootView.findViewById(R.id.administrador_de_torres_lista_torres);
        torreActual = (TextView) rootView.findViewById(R.id.administrador_de_torres_torre_actual);
        torreActual.setText(AccionesTablaTorre.obtenerTorre(getContext(), ProveedorDeRecursos.obtenerIdTorreActual(getContext())).getNombre());
        Torre[] torres = AccionesTablaTorre.obtenerTorres(getContext(), ProveedorDeRecursos.obtenerIdAdministracion(getContext()));
        final String[] elementos = new String[torres.length];
        int i=0;
        for(Torre torre : torres){
            elementos[i++] = torre.getNombre();
        }
        torreActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoDeLista ddl = new DialogoDeLista();
                ddl.setTitulo("Cambio de Torre");
                ddl.setElementos(elementos);
                ddl.setAccion(new DialogoDeLista.AccionDialogoDeLista() {
                    @Override
                    public void objetoSeleccionado(String texto) {
                        torreActual.setText(texto);
                        ProveedorDeRecursos.guardaRecursoInt(getContext(), "idTorre", AccionesTablaTorre.obtenerIdTorre(getContext(), texto));
                    }
                });
                ddl.show(getActivity().getSupportFragmentManager(), "Cambiar Torre");
            }
        });
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.control_de_torres, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        boolean consumado = false;
        int itemId = item.getItemId();
        if(itemId == R.id.agregar_torre){
            agregarNuevaTorre();
            consumado = true;
        }else if(itemId == R.id.remover_torres){
            removerTorres();
            consumado = true;
        }
        return consumado;
    }

    private void removerTorres() {
        RemocionElementos rm = new RemocionElementos();
        final Torre[] torres = AccionesTablaTorre.obtenerTorres(getContext(), ProveedorDeRecursos.obtenerIdAdministracion(getContext()));
        final String[] elementos = new String[torres.length];
        int i = 0;
        for(Torre torre : torres)
            elementos[i++] = torre.getNombre();
        Bundle args = new Bundle();
        args.putStringArray("elementos", elementos);
        rm.setArguments(args);
        rm.setAd(new EntradaTexto.AccionDialogo() {
            @Override
            public void accionPositiva(DialogFragment fragment) {
                final Integer[] seleccion = ((RemocionElementos)fragment).getElementosSeleccionados();
                OrdenDelDia.prepareElements(seleccion);
                ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                    @Override
                    public void resultadoSatisfactorio(Thread t) {
                        String respuesta = ((ContactoConServidor)t).getResponse();
                        if(CompruebaCamposJSON.validaContenido(respuesta)){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    boolean quitaronTorreActual = false;
                                    int idTorreActual = ProveedorDeRecursos.obtenerIdTorreActual(getContext());
                                    for(Integer index : seleccion) {
                                        AccionesTablaTorre.removerTorre(getContext(), torres[index].getId());
                                        adapter.remove(elementos[index]);
                                        quitaronTorreActual = torres[index].getId() == idTorreActual;
                                    }
                                    if(quitaronTorreActual){
                                        if(adapter.getCount() > 0){
                                            String nuevaPrimeraTorre = adapter.getItem(0);
                                            torreActual.setText(nuevaPrimeraTorre);
                                            ProveedorDeRecursos.guardaRecursoInt(getContext(), "idTorre", AccionesTablaTorre.obtenerIdTorre(getContext(), nuevaPrimeraTorre));
                                        }else{
                                            torreActual.setText("");
                                            ProveedorDeRecursos.guardaRecursoInt(getContext(), "idTorre", -1);
                                        }
                                    }
                                    MuestraMensajeDesdeHilo.muestraMensaje(getActivity(), torreActual, "Hecho");
                                }
                            });
                        }else{
                            MuestraMensajeDesdeHilo.muestraMensaje(getActivity(), torreActual, "Servicio momentaneamente inaccesible");
                        }
                    }

                    @Override
                    public void problemasDeConexion(Thread t) {
                        MuestraMensajeDesdeHilo.muestraMensaje(getActivity(), torreActual, "Servicio por el momento no disponible");
                    }
                }, prepararCuerpoDeMensaje(seleccion));
                contacto.start();
            }

            private String prepararCuerpoDeMensaje(Integer[] elementos) {
                String cuerpoDeMensaje = null;
                try{
                    JSONObject json = new JSONObject();
                    json.put("action", ProveedorDeRecursos.REMOCION_DE_TORRES);
                    JSONArray elementosSeleccionados = new JSONArray();
                    for(Integer i : elementos)
                        elementosSeleccionados.put(torres[i].getId());
                    json.put("elementos", elementosSeleccionados);
                    cuerpoDeMensaje = json.toString();
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return cuerpoDeMensaje;
            }

            @Override
            public void accionNegativa(DialogFragment fragment) {}
        });
        rm.show(getActivity().getSupportFragmentManager(), "Remover torres");
    }

    private void agregarNuevaTorre() {
        startActivityForResult(new Intent(getContext(), RegistroDeTorre.class), NUEVA_TORRE);
    }

    @Override
    public void onResume(){
        super.onResume();
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Torres");
        setupData();
    }

    private void setupData(){
        final Torre[] torres = AccionesTablaTorre.obtenerTorres(getContext(), ProveedorDeRecursos.obtenerIdAdministracion(getContext()));
        List<String> nombres = new ArrayList<>();
        for(Torre torre : torres)
            nombres.add(torre.getNombre());
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, nombres);
        listaDeTorres.setAdapter(adapter);
        listaDeTorres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                colocarFragmentoDeModificacionDeTorre(torres[position]);
            }
        });
        listaDeTorres.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                torreActual.setText(adapter.getItem(position));
                ProveedorDeRecursos.guardaRecursoInt(getContext(), "idTorre", torres[position].getId());
                return true;
            }
        });
    }

    private void colocarFragmentoDeModificacionDeTorre(Torre torre) {
        Bundle args = new Bundle();
        args.putString("nombre", torre.getNombre());
        args.putString("numero_de_pisos", String.valueOf(torre.getCantidadDePisos()));
        args.putString("numero_de_focos", String.valueOf(torre.getCantidadDeFocos()));
        args.putString("numero_de_departamentos", String.valueOf(torre.getCantidadDeDepartamentos()));
        args.putBoolean("posee_elevador", torre.isPoseeElevador());
        args.putInt("idTorre", torre.getId());
        ActualizarDatosDeTorre adt = new ActualizarDatosDeTorre();
        adt.setArguments(args);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.preparacion_main_container, adt)
                .addToBackStack("Actualizar_datos_de_torre")
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == NUEVA_TORRE && resultCode == Activity.RESULT_OK){
            adapter.add(((Torre)data.getSerializableExtra("torre")).getNombre());
        }
    }
}

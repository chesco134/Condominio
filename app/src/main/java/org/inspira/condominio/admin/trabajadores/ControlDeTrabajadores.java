package org.inspira.condominio.admin.trabajadores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.adaptadores.AdaptadorDeTrabajadores;
import org.inspira.condominio.admon.AccionesTablaTrabajador;
import org.inspira.condominio.admon.CompruebaCamposJSON;
import org.inspira.condominio.datos.Trabajador;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.RegistroDeTrabajador;
import org.inspira.condominio.dialogos.RemocionElementos;
import org.inspira.condominio.fragmentos.OrdenDelDia;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ControlDeTrabajadores extends AppCompatActivity implements ActionBar.OnNavigationListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_de_trabajadores);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // Show the Up button in the action bar.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        getResources().getStringArray(R.array.tipos_de_trabajadores)),
                this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int sectionNumber;
        private ListView listaTrabajadores;
        private AdaptadorDeTrabajadores adapter;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.control_de_habitantes, container, false);
            listaTrabajadores = (ListView) rootView.findViewById(R.id.control_de_habitantes_lista);
            sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            adapter = new AdaptadorDeTrabajadores(getContext(), AccionesTablaTrabajador.obtenerIdTipoDeTrabajador(getContext(),
                    getResources().getStringArray(R.array.tipos_de_trabajadores)[sectionNumber - 1]));
            listaTrabajadores.setAdapter(adapter);
            listaTrabajadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trabajador trabajador = (Trabajador)adapter.getItem(position);
                    Intent i = new Intent(getContext(), DetallesTrabajador.class);
                    i.putExtra("trabajador", trabajador);
                    startActivity(i);
                }
            });
            setHasOptionsMenu(true);
            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
            inflater.inflate(R.menu.control_de_trabajadores, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            int itemId = item.getItemId();
            if(itemId == R.id.control_de_trabajadores_agregar_trabajador){
                lanzarDialogoDeRegistro();
                return true;
            }else if(itemId == R.id.control_de_trabajadores_remover_trabajador){
                lanzarDialogoDeRemocion();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void lanzarDialogoDeRemocion() {
            RemocionElementos rm = new RemocionElementos();
            final List<Trabajador> trabajadores = adapter.obtenerTrabajadores();
            String[] elementos = new String[trabajadores.size()];
            int i=0;
            for(Trabajador trabajador : trabajadores)
                elementos[i++] = trabajador.getApPaterno() + " " + trabajador.getApMaterno() + " " + trabajador.getNombres();
            Bundle args = new Bundle();
            args.putStringArray("elementos", elementos);
            rm.setArguments(args);
            rm.setAd(new EntradaTexto.AccionDialogo() {
                @Override
                public void accionPositiva(DialogFragment fragment) {
                    final Integer[] elementosSeleccionados = ((RemocionElementos) fragment).getElementosSeleccionados();
                    OrdenDelDia.prepareElements(elementosSeleccionados);
                    String cuerpoDeMensaje = armarCuerpoDeMensaje(elementosSeleccionados);
                    ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                        @Override
                        public void resultadoSatisfactorio(Thread t) {
                            String resultado = ((ContactoConServidor)t).getResponse();
                            if(CompruebaCamposJSON.validaContenido(resultado)){
                                ((AppCompatActivity)getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (Integer index : elementosSeleccionados)
                                            AccionesTablaTrabajador.removerTrabajador(getContext(), adapter.removerTrabajador(index).getId());
                                    }
                                });
                            }else{
                                MuestraMensajeDesdeHilo.muestraToast((AppCompatActivity)getContext(), "Servicio por el momento inalcanzable, sin cambios");
                            }
                        }

                        @Override
                        public void problemasDeConexion(Thread t) {

                        }
                    }, cuerpoDeMensaje);
                    contacto.start();
                }

                private String armarCuerpoDeMensaje(Integer[] elementosSeleccionados) {
                    String cuerpoDeMensaje = null;
                    try{
                        JSONObject json = new JSONObject();
                        json.put("action", ProveedorDeRecursos.REMOCION_DE_TRABAJADORES);
                        JSONArray elementos = new JSONArray();
                        for(Integer i : elementosSeleccionados)
                            elementos.put(trabajadores.get(i).getId());
                        json.put("elementos", elementos);
                        cuerpoDeMensaje = json.toString();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    return cuerpoDeMensaje;
                }

                @Override
                public void accionNegativa(DialogFragment fragment) {}
            });
            rm.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "Remover trabajadores");
        }

        private void lanzarDialogoDeRegistro() {
            RegistroDeTrabajador rdt = new RegistroDeTrabajador();
            Bundle args = new Bundle();
            args.putString("tipo_de_trabajador", getResources().getStringArray(R.array.tipos_de_trabajadores)[sectionNumber-1]);
            rdt.setArguments(args);
            rdt.setArdt(new RegistroDeTrabajador.AccionRegistroDeTrabajador() {
                @Override
                public void trabajadorRegistrado(final Trabajador trabajador) {
                    ((AppCompatActivity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.agregarTrabajador(trabajador);
                            MuestraMensajeDesdeHilo.muestraMensaje(((AppCompatActivity)getContext()), listaTrabajadores, "Hecho");
                        }
                    });
                }
            });
            rdt.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "Agregar trabajador");
        }
    }

}

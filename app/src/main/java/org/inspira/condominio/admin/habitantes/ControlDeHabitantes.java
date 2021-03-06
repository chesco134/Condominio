package org.inspira.condominio.admin.habitantes;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.adaptadores.AdaptadorDeHabitantes;
import org.inspira.condominio.admon.AccionesTablaHabitante;
import org.inspira.condominio.admon.AccionesTablaTorre;
import org.inspira.condominio.actividades.CompruebaCamposJSON;
import org.inspira.condominio.admon.RegistroDeTorre;
import org.inspira.condominio.admon.SelectorDeTorre;
import org.inspira.condominio.datos.Habitante;
import org.inspira.condominio.datos.Torre;
import org.inspira.condominio.dialogos.DialogoDeConsultaSimple;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.RegistroDeHabitante;
import org.inspira.condominio.dialogos.RemocionElementos;
import org.inspira.condominio.fragmentos.OrdenDelDia;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 25/03/16.
 */
public class ControlDeHabitantes extends AppCompatActivity {

    private AdaptadorDeHabitantes adapter;
    private ListView listaHabitantes;

    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.control_de_habitantes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listaHabitantes = (ListView) findViewById(R.id.control_de_habitantes_lista);
        listaHabitantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Habitante habitante = (Habitante) adapter.getItem(position);
                Intent i = new Intent(ControlDeHabitantes.this, ResumenHabitante.class);
                i.putExtra("habitante", habitante);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.control_de_habitantes, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.buscar_habitante).getActionView();
            searchView.setQueryHint("Nombre de habitante");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        boolean accionConsumida = false;
        if( itemId == R.id.buscar_habitante){
            onSearchRequested();
            accionConsumida =  true;
        } else if(itemId == R.id.agregar_habitante){
            agregarHabitante();
            accionConsumida = true;
        }else if(itemId == R.id.remover_habitante){
            removerHabitante();
            accionConsumida = true;
        } else if(itemId == R.id.torres){
            iniciaSelectorDeTorre();
            accionConsumida = true;
        }
        return accionConsumida;
    }

    private void iniciaSelectorDeTorre() {
        startActivity(new Intent(this, SelectorDeTorre.class));
    }

    private void removerHabitante() {
        RemocionElementos rm = new RemocionElementos();
        final Habitante[] habitantes = AccionesTablaHabitante.obtenerHabitantes(this, ProveedorDeRecursos.obtenerIdTorreActual(this));
        String[] nombresHabitantes = new String[habitantes.length];
        int i=0;
        for(Habitante habitante : habitantes)
            nombresHabitantes[i++] = habitante.getApPaterno() + " " + habitante.getApMaterno() + " " + habitante.getNombres();
        Bundle args = new Bundle();
        args.putStringArray("elementos", nombresHabitantes);
        rm.setArguments(args);
        rm.setAd(new EntradaTexto.AccionDialogo() {
            @Override
            public void accionPositiva(DialogFragment fragment) {
                final Integer[] elementosSeleccionados = ((RemocionElementos)fragment).getElementosSeleccionados();
                OrdenDelDia.prepareElements(elementosSeleccionados);
                String cuerpoMensaje = armaCuerpoDeMensaje(elementosSeleccionados);
                ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                    @Override
                    public void resultadoSatisfactorio(Thread t) {
                        String resultado = ((ContactoConServidor)t).getResponse();
                        if(CompruebaCamposJSON.validaContenido(resultado)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for(Integer index : elementosSeleccionados) {
                                        adapter.removerHabitante(index);
                                        AccionesTablaHabitante.removerHabitante(ControlDeHabitantes.this, habitantes[index].getId());
                                    }
                                }
                            });
                        }else{
                            MuestraMensajeDesdeHilo.muestraToast(ControlDeHabitantes.this, "Servicio por el momento inalcanzable, sin cambios");
                        }
                    }

                    @Override
                    public void problemasDeConexion(Thread t) {
                        MuestraMensajeDesdeHilo.muestraToast(ControlDeHabitantes.this, "Servicio temporalmente no disponible");
                    }
                }, cuerpoMensaje);
                contacto.start();
            }

            private String armaCuerpoDeMensaje(Integer[] elementosSeleccionados) {
                String cuerpoDeMensaje = null;
                try{
                    JSONObject json = new JSONObject();
                    json.put("action", ProveedorDeRecursos.REMOCION_DE_HABITANTES);
                    JSONArray elementos = new JSONArray();
                    for(Integer i : elementosSeleccionados)
                        elementos.put(habitantes[i].getId());
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
        rm.show(getSupportFragmentManager(), "Quitar habitantes");
    }

    private void agregarHabitante() {
        RegistroDeHabitante rdh = new RegistroDeHabitante();
        Bundle args = new Bundle();
        args.putString("titulo", "Nuevo Habitante");
        rdh.setArguments(args);
        rdh.show(getSupportFragmentManager(), "Agregar habitante");
    }

    @Override
    protected void onResume(){
        super.onResume();
        ActionBar bar = getSupportActionBar();
        String currentTitle = "Habitantes ";
        Torre torreActual = AccionesTablaTorre.obtenerTorre(this, ProveedorDeRecursos.obtenerIdTorreActual(this));
        if(torreActual != null)
            currentTitle = currentTitle.concat(torreActual.getNombre());
        if( bar != null)
            bar.setTitle(currentTitle);
        if(!AccionesTablaTorre.existenTorres(this)){
            mostrarMensajeDeRegistroDeTorres();
        }
        adapter = new AdaptadorDeHabitantes(this);
        listaHabitantes.setAdapter(adapter);
    }

    private void mostrarMensajeDeRegistroDeTorres() {
        DialogoDeConsultaSimple ddcs = new DialogoDeConsultaSimple();
        Bundle args = new Bundle();
        args.putString("mensaje", "Antes de continuar, es necesario registrar una torre al menos");
        ddcs.setArguments(args);
        ddcs.setAgenteDeInteraccion(new DialogoDeConsultaSimple.AgenteDeInteraccionConResultado() {
            @Override
            public void clickSobreAccionPositiva(DialogFragment dialogo) {
                startActivity(new Intent(ControlDeHabitantes.this, RegistroDeTorre.class));
            }

            @Override
            public void clickSobreAccionNegativa(DialogFragment dialogo) {
                setResult(RESULT_OK);
                finish();
            }
        });
        ddcs.show(getSupportFragmentManager(), "Agregar Torre");
    }

    public void agregarHabitante(final Habitante habitante){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.agregarHabitante(habitante);
            }
        });
    }

}

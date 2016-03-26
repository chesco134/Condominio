package org.inspira.condominio.admin.habitantes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.adaptadores.AdaptadorDeHabitantes;
import org.inspira.condominio.admon.AccionesTablaTorre;
import org.inspira.condominio.admon.RegistroDeTorre;
import org.inspira.condominio.datos.Habitante;
import org.inspira.condominio.dialogos.DialogoDeConsultaSimple;
import org.inspira.condominio.dialogos.RegistroDeHabitante;

/**
 * Created by jcapiz on 25/03/16.
 */
public class ControlDeHabitantes extends AppCompatActivity {

    private AdaptadorDeHabitantes adapter;

    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.control_de_habitantes);
        ListView listaHabitantes = (ListView) findViewById(R.id.control_de_habitantes_lista);
        adapter = new AdaptadorDeHabitantes(this);
        listaHabitantes.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.control_de_habitantes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        boolean accionConsumida = false;
        if( itemId == R.id.buscar_habitante ){

            accionConsumida = true;
        } else if(itemId == R.id.agregar_habitante){
            agregarHabitante();
            accionConsumida = true;
        }else if(itemId == R.id.remover_habitante){

            accionConsumida = true;
        }else if(itemId == R.id.torres){

            accionConsumida = true;
        }
        return accionConsumida;
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
        if(!AccionesTablaTorre.existenTorres(this)){
            mostrarMensajeDeRegistroDeTorres();
        }
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

    public void agregarHabitante(Habitante habitante){
        adapter.agregarHabitante(habitante);
    }

}

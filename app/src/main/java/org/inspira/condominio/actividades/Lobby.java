package org.inspira.condominio.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import org.inspira.condominio.R;
import org.inspira.condominio.adaptadores.AdaptadorParaConvocatoria;
import org.inspira.condominio.datos.AlmacenamientoInterno;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.pdf.ExportarConvocatoria;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lobby extends AppCompatActivity {

    public static final int VISOR_PDF = 3;
    private static final int CREAR_CONVOCATORIA = 319;
    private ListView listaConvocatorias;
    private AdaptadorParaConvocatoria adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.texto_convocatoria));
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Lobby.this, CrearConvocatoria.class), CREAR_CONVOCATORIA);
            }
        });
        listaConvocatorias = (ListView) findViewById(R.id.content_lobby_lista_convocatorias);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadContent();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case CREAR_CONVOCATORIA:
                    Bundle extra = data.getExtras();
                    Convocatoria conv = (Convocatoria)extra.getSerializable("convocatoria");
                    if(conv == null)
                        ProveedorSnackBar.muestraBarraDeBocados(listaConvocatorias, "Merga");
                    else
                        adapter.addItem(conv);
                    break;
            }
        }
        if(requestCode == VISOR_PDF){
            adapter.borrarArchivos();
        }
        Log.d("Arizawa", "-----------------------------> " + requestCode);
    }

    private void loadContent(){
        CondominioBD db = new CondominioBD(this);
        List<Convocatoria> convocatorias = new ArrayList<>();
        Collections.addAll(convocatorias,db.obtenerConvocatorias());
        adapter = new AdaptadorParaConvocatoria(this, convocatorias);
        listaConvocatorias.setAdapter(adapter);
    }
}
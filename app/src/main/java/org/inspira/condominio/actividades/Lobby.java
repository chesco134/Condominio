package org.inspira.condominio.actividades;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.inspira.condominio.R;
import org.inspira.condominio.adaptadores.AdaptadorParaConvocatoria;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
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
        // new DummyTester().start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case CREAR_CONVOCATORIA:
                    Bundle extra = data.getExtras();
                    Convocatoria conv = (Convocatoria)extra.getSerializable("convocatoria");
                    if(conv == null)
                        ProveedorSnackBar.muestraBarraDeBocados(listaConvocatorias, "u.u");
                    else
                        adapter.addItem(conv);
                    break;
            }
        }
        if(requestCode == VISOR_PDF){
            try{ adapter.borrarArchivos(); }catch(NullPointerException e){ e.printStackTrace(); }
        }
    }

    private void loadContent(){
        CondominioBD db = new CondominioBD(this);
        List<Convocatoria> convocatorias = new ArrayList<>();
        Collections.addAll(convocatorias,db.obtenerConvocatorias(ProveedorDeRecursos.obtenerEmail(this)));
        adapter = new AdaptadorParaConvocatoria(this, convocatorias);
        listaConvocatorias.setAdapter(adapter);
    }

    private class DummyTester extends Thread{

        @Override public void run(){
            try{
                HttpURLConnection con = (HttpURLConnection) new URL("http://192.168.43.23:8080/Condominio/RecibirUsuario").openConnection();
                con.setDoOutput(true);
                DataOutputStream salida = new DataOutputStream(con.getOutputStream());
                JSONObject json = new JSONObject();
                json.put("name", "Juan");
                json.put("ap", "Capiz");
                salida.write(("tunas=" + json.toString()).getBytes());
                salida.write("&cocol=Hay cocoles".getBytes());
                salida.flush();
                DataInputStream entrada = new DataInputStream(con.getInputStream());
                int length;
                byte[] chunk = new byte[128];
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while( (length = entrada.read(chunk)) != -1 )
                    baos.write(chunk, 0, length);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Lobby.this, baos.toString(), Toast.LENGTH_LONG).show();
                        try{baos.close();}catch(IOException ignore){}
                    }
                });
                con.disconnect();
            }catch(JSONException | IOException e){
                e.printStackTrace();
            }
        }
    }
}
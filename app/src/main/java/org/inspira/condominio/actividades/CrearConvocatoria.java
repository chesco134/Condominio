package org.inspira.condominio.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.inspira.condominio.R;
import org.inspira.condominio.admon.AccionesTablaConvocatoria;
import org.inspira.condominio.datos.AlmacenamientoInterno;
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.datos.PuntoOdD;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.Informacion;
import org.inspira.condominio.dialogos.ProveedorToast;
import org.inspira.condominio.fragmentos.DatosDeEncabezado;
import org.inspira.condominio.fragmentos.OrdenDelDia;
import org.inspira.condominio.networking.ContactoConServidor;
import org.inspira.condominio.pdf.ExportarConvocatoria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CrearConvocatoria extends AppCompatActivity {

    private DatosDeEncabezado datosDeEncabezado;
    private OrdenDelDia ordenDelDia;
    private Convocatoria convocatoria;
    private int state;
    private String nombreDeArchivo;
    private List<String> puntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formato_para_convocatoria);
        datosDeEncabezado = new DatosDeEncabezado();
        ordenDelDia = new OrdenDelDia();
        ordenDelDia.setAction(new ColocarTituloActionbarFragmen() {
            @Override
            public void onResume() {
                getSupportActionBar().setTitle(R.string.orden_del_dia_definir_orden_del_dia);
            }
        });
        if (savedInstanceState == null){
            convocatoria = new Convocatoria();
            state = 0;
        }else {
            state = savedInstanceState.getInt("state");
            convocatoria = (Convocatoria)savedInstanceState.getSerializable("convocatoria");
            nombreDeArchivo = savedInstanceState.getString("nombre_de_archivo");
            puntos = savedInstanceState.getStringArrayList("puntos");
        }
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putInt("state", state);
        grabData();
        outState.putSerializable("convocatoria", convocatoria);
        outState.putString("nombre_de_archivo", nombreDeArchivo);
        outState.putStringArrayList("puntos", (ArrayList<String>)puntos);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        state = savedInstanceState.getInt("state");
        convocatoria = (Convocatoria)savedInstanceState.getSerializable("convocatoria");
        nombreDeArchivo = savedInstanceState.getString("nombre_de_archivo");
        puntos = savedInstanceState.getStringArrayList("puntos");
    }

    @Override
    public void onResume(){
        super.onResume();
        colocarFragmento();
    }

    @Override
    public void onBackPressed(){
        if(state == 1){
            grabData();
            state--;
            super.onBackPressed();
        }else {
            muestraMensajeInformacion(getResources().getString(R.string.dialogo_informacion_confirmar_salida));
        }
    }

    private void colocarFragmento(){
        switch (state){
            case 0:
                colocaDatosDeEncabezadoFragmento();
                break;
            case 1:
                colocaDatosDeEncabezadoFragmento();
                colocaOrdenDelDiaFragmento();
                break;
        }
    }

    private void muestraMensajeInformacion(String mensaje){
        Bundle arguments = new Bundle();
        arguments.putString("titulo","Alerta");
        arguments.putString("mensaje", mensaje);
        Informacion info = new Informacion();
        info.setArguments(arguments);
        info.setAccion(new EntradaTexto.AccionDialogo() {
            @Override
            public void accionPositiva(DialogFragment fragment) {
                finish();
            }

            @Override
            public void accionNegativa(DialogFragment fragment) {
            }
        });
        info.show(getSupportFragmentManager(), "Informacion");
    }

    public void colocaDatosDeEncabezadoFragmento(){
        Bundle args = new Bundle();
        args.putSerializable("convocatoria", convocatoria);
        datosDeEncabezado.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.formato_convocatoria_contenedor, datosDeEncabezado)
                .commit();
    }

    public void colocaOrdenDelDiaFragmento() {
        Bundle args = new Bundle();
        if(puntos != null) {
            args.putStringArrayList("convocatoria", (ArrayList<String>)puntos);
            args.putString("nombre_de_archivo", nombreDeArchivo);
            ordenDelDia.setArguments(args);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.formato_convocatoria_contenedor, ordenDelDia, "Orden del Día")
                .setCustomAnimations(R.anim.traslacion_derecha, R.anim.traslacion_izquierda)
                .addToBackStack("Kaoru")
                .commit();
        state = 1;
    }

    public void creaConvocatoria(){
        grabData();
        difundeConvocatoria();
    }

    private void grabData(){
        convocatoria.setAsunto(datosDeEncabezado.getAsunto());
        convocatoria.setUbicacionInterna(datosDeEncabezado.getUbicacionInterna());
        String[] fechaInicial = datosDeEncabezado.getFechaInicial().split("/");
        String[] tiempoInicial = datosDeEncabezado.getTiempoInicial().split(":");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(fechaInicial[0]));
        c.set(Calendar.MONTH, Integer.parseInt(fechaInicial[1])-1);
        c.set(Calendar.YEAR, Integer.parseInt(fechaInicial[2]));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tiempoInicial[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(tiempoInicial[1]));
        convocatoria.setFechaInicio(c.getTimeInMillis());
        String[] pts = ordenDelDia.getPuntos();
        Log.d("Putos", "-->" + (pts == null));
        puntos = new ArrayList<>();
        if(pts != null) {
            for(String pt : pts) {
                Log.d("Basura", "--------> " + pt);
                puntos.add(pt);
            }
        }
        nombreDeArchivo = ordenDelDia.getNombreArchivo();
    }

    private void generaPDF(){
        new Thread() {
            @Override public void run() {
                ExportarConvocatoria generar = new ExportarConvocatoria(CrearConvocatoria.this, convocatoria);
                try {
                    AlmacenamientoInterno a = new AlmacenamientoInterno(CrearConvocatoria.this);
                    a.crearDirectorio();
                    File archivo = new File(a.obtenerRutaDeAlmacenamiento() + "/" + nombreDeArchivo);
                    generar.crearArchivo(archivo);
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            ProveedorToast.showToast(CrearConvocatoria.this,R.string.crear_convocatoria_archivo_creado);
                        }
                    });
                }catch(IOException e){
                    e.printStackTrace();
                    MuestraMensajeDesdeHilo.muestraMensaje(CrearConvocatoria.this, findViewById(R.id.formato_convocatoria_contenedor), getString(R.string.crear_convocatoria_error_pdf));
                }
            }
        }.start();
    }

    private void guardaEnBaseDeDatos(int idConvocatoria){
        convocatoria.setId(idConvocatoria);
        AccionesTablaConvocatoria.agregaConvocatoria(this, convocatoria);
        PuntoOdD punto;
        for(String descPunto : puntos){
            punto = new PuntoOdD();
            punto.setDescripcion(descPunto);
            punto.setIdConvocatoria(idConvocatoria);
            AccionesTablaConvocatoria.agregaPuntoOdD(this, punto);
        }
    }

    private void difundeConvocatoria(){
        ContactoConServidor contacto = new ContactoConServidor(new RespuestaAccionSyncConvocatoria(), armarContenidoDeMensaje());
        contacto.start();
    }

    private String armarContenidoDeMensaje(){
        String contenido = null;
        convocatoria.setEmail(ProveedorDeRecursos.obtenerEmail(this));
        convocatoria.setFirma(ProveedorDeRecursos.obtenerUsuario(this));
        try{
            JSONObject json = new JSONObject();
            json.put("action", 3);
            json.put("Asunto", convocatoria.getAsunto());
            json.put("Ubicacion_Interna", convocatoria.getUbicacionInterna());
            json.put("Fecha_de_Inicio", convocatoria.getFechaInicio());
            json.put("firma", convocatoria.getFirma());
            json.put("email", convocatoria.getEmail());
            JSONArray puntosOdD = new JSONArray();
            for(String descPunto : puntos)
                puntosOdD.put(descPunto);
            json.put("puntos", puntosOdD);
            contenido = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return contenido;
    }

    private class RespuestaAccionSyncConvocatoria implements ContactoConServidor.AccionesDeValidacionConServidor{

        @Override
        public void resultadoSatisfactorio(Thread t) {
            String respuesta = ((ContactoConServidor)t).getResponse();
            int idConvocatoria = obtenerIdConvocatoria(respuesta);
            guardaEnBaseDeDatos(idConvocatoria);
            generaPDF();
            Intent i = new Intent();
            i.putExtra("convocatoria", convocatoria);
            setResult(RESULT_OK, i);
            finish();
        }

        @Override
        public void problemasDeConexion(Thread t) {
            MuestraMensajeDesdeHilo.muestraMensaje(CrearConvocatoria.this, findViewById(R.id.preparacion_main_container), "Hecho");
        }

        private int obtenerIdConvocatoria(String respuesta) {
            int idConvocatoria = -1;
            try{
                JSONObject json = new JSONObject(respuesta);
                idConvocatoria = json.getInt("idConvocatoria");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return idConvocatoria;
        }
    }
}
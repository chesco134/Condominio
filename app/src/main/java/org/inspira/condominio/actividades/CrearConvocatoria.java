package org.inspira.condominio.actividades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import org.inspira.condominio.R;
import org.inspira.condominio.admin.CentralPoint;
import org.inspira.condominio.datos.AlmacenamientoInterno;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.datos.PuntoOdD;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.Informacion;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.dialogos.ProveedorToast;
import org.inspira.condominio.fragmentos.DatosDeEncabezado;
import org.inspira.condominio.fragmentos.OrdenDelDia;
import org.inspira.condominio.pdf.ExportarConvocatoria;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formato_para_convocatoria);
        datosDeEncabezado = new DatosDeEncabezado();
        ordenDelDia = new OrdenDelDia();
        if (savedInstanceState == null){
            convocatoria = new Convocatoria();
            state = 0;
    }else {
            state = savedInstanceState.getInt("state");
            convocatoria = (Convocatoria)savedInstanceState.getSerializable("convocatoria");
            nombreDeArchivo = savedInstanceState.getString("nombre_de_archivo");
        }
        colocarFragmento();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putInt("state", state);
        grabData();
        outState.putSerializable("convocatoria", convocatoria);
        outState.putString("nombre_de_archivo", nombreDeArchivo);
    }

    @Override
    public void onBackPressed(){
        if(state == 1){
            getSupportActionBar().setTitle(R.string.hacer_encabezado_convocatoria_nueva_convocatoria);
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
        getSupportActionBar().setTitle(R.string.hacer_encabezado_convocatoria_nueva_convocatoria);
        Bundle args = new Bundle();
        args.putSerializable("convocatoria", convocatoria);
        datosDeEncabezado.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.formato_convocatoria_contenedor, datosDeEncabezado)
                .commit();
    }

    public void colocaOrdenDelDiaFragmento() {
        getSupportActionBar().setTitle(R.string.orden_del_dia_definir_orden_del_dia);
        Bundle args = new Bundle();
        if(convocatoria.getPuntos() != null) {
            args.putSerializable("convocatoria", convocatoria);
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
        generaPDF();
        guardaEnBaseDeDatos();
        difundeConvocatoria();
    }

    private void grabData(){
        convocatoria.setAsunto(datosDeEncabezado.getAsunto());
        convocatoria.setCondominio(datosDeEncabezado.getCondominio());
        convocatoria.setUbicacion(datosDeEncabezado.getUbicacion());
        convocatoria.setUbicacionInterna(datosDeEncabezado.getUbicacionInterna());
        convocatoria.setFirma(datosDeEncabezado.getFirma());
        String[] fechaInicial = datosDeEncabezado.getFechaInicial().split("/");
        String[] tiempoInicial = datosDeEncabezado.getTiempoInicial().split(":");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(fechaInicial[0]));
        c.set(Calendar.MONTH, Integer.parseInt(fechaInicial[1])-1);
        c.set(Calendar.YEAR, Integer.parseInt(fechaInicial[2]));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tiempoInicial[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(tiempoInicial[1]));
        convocatoria.setFechaInicio(c.getTimeInMillis());
        List<PuntoOdD> puntos = new ArrayList<>();
        if(ordenDelDia.getPuntos() != null)
        for(String descripcion : ordenDelDia.getPuntos()){
            PuntoOdD punto = new PuntoOdD();
            punto.setDescripcion(descripcion);
            puntos.add(punto);
        }
        convocatoria.setPuntos(puntos);
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
                    ProveedorSnackBar
                            .muestraBarraDeBocados(findViewById(R.id.formato_convocatoria_contenedor), getString(R.string.crear_convocatoria_error_pdf));
                }
            }
        }.start();
    }

    private void guardaEnBaseDeDatos(){
        CondominioBD db = new CondominioBD(this);
        convocatoria.setId(db.insertaConvocatoria(convocatoria, getSharedPreferences(CentralPoint.class.getName(), Context.MODE_PRIVATE).getString("usuario", "NaN")));
        for(PuntoOdD punto : convocatoria.getPuntos())
            db.insertaPuntoOdD(punto);
    }

    private void difundeConvocatoria(){
        Intent i = new Intent();
        i.putExtra("convocatoria", convocatoria);
        setResult(RESULT_OK, i);
        finish();
    }
}
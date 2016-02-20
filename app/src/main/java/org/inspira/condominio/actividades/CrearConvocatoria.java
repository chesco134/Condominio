package org.inspira.condominio.actividades;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import org.inspira.condominio.R;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.Informacion;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.fragmentos.DatosDeEncabezado;
import org.inspira.condominio.fragmentos.OrdenDelDia;

public class CrearConvocatoria extends AppCompatActivity {

    private DatosDeEncabezado datosDeEncabezado;
    private OrdenDelDia ordenDelDia;
    private String tiempoInicial;
    private String asunto;
    private String condominio;
    private String ubicacion;
    private String ubicacionInterna;
    private String fechaInicial;
    private String[] puntos;
    private int state;
    private String firma;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formato_para_convocatoria);
        datosDeEncabezado = new DatosDeEncabezado();
        ordenDelDia = new OrdenDelDia();
        if(savedInstanceState == null)
            state = 0;
        else {
            state = savedInstanceState.getInt("state");
            asunto = savedInstanceState.getString("asunto");
            condominio = savedInstanceState.getString("condominio");
            ubicacion = savedInstanceState.getString("ubicacion");
            ubicacionInterna = savedInstanceState.getString("ubicacion_interna");
            fechaInicial = savedInstanceState.getString("fecha_inicial");
            tiempoInicial = savedInstanceState.getString("tiempo_inicial");
            firma = savedInstanceState.getString("firma");
            puntos = savedInstanceState.getStringArray("puntos");
        }
        colocarFragmento();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putInt("state", state);
        grabData();
        outState.putString("asunto", asunto);
        outState.putString("condominio", condominio);
        outState.putString("ubicacion", ubicacion);
        outState.putString("ubicacion_interna", ubicacionInterna);
        outState.putString("firma", firma);
        outState.putString("fecha_inicial", fechaInicial);
        outState.putString("tiempo_inicial", tiempoInicial);
        outState.putStringArray("puntos", puntos);
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
        if(asunto!=null)
            args.putString("asunto",asunto);
        if(condominio != null)
            args.putString("condominio",condominio);
        if(ubicacion != null)
            args.putString("ubicacion", ubicacion);
        if(ubicacionInterna != null)
            args.putString("ubicacion_interna", ubicacionInterna);
        if(firma != null)
            args.putString("firma", firma);
        if(fechaInicial != null)
            args.putString("fecha_inicial", fechaInicial);
        if(tiempoInicial != null)
            args.putString("tiempo_inicial", tiempoInicial);
        datosDeEncabezado.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.formato_convocatoria_contenedor, datosDeEncabezado)
                .commit();
    }

    public void colocaOrdenDelDiaFragmento() {
        getSupportActionBar().setTitle(R.string.orden_del_dia_definir_orden_del_dia);
        Bundle args = new Bundle();
        if(puntos != null) {
            args.putStringArray("puntos", puntos);
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
        asunto = datosDeEncabezado.getAsunto();
        condominio = datosDeEncabezado.getCondominio();
        ubicacion = datosDeEncabezado.getUbicacion();
        ubicacionInterna = datosDeEncabezado.getUbicacionInterna();
        firma = datosDeEncabezado.getFirma();
        fechaInicial = datosDeEncabezado.getFechaInicial();
        tiempoInicial = datosDeEncabezado.getTiempoInicial();
        puntos = ordenDelDia.getPuntos();
    }

    private void generaPDF(){}

    private void guardaEnBaseDeDatos(){}

    private void difundeConvocatoria(){
        ProveedorSnackBar.muestraBarraDeBocados(findViewById(R.id.formato_convocatoria_contenedor),
                getString(R.string.crear_convocatoria_sitio_en_construccion));
    }
}
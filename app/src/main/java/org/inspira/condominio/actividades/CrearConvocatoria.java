package org.inspira.condominio.actividades;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.inspira.condominio.R;
import org.inspira.condominio.datos.AlmacenamientoInterno;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.Informacion;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.fragmentos.DatosDeEncabezado;
import org.inspira.condominio.fragmentos.OrdenDelDia;
import org.inspira.condominio.pdf.ExportarConvocatoria;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CrearConvocatoria extends AppCompatActivity {

    private static final int TERCERA_CONV = 3;
    private static final int SEGUNDA_CONV = 2;
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
    private String nombreDeArchivo;

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
            nombreDeArchivo = savedInstanceState.getString("nombre_de_archivo");
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
        asunto = datosDeEncabezado.getAsunto();
        condominio = datosDeEncabezado.getCondominio();
        ubicacion = datosDeEncabezado.getUbicacion();
        ubicacionInterna = datosDeEncabezado.getUbicacionInterna();
        firma = datosDeEncabezado.getFirma();
        fechaInicial = datosDeEncabezado.getFechaInicial();
        tiempoInicial = datosDeEncabezado.getTiempoInicial();
        puntos = ordenDelDia.getPuntos();
        nombreDeArchivo = ordenDelDia.getNombreArchivo();
    }

    private void generaPDF(){
        ExportarConvocatoria generar = new ExportarConvocatoria(this,asunto, condominio, ubicacion,
                convertirFecha(fechaInicial), ubicacionInterna, formatoOrdenDelDia(), tiempoInicial,
                calcularTiempo(SEGUNDA_CONV), calcularTiempo(TERCERA_CONV),
                new Date(), firma);
        try {
            File f = new File(Environment.getExternalStorageDirectory(), "Abbit");
            if (!f.exists()) {
                f.mkdirs();
            }
            File archivo = //new File(AlmacenamientoInterno.obtenerRutaAlmacenamientoInterno());
            new File(AlmacenamientoInterno.obtenerRutaAlmacenamientoInterno() + "/" + nombreDeArchivo);
            generar.crearArchivo(archivo);
        }catch(IOException e){
            e.printStackTrace();
            ProveedorSnackBar
                    .muestraBarraDeBocados(findViewById(R.id.formato_convocatoria_contenedor), getString(R.string.crear_convocatoria_error_pdf));
        }
    }

    private void guardaEnBaseDeDatos(){}

    private void difundeConvocatoria(){
        ProveedorSnackBar.muestraBarraDeBocados(findViewById(R.id.formato_convocatoria_contenedor),
                getString(R.string.crear_convocatoria_sitio_en_construccion));
    }

    private String calcularTiempo(int choose){
        String[] factores = tiempoInicial.split(":");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(factores[0]));
        c.set(Calendar.MINUTE,Integer.parseInt(factores[1]));
        switch(choose) {
            case SEGUNDA_CONV:
                c.setTime(new Date(c.getTime().getTime() + 3600000 * 30));
                break;
            case TERCERA_CONV:
                c.setTime(new Date(c.getTime().getTime() + 3600000 * 60));
                break;
        }
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minuto = c.get(Calendar.MINUTE);
        return (hora < 10 ? "0" + hora : hora) + ":" + (minuto < 10 ? "0" + minuto : minuto);
    }

    private String formatoOrdenDelDia(){
        StringBuffer sb = new StringBuffer();
        for(String str : puntos)
            sb.append(!str.equals(puntos[puntos.length-1]) ? str + "\n" : str);
        return sb.toString();
    }

    private Date convertirFecha(String fecha){
        Date date = null;
        try{
            date = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return date;
    }
}
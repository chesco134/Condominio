package org.inspira.condominio.actividades;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.Informacion;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.fragmentos.DatosDeEncabezado;
import org.inspira.condominio.fragmentos.OrdenDelDia;

public class CrearConvocatoria extends AppCompatActivity {

    private DatosDeEncabezado datosDeEncabezado;
    private OrdenDelDia ordenDelDia;
    private TextView tiempoInicial;
    private String asunto;
    private String condominio;
    private String ubicacion;
    private String ubicacionInterna;
    private String fechaInicial;
    private String[] puntos;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formato_para_convocatoria);
        if(savedInstanceState == null){
            datosDeEncabezado = new DatosDeEncabezado();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.formato_convocatoria_contenedor,datosDeEncabezado,"DatosDeEncabezado")
                    .setCustomAnimations(R.anim.traslacion_derecha,R.anim.traslacion_izquierda)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_convocatoria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        muestraMensajeInformacion(getResources().getString(R.string.dialogo_informacion_confirmar_salida));
    }

    private void muestraMensajeInformacion(String mensaje){
        Bundle arguments = new Bundle();
        arguments.putString("titulo","Alerta");
        arguments.putString("mensaje",mensaje);
        Informacion info = new Informacion();
        info.setArguments(arguments);
        info.setAccion(new EntradaTexto.AccionDialogo() {
            @Override
            public void accionPositiva(DialogFragment fragment) {
                finish();
            }

            @Override
            public void accionNegativa(DialogFragment fragment) {}
        });
        info.show(getSupportFragmentManager(),"Informacion");
    }

    public void colocaOrdenDelDiaFragmento(){
        ordenDelDia = new OrdenDelDia();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.formato_convocatoria_contenedor, ordenDelDia, "Orden del Día")
                .setCustomAnimations(R.anim.traslacion_derecha, R.anim.traslacion_izquierda)
                .commit();
    }

    public void creaConvocatoria(){
        asunto = datosDeEncabezado.getAsunto();
        condominio = datosDeEncabezado.getCondominio();
        ubicacion = datosDeEncabezado.getUbicacion();
        ubicacionInterna = datosDeEncabezado.getUbicacionInterna();
        fechaInicial = datosDeEncabezado.getFechaInicial();
        tiempoInicial = datosDeEncabezado.getTiempoInicial();
        puntos = ordenDelDia.getPuntos();
        generaPDF();
        guardaEnBaseDeDatos();
    }

    private void generaPDF(){}

    private void guardaEnBaseDeDatos(){
        ProveedorSnackBar.muestraBarraDeBocados(findViewById(R.id.formato_convocatoria_contenedor),
                getString(R.string.crear_convocatoria_sitio_en_construccion));
    }
}
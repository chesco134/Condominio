package org.inspira.condominio.admin.condominio;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.ActualizaTextoDesdeEntradaLibre;
import org.inspira.condominio.actividades.ActualizaTextoDesdeLista;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admon.CompruebaCamposJSON;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 29/03/16.
 */
public class EstadoDeCondominio extends AppCompatActivity{

    private static final int TIPO_TEXTO = EditorInfo.TYPE_CLASS_TEXT;
    private static final int TIPO_NUMERICO = EditorInfo.TYPE_CLASS_NUMBER;
    private static final int TIPO_FLOTANTE = EditorInfo.TYPE_NUMBER_FLAG_DECIMAL;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_resumen_condominio);
        findViewById(R.id.resumen_condominio_contenedor_antiguedad)
                .setOnClickListener(new ColocaValorDesdeDialogo(R.id.resumen_condominio_etiqueta_antiguedad, R.id.resumen_condominio_antiguedad, TIPO_NUMERICO, "edad"));
        findViewById(R.id.resumen_condominio_contenedor_capacidad_cisterna)
                .setOnClickListener(new ColocaValorDesdeDialogo(R.id.resumen_condominio_etiqueta_capacidad_cisterna, R.id.resumen_condominio_capacidad_cisterna, TIPO_NUMERICO, "capacidad_cisterna"));
        findViewById(R.id.resumen_condominio_contenedor_costo_aproximado_por_unidad_privativa)
                .setOnClickListener(new ColocaValorDesdeDialogo(R.id.resumen_condominio_etiqueta_costo_aprox_por_unidad_privativa, R.id.resumen_condominio_costo_aprox_por_unidad_privativa, TIPO_FLOTANTE, "costo_aproximado"));
        findViewById(R.id.resumen_condominio_contenedor_inmoviliaria)
                .setOnClickListener(new ColocaValorDesdeDialogo(R.id.resumen_condominio_etiqueta_inmoviliaria, R.id.resumen_condominio_inmoviliaria, TIPO_TEXTO, "inmoviliaria"));
        findViewById(R.id.resumen_condominio_contenedor_estacionamiento)
                .setOnClickListener(new ColocaValorDesdeDialogo(R.id.resumen_condominio_etiqueta_cantidad_lugares_estacionamiento, R.id.resumen_condominio_cantidad_lugares_estacionamiento, TIPO_NUMERICO, "cantidad_de_lugares_estacionamiento"));
        findViewById(R.id.resumen_condominio_contenedor_estacionamiento_visitas)
                .setOnClickListener(new ColocaValorDesdeDialogo(R.id.resumen_condominio_etiqueta_cantidad_lugares_estacionamiento_visitas, R.id.resumen_condominio_cantidad_lugares_estacionamiento_visitas, TIPO_NUMERICO, "cantidad_de_lugares_estacionamiento_visitas"));
        findViewById(R.id.resumen_condominio_tipo_de_condominio).setOnClickListener(new ActualizaTextoDesdeLista(R.array.tipos_de_condominio, "Tipo de condominio"));
        findViewById(R.id.resumen_condominio_nombre).setOnClickListener(new ColocaValorDesdeDialogo("Nombde del Condominio",R.id.resumen_condominio_nombre, TIPO_TEXTO, "nombre"));
        if(savedInstanceState == null){

        }
    }

    private class ColocaValorDesdeDialogo implements View.OnClickListener{

        private int viewResLabel;
        private int viewResToChange;
        private int tipoDeEntradaDeTexto;
        private String nombreCampo;
        private String key;

        public ColocaValorDesdeDialogo(int viewResLabel, int viewResToChange, int tipoDeEntradaDeTexto, String key) {
            this.viewResLabel = viewResLabel;
            this.viewResToChange = viewResToChange;
            this.tipoDeEntradaDeTexto = tipoDeEntradaDeTexto;
            this.key = key;
        }

        public ColocaValorDesdeDialogo(String nombreCampo, int viewResToChange, int tipoDeEntradaDeTexto, String key) {
            this.nombreCampo = nombreCampo;
            this.viewResToChange = viewResToChange;
            this.tipoDeEntradaDeTexto = tipoDeEntradaDeTexto;
            this.key = key;
        }

        @Override
        public void onClick(View view){
            EntradaTexto et = new EntradaTexto();
            et.setTipoDeEntradaDeTexto(tipoDeEntradaDeTexto);
            final TextView viewToChange = (TextView)view.findViewById(viewResToChange);
            TextView viewLabel;
            String labelText;
            if( nombreCampo == null ) {
                viewLabel = (TextView) view.findViewById(viewResLabel);
                labelText = viewLabel.getText().toString();
            }else{
                labelText = nombreCampo;
            }
            String prevText = viewToChange.getText().toString().trim();
            Bundle args = new Bundle();
            args.putString("contenido", prevText);
            args.putString("mensaje", labelText);
            et.setArguments(args);
            et.setAccionDialogo(new EntradaTexto.AccionDialogo() {
                @Override
                public void accionPositiva(DialogFragment fragment) {
                    final String nuevoTexto = ((EntradaTexto) fragment).getEntradaDeTexto().trim();
                    if (!"".equals(nuevoTexto)) {
                        String contenidoMensaje = armarContenidoDeMensaje(nuevoTexto);
                        ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                            @Override
                            public void resultadoSatisfactorio(Thread t) {
                                String resultado = ((ContactoConServidor)t).getResponse();
                                if(CompruebaCamposJSON.validaContenido(resultado)){
                                    EstadoDeCondominio.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            viewToChange.setText(nuevoTexto);
                                        }
                                    });
                                    MuestraMensajeDesdeHilo.muestraMensaje(EstadoDeCondominio.this, viewToChange, "Hecho");
                                }
                            }

                            @Override
                            public void problemasDeConexion(Thread t) {
                                MuestraMensajeDesdeHilo.muestraMensaje(EstadoDeCondominio.this, viewToChange, "Servicio no disponible por el momento");
                            }
                        }, contenidoMensaje);
                        contacto.start();
                    }
                }

                private String armarContenidoDeMensaje(String nuevoTexto) {
                    String contenidoDeMensaje = null;
                    try {
                        JSONObject json = new JSONObject();
                        json.put("action", ProveedorDeRecursos.ACTUALIZAR_DATOS_CONDOMINIO);
                        json.put("key", key);
                        json.put("value", nuevoTexto);
                        contenidoDeMensaje = json.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return contenidoDeMensaje;
                }

                @Override
                public void accionNegativa(DialogFragment fragment) {
                }
            });
            et.show(getSupportFragmentManager(), "Modificar campo");
        }
    }
}

package org.inspira.condominio.admin.condominio;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admon.AccionesTablaCondominio;
import org.inspira.condominio.admon.CompruebaCamposJSON;
import org.inspira.condominio.datos.Condominio;
import org.inspira.condominio.dialogos.DialogoDeLista;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
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
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Condominio condominio = AccionesTablaCondominio.obtenerCondominio(this, ProveedorDeRecursos.obtenerIdCondominio(this));
        ((TextView) findViewById(R.id.resumen_condominio_nombre)).setText(condominio.getNombre());
        ((TextView) findViewById(R.id.resumen_condominio_tipo_de_condominio)).setText(AccionesTablaCondominio.obtenerTipoDeCondominio(this, condominio.getTipoDeCondominio().getId()).getDescripcion());
        ((TextView) findViewById(R.id.resumen_condominio_antiguedad)).setText(String.valueOf(condominio.getEdad()));
        ((TextView) findViewById(R.id.resumen_condominio_inmoviliaria)).setText(condominio.getInmoviliaria());
        ((TextView) findViewById(R.id.resumen_condominio_capacidad_cisterna)).setText(String.valueOf(condominio.getCapacidadDeCisterna()));
        ((TextView) findViewById(R.id.resumen_condominio_cantidad_lugares_estacionamiento)).setText(String.valueOf(condominio.getCantidadDeLugaresEstacionamiento()));
        ((TextView) findViewById(R.id.resumen_condominio_cantidad_lugares_estacionamiento_visitas)).setText(String.valueOf(condominio.getCantidadDeLugaresEstacionamientoVisitas()));
        ((TextView) findViewById(R.id.resumen_condominio_costo_aprox_por_unidad_privativa)).setText(String.valueOf(condominio.getCostoAproximadoPorUnidadPrivativa()));
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
        findViewById(R.id.resumen_condominio_tipo_de_condominio).setOnClickListener(new ActualizaEntradaDesdeArreglo());
        findViewById(R.id.resumen_condominio_nombre).setOnClickListener(new ColocaValorDesdeDialogo("Nombde del Condominio", R.id.resumen_condominio_nombre, TIPO_TEXTO, "nombre"));
        CheckBox poseeSalaDejuntas = (CheckBox)findViewById(R.id.resumen_condominio_posee_sala_de_juntas);
        CheckBox poseeEspacioRecreativo = (CheckBox)findViewById(R.id.resumen_condominio_posee_espacio_recreativo);
        CheckBox poseeEspacioCultural = (CheckBox)findViewById(R.id.resumen_condominio_posee_espacio_cultural);
        CheckBox poseeOficinasAdministrativas = (CheckBox)findViewById(R.id.resumen_condominio_posee_oficinas_administrativas);
        CheckBox poseeCisternaAguaPluvial = (CheckBox)findViewById(R.id.resumen_condominio_posee_cisterna_agua_pluvial);
        CheckBox poseeGym = (CheckBox)findViewById(R.id.resumen_condominio_posee_gym);
        CheckBox poseeAlarmaCismica = (CheckBox)findViewById(R.id.resumen_condominio_posee_alarma_sismica);
        poseeSalaDejuntas.setChecked(condominio.isPoseeSalaDeJuntas());
        poseeEspacioRecreativo.setChecked(condominio.isPoseeEspacioRecreativo());
        poseeEspacioCultural.setChecked(condominio.isPoseeEspacioCultural());
        poseeOficinasAdministrativas.setChecked(condominio.isPoseeOficinasAdministrativas());
        poseeCisternaAguaPluvial.setChecked(condominio.isPoseeCisternaAguaPluvial());
        poseeAlarmaCismica.setChecked(condominio.isPoseeAlarmaSismica());
        poseeGym.setChecked(condominio.isPoseeGym());
        poseeAlarmaCismica.setOnCheckedChangeListener(new AccionCheckBox());
        poseeSalaDejuntas.setOnCheckedChangeListener(new AccionCheckBox());
        poseeEspacioRecreativo.setOnCheckedChangeListener(new AccionCheckBox());
        poseeEspacioCultural.setOnCheckedChangeListener(new AccionCheckBox());
        poseeOficinasAdministrativas.setOnCheckedChangeListener(new AccionCheckBox());
        poseeCisternaAguaPluvial.setOnCheckedChangeListener(new AccionCheckBox());
        poseeGym.setOnCheckedChangeListener(new AccionCheckBox());
        ((TextView) findViewById(R.id.resumen_condominio_direccion)).setText(condominio.getDireccion());
        findViewById(R.id.resumen_condominio_direccion)
                .setOnClickListener(new ColocaValorDesdeDialogo("Dirección", R.id.resumen_condominio_direccion, TIPO_TEXTO, "direccion"));
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
                        String contenidoMensaje = armarContenidoDeMensaje(key, nuevoTexto);
                        ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                            @Override
                            public void resultadoSatisfactorio(Thread t) {
                                String resultado = ((ContactoConServidor)t).getResponse();
                                AccionesTablaCondominio.actualizaCampo(EstadoDeCondominio.this, key, nuevoTexto);
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

                @Override
                public void accionNegativa(DialogFragment fragment) {
                }
            });
            et.show(getSupportFragmentManager(), "Modificar campo");
        }
    }

    private class ActualizaEntradaDesdeArreglo implements View.OnClickListener{

        @Override
        public void onClick(View view){
            DialogoDeLista ddl = new DialogoDeLista();
            ddl.setStringArrayRes(R.array.tipos_de_condominio);
            ddl.setTitulo("Tipo de condominio");
            ddl.setAccion(new MiAccionDialogDeLista((TextView)view));
            ddl.show(getSupportFragmentManager(), "Sel tipo de condominio");
        }
    }

    private class MiAccionDialogDeLista implements DialogoDeLista.AccionDialogoDeLista{

        private TextView hostElement;

        public MiAccionDialogDeLista(TextView hostElement) {
            this.hostElement = hostElement;
        }

        @Override
        public void objetoSeleccionado(final String texto) {
            String contenidoDeMensaje = armarContenidoDeMensaje("idTipo_de_Condominio", String.valueOf(AccionesTablaCondominio.obtenerIdTipoDeCondominio(EstadoDeCondominio.this,texto)));
            ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                @Override
                public void resultadoSatisfactorio(Thread t) {
                    String respuesta = ((ContactoConServidor)t).getResponse();
                    if(CompruebaCamposJSON.validaContenido(respuesta)){
                        AccionesTablaCondominio.actualizaCampo(EstadoDeCondominio.this, "idTipo_de_Condominio", String.valueOf(AccionesTablaCondominio.obtenerIdTipoDeCondominio(EstadoDeCondominio.this, texto)));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hostElement.setText(texto);
                                ProveedorSnackBar
                                        .muestraBarraDeBocados(hostElement, "Hecho");
                            }
                        });
                    }else{
                        MuestraMensajeDesdeHilo
                                .muestraMensaje(EstadoDeCondominio.this, hostElement, "Servicio por el momento no disponible");
                    }
                }

                @Override
                public void problemasDeConexion(Thread t) {}
            }, contenidoDeMensaje);
            contacto.start();
        }
    }

    private class AccionCheckBox implements CompoundButton.OnCheckedChangeListener{

        private boolean allowToCheck;

        public AccionCheckBox() {
            allowToCheck = true;
        }

        @Override
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
            if(allowToCheck) {
                final String key = buttonView.getText().toString().replace(' ', '_').replace('í','i');
                final String value = isChecked ? "1" : "0";
                String content = armarContenidoDeMensaje(key, value);
                ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                    @Override
                    public void resultadoSatisfactorio(Thread t) {
                        String respuesta = ((ContactoConServidor) t).getResponse();
                        if (CompruebaCamposJSON.validaContenido(respuesta)) {
                            AccionesTablaCondominio.actualizaCampo(EstadoDeCondominio.this, key, value);
                            MuestraMensajeDesdeHilo.muestraMensaje(EstadoDeCondominio.this, buttonView, "Hecho");
                        } else {
                            MuestraMensajeDesdeHilo.muestraMensaje(EstadoDeCondominio.this, buttonView, "Hola ché");
                            allowToCheck = false;
                            buttonView.setChecked(!buttonView.isChecked());
                        }
                    }

                    @Override
                    public void problemasDeConexion(Thread t) {
                        MuestraMensajeDesdeHilo.muestraMensaje(EstadoDeCondominio.this, buttonView, "Servicio por el momento no disponible");
                        allowToCheck = false;
                        buttonView.setChecked(!buttonView.isChecked());
                    }
                }, content);
                contacto.start();
            }else
                allowToCheck = true;
        }
    }

    private String armarContenidoDeMensaje(String key, String valor) {
        String contenidoDeMensaje = null;
        try {
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.ACTUALIZAR_DATOS_CONDOMINIO);
            json.put("idCondominio", ProveedorDeRecursos.obtenerIdCondominio(this));
            json.put("key", key);
            json.put("value", valor);
            contenidoDeMensaje = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contenidoDeMensaje;
    }
}

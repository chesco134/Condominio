package org.inspira.condominio.admin.condominio;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.AccionCheckBox;
import org.inspira.condominio.actividades.ActualizaEntradaDesdeArreglo;
import org.inspira.condominio.actividades.ActualizarCampoDeContacto;
import org.inspira.condominio.actividades.ColocaValorDesdeDialogo;
import org.inspira.condominio.actividades.InsertarElementoMultivalor;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admon.AccionesTablaAdministracion;
import org.inspira.condominio.actividades.CompruebaCamposJSON;
import org.inspira.condominio.datos.Administracion;
import org.inspira.condominio.datos.ContactoAdministracion;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.RemocionElementos;
import org.inspira.condominio.fragmentos.OrdenDelDia;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 30/03/16.
 */
public class EstadoDeAdministracion extends AppCompatActivity implements ColocaValorDesdeDialogo.FormatoDeMensaje, AccionCheckBox.ActualizacionDeCampo {

    private LinearLayout contenedorContactos;
    private int[] idContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_resumen_admon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar appBar = getSupportActionBar();
        assert appBar != null;
        appBar.setDisplayHomeAsUpEnabled(true);
        Administracion administracion = AccionesTablaAdministracion.obtenerAdministracion(this, ProveedorDeRecursos.obtenerIdAdministracion(this));
        ((TextView) findViewById(R.id.resumen_administracion_cuota_anual)).setText(String.valueOf(administracion.getCostoDeCuotaAnual()));
        ((TextView) findViewById(R.id.resumen_administracion_cuota_mensual)).setText(String.valueOf(administracion.getCostoDeCuotaDeMantenimientoMensual()));
        ((TextView) findViewById(R.id.resumen_administracion_intervalo_transparencia)).setText(AccionesTablaAdministracion.obtenerIntervaloDeTransparencia(this, administracion.getIntervaloDeTransparencia().getId()).getIntervaloDeTransparencia());
        ((TextView) findViewById(R.id.resumen_administracion_promedio_egresos)).setText(String.valueOf(administracion.getPromedioInicialDeEgresos()));
        ((TextView) findViewById(R.id.resumen_administracion_promedio_morosidad)).setText(String.valueOf(administracion.getPromedioInicialDeMorosidad()));
        CheckBox maquinas = ((CheckBox) findViewById(R.id.resumen_administracion_posee_mantenimiento_profesional_al_cuarto_de_maquinas));
        maquinas.setChecked((administracion.isPoseeMantenimientoProfesionalCuartoDeMaquinas()));
        CheckBox elevadores = ((CheckBox) findViewById(R.id.resumen_administracion_posee_mantenimiento_profesional_a_elevadores));
        elevadores.setChecked((administracion.isPoseeMantenimientoProfesionalElevadores()));
        CheckBox intramuros = ((CheckBox) findViewById(R.id.resumen_administracion_posee_personal_capacitado_en_seguridad_intramuros));
        intramuros.setChecked((administracion.isPoseePersonalidadCapacitadoEnSeguridadIntramuros()));
        CheckBox planes = ((CheckBox) findViewById(R.id.resumen_administracion_posee_planes_de_trabajo));
        planes.setChecked((administracion.isPoseePlanesDeTrabajo()));
        CheckBox wifi = ((CheckBox) findViewById(R.id.resumen_administracion_posee_wifi_abierto));
        wifi.setChecked((administracion.isPoseeWiFiAbierto()));
        findViewById(R.id.resumen_administracion_contenedor_de_cuota_mensual)
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.resumen_administracion_etiqueta_cuota_mensual, R.id.resumen_administracion_cuota_mensual, EstadoDeCondominio.TIPO_FLOTANTE, "costo_de_cuota_de_mantenimiento_mensual", this, this));
        findViewById(R.id.resumen_administracion_contenedor_de_cuota_anual)
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.resumen_administracion_etiqueta_cuota_anual, R.id.resumen_administracion_cuota_anual, EstadoDeCondominio.TIPO_FLOTANTE, "costo_de_cuota_anual", this, this));
        findViewById(R.id.resumen_administracion_contenedor_promedio_inicial_egresos)
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.resumen_administracion_etiqueta_promedio_egresos, R.id.resumen_administracion_promedio_egresos, EstadoDeCondominio.TIPO_FLOTANTE, "promedio_inicial_de_egresos", this, this));
        findViewById(R.id.resumen_administracion_contenedor_promedio_inicial_morosidad)
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.resumen_administracion_etiqueta_promedio_morosidad, R.id.resumen_administracion_promedio_morosidad, EstadoDeCondominio.TIPO_FLOTANTE, "promedio_inicial_de_morosidad", this, this));
        findViewById(R.id.resumen_administracion_intervalo_transparencia).setOnClickListener(new ActualizaEntradaDesdeArreglo(this, this, R.array.intervalos_de_transparencia_admon, "idIntervalo_Transparencia", this));
        maquinas.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        elevadores.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        intramuros.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        planes.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        wifi.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        ContactoAdministracion[] contactos = AccionesTablaAdministracion.obtenerListaDeContactos(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contenedorContactos = (LinearLayout) findViewById(R.id.resumen_administracion_contenedor_de_contacto);
        TextView nuevoTexto;
        Bundle args = new Bundle();
        args.putInt("action", ProveedorDeRecursos.ACTUALIZACION_DE_CONTACTO);
        args.putString("table", "Contacto_Administracion");
        args.putString("column", "contacto");
        idContactos = new int[contactos.length];
        int i=0;
        for(ContactoAdministracion contacto : contactos){
            args.putInt("pk_value", contacto.getId());
            nuevoTexto = (TextView) inflater.inflate(R.layout.entrada_simple_de_texto, contenedorContactos, false);
            nuevoTexto.setText(contacto.getContacto());
            nuevoTexto.setOnClickListener(ActualizarCampoDeContacto.crearActualizarCampoDeContacto(this, args));
            idContactos[i] = i++;
            contenedorContactos.addView(nuevoTexto);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.resumen_administracion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        if( itemId == R.id.resumen_administracion_add_contacto){
            lanzaDialogoMultivalor();
            return true;
        } else if( itemId == R.id.resumen_administracion_remove_contacto ){
            lanzaDialogoRemocionDeContactos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void lanzaDialogoRemocionDeContactos() {
        RemocionElementos rm = new RemocionElementos();
        final ContactoAdministracion[] contactos = AccionesTablaAdministracion.obtenerListaDeContactos(this);
        String[] elementos = new String[contactos.length];
        int i=0;
        for(ContactoAdministracion contacto : contactos)
            elementos[i++] = contacto.getContacto();
        Bundle args = new Bundle();
        args.putStringArray("elementos", elementos);
        rm.setArguments(args);
        rm.setAd(new EntradaTexto.AccionDialogo() {
            @Override
            public void accionPositiva(DialogFragment fragment) {
                final Integer[] seleccion = ((RemocionElementos)fragment).getElementosSeleccionados();
                OrdenDelDia.prepareElements(seleccion);
                String contenidoDeMensaje = armarContenidoDeMensaje(seleccion);
                ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                    @Override
                    public void resultadoSatisfactorio(Thread t) {
                        String respuesta = ((ContactoConServidor)t).getResponse();
                        if(CompruebaCamposJSON.validaContenido(respuesta)){
                            eliminarElementosEnBaseDeDatos();
                            MuestraMensajeDesdeHilo.muestraMensaje(EstadoDeAdministracion.this, contenedorContactos, "Hecho");
                        }else
                            MuestraMensajeDesdeHilo.muestraMensaje(EstadoDeAdministracion.this, contenedorContactos, "Servicio por el momento no disponible");
                    }

                    private void eliminarElementosEnBaseDeDatos() {
                        List<Integer> ids = new ArrayList<>();
                        for(Integer index : seleccion)
                            ids.add(contactos[index].getId());
                        AccionesTablaAdministracion.eliminarContactos(EstadoDeAdministracion.this, ids.toArray(new Integer[0]));
                        quitarElementosDeLaLista(seleccion);
                    }

                    @Override
                    public void problemasDeConexion(Thread t) {
                        MuestraMensajeDesdeHilo.muestraMensaje(EstadoDeAdministracion.this, contenedorContactos, "Servicio momentaneamente inalcanzable");
                    }
                }, contenidoDeMensaje);
                contacto.start();
            }

            private String armarContenidoDeMensaje(Integer[] seleccion){
                String contenidoDeMensaje = null;
                try{
                    JSONObject json = new JSONObject();
                    json.put("action", ProveedorDeRecursos.REMOCION_DE_CONTACTOS);
                    json.put("table", "Contacto_Administracion");
                    JSONArray array = new JSONArray();
                    for(Integer index : seleccion)
                        array.put(contactos[index].getId());
                    json.put("elementos", array);
                    contenidoDeMensaje = json.toString();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                return contenidoDeMensaje;
            }

            @Override
            public void accionNegativa(DialogFragment fragment) {
                MuestraMensajeDesdeHilo.muestraMensaje(EstadoDeAdministracion.this, contenedorContactos, "Servicio temporalmente inalcanzable");
            }
        });
        rm.show(getSupportFragmentManager(), "Eliminar Contactos");
    }

    private void quitarElementosDeLaLista(final Integer[] seleccion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(Integer index : seleccion)
                    contenedorContactos.removeView(contenedorContactos.getChildAt(index));
            }
        });
    }

    private void lanzaDialogoMultivalor() {
        Bundle args = new Bundle();
        args.putInt(InsertarElementoMultivalor.ACCION, ProveedorDeRecursos.REGISTRO_CONTACTO);
        args.putString(InsertarElementoMultivalor.TITULO, "E-mail, facebook, twitter, tel, etc.");
        args.putString(InsertarElementoMultivalor.TABLE_NAME, "Contacto_Administracion");
        args.putString(InsertarElementoMultivalor.COLUMN_NAME, "contacto");
        args.putString(InsertarElementoMultivalor.FK_NAME, "idAdministracion");
        args.putInt(InsertarElementoMultivalor.FK_VALUE, ProveedorDeRecursos.obtenerIdAdministracion(this));
        args.putInt(InsertarElementoMultivalor.INPUT_TYPE, InputType.TYPE_CLASS_TEXT);
        args.putInt(InsertarElementoMultivalor.RECURSO_DE_CONTENEDOR, R.id.resumen_administracion_contenedor_de_contacto);
        InsertarElementoMultivalor dialogo = InsertarElementoMultivalor.crearDialogo(args);
        dialogo.show(getSupportFragmentManager(), "Insertar 1 valor");
    }

    @Override
    public String armarContenidoDeMensaje(String key, String valor) {
        String contenidoDeMensaje = null;
        try {
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.ACTUALIZAR_DATOS_ADMINISTRACION);
            json.put("idAdministracion", ProveedorDeRecursos.obtenerIdAdministracion(this));
            json.put("key", key);
            json.put("value", valor);
            contenidoDeMensaje = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contenidoDeMensaje;
    }

    @Override
    public void actualizaCampo(String key, String value) {
        AccionesTablaAdministracion.actualizaCampo(this, key, value);
    }

    @Override
    public int obtenerId(String texto) {
        return AccionesTablaAdministracion.obtenerIdIntervaloTransparencia(this, texto);
    }
}

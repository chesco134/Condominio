package org.inspira.condominio.admin.condominio;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.AccionCheckBox;
import org.inspira.condominio.actividades.ActualizaEntradaDesdeArreglo;
import org.inspira.condominio.actividades.ColocaValorDesdeDialogo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admon.AccionesTablaAdministracion;
import org.inspira.condominio.datos.Administracion;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 30/03/16.
 */
public class EstadoDeAdministracion extends AppCompatActivity implements ColocaValorDesdeDialogo.FormatoDeMensaje, AccionCheckBox.ActualizacionDeCampo {

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
        findViewById(R.id.resumen_administracion_intervalo_transparencia).setOnClickListener(new ActualizaEntradaDesdeArreglo(this, R.array.intervalos_de_transparencia_admon, "idIntervalo_Transparencia", this));
        maquinas.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        elevadores.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        intramuros.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        planes.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        wifi.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
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

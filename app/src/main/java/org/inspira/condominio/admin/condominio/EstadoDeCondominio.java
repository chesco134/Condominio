package org.inspira.condominio.admin.condominio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.AccionCheckBox;
import org.inspira.condominio.actividades.ActualizaEntradaDesdeArreglo;
import org.inspira.condominio.actividades.ColocaValorDesdeDialogo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admon.AccionesTablaCondominio;
import org.inspira.condominio.datos.Condominio;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 29/03/16.
 */
public class EstadoDeCondominio extends AppCompatActivity implements ColocaValorDesdeDialogo.FormatoDeMensaje, AccionCheckBox.ActualizacionDeCampo {

    public static final int TIPO_TEXTO = EditorInfo.TYPE_CLASS_TEXT;
    public static final int TIPO_NUMERICO = EditorInfo.TYPE_CLASS_NUMBER;
    public static final int TIPO_FLOTANTE = EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL;

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
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.resumen_condominio_etiqueta_antiguedad, R.id.resumen_condominio_antiguedad, TIPO_NUMERICO, "edad", this, this));
        findViewById(R.id.resumen_condominio_contenedor_capacidad_cisterna)
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.resumen_condominio_etiqueta_capacidad_cisterna, R.id.resumen_condominio_capacidad_cisterna, TIPO_NUMERICO, "capacidad_cisterna", this, this));
        findViewById(R.id.resumen_condominio_contenedor_costo_aproximado_por_unidad_privativa)
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.resumen_condominio_etiqueta_costo_aprox_por_unidad_privativa, R.id.resumen_condominio_costo_aprox_por_unidad_privativa, TIPO_FLOTANTE, "costo_aproximado", this, this));
        findViewById(R.id.resumen_condominio_contenedor_inmoviliaria)
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.resumen_condominio_etiqueta_inmoviliaria, R.id.resumen_condominio_inmoviliaria, TIPO_TEXTO, "inmoviliaria", this, this));
        findViewById(R.id.resumen_condominio_contenedor_estacionamiento)
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.resumen_condominio_etiqueta_cantidad_lugares_estacionamiento, R.id.resumen_condominio_cantidad_lugares_estacionamiento, TIPO_NUMERICO, "cantidad_de_lugares_estacionamiento", this, this));
        findViewById(R.id.resumen_condominio_contenedor_estacionamiento_visitas)
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.resumen_condominio_etiqueta_cantidad_lugares_estacionamiento_visitas, R.id.resumen_condominio_cantidad_lugares_estacionamiento_visitas, TIPO_NUMERICO, "cantidad_de_lugares_estacionamiento_visitas", this, this));
        findViewById(R.id.resumen_condominio_tipo_de_condominio).setOnClickListener(new ActualizaEntradaDesdeArreglo(this, this, R.array.tipos_de_condominio, "idTipo_de_Condominio", this));
        findViewById(R.id.resumen_condominio_nombre).setOnClickListener(new ColocaValorDesdeDialogo(this, "Nombre del Condominio", R.id.resumen_condominio_nombre, TIPO_TEXTO, "nombre", this, this));
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
        poseeAlarmaCismica.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        poseeSalaDejuntas.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        poseeEspacioRecreativo.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        poseeEspacioCultural.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        poseeOficinasAdministrativas.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        poseeCisternaAguaPluvial.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        poseeGym.setOnCheckedChangeListener(new AccionCheckBox(this, this, this));
        ((TextView) findViewById(R.id.resumen_condominio_direccion)).setText(condominio.getDireccion());
        findViewById(R.id.resumen_condominio_direccion)
                .setOnClickListener(new ColocaValorDesdeDialogo(this, "Dirección", R.id.resumen_condominio_direccion, TIPO_TEXTO, "direccion", this, this));
        findViewById(R.id.fab)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(EstadoDeCondominio.this, EstadoDeAdministracion.class));
                    }
                });
    }

    @Override
    public String armarContenidoDeMensaje(String key, String valor) {
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

    @Override
    public void actualizaCampo(String key, String value) {
        AccionesTablaCondominio.actualizaCampo(this, key, value);
    }

    @Override
    public int obtenerId(String texto) {
        return AccionesTablaCondominio.obtenerIdTipoDeCondominio(this, texto);
    }
}

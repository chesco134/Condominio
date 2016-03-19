package org.inspira.condominio.admon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.EfectoDeEnfoque;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.actividades.Verificador;
import org.inspira.condominio.adaptadores.MallaDeCheckBoxes;
import org.inspira.condominio.datos.Administracion;
import org.inspira.condominio.datos.IntervaloDeTransparencia;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jcapiz on 18/03/16.
 */
public class RegistroAdministracion extends AppCompatActivity {

    private EditText cuotaMantenimientoMensual;
    private EditText cuotaMantenimientoAnual;
    private EditText promedioDeMorosidad;
    private EditText promedioDeEgreso;
    private TextView intervaloDeTransparencia;
    private GridView mallaDeOpciones;
    private Map<String, Boolean> marcas;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_registro_administracion);
        cuotaMantenimientoMensual = (EditText) findViewById(R.id.formulario_registro_administracion_costo_mantenimiento_mensual);
        cuotaMantenimientoAnual = (EditText) findViewById(R.id.formulario_registro_administracion_costo_mantenimiento_anual);
        promedioDeMorosidad = (EditText)findViewById(R.id.formulario_registro_administracion_promedio_morosidad);
        promedioDeEgreso = (EditText)findViewById(R.id.formulario_registro_administracion_promedio_egresos);
        intervaloDeTransparencia = (TextView)findViewById(R.id.formulario_registro_administracion_intervalo_de_transparencia);
        mallaDeOpciones = (GridView) findViewById(R.id.formulario_registro_administracion_campos_opcionales);
        findViewById(R.id.formulario_registro_administracion_piso_costo_mantenimiento_mensual)
                .setOnFocusChangeListener(new EfectoDeEnfoque(this, cuotaMantenimientoMensual));
        findViewById(R.id.formulario_registro_administracion_piso_costo_mantenimiento_anual)
                .setOnFocusChangeListener(new EfectoDeEnfoque(this, cuotaMantenimientoAnual));
        findViewById(R.id.formulario_registro_administracion_piso_promedio_ingresos)
                .setOnFocusChangeListener(new EfectoDeEnfoque(this, promedioDeMorosidad));
        findViewById(R.id.formulario_registro_administracion_piso_promedio_egresos)
                .setOnFocusChangeListener(new EfectoDeEnfoque(this, promedioDeEgreso));
        findViewById(R.id.formulario_registro_administracion_boton_hecho)
                .setOnClickListener(new AccionHecho());
        setUpMallaCamposOpcionales();
    }

    private void setUpMallaCamposOpcionales() {
        if(marcas == null)
            marcas = new TreeMap<>();
        MallaDeCheckBoxes adapter = new MallaDeCheckBoxes(this, marcas, getResources().getStringArray(R.array.campos_opcionales_administracion));
        mallaDeOpciones.setAdapter(adapter);
    }

    private class AccionHecho implements View.OnClickListener{

        @Override
        public void onClick(View view){
            view.setEnabled(false);
            if(validaCampos()){
                contactaConServidor();
            }else{
                ProveedorSnackBar
                        .muestraBarraDeBocados(cuotaMantenimientoMensual, "Por favor verifique los campos");
            }
        }
    }

    private void contactaConServidor() {
        ProveedorSnackBar
                .muestraBarraDeBocados(cuotaMantenimientoMensual, "Sincronizando...");
        String contenido = generaContenidoDeMensaje();
        ContactoConServidor contacto = new ContactoConServidor(new AccionesContactoConServidor(), contenido);
        contacto.start();
    }

    private boolean validaCampos() {
        boolean veredictos[] = new boolean[5];
        veredictos[0] = Verificador.marcaCampo(this, cuotaMantenimientoMensual, Verificador.FLOTANTE);
        veredictos[1] = Verificador.marcaCampo(this, cuotaMantenimientoAnual, Verificador.FLOTANTE);
        veredictos[2] = Verificador.marcaCampo(this, promedioDeMorosidad, Verificador.FLOTANTE);
        veredictos[3] = Verificador.marcaCampo(this, promedioDeEgreso, Verificador.FLOTANTE);
        veredictos[4] = Verificador.marcaCampo(this, intervaloDeTransparencia, Verificador.TEXTO);
        boolean veredicto = true;
        for (boolean veredicto1 : veredictos)
            if (!(veredicto = veredicto1))
                break;
        return veredicto;
    }

    private String generaContenidoDeMensaje() {
        String contenido = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action", 14);
            json.put("cuota_de_mantenimiento_mensual", Float.parseFloat(cuotaMantenimientoMensual.getText().toString().trim()));
            json.put("cuota_de_mantenimiento_anual", Float.parseFloat(cuotaMantenimientoAnual.getText().toString().trim()));
            json.put("promedio_de_ingreso", Float.parseFloat(promedioDeMorosidad.getText().toString().trim()));
            json.put("promedio_de_egreso", Float.parseFloat(promedioDeEgreso.getText().toString().trim()));
            json.put("intervalo_de_transparencia", Float.parseFloat(intervaloDeTransparencia.getText().toString().trim()));
            String[] keys = getResources().getStringArray(R.array.campos_opcionales_administracion);
            json.put("posee_planes_de_trabajo", marcas.get(keys[0]));
            json.put("posee_mantenimiento_profesional_a_elevadores", marcas.get(keys[1]));
            json.put("posee_personal_capacitado_en_seguridad_intramuros", marcas.get(keys[2]));
            json.put("posee_mantenimiento_profesional_al_cuarto_de_maquinas", marcas.get(keys[3]));
            json.put("posee_wifi_abierto", marcas.get(keys[4]));
            contenido = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return contenido;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("cuota_de_mantenimiento_mensual", cuotaMantenimientoMensual.getText().toString().trim());
        outState.putString("cuota_de_mantenimiento_anual", cuotaMantenimientoAnual.getText().toString().trim());
        outState.putString("promedio_de_ingreso", promedioDeMorosidad.getText().toString().trim());
        outState.putString("promedio_de_egreso", promedioDeEgreso.getText().toString().trim());
        outState.putString("intervalo_de_transparencia", intervaloDeTransparencia.getText().toString().trim());
        Boolean[] valoresMarcados = this.marcas.values().toArray(new Boolean[1]);
        boolean[] marcas = new boolean[valoresMarcados.length];
        for(int i=0; i<marcas.length;i++)
            marcas[i] = valoresMarcados[i];
        outState.putBooleanArray("valores_marcados", marcas);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        cuotaMantenimientoMensual.setText(savedInstanceState.getString("cuota_de_mantenimiento_mensual"));
        cuotaMantenimientoAnual.setText(savedInstanceState.getString("cuota_de_mantenimiento_anual"));
        promedioDeMorosidad.setText(savedInstanceState.getString("promedio_de_ingreso"));
        promedioDeEgreso.setText(savedInstanceState.getString("promedio_de_egreso"));
        intervaloDeTransparencia.setText(savedInstanceState.getString("intervalo_de_transparencia"));
        String[] etiquetasDeOpciones = getResources().getStringArray(R.array.campos_opcionales_administracion);
        boolean[] valoresMarcados = savedInstanceState.getBooleanArray("valores_marcados");
        for(int i=0; i<etiquetasDeOpciones.length; i++) {
            assert valoresMarcados != null;
            marcas.put(etiquetasDeOpciones[i], valoresMarcados[i]);
        }
        ((MallaDeCheckBoxes)mallaDeOpciones.getAdapter()).notifyDataSetChanged();
    }

    private class AccionesContactoConServidor implements ContactoConServidor.AccionesDeValidacionConServidor{

        @Override
        public void resultadoSatisfactorio(Thread t) {
            String respuesta = ((ContactoConServidor)t).getResponse();
            if(revisaRespuesta(respuesta)) {
                guardaEnBaseDedatos();
                notificaResultado("Hecho");
                iniciaRegistroDeUsuario();
            }else{
                notificaResultado(mensajeDeServidor(respuesta));
            }
        }

        @Override
        public void problemasDeConexion(Thread t) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProveedorSnackBar
                            .muestraBarraDeBocados(cuotaMantenimientoMensual, "Servicio temporalmente no disponible");
                    habilitaBoton();
                }
            });
        }

        private String mensajeDeServidor(String respuesta) {
            String mensaje = null;
            try{
                JSONObject json = new JSONObject(respuesta);
                mensaje = json.getString("mensaje");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return mensaje;
        }

        private boolean revisaRespuesta(String respuesta) {
            boolean resultado = false;
            try{
                JSONObject json = new JSONObject(respuesta);
                resultado = json.getBoolean("content");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return resultado;
        }
    }

    private void notificaResultado(final String mensaje){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                habilitaBoton();
                ProveedorSnackBar
                        .muestraBarraDeBocados(cuotaMantenimientoMensual, mensaje);
            }
        });
    }

    private void guardaEnBaseDedatos() {
        Administracion administracion = new Administracion();
        administracion.setCondominio(AccionesTablaCondominio.obtenerCondominio(this, ProveedorDeRecursos.obtenerIdCondominio(this)));
        IntervaloDeTransparencia intervaloDeTransparencia = new IntervaloDeTransparencia(AccionesTablaAdministracion.obtenerIdIntervaloTransparencia(this, this.intervaloDeTransparencia.getText().toString().trim()));
        administracion.setIntervaloDeTransparencia(intervaloDeTransparencia);
        administracion.setCostoDeCuotaAnual(Float.parseFloat(cuotaMantenimientoAnual.getText().toString().trim()));
        administracion.setCostoDeCuotaDeMantenimientoMensual(Float.parseFloat(cuotaMantenimientoMensual.getText().toString().trim()));
        administracion.setPromedioInicialDeEgresos(Float.parseFloat(promedioDeEgreso.getText().toString().trim()));
        administracion.setPromedioInicialDeMorosidad(Float.parseFloat(promedioDeMorosidad.getText().toString().trim()));
        String[] keys = getResources().getStringArray(R.array.campos_opcionales_administracion);
        administracion.setPoseePlanesDeTrabajo(marcas.get(keys[0]));
        administracion.setPoseeMantenimientoProfesionalElevadores(marcas.get(keys[1]));
        administracion.setPoseePersonalidadCapacitadoEnSeguridadIntramuros(marcas.get(keys[2]));
        administracion.setPoseeMantenimientoProfesionalCuartoDeMaquinas(marcas.get(keys[3]));
        administracion.setPoseeWiFiAbierto(marcas.get(keys[4]));
        AccionesTablaAdministracion.agregaAdministracion(this, administracion);
    }

    private void iniciaRegistroDeUsuario() {

    }

    private void habilitaBoton(){
        findViewById(R.id.formulario_registro_administracion_boton_hecho)
                .setEnabled(true);
    }
}

package org.inspira.condominio.admon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.Configuraciones;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.adaptadores.MallaDeCheckBoxes;
import org.inspira.condominio.datos.Condominio;
import org.inspira.condominio.datos.TipoDeCondominio;
import org.inspira.condominio.dialogos.ActividadDeEspera;
import org.inspira.condominio.dialogos.ProveedorToast;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jcapiz on 16/03/16.
 */
public class NuevoCondominioActivity extends AppCompatActivity {

    private String direccion;
    private int edadCondominio;
    private String tipo;
    private String inmoviliaria;
    private String nombre;
    private Map<String, Boolean> camposOpcionales;
    private int cajonesDeEstacionamiento;
    private int cajonesDeEstacionamientoVisitas;
    private float costoPorUnidadPrivativa;
    private float capacidadDeCisterna;

    private static NuevoCondominioFragment2 f2;
    private NuevoCondominioFragment1.AccionNCondominio1 accion1 = new NuevoCondominioFragment1.AccionNCondominio1() {
        @Override
        public void siguiente(String direccion, int edad, String tipo, String inmoviliaria, String nombre) {
            NuevoCondominioActivity.this.direccion = direccion;
            NuevoCondominioActivity.this.edadCondominio = edad;
            NuevoCondominioActivity.this.tipo = tipo;
            NuevoCondominioActivity.this.inmoviliaria = inmoviliaria;
            NuevoCondominioActivity.this.nombre = nombre;
            f2 = new NuevoCondominioFragment2();
            f2.setAccion(accion2);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.preparacion_main_container, f2)
                    .addToBackStack("Dos")
                    .commit();
        }

        @Override
        public void onResume() {
            getSupportActionBar().setTitle("(1/3) Registro Condominio");
        }
    };

    private static NuevoCondominioFragment3 f3;
    private NuevoCondominioFragment2.AccionNCondominio2 accion2 = new NuevoCondominioFragment2.AccionNCondominio2() {
        @Override
        public void siguiente(Map<String, Boolean> values) {
            camposOpcionales = values;
            f3 = new NuevoCondominioFragment3();
            f3.setAccion(accion3);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.preparacion_main_container, f3)
                    .addToBackStack("Tres")
                    .commit();
        }

        @Override
        public void onResume() {
            getSupportActionBar().setTitle("(2/3) Registro Condominio");
        }
    };

    private NuevoCondominioFragment3.AccionNCondominio3 accion3 = new NuevoCondominioFragment3.AccionNCondominio3() {
        @Override
        public void hecho(int cajonesDeEstacionamiento, int cajonesDeEstacionamientoVisitas, float costoPorUnidadPrivativa, float capacidadDeCisterna) {
            NuevoCondominioActivity.this.cajonesDeEstacionamiento = cajonesDeEstacionamiento;
            NuevoCondominioActivity.this.cajonesDeEstacionamientoVisitas = cajonesDeEstacionamientoVisitas;
            NuevoCondominioActivity.this.costoPorUnidadPrivativa = costoPorUnidadPrivativa;
            NuevoCondominioActivity.this.capacidadDeCisterna = capacidadDeCisterna;
            enviaInformacionAlServidor();
        }

        @Override
        public void onResume() {
            getSupportActionBar().setTitle("(3/3) Registro Condominio");
        }
    };
    private static NuevoCondominioFragment1 f1;
    private boolean restoringActivity;

    private void enviaInformacionAlServidor() {
        startConnectorThread();
        launchWaitingActivity();
    }

    private void launchWaitingActivity() {
        startActivityForResult(new Intent(this, ActividadDeEspera.class), 1234);
    }

    private void startConnectorThread() {
        String content = armaCuerpoDeMensaje();
        if(content != null){
            Log.d("Content", "To send: " + content);
            new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                @Override
                public void resultadoSatisfactorio(final Thread t) {
                    ContactoConServidor aval = (ContactoConServidor) t;
                    final String respuesta = aval.getResponse();
                    if(CompruebaCamposJSON.validaContenido(respuesta)) {
                        guardaRegistroEnBaseDeDatos(obtenerIdCondominio(respuesta));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProveedorToast.showToast(NuevoCondominioActivity.this, "Hecho");
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                revisaCamposIncorrectos(respuesta);
                            }
                        });
                    }
                    terminaActividadDeEspera();
                }

                private int obtenerIdCondominio(String respuesta) {
                    int idCondominio = -1;
                    try{
                        JSONObject json = new JSONObject(respuesta);
                        idCondominio = json.getInt("idCondominio");
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    return idCondominio;
                }

                @Override
                public void problemasDeConexion(Thread t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ProveedorToast.showToast(NuevoCondominioActivity.this, "Servicio no disponible por el momento");
                        }
                    });
                    terminaActividadDeEspera();
                }
            }, content).start();
        }else{
            ProveedorToast
                    .showToast(this, "¿Problemas internos? Esto no tiene sentido.");
        }
    }

    private void terminaActividadDeEspera() {
        finishActivity(1234);
    }

    private void revisaCamposIncorrectos(String respuesta) {
        Map<String, Object> map = CompruebaCamposJSON.obtenerCampos(respuesta);
        Log.d("Revisor", "Tul");
    }

    private void guardaRegistroEnBaseDeDatos(int idCondominio) {
        Condominio condominio = new Condominio(idCondominio);
        condominio.setNombre(nombre);
        condominio.setCostoAproximadoPorUnidadPrivativa(costoPorUnidadPrivativa);
        TipoDeCondominio tipoDeCondominio =
                new TipoDeCondominio(AccionesTablaCondominio.obtenerIdTipoDeCondominio(this,tipo));
        tipoDeCondominio.setDescripcion(tipo);
        condominio.setTipoDeCondominio(tipoDeCondominio);
        condominio.setCantidadDeLugaresEstacionamiento(cajonesDeEstacionamiento);
        condominio.setCantidadDeLugaresEstacionamientoVisitas(cajonesDeEstacionamientoVisitas);
        condominio.setCapacidadDeCisterna(capacidadDeCisterna);
        condominio.setDireccion(direccion);
        condominio.setEdad(edadCondominio);
        condominio.setInmoviliaria(inmoviliaria);
        condominio.setPoseeSalaDeJuntas(camposOpcionales.get(MallaDeCheckBoxes.TEXTOS[0]));
        condominio.setPoseeGym(camposOpcionales.get(MallaDeCheckBoxes.TEXTOS[1]));
        condominio.setPoseeEspacioRecreativo(camposOpcionales.get(MallaDeCheckBoxes.TEXTOS[2]));
        condominio.setPoseeEspacioCultural(camposOpcionales.get(MallaDeCheckBoxes.TEXTOS[3]));
        condominio.setPoseeOficinasAdministrativas(camposOpcionales.get(MallaDeCheckBoxes.TEXTOS[4]));
        condominio.setPoseeAlarmaSismica(camposOpcionales.get(MallaDeCheckBoxes.TEXTOS[5]));
        condominio.setPoseeCisternaAguaPluvial(camposOpcionales.get(MallaDeCheckBoxes.TEXTOS[6]));
        AccionesTablaCondominio.agregarCondominio(this, condominio);
        registroDeIdCondominio(condominio.getId());
        iniciaRegistroDeAdministracion();
    }

    private void registroDeIdCondominio(int idCondominio) {
        SharedPreferences.Editor editor =
                getSharedPreferences(Configuraciones.class.getName(), Context.MODE_PRIVATE).edit();
        editor.putInt("idCondominio", idCondominio);
        editor.apply();
    }

    private void iniciaRegistroDeAdministracion() {
        /** Si se debe registrar a cada torre por separado, lanzar ese número de formularios. **/
        startActivity(new Intent(this, RegistroAdministracion.class));
        finish();
    }

    private String armaCuerpoDeMensaje() {
        String content = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.REGISTRO_DE_CONDOMINIO);
            json.put("direccion", direccion);
            json.put("edad", edadCondominio);
            json.put("tipo", tipo);
            json.put("inmoviliaria", inmoviliaria);
            json.put("nombre", nombre);
            String[] elementos = MallaDeCheckBoxes.TEXTOS;
            json.put("posee_sala_de_juntas", camposOpcionales.get(elementos[0]));
            json.put("posee_gym", camposOpcionales.get(elementos[1]));
            json.put("posee_espacio_recreativo", camposOpcionales.get(elementos[2]));
            json.put("posee_espacio_cultural", camposOpcionales.get(elementos[3]));
            json.put("posee_oficinas_administrativas", camposOpcionales.get(elementos[4]));
            json.put("posee_alarma_sismica", camposOpcionales.get(elementos[5]));
            json.put("posee_cisterna_agua_pluvial", camposOpcionales.get(elementos[6]));
            json.put("cajones_de_estacionamiento", cajonesDeEstacionamiento);
            json.put("cajones_de_estacionamiento_visitas", cajonesDeEstacionamientoVisitas);
            json.put("costo_por_unidad_privativa", costoPorUnidadPrivativa);
            json.put("capacidad_de_cisterna", capacidadDeCisterna);
            content = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        restoringActivity = true;
        if(savedInstanceState == null){
            f1 = new NuevoCondominioFragment1();
            direccion = "";
            edadCondominio = -1;
            tipo = "";
            nombre = "";
        }
        if(camposOpcionales == null)
            camposOpcionales = new TreeMap<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("direccion", direccion);
        outState.putInt("edad", edadCondominio);
        outState.putString("tipo", tipo);
        outState.putString("inmoviliaria", inmoviliaria);
        outState.putString("nombre", nombre);
        Bundle b = new Bundle();
        for(String key : camposOpcionales.keySet())
            b.putBoolean(key, camposOpcionales.get(key));
        outState.putBundle("campos_opcionales", b);
        outState.putInt("cajones_de_estacionamiento", cajonesDeEstacionamiento);
        outState.putInt("cajones_de_estacionamiento_visitas", cajonesDeEstacionamientoVisitas);
        outState.putFloat("costo_por_unidad_privativa", costoPorUnidadPrivativa);
        outState.putFloat("capacidad_de_cisterna", capacidadDeCisterna);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        Log.d("SupremeAlchemist", "--->" + savedInstanceState.getString("numTorres") + "<---");
        direccion = savedInstanceState.getString("direccion");
        edadCondominio = savedInstanceState.getInt("edad");
        tipo = savedInstanceState.getString("tipo");
        inmoviliaria = savedInstanceState.getString("inmoviliaria");
        nombre = savedInstanceState.getString("nombre");
        Bundle b = savedInstanceState.getBundle("campos_opcionales");
        camposOpcionales = new TreeMap<>();
        assert b != null;
        for(String key : b.keySet())
            camposOpcionales.put(key, b.getBoolean(key));
        cajonesDeEstacionamiento = savedInstanceState.getInt("cajones_de_estacionamiento");
        cajonesDeEstacionamientoVisitas = savedInstanceState.getInt("cajones_de_estacionamiento_visitas");
        costoPorUnidadPrivativa = savedInstanceState.getFloat("costo_por_unidad_privativa");
        capacidadDeCisterna = savedInstanceState.getFloat("capacidad_de_cisterna");
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(restoringActivity)
            colocaFragmentos();
        restoringActivity = false;
    }

    private void colocaFragmentos() {
        if( "".equals(direccion) || edadCondominio <= -1 || "".equals(tipo) || "".equals(nombre) ){
            f1.setAccion(accion1);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.preparacion_main_container, f1, "f1")
                    .commit();
        }else{
            if( camposOpcionales.size() == 0 ){
                f1.setAccion(accion1);
                f2.setAccion(accion2);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.preparacion_main_container, f1, "f1")
                        .replace(R.id.preparacion_main_container, f2, "f2")
                        .addToBackStack("tul")
                        .commit();
            }else{
                f1.setAccion(accion1);
                f2.setAccion(accion2);
                f3.setAccion(accion3);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.preparacion_main_container, f1, "f1")
                        .replace(R.id.preparacion_main_container, f2, "f2")
                        .replace(R.id.preparacion_main_container, f3, "f3")
                        .addToBackStack("tul2")
                        .commit();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        setResult(resultCode);
        finish();
    }
}

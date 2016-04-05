package org.inspira.condominio.admin.trabajadores;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import org.inspira.condominio.admon.AccionesTablaHabitante;
import org.inspira.condominio.admon.AccionesTablaTrabajador;
import org.inspira.condominio.admon.CompruebaCamposJSON;
import org.inspira.condominio.datos.ContactoHabitante;
import org.inspira.condominio.datos.ContactoTrabajador;
import org.inspira.condominio.datos.Trabajador;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.RemocionElementos;
import org.inspira.condominio.dialogos.TomarNombrePersona;
import org.inspira.condominio.fragmentos.OrdenDelDia;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 4/04/16.
 */
public class DetallesTrabajador extends AppCompatActivity implements ColocaValorDesdeDialogo.FormatoDeMensaje,
        AccionCheckBox.ActualizacionDeCampo{

    private TextView nombreTrabajador;
    private CheckBox poseeSeguro;
    private Trabajador trabajador;
    private TextView tipoDeTrabajador;
    private LinearLayout contenedorContactos;
    private int[] idContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_detalles_trabajador);
        Bundle extras = savedInstanceState == null ? getIntent().getExtras() : savedInstanceState;
        trabajador = (Trabajador)extras.getSerializable("trabajador");
        ImageView arrow = (ImageView) findViewById(R.id.detalles_trabajador_back_arrow);
        assert arrow != null;
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView image = (ImageView) findViewById(R.id.detalles_trabajador_profile_picture);
        assert image != null;
        image.setImageResource(trabajador.isGenero() ? R.drawable.user_coin : R.drawable.woman_coin_2);
        nombreTrabajador = (TextView) findViewById(R.id.detalles_trabajador_nombre);
        String nombreCompleto = trabajador.getApPaterno() + " " + trabajador.getApMaterno() + " "
                + trabajador.getNombres();
        assert nombreTrabajador != null;
        nombreTrabajador.setText(nombreCompleto);
        tipoDeTrabajador = (TextView) findViewById(R.id.detalles_trabajador_tipo_trabajador);
        tipoDeTrabajador.setText(AccionesTablaTrabajador.obtenerTipoDeTrabajador(this, trabajador.getIdTipoDeTrabajador()));
        tipoDeTrabajador.setOnClickListener(new ActualizaEntradaDesdeArreglo(this, this, R.array.tipos_de_trabajadores, "idTipo_de_Trabajador", this));
        nombreTrabajador
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();
                        args.putSerializable("persona", trabajador);
                        TomarNombrePersona.obtenerTomarNombre(args)
                                .show(getSupportFragmentManager(), "Tomar nombre");
                    }
                });
        poseeSeguro = (CheckBox) findViewById(R.id.detalles_trabajador_posee_seguro);
        assert poseeSeguro != null;
        poseeSeguro.setChecked(trabajador.isPoseeSeguroSocial());
        poseeSeguro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                actualizaEstadoDeSeguroDeTrabajador(isChecked);
            }
        });
        ContactoTrabajador[] contactos = AccionesTablaTrabajador.obtenerListaDeContactos(this, trabajador.getId());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contenedorContactos = (LinearLayout) findViewById(R.id.detalles_trabajador_contenedor_de_contacto);
        TextView nuevoTexto;
        Bundle args = new Bundle();
        args.putInt("action", ProveedorDeRecursos.ACTUALIZACION_DE_CONTACTO);
        args.putString("table", "Contacto_Trabajador");
        args.putString("column", "contacto");
        idContactos = new int[contactos.length];
        int i=0;
        for(ContactoTrabajador contacto : contactos){
            args.putInt("pk_value", contacto.getId());
            nuevoTexto = (TextView) inflater.inflate(R.layout.entrada_simple_de_texto, contenedorContactos, false);
            nuevoTexto.setText(contacto.getContacto());
            nuevoTexto.setOnClickListener(ActualizarCampoDeContacto.crearActualizarCampoDeContacto(this, args));
            idContactos[i] = i++;
            contenedorContactos.addView(nuevoTexto);
        }
        View etiquetaContacto = findViewById(R.id.detalles_habitante_etiqueta_contacto);
        etiquetaContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzaDialogoMultivalor();
            }
        });
        etiquetaContacto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                lanzaDialogoRemocionDeContactos();
                return true;
            }
        });
    }

    private void lanzaDialogoRemocionDeContactos() {
        RemocionElementos rm = new RemocionElementos();
        final ContactoTrabajador[] contactos = AccionesTablaTrabajador.obtenerListaDeContactos(this, trabajador.getId());
        String[] elementos = new String[contactos.length];
        int i=0;
        for(ContactoTrabajador contacto : contactos)
            elementos[i++] = contacto.getContacto();
        Bundle args = new Bundle();
        args.putStringArray("elementos", elementos);
        rm.setArguments(args);
        rm.setAd(new EntradaTexto.AccionDialogo() {
            @Override
            public void accionPositiva(DialogFragment fragment) {
                final Integer[] seleccion = ((RemocionElementos) fragment).getElementosSeleccionados();
                OrdenDelDia.prepareElements(seleccion);
                String contenidoDeMensaje = armarContenidoDeMensaje(seleccion);
                ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                    @Override
                    public void resultadoSatisfactorio(Thread t) {
                        String respuesta = ((ContactoConServidor) t).getResponse();
                        if (CompruebaCamposJSON.validaContenido(respuesta)) {
                            eliminarElementosEnBaseDeDatos();
                            MuestraMensajeDesdeHilo.muestraMensaje(DetallesTrabajador.this, contenedorContactos, "Hecho");
                        } else
                            MuestraMensajeDesdeHilo.muestraMensaje(DetallesTrabajador.this, contenedorContactos, "Servicio por el momento no disponible");
                    }

                    private void eliminarElementosEnBaseDeDatos() {
                        AccionesTablaAdministracion.eliminarContactos(DetallesTrabajador.this, seleccion);
                        quitarElementosDeLaLista(seleccion);
                    }

                    @Override
                    public void problemasDeConexion(Thread t) {
                        MuestraMensajeDesdeHilo.muestraMensaje(DetallesTrabajador.this, contenedorContactos, "Servicio momentaneamente inalcanzable");
                    }
                }, contenidoDeMensaje);
                contacto.start();
            }

            private String armarContenidoDeMensaje(Integer[] seleccion) {
                String contenidoDeMensaje = null;
                try {
                    JSONObject json = new JSONObject();
                    json.put("action", ProveedorDeRecursos.REMOCION_DE_CONTACTOS);
                    json.put("table", "Contacto_Habitante");
                    JSONArray array = new JSONArray();
                    for (Integer index : seleccion)
                        array.put(contactos[index].getId());
                    json.put("elementos", array);
                    contenidoDeMensaje = json.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return contenidoDeMensaje;
            }

            @Override
            public void accionNegativa(DialogFragment fragment) {
                MuestraMensajeDesdeHilo.muestraMensaje(DetallesTrabajador.this, contenedorContactos, "Servicio temporalmente inalcanzable");
            }
        });
        rm.show(getSupportFragmentManager(), "Eliminar Contactos");
    }

    private void quitarElementosDeLaLista(final Integer[] seleccion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Integer index : seleccion)
                    contenedorContactos.removeView(contenedorContactos.getChildAt(index));
            }
        });
    }

    private void lanzaDialogoMultivalor() {
        Bundle args = new Bundle();
        args.putInt(InsertarElementoMultivalor.ACCION, ProveedorDeRecursos.REGISTRO_CONTACTO);
        args.putString(InsertarElementoMultivalor.TITULO, "E-mail, facebook, twitter, tel, etc.");
        args.putString(InsertarElementoMultivalor.TABLE_NAME, "Contacto_Trabajador");
        args.putString(InsertarElementoMultivalor.COLUMN_NAME, "contacto");
        args.putString(InsertarElementoMultivalor.FK_NAME, "idTrabajador");
        args.putInt(InsertarElementoMultivalor.FK_VALUE, trabajador.getId());
        args.putInt(InsertarElementoMultivalor.INPUT_TYPE, InputType.TYPE_CLASS_TEXT);
        args.putInt(InsertarElementoMultivalor.RECURSO_DE_CONTENEDOR, R.id.detalles_trabajador_contenedor_de_contacto);
        InsertarElementoMultivalor dialogo = InsertarElementoMultivalor.crearDialogo(args);
        dialogo.show(getSupportFragmentManager(), "Insertar 1 valor");
    }

    private void actualizaEstadoDeSeguroDeTrabajador(final boolean asegurado) {
        try{
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.ACTUALIZACION_DE_CONTACTO);
            json.put("table", "Trabajador");
            json.put("column", "posee_seguro_social");
            json.put("value", poseeSeguro.isChecked() ? 1 : 0);
            json.put("pk_value", trabajador.getId());
            new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                @Override
                public void resultadoSatisfactorio(Thread t) {
                    String resultado = ((ContactoConServidor)t).getResponse();
                    if(CompruebaCamposJSON.validaContenido(resultado)){
                        AccionesTablaTrabajador.actualizacionDeCampo(DetallesTrabajador.this, "posee_seguro_social", asegurado ? "1" : "0", trabajador.getId());
                        MuestraMensajeDesdeHilo
                                .muestraMensaje(DetallesTrabajador.this, nombreTrabajador, "Hecho");
                    }else{
                        MuestraMensajeDesdeHilo
                                .muestraMensaje(DetallesTrabajador.this, nombreTrabajador, "Servicio por el momento no disponible");
                    }
                }

                @Override
                public void problemasDeConexion(Thread t) {
                    MuestraMensajeDesdeHilo
                            .muestraMensaje(DetallesTrabajador.this, nombreTrabajador, "Servicio platónicamente alcanzable por el momento");
                }
            }, json.toString()).start();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void actualizaCampo(String key, String value) {
        AccionesTablaTrabajador.actualizacionDeCampo(this, key, value, trabajador.getId());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public int obtenerId(String texto) {
        return trabajador.getId();
    }

    @Override
    public String armarContenidoDeMensaje(String key, String value) {
        String contenidoDeMensaje = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.ACTUALIZACION_DE_TRABAJADOR);
            json.put("idTrabajador", trabajador.getId());
            json.put("key", key);
            json.put("value", value);
            contenidoDeMensaje = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return contenidoDeMensaje;
    }

    public void actualizarNombre(final String nuevoNombre){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nombreTrabajador.setText(nuevoNombre);
            }
        });
    }
}

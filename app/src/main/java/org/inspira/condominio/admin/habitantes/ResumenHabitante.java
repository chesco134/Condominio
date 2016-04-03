package org.inspira.condominio.admin.habitantes;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.AccionCheckBox;
import org.inspira.condominio.actividades.ActualizaEntradaDesdeArreglo;
import org.inspira.condominio.actividades.ActualizarCampoDeContacto;
import org.inspira.condominio.actividades.ColocaValorDesdeDialogo;
import org.inspira.condominio.actividades.InsertarElementoMultivalor;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admin.condominio.EstadoDeCondominio;
import org.inspira.condominio.admon.AccionesTablaAdministracion;
import org.inspira.condominio.admon.AccionesTablaHabitante;
import org.inspira.condominio.admon.AccionesTablaTorre;
import org.inspira.condominio.admon.CompruebaCamposJSON;
import org.inspira.condominio.datos.ContactoHabitante;
import org.inspira.condominio.datos.Habitante;
import org.inspira.condominio.datos.PropietarioDeDepartamento;
import org.inspira.condominio.datos.Torre;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.dialogos.RemocionElementos;
import org.inspira.condominio.dialogos.TomarNombreHabitante;
import org.inspira.condominio.fragmentos.OrdenDelDia;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 1/04/16.
 */
public class ResumenHabitante extends AppCompatActivity implements ColocaValorDesdeDialogo.FormatoDeMensaje, AccionCheckBox.ActualizacionDeCampo {

    private Habitante habitante;
    private LinearLayout contenedorContactos;
    private int[] idContactos;
    private TextView nombreHabitante;
    private CheckBox poseeSeguro;
    private CheckBox marcaEsPropietario;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_detalles_habitante);
        ImageView arrow = (ImageView) findViewById(R.id.detalles_habitante_back_arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(savedInstanceState == null)
            habitante = (Habitante) getIntent().getSerializableExtra("habitante");
        else
            habitante = AccionesTablaHabitante.obtenerHabitante(this, savedInstanceState.getInt("idHabitante"));
        nombreHabitante = (TextView) findViewById(R.id.detalles_habitante_nombre);
        String nombreCompleto = habitante.getApPaterno() + " " + habitante.getApMaterno() + " "
                + habitante.getNombres();
        nombreHabitante.setText(nombreCompleto);
        TextView torre = (TextView) findViewById(R.id.detalles_habitante_torre);
        torre.setText(AccionesTablaTorre.obtenerTorre(this, habitante.getIdTorre()).getNombre());
        Torre[] torres = AccionesTablaTorre.obtenerTorres(this, ProveedorDeRecursos.obtenerIdAdministracion(this));
        String[] elementos = new String[torres.length];
        int i = 0;
        for(Torre t : torres)
            elementos[i++] = t.getNombre();
        torre.setOnClickListener(
                new ActualizaEntradaDesdeArreglo(this, new ColocaValorDesdeDialogo.FormatoDeMensaje() {
                    @Override
                    public String armarContenidoDeMensaje(String key, String value) {
                        String contenidoDeMensaje = null;
                        try{
                            JSONObject json = new JSONObject();
                            json.put("action", ProveedorDeRecursos.ACTUALIZAR_DATOS_HABITANTE);
                            json.put("idTorre", habitante.getId());
                            json.put("key", key);
                            json.put("value", value);
                            contenidoDeMensaje = json.toString();
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        return contenidoDeMensaje;
                    }
                }, elementos, "idTorre", this));
        TextView nombreDepartamento = (TextView) findViewById(R.id.detalles_habitante_nombre_departamento);
        nombreDepartamento.setText(habitante.getNombreDepartamento());
        ImageView image = (ImageView) findViewById(R.id.detalles_habitante_profile_picture);
        image.setImageResource(habitante.isGenero() ? R.drawable.user_coin : R.drawable.woman_coin_2);
        boolean esPropietario = AccionesTablaHabitante.esPropietario(this, habitante.getId());
        marcaEsPropietario = (CheckBox) findViewById(R.id.detalles_habitante_es_propietario);
        poseeSeguro = (CheckBox) findViewById(R.id.detalles_habitante_posee_seguro);
        marcaEsPropietario.setChecked(esPropietario);
        if(marcaEsPropietario.isChecked())
            poseeSeguro.setChecked(AccionesTablaHabitante.poseeSeguro(this, habitante.getId()));
        poseeSeguro.setEnabled(marcaEsPropietario.isChecked());
        marcaEsPropietario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                poseeSeguro.setEnabled(isChecked);
                if (!isChecked)
                    quitarHabitanteDePropietarios();
                else
                    agregaHabitantePropietarios();
            }
        });
        poseeSeguro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                actualizaEstadoDeSeguroDePropietario(isChecked);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProveedorSnackBar
                        .muestraBarraDeBocados(v, "Uy que me haces cosquillas! :D");
            }
        });
        RelativeLayout contenedorNombreDepartamento = (RelativeLayout) findViewById(R.id.detalles_habitante_contenedor_departamento);
        contenedorNombreDepartamento
                .setOnClickListener(new ColocaValorDesdeDialogo(this, R.id.detalles_habitante_etiqueta_nombre_departamento, R.id.detalles_habitante_nombre_departamento, EstadoDeCondominio.TIPO_NUMERICO, "nombre_departamento", this, this));
        nombreHabitante
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();
                        args.putSerializable("habitante", habitante);
                        TomarNombreHabitante.obtenerTomarNombreHabitante(args)
                                .show(getSupportFragmentManager(), "Tomar nombre");
                    }
                });
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
        ContactoHabitante[] contactos = AccionesTablaHabitante.obtenerListaDeContactos(this, habitante.getId());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contenedorContactos = (LinearLayout) findViewById(R.id.detalles_habitante_contenedor_de_contacto);
        TextView nuevoTexto;
        Bundle args = new Bundle();
        args.putInt("action", ProveedorDeRecursos.ACTUALIZACION_DE_CONTACTO);
        args.putString("table", "Contacto_Habitante");
        args.putString("column", "contacto");
        idContactos = new int[contactos.length];
        i=0;
        for(ContactoHabitante contacto : contactos){
            args.putInt("pk_value", contacto.getId());
            nuevoTexto = (TextView) inflater.inflate(R.layout.entrada_simple_de_texto, contenedorContactos, false);
            nuevoTexto.setText(contacto.getContacto());
            nuevoTexto.setOnClickListener(ActualizarCampoDeContacto.crearActualizarCampoDeContacto(this, args));
            idContactos[i] = i++;
            contenedorContactos.addView(nuevoTexto);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putInt("idHabitante", habitante.getId());
    }

    private void agregaHabitantePropietarios() {
        try{
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.REGISTRO_DE_PROPIETARIO);
            json.put("idHabitante", habitante.getId());
            json.put("posee_seguro", poseeSeguro.isChecked() ? 1 : 0);
            final PropietarioDeDepartamento propietarioDeDepartamento = new PropietarioDeDepartamento();
            propietarioDeDepartamento.setIdHabitante(habitante.getId());
            propietarioDeDepartamento.setPoseeSeguro(poseeSeguro.isChecked());
            new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                @Override
                public void resultadoSatisfactorio(Thread t) {
                    String resultado = ((ContactoConServidor)t).getResponse();
                    if(CompruebaCamposJSON.validaContenido(resultado)){
                        AccionesTablaHabitante.agregarHabitantePropietario(ResumenHabitante.this, propietarioDeDepartamento);
                        MuestraMensajeDesdeHilo
                                .muestraMensaje(ResumenHabitante.this, nombreHabitante, "Hecho");
                    }else{
                        MuestraMensajeDesdeHilo
                                .muestraMensaje(ResumenHabitante.this, nombreHabitante, "Servicio por el momento no disponible");
                    }
                }

                @Override
                public void problemasDeConexion(Thread t) {
                    MuestraMensajeDesdeHilo
                            .muestraMensaje(ResumenHabitante.this, nombreHabitante, "Servicio platónicamente alcanzable por el momento");
                }
            }, json.toString()).start();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void actualizaEstadoDeSeguroDePropietario(final boolean isChecked) {
        try{
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.ACTUALIZACION_DE_PROPIETARIO_DE_DEPARTAMENTO);
            json.put("posee_seguro", poseeSeguro.isChecked() ? 1 : 0);
            json.put("idHabitante", habitante.getId());
            new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                @Override
                public void resultadoSatisfactorio(Thread t) {
                    String resultado = ((ContactoConServidor)t).getResponse();
                    if(CompruebaCamposJSON.validaContenido(resultado)){
                        AccionesTablaHabitante.actualizaEstadoDeSeguroDePropietario(ResumenHabitante.this, isChecked, habitante.getId());
                        MuestraMensajeDesdeHilo
                                .muestraMensaje(ResumenHabitante.this, nombreHabitante, "Hecho");
                    }else{
                        MuestraMensajeDesdeHilo
                                .muestraMensaje(ResumenHabitante.this, nombreHabitante, "Servicio por el momento no disponible");
                    }
                }

                @Override
                public void problemasDeConexion(Thread t) {
                    MuestraMensajeDesdeHilo
                            .muestraMensaje(ResumenHabitante.this, nombreHabitante, "Servicio platónicamente alcanzable por el momento");
                }
            }, json.toString()).start();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void quitarHabitanteDePropietarios() {
        try{
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.REMOCION_DE_PROPIETARIO_DE_DEPARTAMENTO);
            json.put("idHabitante", habitante.getId());
            new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
                @Override
                public void resultadoSatisfactorio(Thread t) {
                    String resultado = ((ContactoConServidor)t).getResponse();
                    if(CompruebaCamposJSON.validaContenido(resultado)){
                        AccionesTablaHabitante.quitaPropietarioDeDepartamento(ResumenHabitante.this, habitante.getId());
                        MuestraMensajeDesdeHilo
                                .muestraMensaje(ResumenHabitante.this, nombreHabitante, "Hecho");
                    }else{
                        MuestraMensajeDesdeHilo
                                .muestraMensaje(ResumenHabitante.this, nombreHabitante, "Servicio por el momento no disponible");
                    }
                }

                @Override
                public void problemasDeConexion(Thread t) {
                    MuestraMensajeDesdeHilo
                            .muestraMensaje(ResumenHabitante.this, nombreHabitante, "Servicio platónicamente alcanzable por el momento");
                }
            }, json.toString()).start();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void actualizaCampo(String key, String value) {
        AccionesTablaHabitante.actualizaCampo(this, habitante.getId(), key, value);
    }

    @Override
    public int obtenerId(String texto) {
        return AccionesTablaTorre.obtenerIdTorre(this, texto);
    }

    @Override
    public String armarContenidoDeMensaje(String key, String value) {
        String contenidoDeMensaje = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action", ProveedorDeRecursos.ACTUALIZAR_DATOS_HABITANTE);
            json.put("idHabitante", habitante.getId());
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
                nombreHabitante.setText(nuevoNombre);
            }
        });
    }

    private void lanzaDialogoMultivalor() {
        Bundle args = new Bundle();
        args.putInt(InsertarElementoMultivalor.ACCION, ProveedorDeRecursos.REGISTRO_CONTACTO);
        args.putString(InsertarElementoMultivalor.TITULO, "E-mail, facebook, twitter, tel, etc.");
        args.putString(InsertarElementoMultivalor.TABLE_NAME, "Contacto_Habitante");
        args.putString(InsertarElementoMultivalor.COLUMN_NAME, "contacto");
        args.putString(InsertarElementoMultivalor.FK_NAME, "idHabitante");
        args.putInt(InsertarElementoMultivalor.FK_VALUE, habitante.getId());
        args.putInt(InsertarElementoMultivalor.INPUT_TYPE, InputType.TYPE_CLASS_TEXT);
        args.putInt(InsertarElementoMultivalor.RECURSO_DE_CONTENEDOR, R.id.detalles_habitante_contenedor_de_contacto);
        InsertarElementoMultivalor dialogo = InsertarElementoMultivalor.crearDialogo(args);
        dialogo.show(getSupportFragmentManager(), "Insertar 1 valor");
    }

    private void lanzaDialogoRemocionDeContactos() {
        RemocionElementos rm = new RemocionElementos();
        final ContactoHabitante[] contactos = AccionesTablaHabitante.obtenerListaDeContactos(this, habitante.getId());
        String[] elementos = new String[contactos.length];
        int i=0;
        for(ContactoHabitante contacto : contactos)
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
                            MuestraMensajeDesdeHilo.muestraMensaje(ResumenHabitante.this, contenedorContactos, "Hecho");
                        } else
                            MuestraMensajeDesdeHilo.muestraMensaje(ResumenHabitante.this, contenedorContactos, "Servicio por el momento no disponible");
                    }

                    private void eliminarElementosEnBaseDeDatos() {
                        AccionesTablaAdministracion.eliminarContactos(ResumenHabitante.this, seleccion);
                        quitarElementosDeLaLista(seleccion);
                    }

                    @Override
                    public void problemasDeConexion(Thread t) {
                        MuestraMensajeDesdeHilo.muestraMensaje(ResumenHabitante.this, contenedorContactos, "Servicio momentaneamente inalcanzable");
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
                MuestraMensajeDesdeHilo.muestraMensaje(ResumenHabitante.this, contenedorContactos, "Servicio temporalmente inalcanzable");
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
}

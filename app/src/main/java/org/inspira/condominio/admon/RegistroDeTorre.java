package org.inspira.condominio.admon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.EditText;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.EfectoDeEnfoque;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.actividades.Verificador;
import org.inspira.condominio.datos.Torre;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jcapiz on 18/03/16.
 */
public class RegistroDeTorre extends AppCompatActivity {

    private EditText nombre;
    private EditText numeroDePisos;
    private EditText numeroDeFocos;
    private EditText numeroDeDepartamentos;
    private SwitchCompat cuentaConElevador;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_torre);
        nombre = (EditText) findViewById(R.id.formulario_torre_nombre);
        numeroDePisos = (EditText) findViewById(R.id.formulario_torre_cantidad_de_pisos);
        numeroDeFocos = (EditText) findViewById(R.id.formulario_torre_cantidad_de_focos);
        numeroDeDepartamentos = (EditText) findViewById(R.id.formulario_torre_cantidad_de_departamentos);
        cuentaConElevador = (SwitchCompat) findViewById(R.id.formulario_torre_posee_elevador);
        nombre.setOnFocusChangeListener(new EfectoDeEnfoque(this, findViewById(R.id.formato_de_egreso_piso_nombre)));
        numeroDePisos
                .setOnFocusChangeListener(new EfectoDeEnfoque(this, findViewById(R.id.formulario_torre_piso_cantidad_de_pisos)));
        numeroDeFocos
                .setOnFocusChangeListener(new EfectoDeEnfoque(this, findViewById(R.id.formulario_torre_piso_cantidad_de_focos)));
        numeroDeDepartamentos
                .setOnFocusChangeListener(new EfectoDeEnfoque(this, findViewById(R.id.formulario_torre_piso_cantidad_de_departamentos)));
        findViewById(R.id.formulario_torre_hecho)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!validaCampos()) {
                            ProveedorSnackBar
                                    .muestraBarraDeBocados(nombre, "Verifique los campos marcados");
                        } else {
                            correcto();
                        }
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("nombre", nombre.getText().toString().trim());
        outState.putString("numero_de_pisos", numeroDePisos.getText().toString().trim());
        outState.putString("numero_de_departamentos", numeroDeDepartamentos.getText().toString().trim());
        outState.putString("numero_de_focos", numeroDeFocos.getText().toString().trim());
        outState.putBoolean("cuenta_con_elevador", cuentaConElevador.isChecked());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        nombre.setText(savedInstanceState.getString("nombre"));
        numeroDePisos.setText(savedInstanceState.getString("numero_de_pisos"));
        numeroDeDepartamentos.setText(savedInstanceState.getString("numero_de_departamentos"));
        numeroDeFocos.setText(savedInstanceState.getString("numero_de_focos"));
        cuentaConElevador.setChecked(savedInstanceState.getBoolean("cuenta_con_elevador"));
    }

    private void correcto() {
        String cuerpoDeMensaje = armarCuerpoDeMensaje();
        validaInformacionEnServidor(cuerpoDeMensaje);
    }

    private void validaInformacionEnServidor(String cuerpoDeMensaje) {
        ContactoConServidor contacto = new ContactoConServidor(new InteraccionConServidor(), cuerpoDeMensaje);
        contacto.start();
    }

    private String armarCuerpoDeMensaje() {
        String cuerpoDeMensaje = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action", 13);
            json.put("nombre", nombre.getText().toString().trim());
            json.put("posee_elevador", cuentaConElevador.isChecked());
            json.put("cantidad_de_pisos", Integer.parseInt(numeroDePisos.getText().toString().trim()));
            json.put("cantidad_de_focos", Integer.parseInt(numeroDeFocos.getText().toString().trim()));
            json.put("cantidad_de_departamentos", Integer.parseInt(numeroDeDepartamentos.getText().toString().trim()));
            json.put("idCondominio", ProveedorDeRecursos.obtenerIdCondominio(this));
            cuerpoDeMensaje = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return cuerpoDeMensaje;
    }

    private Torre guardarCamposEnBaseDeDatos() {
        Torre torre = new Torre();
        torre.setCondominio(AccionesTablaCondominio.obtenerCondominio(this, ProveedorDeRecursos.obtenerIdCondominio(this)));
        torre.setPoseeElevador(cuentaConElevador.isChecked());
        torre.setNombre(nombre.getText().toString().trim());
        torre.setCantidadDePisos(Integer.parseInt(numeroDePisos.getText().toString().trim()));
        torre.setCantidadDeDepartamentos(Integer.parseInt(numeroDeDepartamentos.getText().toString().trim()));
        torre.setCantidadDeFocos(Integer.parseInt(numeroDeFocos.getText().toString().trim()));
        torre.setId(AccionesTablaTorre.agregarTorre(this, torre));
        return torre;
    }

    private boolean validaCampos(){
        boolean[] validezCampos = new boolean[4];
        validezCampos[0] = Verificador.marcaCampo(this, nombre, Verificador.TEXTO);
        validezCampos[1] = Verificador.marcaCampo(this, numeroDePisos, Verificador.ENTERO);
        validezCampos[2] = Verificador.marcaCampo(this, numeroDeFocos, Verificador.ENTERO);
        validezCampos[3] = Verificador.marcaCampo(this, numeroDeDepartamentos, Verificador.ENTERO);
        return validezCampos[0] && validezCampos[1] && validezCampos[2] && validezCampos[3];
    }

    private class InteraccionConServidor implements ContactoConServidor.AccionesDeValidacionConServidor {
        @Override
        public void resultadoSatisfactorio(Thread t) {
            guardarCamposEnBaseDeDatos();
            iniciaRegistroDeAdministracion();
        }

        @Override
        public void problemasDeConexion(Thread t) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProveedorSnackBar
                            .muestraBarraDeBocados(nombre, "Servicio termporalmente no disponible");
                }
            });
        }
    }

    private void iniciaRegistroDeAdministracion() {
        startActivity(new Intent(this, RegistroAdministracion.class));
    }
}

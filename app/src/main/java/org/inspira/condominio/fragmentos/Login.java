package org.inspira.condominio.fragmentos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admin.CentralPoint;
import org.inspira.condominio.admon.AccionesTablaAdministracion;
import org.inspira.condominio.admon.AccionesTablaCondominio;
import org.inspira.condominio.admon.AccionesTablaHabitante;
import org.inspira.condominio.admon.AccionesTablaTorre;
import org.inspira.condominio.admon.AccionesTablaTrabajador;
import org.inspira.condominio.admon.AccionesTablaUsuario;
import org.inspira.condominio.datos.Administracion;
import org.inspira.condominio.datos.Condominio;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.datos.Habitante;
import org.inspira.condominio.datos.PuntoOdD;
import org.inspira.condominio.datos.Torre;
import org.inspira.condominio.datos.Trabajador;
import org.inspira.condominio.dialogos.ActividadDeEspera;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.networking.LoginConnection;
import org.inspira.condominio.networking.ObtencionDeConvocatorias;
import org.inspira.condominio.seguridad.Hasher;
import org.inspira.condominio.datos.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Login extends Fragment {

    EditText user, password;
    Button start;
    String userString, pwString;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login, parent, false);

        user = (EditText)rootView.findViewById(R.id.usuario);
        password = (EditText)rootView.findViewById(R.id.pw);
        start = (Button)rootView.findViewById(R.id.iniciar);
        password.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf"));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    userString = user.getText().toString().trim();
                    pwString = password.getText().toString().trim();
                    if (!"".equals(userString) && !"".equals(pwString)) {
                        ((AppCompatActivity)getContext()).startActivityForResult(new Intent(getContext(), ActividadDeEspera.class), 1234);
                        LoginConnection lc = new LoginConnection(new LoginConnection.OnConnectionAction() {
                            @Override
                            public void validationSucceded(JSONObject json) {
                                obtencionDeConvocatorias(json);
                            }

                            @Override
                            public void validationError() {
                                cleanPass();
                            }
                        });
                        lc.execute(userString, new Hasher().makeHashString(pwString));
                    } else {
                        ProveedorSnackBar
                                .muestraBarraDeBocados(user, "Debe llenar los campos :)");
                    }
                } else {
                    ProveedorSnackBar
                            .muestraBarraDeBocados(user, "Problemas de conexión :(");
                }
            }
        });
        rootView.findViewById(R.id.registrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.preparacion_main_container, new BuscarCondominio())
                        .addToBackStack("login")
                        .commit();
            }
        });
        if(savedInstanceState != null){
            pwString = savedInstanceState.getString("pass");
            userString = savedInstanceState.getString("user");
            password.setText(pwString);
            user.setText(userString);
        }
        return rootView;
    }

    private void obtencionDeConvocatorias(JSONObject json) {
        try {
            ObtencionDeConvocatorias obt = new ObtencionDeConvocatorias(json.getString("email"));
            obt.setAcciones(new RespuestaObtencionDeConvocatorias(json));
            obt.start();
        }catch(JSONException ignore){}
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("pass", pwString);
        outState.putString("user", userString);
    }

    @Override
    public void onResume(){
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Iniciar sesión");
    }

    private void setCondominio(JSONObject json){
        try{
            JSONObject jcondominio = json.getJSONObject("Condominio");
            Condominio condominio = new Condominio(jcondominio.getInt("idCondominio"));
            condominio.setDireccion(jcondominio.getString("direccion"));
            condominio.setEdad(jcondominio.getInt("edad"));
            condominio.setCantidadDeLugaresEstacionamiento(jcondominio.getInt("cantidad_de_lugares_estacionamiento"));
            condominio.setCantidadDeLugaresEstacionamientoVisitas(jcondominio.getInt("cantidad_de_lugares_estacionamiento_visitas"));
            condominio.setCapacidadDeCisterna((float) jcondominio.getDouble("capacidad_cisterna"));
            condominio.setCostoAproximadoPorUnidadPrivativa((float) jcondominio.getDouble("costo_aproximado"));
            condominio.setInmoviliaria(jcondominio.getString("inmoviliaria"));
            condominio.setNombre(jcondominio.getString("nombre"));
            condominio.setTipoDeCondominio(AccionesTablaCondominio.obtenerTipoDeCondominio(getContext(), jcondominio.getInt("idTipo_de_Condominio")));
            condominio.setPoseeAlarmaSismica(jcondominio.getInt("posee_alarma_sismica") != 0);
            condominio.setPoseeCisternaAguaPluvial(jcondominio.getInt("posee_cisterna_agua_pluvial") != 0);
            condominio.setPoseeEspacioCultural(jcondominio.getInt("posee_espacio_cultural") != 0);
            condominio.setPoseeEspacioRecreativo(jcondominio.getInt("posee_espacio_recreativo") != 0);
            condominio.setPoseeGym(jcondominio.getInt("posee_gym") != 0);
            condominio.setPoseeOficinasAdministrativas(jcondominio.getInt("posee_oficinas_administrativas") != 0);
            condominio.setPoseeSalaDeJuntas(jcondominio.getInt("posee_sala_de_juntas") != 0);
            AccionesTablaCondominio.agregarCondominio(getContext(), condominio);
            ProveedorDeRecursos.guardaRecursoInt(getContext(), "idCondominio",condominio.getId());
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void setAdministracion(JSONObject json){
        try{
            JSONObject jadministracion = json.getJSONObject("Administracion");
            Administracion administracion = new Administracion(jadministracion.getInt("idAdministracion"));
            administracion.setCostoDeCuotaAnual((float) jadministracion.getDouble("costo_de_cuota_anual"));
            administracion.setCostoDeCuotaDeMantenimientoMensual((float) jadministracion.getDouble("costo_de_cuota_de_mantenimiento_mensual"));
            administracion.setIdCondominio(jadministracion.getInt("idCondominio"));
            administracion.setIntervaloDeTransparencia(AccionesTablaAdministracion.obtenerIntervaloDeTransparencia(getContext(), jadministracion.getInt("idIntervalo_Transparencia")));
            administracion.setPoseeMantenimientoProfesionalCuartoDeMaquinas(jadministracion.getInt("posee_mantenimiento_profesional_al_cuarto_de_maquinas") != 0);
            administracion.setPoseeMantenimientoProfesionalElevadores(jadministracion.getInt("posee_mantenimiento_profesional_a_elevadores") != 0);
            administracion.setPoseePersonalidadCapacitadoEnSeguridadIntramuros(jadministracion.getInt("posee_personal_capacitado_en_seguridad_intramuros") != 0);
            administracion.setPoseePlanesDeTrabajo(jadministracion.getInt("posee_planes_de_trabajo") != 0);
            administracion.setPoseeWiFiAbierto(jadministracion.getInt("posee_wifi_abierto") != 0);
            administracion.setPromedioInicialDeEgresos((float) jadministracion.getDouble("promedio_inicial_de_egresos"));
            administracion.setPromedioInicialDeMorosidad((float) jadministracion.getDouble("promedio_de_morosidad"));
            AccionesTablaAdministracion.agregaAdministracion(getContext(), administracion);
            ProveedorDeRecursos.guardaRecursoInt(getContext(), "idAdministracion", administracion.getId());
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void setTorresInfo(JSONObject json){
        try{
            JSONArray torres = json.getJSONArray("Torres");
            JSONObject jtorre;
            Torre torre;
            JSONArray habitantes;
            JSONObject jhabitante;
            Habitante habitante;
            for(int i=0; i<torres.length(); i++){
                jtorre = torres.getJSONObject(i);
                torre = new Torre(jtorre.getInt("idTorre"));
                torre.setNombre(jtorre.getString("nombre"));
                torre.setPoseeElevador(jtorre.getInt("posee_elevador") != 0);
                torre.setCantidadDePisos(jtorre.getInt("cantidad_de_pisos"));
                torre.setCantidadDeFocos(jtorre.getInt("cantidad_de_focos"));
                torre.setCantidadDeDepartamentos(jtorre.getInt("cantidad_de_departamentos"));
                torre.setIdAdministracion(jtorre.getInt("idAdministracion"));
                if(i==0)
                    ProveedorDeRecursos.guardaRecursoInt(getContext(), "idTorre", torre.getId());
                AccionesTablaTorre.agregarTorre(getContext(), torre);
                habitantes = jtorre.getJSONArray("Habitantes");
                for(int j=0; j<habitantes.length(); j++){
                    jhabitante = habitantes.getJSONObject(j);
                    habitante = new Habitante(jhabitante.getInt("idHabitante"));
                    habitante.setNombres(jhabitante.getString("nombres"));
                    habitante.setApPaterno(jhabitante.getString("ap_paterno"));
                    habitante.setApMaterno(jhabitante.getString("ap_materno"));
                    habitante.setGenero(jhabitante.getInt("genero") == 1);
                    habitante.setNombreDepartamento(jhabitante.getString("nombre_departamento"));
                    habitante.setIdTorre(torre.getId());
                    AccionesTablaHabitante.agregarHabitante(getContext(), habitante);
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void setTrabajadoresInfo(JSONObject json){
        try{
            JSONArray trabajadores = json.getJSONArray("Trabajadores");
            Trabajador trabajador;
            JSONObject jtrabajador;
            for(int i=0; i<trabajadores.length(); i++){
                jtrabajador = trabajadores.getJSONObject(i);
                trabajador = new Trabajador(jtrabajador.getInt("idTrabajador"));
                trabajador.setNombres(jtrabajador.getString("nombres"));
                trabajador.setApPaterno(jtrabajador.getString("ap_paterno"));
                trabajador.setApMaterno(jtrabajador.getString("ap_materno"));
                trabajador.setSalario((float) jtrabajador.getDouble("salario"));
                trabajador.setGenero(jtrabajador.getInt("genero") == 1);
                trabajador.setIdTipoDeTrabajador(jtrabajador.getInt("idTipo_de_Trabajador"));
                trabajador.setPoseeSeguroSocial(jtrabajador.getInt("posee_seguro_social") != 0);
                trabajador.setIdAdministracion(jtrabajador.getInt("idAdministracion"));
                AccionesTablaTrabajador.agregarTrabajador(getContext(), trabajador);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void setUserInfo(JSONObject json){
        try{
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(CentralPoint.class.getName(), Context.MODE_PRIVATE).edit();
            editor.putString("usuario", json.getString("nickname"));
            editor.putString("email", json.getString("email"));
            editor.apply();
            Usuario usuario = new Usuario();
            usuario.setEmail(json.getString("email"));
            usuario.setNombres(json.getString("nombres"));
            usuario.setApPaterno(json.getString("ap_paterno"));
            usuario.setApMaterno(json.getString("ap_materno"));
            usuario.setDateOfBirth(json.getLong("fecha_de_nacimiento"));
            usuario.setTipoDeAdministrador(AccionesTablaUsuario.obtenerTipoDeAdministrador(getContext(), json.getString("tipo_de_administrador")));
            usuario.setEscolaridad(AccionesTablaUsuario.obtenerEscolaridad(getContext(), json.getString("escolaridad")));
            usuario.setRemuneracion(((float) json.getDouble("remuneracion")));
            usuario.setAdministracion(AccionesTablaAdministracion.obtenerAdministracion(getContext(), json.getInt("idAdministracion")));
            AccionesTablaUsuario.agregaUsuario(getContext(), usuario);
        }catch (JSONException e) {
            e.printStackTrace();
            ProveedorSnackBar
                    .muestraBarraDeBocados(user, "Servicio temproalmente no disponible");
        }
    }

    private void cleanPass(){
        password.setText("");
        ProveedorSnackBar
                .muestraBarraDeBocados(password, "Error en las credenciales");
        ((AppCompatActivity)getContext()).finishActivity(1234);
    }

    private class RespuestaObtencionDeConvocatorias implements ObtencionDeConvocatorias.AccionesObtencionDeConvocatorias{

        private JSONObject usrInfo;

        public RespuestaObtencionDeConvocatorias(JSONObject usrInfo) {
            this.usrInfo = usrInfo;
        }

        @Override
        public void obtencionCorrecta(JSONObject json) {
            setCondominio(usrInfo);
            setAdministracion(usrInfo);
            setTorresInfo(usrInfo);
            setTrabajadoresInfo(usrInfo);
            setUserInfo(usrInfo);
            setConvocatorias(json);
            getActivity().finishActivity(1234);
            getActivity().setResult(AppCompatActivity.RESULT_OK);
            Log.d("Atuni", "Setting resultCode: " + AppCompatActivity.RESULT_OK + " -- " + Activity.RESULT_OK);
            getActivity().finish();
        }

        @Override
        public void obtencionIncorrecta(final String mensaje) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProveedorSnackBar
                            .muestraBarraDeBocados(user, mensaje);
                }
            });
            getActivity().finishActivity(1234);
        }
    }

    /*** Queda pendiente una revisión ***/
    private void setConvocatorias(JSONObject resp) {
        try {
            CondominioBD db = new CondominioBD(getContext());
            String email = getContext().getSharedPreferences(CentralPoint.class.getName(), Context.MODE_PRIVATE).getString("email", "NaN");
            JSONArray convocatorias = resp.getJSONArray("content");
            for(int i=0; i < convocatorias.length(); i++) {
                JSONObject json = convocatorias.getJSONObject(i);
                Convocatoria convocatoria = new Convocatoria();
                convocatoria.setAsunto(json.getString("Asunto"));
                convocatoria.setUbicacionInterna(json.getString("Ubicacion_Interna"));
                convocatoria.setFechaInicio(json.getLong("Fecha_de_Inicio"));
                convocatoria.setFirma(json.getString("firma"));
                JSONArray puntosConv = json.getJSONArray("puntos");
                List<PuntoOdD> puntos = new ArrayList<>();
                for(int j=0; j<puntosConv.length(); j++){
                    PuntoOdD punto = new PuntoOdD();
                    punto.setDescripcion(puntosConv.getString(j));
                    punto.setIdConvocatoria(json.getInt("idConvocatoria"));
                    puntos.add(punto);
                }
                convocatoria.setId(json.getInt("idConvocatoria"));
                db.insertaConvocatoria(convocatoria, email);
                for(PuntoOdD punto : puntos)
                    db.insertaPuntoOdD(punto);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}

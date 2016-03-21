package org.inspira.condominio.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.inspira.condominio.R;
import org.inspira.condominio.admin.CentralPoint;
import org.inspira.condominio.admon.AccionesTablaAdministracion;
import org.inspira.condominio.admon.AccionesTablaUsuario;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.datos.PuntoOdD;
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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    userString = user.getText().toString().trim();
                    pwString = password.getText().toString().trim();
                    if (!"".equals(userString) && !"".equals(pwString)) {
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
            usuario.setAdministracion(AccionesTablaAdministracion.obtenerAdministracion(getContext(),json.getInt("idAdministracion")));
            AccionesTablaUsuario.agregaUsuario(getContext(), usuario);
        }catch(JSONException e){
            e.printStackTrace();
            ProveedorSnackBar
                    .muestraBarraDeBocados(user, "Servicio temproalmente no disponible");
        }
    }

    private void cleanPass(){
        password.setText("");
        ProveedorSnackBar
                .muestraBarraDeBocados(password, "Error en las credenciales");
    }

    private class RespuestaObtencionDeConvocatorias implements ObtencionDeConvocatorias.AccionesObtencionDeConvocatorias{

        private JSONObject usrInfo;

        public RespuestaObtencionDeConvocatorias(JSONObject usrInfo) {
            this.usrInfo = usrInfo;
        }

        @Override
        public void obtencionCorrecta(JSONObject json) {
            setUserInfo(usrInfo);
            setConvocatorias(json);
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
                convocatoria.setFechaInicio(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(json.getString("Fecha_de_Inicio")).getTime());
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
        }catch(JSONException | ParseException e){
            e.printStackTrace();
        }
    }
}

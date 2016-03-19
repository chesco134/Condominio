package org.inspira.condominio.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.ActualizaTextoDesdeLista;
import org.inspira.condominio.actividades.EfectoDeEnfoque;
import org.inspira.condominio.actividades.Verificador;
import org.inspira.condominio.admin.CentralPoint;
import org.inspira.condominio.admon.AccionesTablaAdministracion;
import org.inspira.condominio.admon.AccionesTablaUsuario;
import org.inspira.condominio.datos.NombreDeUsuario;
import org.inspira.condominio.datos.Usuario;
import org.inspira.condominio.dialogos.DialogoDeConsultaSimple;
import org.inspira.condominio.dialogos.ObtenerFecha;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jcapiz on 29/02/16.
 */
public class SignUp extends Fragment {

    private EditText email;
    private EditText nombres;
    private EditText pass;
    private TextView date;
    private Button confirm;
    private long fechaDeNacimiento;
    private EditText apPaterno;
    private EditText apMaterno;
    private EditText remuneracion;
    private EditText contacto;
    private TextView tipoDeAdministrador;
    private TextView escolaridad;
    private CheckBox habilitarProfesion;
    private EditText profesion;
    private TextView pisoProfesion;

    private MyWatcher myWatcher;
    private MyMailWatcher mailWatcher;
    private boolean cStatus;
    private int idAdministracion;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.signup, parent, false);
        email = (EditText) rootView.findViewById(R.id.signup_email);
        mailWatcher = new MyMailWatcher();
        email.addTextChangedListener(mailWatcher);
        rootView.findViewById(R.id.signup_piso_email)
                .setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), email));
        nombres = (EditText) rootView.findViewById(R.id.signup_usuario);
        rootView.findViewById(R.id.signup_piso_usuario)
                .setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), nombres));
        pass = (EditText) rootView.findViewById(R.id.signup_pass);
        rootView.findViewById(R.id.signup_piso_pass)
                .setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), pass));
        myWatcher = new MyWatcher();
        pass.addTextChangedListener(myWatcher);
        date = (TextView) rootView.findViewById(R.id.signup_fecha_de_nacimiento);
        apPaterno = (EditText) rootView.findViewById(R.id.signup_ap_paterno);
        rootView.findViewById(R.id.signup_piso_ap_paterno)
                .setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), apPaterno));
        apMaterno = (EditText) rootView.findViewById(R.id.signup_ap_materno);
        rootView.findViewById(R.id.signup_piso_ap_materno)
                .setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), apMaterno));
        remuneracion = (EditText) rootView.findViewById(R.id.signup_remuneracion);
        rootView.findViewById(R.id.signup_piso_remuneracion)
                .setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), remuneracion));
        tipoDeAdministrador = (TextView) rootView.findViewById(R.id.signup_tipo_de_administrador);
        tipoDeAdministrador.setOnClickListener(new ActualizaTextoDesdeLista(R.array.tipos_de_administrador, "Tipo de Administrador"));
        escolaridad = (TextView) rootView.findViewById(R.id.signup_escolaridad);
        escolaridad.setOnClickListener(new ActualizaTextoDesdeLista(R.array.escolaridades, "Escolaridad"));
        contacto = (EditText)rootView.findViewById(R.id.signup_contacto);
        rootView.findViewById(R.id.signup_piso_contacto)
                .setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), contacto));
        profesion = (EditText)rootView.findViewById(R.id.signup_profesion);
        pisoProfesion = (TextView) rootView.findViewById(R.id.signup_piso_profesion);
        pisoProfesion.setOnFocusChangeListener(new EfectoDeEnfoque(getActivity(), profesion));
        habilitarProfesion = (CheckBox)rootView.findViewById(R.id.signup_habilitar_profesion);
        habilitarProfesion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    habilitaCamposDeProfesion();
                }else{
                    deshabilitaCamposDeProfesion();
                }
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialogoFecha();
            }

            private void launchDialogoFecha() {
                ObtenerFecha of = new ObtenerFecha();
                of.setAgenteDeInteraccion(new DialogoDeConsultaSimple.AgenteDeInteraccionConResultado() {
                    @Override
                    public void clickSobreAccionPositiva(DialogFragment dialogo) {
                        fechaDeNacimiento = ((ObtenerFecha) dialogo).getFecha().getTime();
                        Calendar c = Calendar.getInstance();
                        if (c.getTimeInMillis() - fechaDeNacimiento >= 662256e6) // 18 años
                            date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(fechaDeNacimiento)));
                        else
                            ProveedorSnackBar
                                    .muestraBarraDeBocados(email, "La edad mínima son 21 años");
                    }

                    @Override
                    public void clickSobreAccionNegativa(DialogFragment dialogo) {
                    }
                });
                of.show(getActivity().getSupportFragmentManager(), "More");
            }
        });
        confirm = (Button) rootView.findViewById(R.id.signup_registrar);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                cStatus = false;
                if(validarInformacion()){
                    validarRemotamente();
                }else{
                    cStatus = true;
                    v.setEnabled(true);
                    ProveedorSnackBar
                            .muestraBarraDeBocados(v, "Por favor verifique los campos marcados");
                }
            }
        });
        if(savedInstanceState == null){
            fechaDeNacimiento = 0;
            cStatus = true;
            idAdministracion = getArguments().getInt("idAdministracion");
        }else{
            fechaDeNacimiento = savedInstanceState.getLong("fecha_de_nacimiento");
            email.setText(savedInstanceState.getString("email"));
            nombres.setText(savedInstanceState.getString("nombres"));
            cStatus = savedInstanceState.getBoolean("confirm_status");
            date.setText(savedInstanceState.getString("date"));
            apPaterno.setText(savedInstanceState.getString("ap_paterno"));
            apMaterno.setText(savedInstanceState.getString("ap_materno"));
            remuneracion.setText(savedInstanceState.getString("remuneracion"));
            String tipoAdmin = savedInstanceState.getString("tipo_de_administrador");
            tipoDeAdministrador.setText(tipoAdmin);
            if(!"".equals(tipoAdmin))
                tipoDeAdministrador.setTextColor(Color.BLACK);
            String esco = savedInstanceState.getString("escolaridad");
            escolaridad.setText(esco);
            if(!"".equals(esco))
                escolaridad.setTextColor(Color.BLACK);
            idAdministracion = savedInstanceState.getInt("idAdministracion");
            habilitarProfesion.setChecked(savedInstanceState.getBoolean("habilita_profesion"));
            contacto.setText(savedInstanceState.getString("contacto"));
            profesion.setText(savedInstanceState.getString("profesion"));
            if(habilitarProfesion.isChecked())
                habilitaCamposDeProfesion();
            else
                deshabilitaCamposDeProfesion();
        }
        confirm.setEnabled(cStatus);
        return rootView;
    }

    private void habilitaCamposDeProfesion() {
        profesion.setEnabled(true);
        pisoProfesion.setEnabled(true);

    }

    private void deshabilitaCamposDeProfesion() {
        profesion.setEnabled(false);
        pisoProfesion.setEnabled(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putLong("fecha_de_nacimiento", fechaDeNacimiento);
        outState.putString("email", email.getText().toString());
        outState.putString("nombres", nombres.getText().toString());
        outState.putBoolean("confirm_status", cStatus);
        outState.putString("date", date.getText().toString());
        outState.putString("ap_paterno", apPaterno.getText().toString());
        outState.putString("ap_materno", apMaterno.getText().toString());
        outState.putString("remuneracion", remuneracion.getText().toString());
        outState.putString("tipo_de_administrador", tipoDeAdministrador.getText().toString());
        outState.putString("escolaridad", escolaridad.getText().toString());
        outState.putInt("idAdministracion", idAdministracion);
        outState.putBoolean("habilita_profesion", habilitarProfesion.isChecked());
        outState.putString("profesion", profesion.getText().toString());
        outState.putString("contacto", contacto.getText().toString());
    }

    private boolean validarInformacion() {
        boolean[] veredictos = new boolean[9];
        veredictos[0] = Verificador.marcaCampo(getContext(), email, Verificador.TEXTO) && mailWatcher.isEnabled();
        veredictos[1] = Verificador.marcaCampo(getContext(), pass, Verificador.TEXTO) && myWatcher.isEnabled();
        veredictos[2] = Verificador.marcaCampo(getContext(), nombres, Verificador.TEXTO);
        veredictos[3] = Verificador.marcaCampo(getContext(), date, Verificador.TEXTO);
        veredictos[4] = Verificador.marcaCampo(getContext(), apPaterno, Verificador.TEXTO);
        veredictos[5] = Verificador.marcaCampo(getContext(), apMaterno, Verificador.TEXTO);
        veredictos[6] = Verificador.marcaCampo(getContext(), remuneracion, Verificador.FLOTANTE);
        veredictos[7] = Verificador.marcaCampo(getContext(), tipoDeAdministrador, Verificador.TEXTO);
        veredictos[8] = Verificador.marcaCampo(getContext(), escolaridad, Verificador.TEXTO);
        boolean veredicto = true;
        for(boolean iter : veredictos)
            if(!(veredicto = iter))
                break;
        return veredicto;
    }

    private void colocarPantallaPrincipal() {
        getActivity().finish();
    }

    private void guardarInformacion() {
        final Usuario user = new Usuario();
        user.setEmail(email.getText().toString().trim());
        NombreDeUsuario nombreDeUsuario = new NombreDeUsuario();
        nombreDeUsuario.setNombres(nombres.getText().toString().trim());
        nombreDeUsuario.setApPaterno(apPaterno.getText().toString().trim());
        nombreDeUsuario.setApMaterno(apMaterno.getText().toString().trim());
        nombreDeUsuario.setId(AccionesTablaUsuario.agregaNombreDeUsuario(getContext(), nombreDeUsuario));
        user.setNombreDeUsuario(nombreDeUsuario);
        user.setDateOfBirth(fechaDeNacimiento);
        user.setAdministracion(AccionesTablaAdministracion.obtenerAdministracion(getContext(), idAdministracion));
        user.setRemuneracion(Float.parseFloat(remuneracion.getText().toString().trim()));
        user.setEscolaridad(AccionesTablaUsuario.obtenerEscolaridad(getContext(), escolaridad.getText().toString()));
        user.setTipoDeAdministrador(AccionesTablaUsuario.obtenerTipoDeAdministrador(getContext(), tipoDeAdministrador.getText().toString()));
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(CentralPoint.class.getName(), Context.MODE_PRIVATE).edit();
        editor.putString("usuario", user.getNombreDeUsuario().getNombres() + " " + user.getNombreDeUsuario().getApPaterno());
        editor.putString("email", user.getEmail());
        editor.apply();
        AccionesTablaUsuario.agregaUsuario(getContext(), user);
        String prof = profesion.getText().toString().trim();
        if(!"".equals(prof))
            AccionesTablaUsuario.agregaUsuarioProfesionista(getContext(), user.getEmail(), prof);
        String telefono = contacto.getText().toString().trim();
        if(!"".equals(telefono))
            AccionesTablaUsuario.agregaContactoUsuario(getContext(), user.getEmail(), telefono);
    }

    private String armarContenido(){
        String contenido = null;
        try{
            JSONObject json = new JSONObject();
            json.put("action",15);
            json.put("email", email.getText().toString().trim());
            json.put("nombres", nombres.getText().toString().trim());
            json.put("ap_paterno", apPaterno.getText().toString());
            json.put("ap_materno", apMaterno.getText().toString().trim());
            json.put("fecha_de_nacimiento", fechaDeNacimiento);
            json.put("idAdministracion", idAdministracion);
            json.put("remuneracion", Float.parseFloat(remuneracion.getText().toString().trim()));
            json.put("escolaridad", escolaridad.getText().toString());
            json.put("tipo_de_administrador", tipoDeAdministrador.getText().toString());
            json.put("profesion", profesion.getText().toString().trim());
            json.put("contacto", contacto.getText().toString().trim());
            contenido = json.toString();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return contenido;
    }

    private void validarRemotamente() {
        ContactoConServidor contacto = new ContactoConServidor(new ContactoConServidor.AccionesDeValidacionConServidor() {
            @Override
            public void resultadoSatisfactorio(Thread t) {
                String respuesta = ((ContactoConServidor)t).getResponse();
                try{
                    JSONObject json = new JSONObject(respuesta);
                    if(json.getBoolean("content")) {
                        muestraMensajeDesdeHilo("Hecho");
                        guardarInformacion();
                        colocarPantallaPrincipal();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                habilitaBotonDesdeHilo();
            }

            @Override
            public void problemasDeConexion(Thread t) {
                muestraMensajeDesdeHilo("Servidor temporalmente no disponible");
                habilitaBotonDesdeHilo();
            }
        }, armarContenido());
        contacto.start();
    }

    private void muestraMensajeDesdeHilo(final String mensaje){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProveedorSnackBar
                        .muestraBarraDeBocados(email, mensaje);
                habilitaBotonDesdeHilo();
            }
        });
    }

    private void habilitaBotonDesdeHilo(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                confirm.setEnabled(true);
                cStatus = true;
            }
        });
    }

    private class MyWatcher implements TextWatcher{

        private boolean enabled = false;
        private Pattern pattern;
        private Matcher matcher;
        private static final String PASS_PATTERN =
                "(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{5,}";

        public MyWatcher() {
            pattern = Pattern.compile(PASS_PATTERN);
        }

        public boolean validate(final String hex) {
            matcher = pattern.matcher(hex);
            return matcher.matches();
        }

        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(validate(pass.getText().toString())) {
                pass.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                enabled = true;
            }else {
                pass.setBackgroundColor(getActivity().getResources().getColor(R.color.error));
                enabled = false;
            }
        }
    }

    private class MyMailWatcher implements TextWatcher{

        private boolean enabled = false;
        private Pattern pattern;
        private Matcher matcher;
        private static final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public MyMailWatcher() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        public boolean isEnabled() {
            return enabled;
        }

        public boolean validate(final String hex) {
            matcher = pattern.matcher(hex);
            return matcher.matches();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (validate(email.getText().toString())) {
                email.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                enabled = true;
            }else {
                enabled = false;
            }
        }
    }
}
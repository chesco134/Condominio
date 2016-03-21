package org.inspira.condominio.admon;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.inspira.condominio.admin.R;
import org.inspira.condominio.dialogos.DialogoDeLista;
import org.inspira.condominio.dialogos.ProveedorSnackBar;

/**
 * Created by jcapiz on 16/03/16.
 */
public class NuevoCondominioFragment1 extends Fragment {

    private EditText direccion;
    private EditText edad;
    private TextView tipoDeCondominio;
    private EditText inmoviliaria;
    private EditText numTorres;
    private AccionNCondominio1 accion;
    private static int ERROR;

    public interface AccionNCondominio1{
        void siguiente(String direccion, int edad, String tipo, String inmoviliaria, int torres);
        void onResume();
    }

    public void setAccion(AccionNCondominio1 accion) {
        this.accion = accion;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        ERROR = getContext().getResources().getColor(R.color.error);
        final View rootView = inflater.inflate(R.layout.nuevo_condominio_1, parent, false);
        direccion = (EditText)rootView.findViewById(R.id.nuevo_condominio_1_direccion);
        edad = (EditText)rootView.findViewById(R.id.nuevo_condominio_1_edad);
        tipoDeCondominio = (TextView)rootView.findViewById(R.id.nuevo_condominio_1_tipo_de_condominio);
        inmoviliaria = (EditText)rootView.findViewById(R.id.nuevo_condominio_1_inmoviliaria);
        numTorres = (EditText)rootView.findViewById(R.id.nuevo_condominio_1_numero_de_torres);
        numTorres.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    rootView.findViewById(R.id.nuevo_condominio_1_piso_numero_de_torres).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    restaurarColor(v);
                }else
                    rootView.findViewById(R.id.nuevo_condominio_1_piso_numero_de_torres).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        inmoviliaria.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    rootView.findViewById(R.id.nuevo_condominio_1_piso_inmoviliaria)
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    restaurarColor(v);
                }else
                    rootView.findViewById(R.id.nuevo_condominio_1_piso_inmoviliaria)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        tipoDeCondominio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    rootView.findViewById(R.id.nuevo_condominio_1_piso_tipo_de_condominio)
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    restaurarColor(v);
                }else
                    rootView.findViewById(R.id.nuevo_condominio_1_piso_tipo_de_condominio)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        edad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    rootView.findViewById(R.id.nuevo_condominio_1_piso_edad)
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    restaurarColor(v);
                }else
                    rootView.findViewById(R.id.nuevo_condominio_1_piso_edad)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        direccion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    rootView.findViewById(R.id.nuevo_condominio_1_direccion_piso)
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    restaurarColor(v);
                }else
                    rootView.findViewById(R.id.nuevo_condominio_1_direccion_piso)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        rootView.findViewById(R.id.nuevo_condominio_1_boton_siguiente)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validaInformacion();
                    }
                });
        tipoDeCondominio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.WHITE);
                launchTextPicker();
            }
        });
        if(savedInstanceState != null){
            direccion.setText(savedInstanceState.getString("direccion"));
            edad.setText(savedInstanceState.getString("edad"));
            tipoDeCondominio.setText(savedInstanceState.getString("tipoDeCondominio"));
            inmoviliaria.setText(savedInstanceState.getString("inmoviliaria"));
            numTorres.setText(savedInstanceState.getString("numTorres"));
            if(!"".equals(tipoDeCondominio.getText()))
                tipoDeCondominio.setTextColor(Color.BLACK);
        }
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        accion.onResume();
    }

    private void launchTextPicker() {
        DialogoDeLista ddlista = new DialogoDeLista();
        ddlista.setAccion(new DialogoDeLista.AccionDialogoDeLista() {
            @Override
            public void objetoSeleccionado(String texto) {
                tipoDeCondominio.setText(texto);
            }
        });
        ddlista.setTitulo("Tipo de condominio");
        ddlista.setStringArrayRes(R.array.tipos_de_condominio);
        ddlista.show(getActivity().getSupportFragmentManager(), "Dialogo Lista");
        tipoDeCondominio.setTextColor(Color.BLACK);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("direccion", direccion.getText().toString().trim());
        outState.putString("edad", edad.getText().toString().trim());
        outState.putString("tipoDeCondominio", tipoDeCondominio.getText().toString());
        outState.putString("inmoviliaria", inmoviliaria.getText().toString().trim());
        outState.putString("numTorres", numTorres.getText().toString().trim());
    }

    private void validaInformacion() {
        String dir = direccion.getText().toString();
        String age = edad.getText().toString();
        String tipo = tipoDeCondominio.getText().toString();
        String inmov = inmoviliaria.getText().toString();
        String numT = numTorres.getText().toString();
        if (!marcaCamposIncorrectos()) {
            Integer edad = null;
            Integer numTorres = null;
            try {
                edad = Integer.parseInt(age);
                numTorres = Integer.parseInt(numT);
                if(!marcaCamposNumericosIncorrectos(edad, numTorres)){
                    accion.siguiente(dir.trim(),edad,tipo.trim(),inmov.trim(),numTorres);
                }else{
                    ProveedorSnackBar
                            .muestraBarraDeBocados(inmoviliaria, "Valor de edad o cantidad de torres incorrecto.");
                }
            }catch(NumberFormatException e){
                if(edad == null)
                    edad = -1;
                if(numTorres == null)
                    numTorres = 0;
                ProveedorSnackBar
                        .muestraBarraDeBocados(inmoviliaria, "Valor de edad o cantidad de torres incorrecto.");
                marcaCamposNumericosIncorrectos(edad, numTorres);
            }
        }else{
            ProveedorSnackBar
                    .muestraBarraDeBocados(direccion, "Por favor verifique los campos necesarios");
        }
    }

    private boolean marcaCamposNumericosIncorrectos(int edad, int numTorres){
        boolean hubieronCamposIncorrectos = false;
        if(edad <= -1) {
            NuevoCondominioFragment1.this.edad.setBackgroundColor(ERROR);
            hubieronCamposIncorrectos = true;
        }
        if(numTorres <= 0) {
            NuevoCondominioFragment1.this.numTorres.setBackgroundColor(ERROR);
            hubieronCamposIncorrectos = true;
        }
        return hubieronCamposIncorrectos;
    }

    private boolean marcaCamposIncorrectos() {
        boolean[] camposMarcados = new boolean[5];
        camposMarcados[0] = coloreaCampo(direccion);
        camposMarcados[1] = coloreaCampo(edad);
        camposMarcados[2] = coloreaCampo(tipoDeCondominio);
        camposMarcados[3] = coloreaCampo(inmoviliaria);
        camposMarcados[4] = coloreaCampo(numTorres);
        return camposMarcados[0]
                || camposMarcados[1]
                || camposMarcados[2]
                || camposMarcados[3]
                || camposMarcados[4];
    }

    private boolean coloreaCampo(TextView view){
        boolean campoColoreado = false;
        if("".equals(view.getText().toString().trim())) {
            view.setBackgroundColor(ERROR);
            campoColoreado = true;
        }
        return campoColoreado;
    }

    private void restaurarColor(View view){
        view.setBackgroundColor(Color.WHITE);
    }
}

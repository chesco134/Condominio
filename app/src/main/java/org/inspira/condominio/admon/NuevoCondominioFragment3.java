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

import org.inspira.condominio.R;
import org.inspira.condominio.dialogos.ProveedorSnackBar;

/**
 * Created by jcapiz on 16/03/16.
 */
public class NuevoCondominioFragment3 extends Fragment {

    private EditText cajonesDeEstacionamiento;
    private EditText cajonesDeEstacionamientoVisitas;
    private EditText costoPorUnidadPrivativa;
    private EditText capacidadDeCisterna;
    private int error_color;
    private AccionNCondominio3 accion;

    public interface AccionNCondominio3 {
        void hecho(int cajonesDeEstacionamiento,
                   int cajonesDeEstacionamientoVisitas,
                   float costoPorUnidadPrivativa,
                   float capacidadDeCisterna);

        void onResume();
    }

    public void setAccion(AccionNCondominio3 accion) {
        this.accion = accion;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.nuevo_condominio_3, parent, false);
        cajonesDeEstacionamiento = (EditText)rootView.findViewById(R.id.nuevo_condominio_3_cantidad_de_lugares_estacionamiento);
        cajonesDeEstacionamientoVisitas = (EditText) rootView.findViewById(R.id.nuevo_condominio_3_cantidad_de_lugares_estacionamiento_visitas);
        costoPorUnidadPrivativa = (EditText)rootView.findViewById(R.id.nuevo_condominio_3_costo_aprox_por_unidad_privativa);
        capacidadDeCisterna = (EditText)rootView.findViewById(R.id.nuevo_condominio_3_capacidad_de_cisterna);
        rootView.findViewById(R.id.nuevo_condominio_3_boton_hecho)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validaCampos();
                    }
                });
        cajonesDeEstacionamiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rootView.findViewById(R.id.nuevo_condominio_3_piso_cantidad_de_lugares_estacionamiento)
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    restoreBackgroundColor(v);
                } else
                    rootView.findViewById(R.id.nuevo_condominio_3_piso_cantidad_de_lugares_estacionamiento)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        cajonesDeEstacionamientoVisitas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rootView.findViewById(R.id.nuevo_condominio_3_piso_cantidad_de_lugares_estacionamiento_visitas)
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    restoreBackgroundColor(v);
                }else
                    rootView.findViewById(R.id.nuevo_condominio_3_piso_cantidad_de_lugares_estacionamiento_visitas)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        costoPorUnidadPrivativa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rootView.findViewById(R.id.nuevo_condominio_3_piso_costo_aprox_por_unidad_privativa)
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    restoreBackgroundColor(v);
                }else
                    rootView.findViewById(R.id.nuevo_condominio_3_piso_costo_aprox_por_unidad_privativa)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        capacidadDeCisterna.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    rootView.findViewById(R.id.nuevo_condominio_3_piso_capacidad_de_cisterna)
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    restoreBackgroundColor(v);
                }else
                    rootView.findViewById(R.id.nuevo_condominio_3_piso_capacidad_de_cisterna)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        error_color = getContext().getResources().getColor(R.color.error);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        accion.onResume();
    }

    private void validaCampos(){
        boolean[] tripod = new boolean[4];
        tripod[0] = this.esUnCampoValido(cajonesDeEstacionamiento);
        tripod[1] = this.esUnCampoValido(cajonesDeEstacionamientoVisitas);
        tripod[2] = this.esUnCampoValido(costoPorUnidadPrivativa);
        tripod[3] = this.esUnCampoValido(capacidadDeCisterna);
        if(tripod[0] && tripod[1] && tripod[2] && tripod[3]){
            accion.hecho(Integer.parseInt(cajonesDeEstacionamiento.getText().toString().trim()),
                    Integer.parseInt(cajonesDeEstacionamientoVisitas.getText().toString().trim()),
                    Float.parseFloat(costoPorUnidadPrivativa.getText().toString().trim()),
                    Float.parseFloat(capacidadDeCisterna.getText().toString().trim()));
        }else{
            ProveedorSnackBar
                    .muestraBarraDeBocados(cajonesDeEstacionamiento, "Por favor verifique los campos marcados.");
        }
    }

    private boolean esUnCampoValido(TextView v){
        boolean esValido = true;
        if("".equals(v.getText().toString().trim())) {
            v.setBackgroundColor(error_color);
            esValido = false;
        }
        return esValido;
    }

    private void restoreBackgroundColor(View v){
        v.setBackgroundColor(Color.WHITE);
    }
}

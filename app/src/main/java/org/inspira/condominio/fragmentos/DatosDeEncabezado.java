package org.inspira.condominio.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.CrearConvocatoria;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.dialogos.TomarFecha;
import org.inspira.condominio.dialogos.TomarTiempo;

public class DatosDeEncabezado extends Fragment {

    private static final int TOMAR_FECHA = 134;
    private static final int TOMAR_TIEMPO = 128;
    private EditText asunto;
    private EditText condominio;
    private EditText ubicacion;
    private EditText ubicacionInterna;
    private TextView fechaInicial;
    private TextView tiempoInicial;
    private EditText firma;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.hacer_encabezado_convocatoria, parent, false);
        asunto = (EditText)rootView.findViewById(R.id.hacer_encabezado_convocatoria_entrada_asunto);
        condominio = (EditText)rootView.findViewById(R.id.hacer_encabezado_convocatoria_entrada_condominio);
        ubicacion = (EditText)rootView.findViewById(R.id.hacer_encabezado_convocatoria_entrada_ubicacion);
        ubicacionInterna = (EditText)rootView.findViewById(R.id.hacer_encabezado_convocatoria_entrada_ubicacion_interna);
        firma = (EditText)rootView.findViewById(R.id.hacer_encabezado_convocatoria_entrada_firma);
        fechaInicial = (TextView)rootView.findViewById(R.id.hacer_encabezado_convocatoria_entrada_fecha_inicial);
        tiempoInicial = (TextView)rootView.findViewById(R.id.hacer_encabezado_convocatoria_entrada_hora_inicial);
        fechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearDialogo(TOMAR_FECHA);
            }
        });
        tiempoInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearDialogo(TOMAR_TIEMPO);
            }
        });
        rootView.findViewById(R.id.hacer_encabezado_convocatoria_boton_aceptar)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (datosCorrectos()) {
                            cambiarFragmento();
                        } else {
                            muestraBarraDeBocados(getActivity()
                                    .getResources()
                                    .getString(R.string.crear_convocatoria_datos_incorrectos));
                        }
                    }
                });
        Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
        asunto.setText(args.getString("asunto"));
        condominio.setText(args.getString("condominio"));
        ubicacion.setText(args.getString("ubicacion"));
        ubicacionInterna.setText(args.getString("ubicacion_interna"));
        firma.setText(args.getString("firma"));
        fechaInicial.setText(args.getString("fecha_inicial"));
        tiempoInicial.setText(args.getString("tiempo_inicial"));
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("asunto", asunto.getText().toString());
        outState.putString("condominio", condominio.getText().toString());
        outState.putString("ubicacion", ubicacion.getText().toString());
        outState.putString("ubicacion_interna", ubicacionInterna.getText().toString());
        outState.putString("firma", firma.getText().toString());
        outState.putString("fecha_inicial", fechaInicial.getText().toString());
        outState.putString("tiempo_inicial", tiempoInicial.getText().toString());
    }

    private void crearDialogo(int tipoDeDialogo){
        switch(tipoDeDialogo){
            case TOMAR_FECHA:
                TomarFecha tf = new TomarFecha();
                tf.setAccionDialogo(new AccionTomarFecha());
                tf.show(getActivity().getSupportFragmentManager(), "TomarFecha");
                break;
            case TOMAR_TIEMPO:
                TomarTiempo tt = new TomarTiempo();
                tt.setAccionDialogo(new AccionTomarTiempo());
                tt.show(getActivity().getSupportFragmentManager(), "TomarTiempo");
                break;
        }
    }

    private class AccionTomarFecha implements EntradaTexto.AccionDialogo{

        @Override
        public void accionPositiva(DialogFragment df){
            TomarFecha dialogo = (TomarFecha)df;
            String fechaObtenida = dialogo.getFecha();
            if(fechaObtenida != null)
                fechaInicial.setText(fechaObtenida);
            else
                muestraBarraDeBocados(getActivity().getResources().getString(R.string.crear_convocatoria_fecha_incorrecta));
        }

        @Override
        public void accionNegativa(DialogFragment df){}
    }

    private class AccionTomarTiempo implements EntradaTexto.AccionDialogo{

        @Override
        public void accionPositiva(DialogFragment df){
            TomarTiempo dialogo = (TomarTiempo)df;
            String fechaObtenida = dialogo.getTiempo();
            if(fechaObtenida != null)
                tiempoInicial.setText(fechaObtenida);
            else
                muestraBarraDeBocados(getActivity().getResources().getString(R.string.crear_convocatoria_hora_incorrecta));
        }

        @Override
        public void accionNegativa(DialogFragment df){}
    }

    private boolean datosCorrectos(){
        String sAsunto = asunto.getText().toString();
        String sUbicacion = ubicacion.getText().toString();
        String sUbicacionInterna = ubicacionInterna.getText().toString();
        String sCondominio = condominio.getText().toString();
        String sFirma = firma.getText().toString();
        String sFechaInicio = fechaInicial.getText().toString();
        String sHoraInicio = tiempoInicial.getText().toString();
        return !sAsunto.equals("") && !sUbicacion.equals("") && !sUbicacionInterna.equals("")
                && !sCondominio.equals("") && !sFechaInicio.equals("")
                && !sHoraInicio.equals("") && !sFirma.equals("");
    }

    private void muestraBarraDeBocados(String mensaje){
        ProveedorSnackBar.muestraBarraDeBocados(asunto, mensaje);
    }

    private void cambiarFragmento(){
        ((CrearConvocatoria)getActivity()).colocaOrdenDelDiaFragmento();
    }

    public String getAsunto() {
        try{return asunto.getText().toString();}catch(NullPointerException ignore){return null;}
    }

    public String getCondominio() {
        try {
            return condominio.getText().toString();
        }catch (NullPointerException ignore){return null;}
    }

    public String getUbicacion() {
        try {
            return ubicacion.getText().toString();
        }catch(NullPointerException ignore){return null;}
    }

    public String getUbicacionInterna() {
        try{return ubicacionInterna.getText().toString();}catch(NullPointerException ignore){return null;}
    }

    public String getFechaInicial() {
        try {
            return fechaInicial.getText().toString();
        }catch(NullPointerException ignore){return null;}
    }

    public String getTiempoInicial() {
        try{
            return tiempoInicial.getText().toString();
        }catch(NullPointerException ignore){
            return null;
        }
    }

    public String getFirma() {
        try {
            return firma.getText().toString();
        }catch (NullPointerException ignore){
            return null;
        }
    }
}
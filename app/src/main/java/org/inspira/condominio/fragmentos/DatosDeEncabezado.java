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
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.dialogos.TomarFecha;
import org.inspira.condominio.dialogos.TomarTiempo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private Convocatoria conv;

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
        conv = (Convocatoria)args.getSerializable("convocatoria");
        asunto.setText(conv.getAsunto());
        condominio.setText(conv.getCondominio());
        ubicacion.setText(conv.getUbicacion());
        ubicacionInterna.setText(conv.getUbicacionInterna());
        firma.setText(conv.getFirma());
        Long fechaInicio = conv.getFechaInicio() == null ? new Date().getTime() : conv.getFechaInicio();
        String[] elementos = new SimpleDateFormat("dd/MM/yyyy@@hh:mm").format(new Date(fechaInicio)).split("@@");
        fechaInicial.setText(elementos[0]);
        tiempoInicial.setText(elementos[1]);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        conv.setAsunto(asunto.getText().toString());
        conv.setCondominio(condominio.getText().toString());
        conv.setUbicacion(condominio.getText().toString());
        conv.setUbicacionInterna(condominio.getText().toString());
        Calendar c = Calendar.getInstance();
        String[] eFecha = fechaInicial.getText().toString().split("/");
        String[] eTiempo = tiempoInicial.getText().toString().split(":");
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(eFecha[0]));
        c.set(Calendar.MONTH, Integer.parseInt(eFecha[1]));
        c.set(Calendar.YEAR, Integer.parseInt(eFecha[2]));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(eTiempo[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(eTiempo[1]));
        conv.setFechaInicio(c.getTimeInMillis());
        conv.setFirma(firma.getText().toString());
        outState.putSerializable("convocatoria", conv);
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
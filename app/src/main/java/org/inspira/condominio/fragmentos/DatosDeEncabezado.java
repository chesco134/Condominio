package org.inspira.condominio.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_convocatoria, menu);
        menu.findItem(R.id.menu_convocatoria_agregar_punto).setVisible(false);
        menu.findItem(R.id.menu_convocatoria_agregar_punto).setEnabled(false);
        menu.findItem(R.id.menu_convocatoria_hecho).setVisible(false);
        menu.findItem(R.id.menu_convocatoria_hecho).setEnabled(false);
        menu.findItem(R.id.menu_convocatoria_quitar_punto).setVisible(false);
        menu.findItem(R.id.menu_convocatoria_quitar_punto).setEnabled(false);
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
        String sFechaInicio = fechaInicial.getText().toString();
        String sHoraInicio = tiempoInicial.getText().toString();
        return !sAsunto.equals("") && !sUbicacion.equals("") && !sUbicacionInterna.equals("")
                && !sCondominio.equals("") && !sFechaInicio.equals("") && !sHoraInicio.equals("");
    }

    private void muestraBarraDeBocados(String mensaje){
        ProveedorSnackBar.muestraBarraDeBocados(asunto, mensaje);
    }

    private void cambiarFragmento(){
        ((CrearConvocatoria)getActivity()).colocaOrdenDelDiaFragmento();
    }

    public String getAsunto() {
        return asunto.getText().toString();
    }

    public String getCondominio() {
        return condominio.getText().toString();
    }

    public String getUbicacion() {
        return ubicacion.getText().toString();
    }

    public String getUbicacionInterna() {
        return ubicacionInterna.getText().toString();
    }

    public String getFechaInicial() {
        return fechaInicial.getText().toString();
    }

    public TextView getTiempoInicial() {
        return tiempoInicial;
    }
}
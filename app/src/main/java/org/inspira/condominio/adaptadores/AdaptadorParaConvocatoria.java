package org.inspira.condominio.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.Lobby;
import org.inspira.condominio.datos.AlmacenamientoInterno;
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.Informacion;
import org.inspira.condominio.dialogos.ProveedorToast;
import org.inspira.condominio.pdf.ExportarConvocatoria;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Siempre on 22/02/2016.
 */
public class AdaptadorParaConvocatoria extends BaseAdapter {

    private Context context;
    private List<Convocatoria> convocatorias;
    private List<File> archivos;

    public AdaptadorParaConvocatoria(Context context, List<Convocatoria> convocatorias){
        this.context = context;
        this.convocatorias = convocatorias;
        archivos = new ArrayList<>();
    }

    public void addItem(Convocatoria conv){
        convocatorias.add(conv);
        notifyDataSetChanged();
    }

    public Convocatoria remove(int position){
        return convocatorias.remove(position);
    }

    @Override
    public int getCount() {
        return convocatorias.size();
    }

    @Override
    public Object getItem(int position) {
        return convocatorias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.elemento_de_lista_de_convocatoria, parent, false);
        TextView asunto = (TextView)rootView.findViewById(R.id.elemento_de_lista_de_convocatoria_asunto);
        asunto.setText(convocatorias.get(position).getAsunto());
        TextView fecha = (TextView)rootView.findViewById(R.id.elemento_de_lista_de_convocatoria_fecha);
        fecha.setText(new SimpleDateFormat("dd/MM/yyyy, hh:mm").format(new Date(convocatorias.get(position).getFechaInicio())));
        Button btn = (Button)rootView.findViewById(R.id.elemento_de_lista_de_convocatoria_boton);
        btn.setOnTouchListener(new TouchOverInfoButton(position));
        rootView.setOnClickListener(new LanzadorDePDF(position));
        return rootView;
    }

    private void lanzaConfirmaRegeneracion(int position){
        Informacion info = new Informacion();
        Bundle args = new Bundle();
        args.putString("mensaje", "¿Desea exportar nuevamente el archivo?");
        args.putString("titulo", "Reexportar archivo");
        info.setArguments(args);
        info.setAccion(new ConfirmaRegeneracion(position));
        info.show(((Lobby) context).getSupportFragmentManager(), "Confirmar reg");
    }

    private void lanzaRegeneracion(int position){
        EntradaTexto et = new EntradaTexto();
        Bundle args = new Bundle();
        args.putString("mensaje", "Escriba el nombre del archivo");
        et.setArguments(args);
        et.setAccionDialogo(new RegenerarPDF(position));
        et.show(((Lobby)context).getSupportFragmentManager(), "Regenerar pdf");
    }

    private class TouchOverInfoButton implements View.OnTouchListener{

        private int position;

        public TouchOverInfoButton(int position){
            this.position = position;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP){
                lanzaConfirmaRegeneracion(position);
            }
            return true;
        }
    }

    private class LanzadorDePDF implements View.OnClickListener{

        private int position;

        public LanzadorDePDF(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            generarPDFTemp(position);
        }
    }

    private class RegenerarPDF implements EntradaTexto.AccionDialogo{

        private int position;

        public RegenerarPDF(int position){
            this.position = position;
        }

        @Override
        public void accionPositiva(DialogFragment fragment) {
            EntradaTexto et = (EntradaTexto) fragment;
            String tituloPropuesto = et.getEntradaDeTexto();
            generarPDF(tituloPropuesto,position);
        }

        @Override
        public void accionNegativa(DialogFragment fragment) {}
    }

    private class ConfirmaRegeneracion implements EntradaTexto.AccionDialogo{

        private int position;

        public ConfirmaRegeneracion(int posotion){
            this.position = position;
        }

        @Override
        public void accionPositiva(DialogFragment fragment) {
            lanzaRegeneracion(position);
        }

        @Override
        public void accionNegativa(DialogFragment fragment) {}
    }

    private void generarPDFTemp(int position){
        Convocatoria conv = convocatorias.get(position);
        ExportarConvocatoria exp = new ExportarConvocatoria(context, conv);
        AlmacenamientoInterno a = new AlmacenamientoInterno(context);
        a.crearDirectorio();
        try {
            String prefix = conv.getAsunto();
            switch(prefix.length()){
                case 0:
                    prefix = "000";
                    break;
                case 1:
                    prefix = "0_" + prefix;
                    break;
                case 2:
                    prefix = "_"+prefix;
                    break;
            }
            File f = File.createTempFile(prefix, ".pdf", new File(a.obtenerRutaDeAlmacenamiento()));
            exp.crearArchivo(f);
            launchPDFViewer(f);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void generarPDF(final String titulo, final int position){
        new Thread() {
            @Override public void run(){
                Convocatoria conv = convocatorias.get(position);
                ExportarConvocatoria exp = new ExportarConvocatoria(context, conv);
                AlmacenamientoInterno a = new AlmacenamientoInterno(context);
                a.crearDirectorio();
                try {
                    File f = new File(a.obtenerRutaDeAlmacenamiento() + "/" + titulo);
                    exp.crearArchivo(f);
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ProveedorToast.showToast(context, "Archivo generado");
                        }
                    });
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void launchPDFViewer(File file){
        archivos.add(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        ((Activity)context).startActivityForResult(intent, Lobby.VISOR_PDF);
    }

    public void borrarArchivos(){
        for(File file : archivos)
            file.delete();
    }
}
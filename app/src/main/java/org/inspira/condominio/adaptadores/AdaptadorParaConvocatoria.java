package org.inspira.condominio.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.Lobby;
import org.inspira.condominio.datos.AlmacenamientoInterno;
import org.inspira.condominio.datos.Convocatoria;
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
        if(convertView != null)
            rootView = convertView;
        else{
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.elemento_de_lista_de_convocatoria, parent, false);
            TextView asunto = (TextView)rootView.findViewById(R.id.elemento_de_lista_de_convocatoria_asunto);
            asunto.setText(convocatorias.get(position).getAsunto());
            TextView fecha = (TextView)rootView.findViewById(R.id.elemento_de_lista_de_convocatoria_fecha);
            fecha.setText(new SimpleDateFormat("dd/MM/yyyy, hh:mm").format(new Date(convocatorias.get(position).getFechaInicio())));
        }
        rootView.setOnClickListener(new LanzadorDePDF(position));
        return rootView;
    }

    private class LanzadorDePDF implements View.OnClickListener{

        private int position;
        private File f;

        public LanzadorDePDF(int position){
            this.position = position;
            f = null;
        }

        @Override
        public void onClick(View v) {

            Convocatoria conv = convocatorias.get(position);
            ExportarConvocatoria exp = new ExportarConvocatoria(context, conv);
            AlmacenamientoInterno a = new AlmacenamientoInterno(context);
            a.crearDirectorio();
            try {
                f = File.createTempFile(conv.getAsunto(), ".pdf", new File(a.obtenerRutaDeAlmacenamiento()));
                exp.crearArchivo(f);
                launchPDFViewer(f);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        private void launchPDFViewer(File file){
            archivos.add(f);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            ((Activity)context).startActivityForResult(intent, Lobby.VISOR_PDF);
        }
    }

    public void borrarArchivos(){
        for(File file : archivos)
            file.delete();
    }
}
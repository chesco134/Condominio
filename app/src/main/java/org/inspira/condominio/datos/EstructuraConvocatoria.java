package org.inspira.condominio.datos;

import android.content.Context;

import org.inspira.condominio.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David Azaraf.
 */
public class EstructuraConvocatoria {

    private Context context;

    public EstructuraConvocatoria(Context context) {
        this.context = context;
    }

    public String getTitulo(){
        return context.getString(R.string.estructura_de_convocatoria_convocatoria_titulo) + "\n\n";
    }

    public String getIntro(String convocatoria){
        return context.getString(R.string.estructura_de_convocatoria_introduccion)
                + " \"" +  convocatoria + "\"";
    }

    public String getOrigenDeConvocatoria(String Origen){
        return " del \"" + Origen + "\"";
    }

    public String getUbicacion(String Ubicacion){
        return " ubicado en " + Ubicacion;
    }

    public String getFechaConvocatoria(Date fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d' de 'MMMM' del 'yyyy");
        String strDate = sdf.format(fecha);
        return context.getString(R.string.estructura_de_convocatoria_relleno_1) + " " + strDate;
    }

    public String getLugar(String lugar){
        return " en " + lugar + ".";
    }

    public String getPrimeraConvocatoriaTitulo(){
        return "\n" + context.getString(R.string.estructura_de_convocatoria_primera_convocatoria) + "\n\n";
    }

    public String getHoraPrimera(String hora){
        return context.getString(R.string.estructura_de_convocatoria_relleno_2) + " " + hora
                + " " + context.getString(R.string.estructura_de_convocatoria_relleno_3);
    }

    public String getHoraSegunda(String hora){
        return context.getString(R.string.estructura_de_convocatoria_relleno_4) + " " + hora + " horas.";
    }

    public String getSegundaConvocatoriaTitulo(){
        return "\n" + context.getString(R.string.estructura_de_convocatoria_segunda_convocatoria) + "\n\n";
    }

    public String getTerceraConvocatoriaTitulo(){
        return "\n" + context.getString(R.string.estructura_de_convocatoria_tercera_convocatoria) + "\n\n";
    }

    public String getHoraTercera(String hora){
        return context.getString(R.string.estructura_de_convocatoria_relleno_5) + " " + hora + " horas.";
    }

    public String getLey(){
        return context.getString(R.string.estructura_de_convocatoria_relleno_6);
    }

    public String getFechaPublicacion(Date fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("d' de 'MMMM' del 'yyyy");
        String strDate = sdf.format(fecha);
        String f = "\n\n" + context.getString(R.string.estructura_de_convocatoria_relleno_7) + " " + strDate + ".";
        return f.toUpperCase();
    }
}
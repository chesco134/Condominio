package org.inspira.condominio.datos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Siempre on 22/02/2016.
 */
public class Convocatoria extends ModeloDeDatos{

    public static final int TERCERA_CONV = 3;
    public static final int SEGUNDA_CONV = 2;
    public static final int PRIMERA_CONV = 1;
    private String asunto;
    private String condominio;
    private String ubicacion;
    private String ubicacionInterna;
    private String firma;
    private Long fechaInicio;
    private List<PuntoOdD> puntos;

    public Convocatoria(){}

    public Convocatoria(int id) {
        super(id);
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCondominio() {
        return condominio;
    }

    public void setCondominio(String condominio) {
        this.condominio = condominio;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUbicacionInterna() {
        return ubicacionInterna;
    }

    public void setUbicacionInterna(String ubicacionInterna) {
        this.ubicacionInterna = ubicacionInterna;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public Long getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(long fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public List<PuntoOdD> getPuntos() {
        return puntos;
    }

    public void setPuntos(List<PuntoOdD> puntos) {
        this.puntos = puntos;
    }

    public List<String> obtenerDescripciones(){
        List<String> descs = new ArrayList<>();
        for(PuntoOdD punto : puntos)
            descs.add(punto.getDescripcion());
        return descs;
    }

    @Override
    public void setId(int id){
        super.setId(id);
        setIdConvocatoriaToPuntos();
    }

    private void setIdConvocatoriaToPuntos(){
        for(PuntoOdD punto : puntos){
            punto.setIdConvocatoria(getId());
        }
    }

    public String formatoDeTiempo(int choose){
        Calendar c = Calendar.getInstance();
        switch(choose) {
            case PRIMERA_CONV:
                c.setTimeInMillis(fechaInicio);
                break;
            case SEGUNDA_CONV:
                c.setTimeInMillis(fechaInicio + (30*60*1000));
                break;
            case TERCERA_CONV:
                c.setTimeInMillis(fechaInicio + (60*60*1000));
                break;
        }
        int horaDelDia = c.get(Calendar.HOUR_OF_DAY);
        int minuto = c.get(Calendar.MINUTE);
        return (horaDelDia < 10 ? "0" + horaDelDia : horaDelDia)
                + ":" +
                (minuto < 10 ? "0" + minuto : minuto);
    }
}

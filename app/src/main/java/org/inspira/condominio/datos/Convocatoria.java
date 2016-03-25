package org.inspira.condominio.datos;

import java.util.Calendar;

/**
 * Created by Siempre on 22/02/2016.
 */
public class Convocatoria extends ModeloDeDatos{

    public static final int TERCERA_CONV = 3;
    public static final int SEGUNDA_CONV = 2;
    public static final int PRIMERA_CONV = 1;
    private String asunto;
    private String ubicacionInterna;
    private Long fechaInicio;
    private String firma;
    private String email;

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

    public String getUbicacionInterna() {
        return ubicacionInterna;
    }

    public void setUbicacionInterna(String ubicacionInterna) {
        this.ubicacionInterna = ubicacionInterna;
    }

    public Long getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Long fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setId(int id){
        super.setId(id);
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

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }
}
package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class Escolaridad extends ModeloDeDatos {

    private String escolaridad;

    public Escolaridad() {
    }

    public Escolaridad(int id) {
        super(id);
    }

    public String getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(String escolaridad) {
        this.escolaridad = escolaridad;
    }
}

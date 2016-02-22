package org.inspira.condominio.datos;

import java.io.Serializable;

/**
 * Created by Siempre on 22/02/2016.
 */
public class ModeloDeDatos implements Serializable {

    private int id;

    public ModeloDeDatos(){}

    public ModeloDeDatos(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

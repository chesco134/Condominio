package org.inspira.condominio.datos;

/**
 * Created by Siempre on 22/02/2016.
 */
public class PuntoOdD extends ModeloDeDatos{

    private String descripcion;
    private int idConvocatoria;

    public PuntoOdD(){}

    public PuntoOdD(int id) {
        super(id);
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdConvocatoria() {
        return idConvocatoria;
    }

    public void setIdConvocatoria(int idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }
}
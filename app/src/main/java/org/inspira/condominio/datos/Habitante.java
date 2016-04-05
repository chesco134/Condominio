package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class Habitante extends Persona {

    private boolean genero;
    private String nombreDepartamento;
    private int idTorre;

    public Habitante() {
    }

    public Habitante(int id) {
        super(id);
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public boolean isGenero() {
        return genero;
    }

    public void setGenero(boolean genero) {
        this.genero = genero;
    }

    public int getIdTorre() {
        return idTorre;
    }

    public void setIdTorre(int idTorre) {
        this.idTorre = idTorre;
    }
}

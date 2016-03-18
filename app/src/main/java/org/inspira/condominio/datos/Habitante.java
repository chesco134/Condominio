package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class Habitante extends ModeloDeDatos {

    private String nombres;
    private String apPaterno;
    private String apMaterno;
    private String nombreDepartamento;

    public Habitante() {
    }

    public Habitante(int id) {
        super(id);
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }
}

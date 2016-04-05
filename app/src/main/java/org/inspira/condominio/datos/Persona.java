package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 4/04/16.
 */
public class Persona extends ModeloDeDatos{

    private String nombres;
    private String apPaterno;
    private String apMaterno;

    public Persona(int id) {
        super(id);
    }

    public Persona() {
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
}

package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class NombreDeUsuario extends ModeloDeDatos {

    private String nombres;
    private String apPaterno;
    private String apMaterno;

    public NombreDeUsuario() {
    }

    public NombreDeUsuario(int id) {
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
}

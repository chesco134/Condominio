package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class TipoDeAdministrador extends ModeloDeDatos {

    private String descripcion;

    public TipoDeAdministrador() {
    }

    public TipoDeAdministrador(int id) {
        super(id);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

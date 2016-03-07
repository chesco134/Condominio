package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 6/03/16.
 */
public class RazonDeEgreso extends ModeloDeDatos {

    private String razonDeEgreso;

    public RazonDeEgreso() {
    }

    public RazonDeEgreso(int id) {
        super(id);
    }

    public String getRazonDeEgreso() {
        return razonDeEgreso;
    }

    public void setRazonDeEgreso(String razonDeEgreso) {
        this.razonDeEgreso = razonDeEgreso;
    }
}

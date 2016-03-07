package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 6/03/16.
 */
public class RazonDeIngreso extends ModeloDeDatos{

    private String razonDeIngreso;

    public RazonDeIngreso() {
    }

    public RazonDeIngreso(int id) {
        super(id);
    }

    public String getRazonDeIngreso() {
        return razonDeIngreso;
    }

    public void setRazonDeIngreso(String razonDeIngreso) {
        this.razonDeIngreso = razonDeIngreso;
    }
}

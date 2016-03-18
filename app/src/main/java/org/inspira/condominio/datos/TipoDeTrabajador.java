package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class TipoDeTrabajador extends ModeloDeDatos {

    private String tipoDeTrabajador;

    public TipoDeTrabajador() {
    }

    public TipoDeTrabajador(int id) {
        super(id);
    }

    public String getTipoDeTrabajador() {
        return tipoDeTrabajador;
    }

    public void setTipoDeTrabajador(String tipoDeTrabajador) {
        this.tipoDeTrabajador = tipoDeTrabajador;
    }
}

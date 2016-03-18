package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class TipoDeSiniestro extends ModeloDeDatos {

    private String tipoDeSiniestro;

    public TipoDeSiniestro() {
    }

    public TipoDeSiniestro(int id) {
        super(id);
    }

    public String getTipoDeSiniestro() {
        return tipoDeSiniestro;
    }

    public void setTipoDeSiniestro(String tipoDeSiniestro) {
        this.tipoDeSiniestro = tipoDeSiniestro;
    }
}

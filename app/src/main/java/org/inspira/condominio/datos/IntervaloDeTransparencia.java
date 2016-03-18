package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class IntervaloDeTransparencia extends ModeloDeDatos {

    private String intervaloDeTransparencia;

    public IntervaloDeTransparencia() {
    }

    public IntervaloDeTransparencia(int id) {
        super(id);
    }

    public String getIntervaloDeTransparencia() {
        return intervaloDeTransparencia;
    }

    public void setIntervaloDeTransparencia(String intervaloDeTransparencia) {
        this.intervaloDeTransparencia = intervaloDeTransparencia;
    }
}

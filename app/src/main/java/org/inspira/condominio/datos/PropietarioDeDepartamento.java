package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class PropietarioDeDepartamento implements Shareable{

    private Habitante habitante;
    private boolean poseeSeguro;

    public Habitante getHabitante() {
        return habitante;
    }

    public void setHabitante(Habitante habitante) {
        this.habitante = habitante;
    }

    public boolean isPoseeSeguro() {
        return poseeSeguro;
    }

    public void setPoseeSeguro(boolean poseeSeguro) {
        this.poseeSeguro = poseeSeguro;
    }
}

package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class PropietarioDeDepartamento implements Shareable{

    private int idHabitante;
    private boolean poseeSeguro;

    public int getIdHabitante() {
        return idHabitante;
    }

    public void setIdHabitante(int idHabitante) {
        this.idHabitante = idHabitante;
    }

    public boolean isPoseeSeguro() {
        return poseeSeguro;
    }

    public void setPoseeSeguro(boolean poseeSeguro) {
        this.poseeSeguro = poseeSeguro;
    }
}

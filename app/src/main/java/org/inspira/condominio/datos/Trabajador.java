package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class Trabajador extends Persona {

    private float salario;
    private boolean poseeSeguroSocial;
    private boolean genero;
    private int idAdministracion;
    private int idTipoDeTrabajador;

    public Trabajador(int id) {
        super(id);
    }

    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }

    public boolean isPoseeSeguroSocial() {
        return poseeSeguroSocial;
    }

    public void setPoseeSeguroSocial(boolean poseeSeguroSocial) {
        this.poseeSeguroSocial = poseeSeguroSocial;
    }

    public int getIdAdministracion() {
        return idAdministracion;
    }

    public void setIdAdministracion(int idAdministracion) {
        this.idAdministracion = idAdministracion;
    }

    public boolean isGenero() {
        return genero;
    }

    public void setGenero(boolean genero) {
        this.genero = genero;
    }

    public int getIdTipoDeTrabajador() {
        return idTipoDeTrabajador;
    }

    public void setIdTipoDeTrabajador(int idTipoDeTrabajador) {
        this.idTipoDeTrabajador = idTipoDeTrabajador;
    }
}

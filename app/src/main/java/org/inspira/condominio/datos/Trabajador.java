package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class Trabajador extends ModeloDeDatos {

    private String nombres;
    private String apPaterno;
    private String apMaterno;
    private float salario;
    private boolean poseeSeguroSocial;
    private Administracion administracion;
    private TipoDeTrabajador tipoDeTrabajador;

    public Trabajador() {
    }

    public Trabajador(int id) {
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

    public Administracion getAdministracion() {
        return administracion;
    }

    public void setAdministracion(Administracion administracion) {
        this.administracion = administracion;
    }

    public TipoDeTrabajador getTipoDeTrabajador() {
        return tipoDeTrabajador;
    }

    public void setTipoDeTrabajador(TipoDeTrabajador tipoDeTrabajador) {
        this.tipoDeTrabajador = tipoDeTrabajador;
    }
}

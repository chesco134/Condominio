package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 6/03/16.
 */
public class Ingreso extends ModeloDeDatos {

    private int idRazonDeIngreso;
    private int idConceptoDeIngreso;
    private float monto;
    private String nombre;
    private String departamento;
    private long fecha;
    private String sello;

    public Ingreso() {
    }

    public Ingreso(int id) {
        super(id);
    }

    public int getIdRazonDeIngreso() {
        return idRazonDeIngreso;
    }

    public void setIdRazonDeIngreso(int idRazonDeIngreso) {
        this.idRazonDeIngreso = idRazonDeIngreso;
    }

    public int getIdConceptoDeIngreso() {
        return idConceptoDeIngreso;
    }

    public void setIdConceptoDeIngreso(int idConceptoDeIngreso) {
        this.idConceptoDeIngreso = idConceptoDeIngreso;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getSello() {
        return sello;
    }

    public void setSello(String sello) {
        this.sello = sello;
    }
}

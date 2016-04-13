package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 6/03/16.
 */
public class Ingreso extends ModeloDeDatos {

    private RazonDeIngreso razonDeIngreso;
    private ConceptoDeIngreso conceptoDeIngreso;
    private float monto;
    private int idHabitante;
    private String departamento;
    private long fecha;
    private String email;

    public Ingreso() {
    }

    public Ingreso(int id) {
        super(id);
    }

    public RazonDeIngreso getRazonDeIngreso() {
        return razonDeIngreso;
    }

    public void setRazonDeIngreso(RazonDeIngreso razonDeIngreso) {
        this.razonDeIngreso = razonDeIngreso;
    }

    public ConceptoDeIngreso getConceptoDeIngreso() {
        return conceptoDeIngreso;
    }

    public void setConceptoDeIngreso(ConceptoDeIngreso conceptoDeIngreso) {
        this.conceptoDeIngreso = conceptoDeIngreso;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public int getIdHabitante() {
        return idHabitante;
    }

    public void setIdHabitante(int idHabitante) {
        this.idHabitante = idHabitante;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


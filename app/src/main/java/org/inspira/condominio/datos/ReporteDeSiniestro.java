package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class ReporteDeSiniestro extends ModeloDeDatos {

    private String descripcion;
    private TipoDeSiniestro tipoDeSiniestro;
    private Habitante habitante;

    public ReporteDeSiniestro() {
    }

    public ReporteDeSiniestro(int id) {
        super(id);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoDeSiniestro getTipoDeSiniestro() {
        return tipoDeSiniestro;
    }

    public void setTipoDeSiniestro(TipoDeSiniestro tipoDeSiniestro) {
        this.tipoDeSiniestro = tipoDeSiniestro;
    }

    public Habitante getHabitante() {
        return habitante;
    }

    public void setHabitante(Habitante habitante) {
        this.habitante = habitante;
    }
}

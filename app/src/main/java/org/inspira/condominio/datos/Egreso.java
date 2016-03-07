package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 6/03/16.
 */
public class Egreso extends ModeloDeDatos {

    private int idRazon_de_Egreso;
    private float monto;
    private String favorecido;
    private long fecha; // La fecha de expedición del comprobante
    private String sello;

    public Egreso() {
    }

    public Egreso(int id) {
        super(id);
    }

    public int getIdRazon_de_Egreso() {
        return idRazon_de_Egreso;
    }

    public void setIdRazon_de_Egreso(int idRazon_de_Egreso) {
        this.idRazon_de_Egreso = idRazon_de_Egreso;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public String getFavorecido() {
        return favorecido;
    }

    public void setFavorecido(String favorecido) {
        this.favorecido = favorecido;
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

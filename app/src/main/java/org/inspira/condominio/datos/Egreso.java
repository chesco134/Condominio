package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 6/03/16.
 */
public class Egreso extends ModeloDeDatos {

    private int idRazonDeEgreso;
    private float monto;
    private String favorecido;
    private long fecha; // La fecha de expedición del comprobante
    private boolean esExtraordinario;
    private String email;

    public Egreso() {
    }

    public Egreso(int id) {
        super(id);
    }

    public int getIdRazonDeEgreso() {
        return idRazonDeEgreso;
    }

    public void setIdRazonDeEgreso(int idRazon_de_Egreso) {
        this.idRazonDeEgreso = idRazon_de_Egreso;
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

    public boolean isEsExtraordinario() {
        return esExtraordinario;
    }

    public void setEsExtraordinario(boolean esExtraordinario) {
        this.esExtraordinario = esExtraordinario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

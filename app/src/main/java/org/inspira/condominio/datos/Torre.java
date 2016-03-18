package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class Torre extends ModeloDeDatos {

    private String nombre;
    private boolean poseeElevador;
    private int cantidadDePisos;
    private int cantidadDeFocos;
    private int cantidadDeDepartamentos;
    private Condominio condominio;

    public Torre() {
    }

    public Torre(int id) {
        super(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isPoseeElevador() {
        return poseeElevador;
    }

    public void setPoseeElevador(boolean poseeElevador) {
        this.poseeElevador = poseeElevador;
    }

    public int getCantidadDePisos() {
        return cantidadDePisos;
    }

    public void setCantidadDePisos(int cantidadDePisos) {
        this.cantidadDePisos = cantidadDePisos;
    }

    public int getCantidadDeFocos() {
        return cantidadDeFocos;
    }

    public void setCantidadDeFocos(int cantidadDeFocos) {
        this.cantidadDeFocos = cantidadDeFocos;
    }

    public int getCantidadDeDepartamentos() {
        return cantidadDeDepartamentos;
    }

    public void setCantidadDeDepartamentos(int cantidadDeDepartamentos) {
        this.cantidadDeDepartamentos = cantidadDeDepartamentos;
    }

    public Condominio getCondominio() {
        return condominio;
    }

    public void setCondominio(Condominio condominio) {
        this.condominio = condominio;
    }
}

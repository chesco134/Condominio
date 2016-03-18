package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class IntegranteDeComiteDeVigilancia extends ModeloDeDatos {

    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private Administracion administracion;

    public IntegranteDeComiteDeVigilancia() {
    }

    public IntegranteDeComiteDeVigilancia(int id) {
        super(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Administracion getAdministracion() {
        return administracion;
    }

    public void setAdministracion(Administracion administracion) {
        this.administracion = administracion;
    }
}

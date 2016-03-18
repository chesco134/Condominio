package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 29/02/16.
 */
public class Usuario implements Shareable {

    private String email;
    private long dateOfBirth;
    private float remuneracion;
    private Administracion administracion;
    private Escolaridad escolaridad;
    private TipoDeAdministrador tipoDeAdministrador;
    private NombreDeUsuario nombreDeUsuario;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public float getRemuneracion() {
        return remuneracion;
    }

    public void setRemuneracion(float remuneracion) {
        this.remuneracion = remuneracion;
    }

    public Administracion getAdministracion() {
        return administracion;
    }

    public void setAdministracion(Administracion administracion) {
        this.administracion = administracion;
    }

    public Escolaridad getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(Escolaridad escolaridad) {
        this.escolaridad = escolaridad;
    }

    public TipoDeAdministrador getTipoDeAdministrador() {
        return tipoDeAdministrador;
    }

    public void setTipoDeAdministrador(TipoDeAdministrador tipoDeAdministrador) {
        this.tipoDeAdministrador = tipoDeAdministrador;
    }

    public NombreDeUsuario getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(NombreDeUsuario nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }
}

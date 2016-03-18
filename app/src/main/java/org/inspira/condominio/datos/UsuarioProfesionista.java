package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class UsuarioProfesionista implements Shareable {

    private String profesion;
    private Usuario usuario;

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

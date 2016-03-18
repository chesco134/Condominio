package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class ContactoUsuario implements Shareable {

    private String contacto;
    private Usuario usuario;

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

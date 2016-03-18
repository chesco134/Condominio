package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class ContactoAdministracion extends ModeloDeDatos {

    private String contacto;
    private Administracion administracion;

    public ContactoAdministracion() {
    }

    public ContactoAdministracion(int id) {
        super(id);
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Administracion getAdministracion() {
        return administracion;
    }

    public void setAdministracion(Administracion administracion) {
        this.administracion = administracion;
    }
}

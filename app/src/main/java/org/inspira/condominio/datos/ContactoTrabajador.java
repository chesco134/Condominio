package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 4/04/16.
 */
public class ContactoTrabajador extends ModeloDeDatos {

    private String contacto;
    private int idTrabajador;

    public ContactoTrabajador(int id) {
        super(id);
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }
}

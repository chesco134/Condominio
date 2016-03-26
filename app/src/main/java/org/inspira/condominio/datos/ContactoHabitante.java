package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class ContactoHabitante extends ModeloDeDatos {

    private String contacto;
    private int idHabitante;

    public ContactoHabitante() {
    }

    public ContactoHabitante(int id) {
        super(id);
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public int getIdHabitante() {
        return idHabitante;
    }

    public void setIdHabitante(int idHabitante) {
        this.idHabitante = idHabitante;
    }
}

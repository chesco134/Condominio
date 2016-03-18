package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class ContactoHabitante extends ModeloDeDatos {

    private String contacto;
    private Habitante habitante;

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

    public Habitante getHabitante() {
        return habitante;
    }

    public void setHabitante(Habitante habitante) {
        this.habitante = habitante;
    }
}

package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 6/03/16.
 */
public class ConceptoDeIngreso extends ModeloDeDatos {

    private String conceptoDeIngreso;

    public ConceptoDeIngreso() {
    }

    public ConceptoDeIngreso(int id) {
        super(id);
    }

    public String getConceptoDeIngreso() {
        return conceptoDeIngreso;
    }

    public void setConceptoDeIngreso(String conceptoDeIngreso) {
        this.conceptoDeIngreso = conceptoDeIngreso;
    }
}

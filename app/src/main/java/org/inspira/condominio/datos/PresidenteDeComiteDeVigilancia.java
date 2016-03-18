package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class PresidenteDeComiteDeVigilancia implements Shareable {

    private String escolaridad;
    private IntegranteDeComiteDeVigilancia integranteDeComiteDeVigilancia;

    public String getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(String escolaridad) {
        this.escolaridad = escolaridad;
    }

    public IntegranteDeComiteDeVigilancia getIntegranteDeComiteDeVigilancia() {
        return integranteDeComiteDeVigilancia;
    }

    public void setIntegranteDeComiteDeVigilancia(IntegranteDeComiteDeVigilancia integranteDeComiteDeVigilancia) {
        this.integranteDeComiteDeVigilancia = integranteDeComiteDeVigilancia;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inspira.condominio.datos;

/**
 *
 * @author jcapiz
 */
public class InformacionEgreso {
    
    private long fecha;
    private String descripcion;
    private float monto;
    private String tipoDePago;

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public String getTipoDePago() {
        return tipoDePago;
    }

    public void setTipoDePago(String tipoDePago) {
        this.tipoDePago = tipoDePago;
    }
}

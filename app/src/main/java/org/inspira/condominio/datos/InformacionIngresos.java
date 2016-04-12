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
public class InformacionIngresos {
    
    private float montoCuentaBancaria;
    private float montoEnCajaDeAdministracion;
    private int totalhabitantes;
    private int totalRegulares;

    public InformacionIngresos(float montoCuentaBancaria, float montoEnCajaDeAdministracion) {
        this.montoCuentaBancaria = montoCuentaBancaria;
        this.montoEnCajaDeAdministracion = montoEnCajaDeAdministracion;
    }

    public float getMontoCuentaBancaria() {
        return montoCuentaBancaria;
    }

    public void setMontoCuentaBancaria(float montoCuentaBancaria) {
        this.montoCuentaBancaria = montoCuentaBancaria;
    }

    public float getMontoEnCajaDeAdministracion() {
        return montoEnCajaDeAdministracion;
    }

    public void setMontoEnCajaDeAdministracion(float montoEnCajaDeAdministracion) {
        this.montoEnCajaDeAdministracion = montoEnCajaDeAdministracion;
    }

    public int getTotalhabitantes() {
        return totalhabitantes;
    }

    public void setTotalhabitantes(int totalhabitantes) {
        this.totalhabitantes = totalhabitantes;
    }

    public int getTotalRegulares() {
        return totalRegulares;
    }

    public void setTotalRegulares(int totalRegulares) {
        this.totalRegulares = totalRegulares;
    }
}

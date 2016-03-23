package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class Administracion extends ModeloDeDatos {

    private boolean poseePlanesDeTrabajo;
    private float costoDeCuotaDeMantenimientoMensual;
    private float costoDeCuotaAnual;
    private float promedioInicialDeEgresos;
    private float promedioInicialDeMorosidad;
    private boolean poseeMantenimientoProfesionalElevadores;
    private boolean poseePersonalidadCapacitadoEnSeguridadIntramuros;
    private boolean poseeMantenimientoProfesionalCuartoDeMaquinas;
    private boolean poseeWiFiAbierto;
    private int idCondominio;
    private IntervaloDeTransparencia intervaloDeTransparencia;

    public Administracion() {
    }

    public Administracion(int id) {
        super(id);
    }

    public boolean isPoseePlanesDeTrabajo() {
        return poseePlanesDeTrabajo;
    }

    public void setPoseePlanesDeTrabajo(boolean poseePlanesDeTrabajo) {
        this.poseePlanesDeTrabajo = poseePlanesDeTrabajo;
    }

    public float getCostoDeCuotaDeMantenimientoMensual() {
        return costoDeCuotaDeMantenimientoMensual;
    }

    public void setCostoDeCuotaDeMantenimientoMensual(float costoDeCuotaDeMantenimientoMensual) {
        this.costoDeCuotaDeMantenimientoMensual = costoDeCuotaDeMantenimientoMensual;
    }

    public float getCostoDeCuotaAnual() {
        return costoDeCuotaAnual;
    }

    public void setCostoDeCuotaAnual(float costoDeCuotaAnual) {
        this.costoDeCuotaAnual = costoDeCuotaAnual;
    }

    public float getPromedioInicialDeEgresos() {
        return promedioInicialDeEgresos;
    }

    public void setPromedioInicialDeEgresos(float promedioInicialDeEgresos) {
        this.promedioInicialDeEgresos = promedioInicialDeEgresos;
    }

    public float getPromedioInicialDeMorosidad() {
        return promedioInicialDeMorosidad;
    }

    public void setPromedioInicialDeMorosidad(float promedioInicialDeMorosidad) {
        this.promedioInicialDeMorosidad = promedioInicialDeMorosidad;
    }

    public boolean isPoseeMantenimientoProfesionalElevadores() {
        return poseeMantenimientoProfesionalElevadores;
    }

    public void setPoseeMantenimientoProfesionalElevadores(boolean poseeMantenimientoProfesionalElevadores) {
        this.poseeMantenimientoProfesionalElevadores = poseeMantenimientoProfesionalElevadores;
    }

    public boolean isPoseePersonalidadCapacitadoEnSeguridadIntramuros() {
        return poseePersonalidadCapacitadoEnSeguridadIntramuros;
    }

    public void setPoseePersonalidadCapacitadoEnSeguridadIntramuros(boolean poseePersonalidadCapacitadoEnSeguridadIntramuros) {
        this.poseePersonalidadCapacitadoEnSeguridadIntramuros = poseePersonalidadCapacitadoEnSeguridadIntramuros;
    }

    public boolean isPoseeMantenimientoProfesionalCuartoDeMaquinas() {
        return poseeMantenimientoProfesionalCuartoDeMaquinas;
    }

    public void setPoseeMantenimientoProfesionalCuartoDeMaquinas(boolean poseeMantenimientoProfesionalCuartoDeMaquinas) {
        this.poseeMantenimientoProfesionalCuartoDeMaquinas = poseeMantenimientoProfesionalCuartoDeMaquinas;
    }

    public boolean isPoseeWiFiAbierto() {
        return poseeWiFiAbierto;
    }

    public void setPoseeWiFiAbierto(boolean poseeWiFiAbierto) {
        this.poseeWiFiAbierto = poseeWiFiAbierto;
    }

    public int getIdCondominio() {
        return idCondominio;
    }

    public void setIdCondominio(int idCondominio) {
        this.idCondominio = idCondominio;
    }

    public IntervaloDeTransparencia getIntervaloDeTransparencia() {
        return intervaloDeTransparencia;
    }

    public void setIntervaloDeTransparencia(IntervaloDeTransparencia intervaloDeTransparencia) {
        this.intervaloDeTransparencia = intervaloDeTransparencia;
    }
}

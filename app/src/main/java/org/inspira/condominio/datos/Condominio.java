package org.inspira.condominio.datos;

/**
 * Created by jcapiz on 17/03/16.
 */
public class Condominio extends ModeloDeDatos {

    private String nombre;
    private String direccion;
    private int edad;
    private TipoDeCondominio tipoDeCondominio;
    private String inmoviliaria;
    private boolean poseeSalaDeJuntas;
    private boolean poseeGym;
    private boolean poseeEspacioRecreativo;
    private boolean poseeEspacioCultural;
    private boolean poseeOficinasAdministrativas;
    private boolean poseeAlarmaSismica;
    private int cantidadDeLugaresEstacionamiento;
    private int cantidadDeLugaresEstacionamientoVisitas;
    private float costoAproximadoPorUnidadPrivativa;
    private float capacidadDeCisterna;
    private boolean poseeCisternaAguaPluvial;

    public Condominio() {
    }

    public Condominio(int id) {
        super(id);
    }

    public String getDireccion() {
        return direccion;
    }

    public int getEdad() {
        return edad;
    }

    public TipoDeCondominio getTipoDeCondominio() {
        return tipoDeCondominio;
    }

    public String getInmoviliaria() {
        return inmoviliaria;
    }

    public boolean isPoseeSalaDeJuntas() {
        return poseeSalaDeJuntas;
    }

    public boolean isPoseeGym() {
        return poseeGym;
    }

    public boolean isPoseeEspacioRecreativo() {
        return poseeEspacioRecreativo;
    }

    public boolean isPoseeEspacioCultural() {
        return poseeEspacioCultural;
    }

    public boolean isPoseeOficinasAdministrativas() {
        return poseeOficinasAdministrativas;
    }

    public boolean isPoseeAlarmaSismica() {
        return poseeAlarmaSismica;
    }

    public int getCantidadDeLugaresEstacionamiento() {
        return cantidadDeLugaresEstacionamiento;
    }

    public int getCantidadDeLugaresEstacionamientoVisitas() {
        return cantidadDeLugaresEstacionamientoVisitas;
    }

    public float getCostoAproximadoPorUnidadPrivativa() {
        return costoAproximadoPorUnidadPrivativa;
    }

    public float getCapacidadDeCisterna() {
        return capacidadDeCisterna;
    }

    public boolean isPoseeCisternaAguaPluvial() {
        return poseeCisternaAguaPluvial;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setTipoDeCondominio(TipoDeCondominio tipoDeCondominio) {
        this.tipoDeCondominio = tipoDeCondominio;
    }

    public void setInmoviliaria(String inmoviliaria) {
        this.inmoviliaria = inmoviliaria;
    }

    public void setPoseeSalaDeJuntas(boolean poseeSalaDeJuntas) {
        this.poseeSalaDeJuntas = poseeSalaDeJuntas;
    }

    public void setPoseeGym(boolean poseeGym) {
        this.poseeGym = poseeGym;
    }

    public void setPoseeEspacioRecreativo(boolean poseeEspacioRecreativo) {
        this.poseeEspacioRecreativo = poseeEspacioRecreativo;
    }

    public void setPoseeEspacioCultural(boolean poseeEspacioCultural) {
        this.poseeEspacioCultural = poseeEspacioCultural;
    }

    public void setPoseeOficinasAdministrativas(boolean poseeOficinasAdministrativas) {
        this.poseeOficinasAdministrativas = poseeOficinasAdministrativas;
    }

    public void setPoseeAlarmaSismica(boolean poseeAlarmaSismica) {
        this.poseeAlarmaSismica = poseeAlarmaSismica;
    }

    public void setCantidadDeLugaresEstacionamiento(int cantidadDeLugaresEstacionamiento) {
        this.cantidadDeLugaresEstacionamiento = cantidadDeLugaresEstacionamiento;
    }

    public void setCantidadDeLugaresEstacionamientoVisitas(int cantidadDeLugaresEstacionamientoVisitas) {
        this.cantidadDeLugaresEstacionamientoVisitas = cantidadDeLugaresEstacionamientoVisitas;
    }

    public void setCostoAproximadoPorUnidadPrivativa(float costoAproximadoPorUnidadPrivativa) {
        this.costoAproximadoPorUnidadPrivativa = costoAproximadoPorUnidadPrivativa;
    }

    public void setCapacidadDeCisterna(float capacidadDeCisterna) {
        this.capacidadDeCisterna = capacidadDeCisterna;
    }

    public void setPoseeCisternaAguaPluvial(boolean poseeCisternaAguaPluvial) {
        this.poseeCisternaAguaPluvial = poseeCisternaAguaPluvial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

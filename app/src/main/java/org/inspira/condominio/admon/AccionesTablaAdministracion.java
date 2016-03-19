package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.datos.Administracion;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.IntervaloDeTransparencia;

/**
 * Created by jcapiz on 18/03/16.
 */
public class AccionesTablaAdministracion {

    public static int obtenerIdIntervaloTransparencia(Context context, String intervaloTransparencia){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idIntervalo_Transparencia from Intervalo_Transparencia where intervalo_transparencia like ?", new String[]{intervaloTransparencia});
        int idIntervaloTransparencia = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return idIntervaloTransparencia;
    }

    public static IntervaloDeTransparencia obtenerIntervaloDeTransparencia(Context context, int idIntervaloDeTransparencia){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select intervalo_de_transparencia from Intervalo_de_Transparencia where idIntervalo_de_Transparencia = CAST(? as INTEGER)", new String[]{String.valueOf(idIntervaloDeTransparencia)});
        IntervaloDeTransparencia intervaloDeTransparencia;
        if(c.moveToFirst()){
            intervaloDeTransparencia = new IntervaloDeTransparencia(idIntervaloDeTransparencia);
            intervaloDeTransparencia.setIntervaloDeTransparencia(c.getString(0));
        }else{
            intervaloDeTransparencia = null;
        }
        c.close();
        db.close();
        return intervaloDeTransparencia;
    }

    public static int agregaAdministracion(Context context, Administracion administracion){
        ContentValues values = new ContentValues();
        values.put("costo_de_cuota_anual", administracion.getCostoDeCuotaAnual());
        values.put("costo_de_cuota_de_mantenimiento_mensual", administracion.getCostoDeCuotaDeMantenimientoMensual());
        values.put("promedio_inicial_de_egresos", administracion.getPromedioInicialDeEgresos());
        values.put("promedio_inicial_de_morosidad", administracion.getPromedioInicialDeMorosidad());
        values.put("idIntervalo_de_Transparencia", administracion.getIntervaloDeTransparencia().getId());
        values.put("idCondominio", administracion.getCondominio().getId());
        values.put("posee_mantenimiento_profesional_al_cuarto_de_maquinas", administracion.isPoseeMantenimientoProfesionalCuartoDeMaquinas());
        values.put("posee_mantenimiento_profesional_a_elevadores", administracion.isPoseeMantenimientoProfesionalElevadores());
        values.put("posee_personal_capacitado_en_seguridad_intramuros", administracion.isPoseePersonalidadCapacitadoEnSeguridadIntramuros());
        values.put("posee_planes_de_trabajo", administracion.isPoseePlanesDeTrabajo());
        values.put("posee_wifi_abierto", administracion.isPoseeWiFiAbierto());
        values.put("idAdministracion", administracion.getId());
        CondominioBD condominioBD = new CondominioBD(context);
        SQLiteDatabase db = condominioBD.getWritableDatabase();
        db.insert("Administracion", "---", values);
        db = condominioBD.getReadableDatabase();
        Cursor c = db.rawQuery("select last_insert_rowid()", null);
        int idAdministracion = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return idAdministracion;
    }

    public static Administracion obtenerAdministracion(Context context, int idAdministracion){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Administracion where idAdministracion = CAST(? as INTEGER)", new String[]{String.valueOf(idAdministracion)});
        Administracion administracion;
        if(c.moveToFirst()){
            administracion = new Administracion(idAdministracion);
            administracion.setCondominio(AccionesTablaCondominio.obtenerCondominio(context, ProveedorDeRecursos.obtenerIdCondominio(context)));
            administracion.setCostoDeCuotaAnual(c.getFloat(c.getColumnIndex("costo_de_cuota_anual")));
            administracion.setCostoDeCuotaDeMantenimientoMensual(c.getFloat(c.getColumnIndex("costo_de_cuota_de_mantenimiento_mensual")));
            administracion.setPoseeMantenimientoProfesionalCuartoDeMaquinas(c.getInt(c.getColumnIndex("posee_mantenimiento_profesional_al_cuarto_de_maquinas")) != 0);
            administracion.setPoseeMantenimientoProfesionalElevadores(c.getInt(c.getColumnIndex("posee_mantenimiento_profesional_a_elevadores")) != 0);
            administracion.setPoseePersonalidadCapacitadoEnSeguridadIntramuros(c.getInt(c.getColumnIndex("posee_personal_capacitado_en_seguridad_intramuros")) != 0);
            administracion.setPoseePlanesDeTrabajo(c.getInt(c.getColumnIndex("posee_planes_de_trabajo")) != 0);
            administracion.setPoseeWiFiAbierto(c.getInt(c.getColumnIndex("posee_wifi_abierto")) != 0);
            administracion.setPromedioInicialDeEgresos(c.getFloat(c.getColumnIndex("promedio_inicial_de_egresos")));
            administracion.setPromedioInicialDeMorosidad(c.getFloat(c.getColumnIndex("promedio_inicial_de_morosidad")));
            IntervaloDeTransparencia intervaloDeTransparencia = obtenerIntervaloDeTransparencia(context, c.getInt(c.getColumnIndex("idIntervalo_de_Transparencia")));
            administracion.setIntervaloDeTransparencia(intervaloDeTransparencia);
        }else
            administracion = null;
        c.close();
        db.close();
        return administracion;
    }
}

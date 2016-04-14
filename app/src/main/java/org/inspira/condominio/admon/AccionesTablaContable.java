package org.inspira.condominio.admon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.datos.ConceptoDeIngreso;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.datos.Egreso;
import org.inspira.condominio.datos.Ingreso;
import org.inspira.condominio.datos.RazonDeEgreso;
import org.inspira.condominio.datos.RazonDeIngreso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jcapiz on 9/04/16.
 */
public class AccionesTablaContable {

    public static String obtenerRazonDeIngreso(Context context, int idRazonDeIngreso){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select Razon_de_Ingreso from Razon_de_Ingreso where idRazon_de_Ingreso = CAST(? as INTEGER)",
                new String[]{String.valueOf(idRazonDeIngreso)});
        String razonDeIngreso = c.moveToFirst() ? c.getString(0) : "NaN";
        c.close();
        db.close();
        return razonDeIngreso;
    }

    public static String obtenerConceptoDeIngreso(Context context, int idConceptoDeIngreso){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select Concepto_de_Ingreso from Concepto_de_Ingreso where idConcepto_de_Ingreso = CAST(? as INTEGER)",
                new String[]{String.valueOf(idConceptoDeIngreso)});
        String conceptoDeIngreso = c.moveToFirst() ? c.getString(0) : "NaN";
        c.close();
        db.close();
        return conceptoDeIngreso;
    }

    public static String obtenerRazonDeEgreso(Context context, int idRazonDeEgreso){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select Razon_de_Egreso from Razon_de_Egreso where idRazon_de_Egreso = CAST(? as INTEGER)",
                new String[]{String.valueOf(idRazonDeEgreso)});
        String razonDeEgreso = c.moveToFirst() ? c.getString(0) : "NaN";
        c.close();
        db.close();
        return razonDeEgreso;
    }

    public static List<String> obtenerRazonesDeIngreso(Context context){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select Razon_de_Ingreso from Razon_de_Ingreso", null);
        List<String> razonesDeIngreso = new ArrayList<>();
        while(c.moveToNext())
            razonesDeIngreso.add(c.getString(0));
        c.close();
        db.close();
        return  razonesDeIngreso;
    }

    public static void agregarRazonDeIngreso(Context context, RazonDeIngreso razonDeIngreso){
        ContentValues values = new ContentValues();
        values.put("idRazon_de_Ingreso", razonDeIngreso.getId());
        values.put("Razon_de_Ingreso", razonDeIngreso.getRazonDeIngreso());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Razon_de_Ingreso", "---", values);
        db.close();
    }

    public static List<String> obtenerConceptosDeIngreso(Context context){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select Concepto_de_Ingreso from Concepto_de_Ingreso", null);
        List<String> conceptosDeIngreso = new ArrayList<>();
        while(c.moveToNext())
            conceptosDeIngreso.add(c.getString(0));
        c.close();
        db.close();
        return  conceptosDeIngreso;
    }

    public static void agregarConceptoDeIngreso(Context context, ConceptoDeIngreso conceptoDeIngreso){
        ContentValues values = new ContentValues();
        values.put("idConcepto_de_Ingreso", conceptoDeIngreso.getId());
        values.put("Concepto_de_Ingreso", conceptoDeIngreso.getConceptoDeIngreso());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Concepto_de_Ingreso", "---", values);
        db.close();
    }

    public static List<String> obtenerRazonesDeEgreso(Context context){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select Razon_de_Egreso from Razon_de_Egreso", null);
        List<String> razonesDeEgreso = new ArrayList<>();
        while(c.moveToNext())
            razonesDeEgreso.add(c.getString(0));
        c.close();
        db.close();
        return  razonesDeEgreso;
    }

    public static void agregarRazonDeEgreso(Context context, RazonDeEgreso razonDeEgreso) {
        ContentValues values = new ContentValues();
        values.put("idRazon_de_Egreso", razonDeEgreso.getId());
        values.put("Razon_de_Egreso", razonDeEgreso.getRazonDeEgreso());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Razon_de_Egreso", "---", values);
        db.close();
    }

    public static boolean comprobarExistenciaDeTexto(Context context, String column, String value, String table){
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + table + " where " + column + " like ?",
                new String[]{value});
        c.moveToFirst();
        boolean existe = c.getInt(0) > 0;
        c.close();
        db.close();
        return existe;
    }

    public static int obtenerIdConceptoDeIngreso(Context context, String concepto) {
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idConcepto_de_Ingreso from Concepto_de_Ingreso where Concepto_de_Ingreso like ?",
                new String[]{concepto});
        int id = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return id;
    }

    public static int obtenerIdRazonDeEgreso(Context context, String razon) {
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idRazon_de_Egreso from Razon_de_Egreso where Razon_de_Egreso like ?",
                new String[]{razon});
        int id = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return id;
    }

    public static int obtenerIdRazonDeIngreso(Context context, String razon) {
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select idRazon_de_Ingreso from Razon_de_Ingreso where Razon_de_Ingreso like ?",
                new String[]{razon});
        int id = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        db.close();
        return id;
    }

    public static void agregarIngreso(Context context, Ingreso ingreso) {
        ContentValues values = new ContentValues();
        values.put("idIngreso", ingreso.getId());
        values.put("idRazon_de_Ingreso", ingreso.getRazonDeIngreso().getId());
        values.put("idConcepto_de_Ingreso", ingreso.getConceptoDeIngreso().getId());
        values.put("idHabitante", ingreso.getIdHabitante());
        values.put("departamento", ingreso.getDepartamento());
        values.put("monto", ingreso.getMonto());
        values.put("fecha", ingreso.getFecha());
        values.put("existe_en_banco", ingreso.isExisteEnBanco() ? 1 : 0);
        values.put("email", ingreso.getEmail());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Ingreso", "---", values);
        db.close();
    }

    public static void agregarEgreso(Context context, Egreso egreso){
        ContentValues values = new ContentValues();
        values.put("idEgreso", egreso.getId());
        values.put("idRazon_de_Egreso", egreso.getIdRazonDeEgreso());
        values.put("favorecido", egreso.getFavorecido());
        values.put("monto", egreso.getMonto());
        values.put("fecha", egreso.getFecha());
        values.put("es_extraordinario", egreso.isEsExtraordinario() ? 1 : 0);
        values.put("email", egreso.getEmail());
        SQLiteDatabase db = new CondominioBD(context).getWritableDatabase();
        db.insert("Egreso", "---", values);
        db.close();
    }

    public static Ingreso[] obtenerIngresosDelMes(Context context){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        long baseTime = c.getTimeInMillis();
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Ingreso where email like ? and " +
                        "fecha >= CAST(? as LONG) order by fecha desc",
                new String[]{ProveedorDeRecursos.obtenerEmail(context), String.valueOf(baseTime)});
        List<Ingreso> ingresos = new ArrayList<>();
        Ingreso ingreso;
        RazonDeIngreso razonDeIngreso;
        ConceptoDeIngreso conceptoDeIngreso;
        while(cursor.moveToNext()){
            ingreso = new Ingreso(cursor.getInt(cursor.getColumnIndex("idIngreso")));
            razonDeIngreso = new RazonDeIngreso(cursor.getInt(cursor.getColumnIndex("idRazon_de_Ingreso")));
            ingreso.setRazonDeIngreso(razonDeIngreso);
            conceptoDeIngreso = new ConceptoDeIngreso(cursor.getInt(cursor.getColumnIndex("idConcepto_de_Ingreso")));
            ingreso.setConceptoDeIngreso(conceptoDeIngreso);
            ingreso.setMonto(cursor.getFloat(cursor.getColumnIndex("monto")));
            ingreso.setIdHabitante(cursor.getInt(cursor.getColumnIndex("idHabitante")));
            ingreso.setDepartamento(cursor.getString(cursor.getColumnIndex("departamento")));
            ingreso.setFecha(cursor.getLong(cursor.getColumnIndex("fecha")));
            ingreso.setExisteEnBanco(cursor.getInt(cursor.getColumnIndex("existe_en_banco")) != 0);
            ingreso.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            ingresos.add(ingreso);
            Log.d("Higer", "id: " + ingreso.getId() + ", idRazon_de_Ingreso: " + ingreso.getRazonDeIngreso().getId() + ", idConcepto_de_Ingreso: " + ingreso.getConceptoDeIngreso().getId() + ", monto: " + ingreso.getMonto() + ", idHabitante: " + ingreso.getIdHabitante() + ", depa: " + ingreso.getDepartamento() + ", fecha: " + ingreso.getFecha() + ", " + ingreso.getEmail());
        }
        cursor.close();
        db.close();
        return ingresos.toArray(new Ingreso[0]);
    }

    public static Ingreso[] obtenerIngresosDelMesOrdinarios(Context context){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        long baseTime = c.getTimeInMillis();
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Ingreso where email like ? and " +
                        "fecha >= CAST(? as LONG) and idConcepto_de_Ingreso = 0 order by fecha desc",
                new String[]{ProveedorDeRecursos.obtenerEmail(context), String.valueOf(baseTime)});
        List<Ingreso> ingresos = new ArrayList<>();
        Ingreso ingreso;
        RazonDeIngreso razonDeIngreso;
        ConceptoDeIngreso conceptoDeIngreso;
        while(cursor.moveToNext()){
            ingreso = new Ingreso(cursor.getInt(cursor.getColumnIndex("idIngreso")));
            razonDeIngreso = new RazonDeIngreso(cursor.getInt(cursor.getColumnIndex("idRazon_de_Ingreso")));
            ingreso.setRazonDeIngreso(razonDeIngreso);
            conceptoDeIngreso = new ConceptoDeIngreso(cursor.getInt(cursor.getColumnIndex("idConcepto_de_Ingreso")));
            ingreso.setConceptoDeIngreso(conceptoDeIngreso);
            ingreso.setMonto(cursor.getFloat(cursor.getColumnIndex("monto")));
            ingreso.setIdHabitante(cursor.getInt(cursor.getColumnIndex("idHabitante")));
            ingreso.setDepartamento(cursor.getString(cursor.getColumnIndex("departamento")));
            ingreso.setFecha(cursor.getLong(cursor.getColumnIndex("fecha")));
            ingreso.setExisteEnBanco(cursor.getInt(cursor.getColumnIndex("existe_en_banco")) != 0);
            ingreso.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            ingresos.add(ingreso);
        }
        cursor.close();
        db.close();
        return ingresos.toArray(new Ingreso[0]);
    }

    public static Ingreso[] obtenerIngresosDelMesExtraordinarios(Context context){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        long baseTime = c.getTimeInMillis();
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Ingreso where email like ? and " +
                        "fecha >= CAST(? as LONG) and idConcepto_de_Ingreso = 1 order by fecha desc",
                new String[]{ProveedorDeRecursos.obtenerEmail(context), String.valueOf(baseTime)});
        List<Ingreso> ingresos = new ArrayList<>();
        Ingreso ingreso;
        RazonDeIngreso razonDeIngreso;
        ConceptoDeIngreso conceptoDeIngreso;
        while(cursor.moveToNext()){
            ingreso = new Ingreso(cursor.getInt(cursor.getColumnIndex("idIngreso")));
            razonDeIngreso = new RazonDeIngreso(cursor.getInt(cursor.getColumnIndex("idRazon_de_Ingreso")));
            ingreso.setRazonDeIngreso(razonDeIngreso);
            conceptoDeIngreso = new ConceptoDeIngreso(cursor.getInt(cursor.getColumnIndex("idConcepto_de_Ingreso")));
            ingreso.setConceptoDeIngreso(conceptoDeIngreso);
            ingreso.setMonto(cursor.getFloat(cursor.getColumnIndex("monto")));
            ingreso.setIdHabitante(cursor.getInt(cursor.getColumnIndex("idHabitante")));
            ingreso.setDepartamento(cursor.getString(cursor.getColumnIndex("departamento")));
            ingreso.setFecha(cursor.getLong(cursor.getColumnIndex("fecha")));
            ingreso.setExisteEnBanco(cursor.getInt(cursor.getColumnIndex("existe_en_banco")) != 0);
            ingreso.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            ingresos.add(ingreso);
        }
        cursor.close();
        db.close();
        return ingresos.toArray(new Ingreso[0]);
    }

    public static Egreso[] obtenerEgresosDelMes(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long baseTime = calendar.getTimeInMillis();
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Egreso where email like ? and " +
                        "fecha >= CAST(? as LONG) order by fecha desc",
                new String[]{ProveedorDeRecursos.obtenerEmail(context), String.valueOf(baseTime)});
        List<Egreso> egresos = new ArrayList<>();
        Egreso egreso;
        while(c.moveToNext()){
            egreso = new Egreso(c.getInt(c.getColumnIndex("idEgreso")));
            egreso.setIdRazonDeEgreso(c.getInt(c.getColumnIndex("idRazon_de_Egreso")));
            egreso.setMonto(c.getFloat(c.getColumnIndex("monto")));
            egreso.setFavorecido(c.getString(c.getColumnIndex("favorecido")));
            egreso.setFecha(c.getLong(c.getColumnIndex("fecha")));
            egreso.setEsExtraordinario(c.getInt(c.getColumnIndex("es_extraordinario")) != 0);
            egreso.setEmail(c.getString(c.getColumnIndex("email")));
            egresos.add(egreso);
        }
        c.close();
        db.close();
        return egresos.toArray(new Egreso[0]);
    }

    public static Egreso[] obtenerEgresosDelMesOrdinarios(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long baseTime = calendar.getTimeInMillis();
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Egreso where email like ? and " +
                        "fecha >= CAST(? as LONG) and es_extraordinario = 0 order by fecha desc",
                new String[]{ProveedorDeRecursos.obtenerEmail(context), String.valueOf(baseTime)});
        List<Egreso> egresos = new ArrayList<>();
        Egreso egreso;
        while(c.moveToNext()){
            egreso = new Egreso(c.getInt(c.getColumnIndex("idEgreso")));
            egreso.setIdRazonDeEgreso(c.getInt(c.getColumnIndex("idRazon_de_Egreso")));
            egreso.setMonto(c.getFloat(c.getColumnIndex("monto")));
            egreso.setFavorecido(c.getString(c.getColumnIndex("favorecido")));
            egreso.setFecha(c.getLong(c.getColumnIndex("fecha")));
            egreso.setEsExtraordinario(c.getInt(c.getColumnIndex("es_extraordinario")) != 0);
            egreso.setEmail(c.getString(c.getColumnIndex("email")));
            egresos.add(egreso);
        }
        c.close();
        db.close();
        return egresos.toArray(new Egreso[0]);
    }

    public static Egreso[] obtenerEgresosDelMesExtraordinarios(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long baseTime = calendar.getTimeInMillis();
        SQLiteDatabase db = new CondominioBD(context).getReadableDatabase();
        Cursor c = db.rawQuery("select * from Egreso where email like ? and " +
                        "fecha >= CAST(? as LONG) and es_extraordinario != 0 order by fecha desc",
                new String[]{ProveedorDeRecursos.obtenerEmail(context), String.valueOf(baseTime)});
        List<Egreso> egresos = new ArrayList<>();
        Egreso egreso;
        while(c.moveToNext()){
            egreso = new Egreso(c.getInt(c.getColumnIndex("idEgreso")));
            egreso.setIdRazonDeEgreso(c.getInt(c.getColumnIndex("idRazon_de_Egreso")));
            egreso.setMonto(c.getFloat(c.getColumnIndex("monto")));
            egreso.setFavorecido(c.getString(c.getColumnIndex("favorecido")));
            egreso.setFecha(c.getLong(c.getColumnIndex("fecha")));
            egreso.setEsExtraordinario(c.getInt(c.getColumnIndex("es_extraordinario")) != 0);
            egreso.setEmail(c.getString(c.getColumnIndex("email")));
            egresos.add(egreso);
        }
        c.close();
        db.close();
        return egresos.toArray(new Egreso[0]);
    }
}

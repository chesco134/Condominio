package org.inspira.condominio.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * adb -d logcat com.example.example:I    *:S
 */
public class CondominioBD extends SQLiteOpenHelper {

    public CondominioBD(Context context){
        super(context, "Condominio", null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        dataBase.execSQL("create table Tipo_de_Condominio(" +
                "idTipo_de_Condominio integer not null primary key autoincrement," +
                "descripcion text not null" +
                ")");
        dataBase.execSQL("insert into Tipo_de_Condominio(descripcion) values('Conjunto Condominal'),('Condominio'),('Unidad Habitacional')");
        dataBase.execSQL("create table Condominio(" +
                "idCondominio integer not null primary key autoincrement," +
                "nombre text not null," +
                "direccion text not null," +
                "edad integer not null," +
                "idTipo_de_Condominio integer not null," +
                "inmoviliaria text not null," +
                "posee_sala_de_juntas integer default 0," +
                "posee_gym integer default 0," +
                "posee_espacio_recreativo integer default 0," +
                "posee_espacio_cultural integer default 0," +
                "posee_oficinas_administrativas integer default 0," +
                "posee_alarma_sismica integer default 0," +
                "cantidad_de_lugares_estacionamiento integer not null," +
                "cantidad_de_lugares_estacionamiento_visitas integer not null," +
                "costo_aproximado float not null," + // costo aproximado por unidad privativa
                "capacidad_cisterna float not null," +
                "posee_cisterna_agua_pluvial integer default 0," +
                "foreign key(idTipo_de_Condominio) references Tipo_de_Condominio(idTipo_de_Condominio)" +
                ")");
        dataBase.execSQL("create table Intervalo_Transparencia(" +
                "idIntervalo_Transparencia integer not null primary key autoincrement," +
                "intervalo_de_transparencia text not null" +
                ")");
        dataBase.execSQL("insert into Intervalo_Transparencia(intervalo_de_transparencia) values('Corto plazo'),('Mediano plazo'), ('Largo plazo')");
        dataBase.execSQL("create table Administracion(" +
                "idAdministracion integer not null primary key autoincrement," +
                "posee_planes_de_trabajo integer default 0," +
                "costo_de_cuota_de_mantenimiento_mensual float not null," +
                "costo_de_cuota_anual float not null," +
                "promedio_inicial_de_egresos float not null," + // promedio de egresos de los 6 meses anteriores al registro.
                "promedio_inicial_de_morosidad float not null," +// promedio de morosidad de los 6 meses anteriores al registro.
                "posee_mantenimiento_profesional_a_elevadores integer default 0," +
                "posee_personal_capacitado_en_seguridad_intramuros integer default 0," +
                "posee_mantenimiento_profesional_al_cuarto_de_maquinas integer default 0," +
                "posee_wifi_abierto integer default 0," +
                "idCondominio integer not null," +
                "idIntervalo_Transparencia integer not null," +
                "foreign key(idCondominio) references Condominio(idCondominio)," +
                "foreign key(idIntervalo_Transparencia) references Intervalo_Transparencia(idIntervalo_Transparencia)" +
                ")");
        dataBase.execSQL("create table Contacto_Administracion(" +
                "idContacto_Administracion integer not null primary key autoincrement," +
                "contacto text not null," +
                "idAdministracion integer not null," +
                "foreign key(idAdministracion) references Administracion(idAdministracion)" +
                ")");
        dataBase.execSQL("create table Torre(" +
                "idTorre integer not null primary key autoincrement," +
                "nombre text not null," +
                "posee_elevador integer default 0," +
                "cantidad_de_pisos integer not null," +
                "cantidad_de_focos integer not null," +
                "cantidad_de_departamentos integer not null," +
                "idAdministracion integer not null," +
                "foreign key(idAdministracion) references Administracion(idAdministracion)" +
                ")");
        dataBase.execSQL("create table Habitante(" +
                "idHabitante integer not null primary key autoincrement," +
                "nombres text not null," +
                "ap_paterno text not null," +
                "ap_materno text not null," +
                "nombre_departamento text not null," +
                "idTorre integer not null," +
                "foreign key(idTorre) references Torre(idTorre) on delete cascade on update cascade" +
                ")");
        dataBase.execSQL("create table Contacto_Habitante(" +
                "idContacto_Habitante integer not null primary key autoincrement," +
                "contacto text not null," +
                "idHabitante integer not null," +
                "foreign key(idHabitante) references Habitante(idHabitante)" +
                ")");
        dataBase.execSQL("create table Propietario_de_Departamento(" +
                "idHabitante integer not null primary key," +
                "posee_seguro integer default 0," +
                "foreign key(idHabitante) references Habitante(idHabitante)" +
                ")");
        dataBase.execSQL("create table Tipo_de_Siniestro(" +
                "idTipo_de_Siniestro integer not null primary key autoincrement," +
                "tipo_de_siniestro text" +
                ")");
        /** La siguiente tabla debe contar con un método de ingreso de más campos. **/
        dataBase.execSQL("insert into Tipo_de_Siniestro(tipo_de_siniestro) values('Asalto'),('Robo a departamento'),('Robo de autopartes'),('Violencia física'),('Incendio'),('Desabasto de agua'),('Eléctrico'),('Administrativo'),('Sismos'),('Inundación'),('Material de construcción'),('Cajones de Estacionamiento'),('Fuga de agua'),('Fuga de gas')");
        dataBase.execSQL("create table Reporte_de_Siniestro(" +
                "idreporte_de_Siniestro integer not null primary key autoincrement," +
                "idTipo_de_Siniestro integer not null," +
                "idHabitante integer not null," +
                "descripcion text not null," +
                "foreign key(idTipo_de_Siniestro) references Tipo_de_Siniestro(idTipo_de_Siniestro)," +
                "foreign key(idHabitante) references Habitante(idHabitante)" +
                ")");
        dataBase.execSQL("create table Tipo_de_Administrador(" +
                "idTipo_de_Administrador integer not null primary key autoincrement," +
                "descripcion text not null" +
                ")");
        dataBase.execSQL("insert into Tipo_de_Administrador(descripcion) values('Profesional'),('Condómino'),('Provisional')");
        dataBase.execSQL("create table Escolaridad(" +
                "idEscolaridad integer not null primary key autoincrement," +
                "escolaridad text not null" +
                ")");
        dataBase.execSQL("insert into Escolaridad(escolaridad) values('Básico'),('Medio superior'),('Superior'),('Maestría'),('Doctorado')");
        dataBase.execSQL("create table Usuario(" + // Se trata del admin.
                "email TEXT NOT NULL PRIMARY KEY," +
                "nombres TEXT NOT NULL," +
                "ap_paterno TEXT NOT NULL," +
                "ap_materno TEXT NOT NULL," +
                "dateOfBirth long not null," +
                "remuneracion float," +
                "idAdministracion integer not null," +
                "idEscolaridad integer not null," +
                "idTipo_de_Administrador integer not null," +
                "foreign key(idAdministracion) references Administracion(idAdministracion)," +
                "foreign key(idEscolaridad) references Escolaridad(idEscolaridad)," +
                "foreign key(idTipo_de_Administrador) references Tipo_de_Administrador(idTipo_de_Administrador)" +
                ")");
        dataBase.execSQL("create table Usuario_Profesionista(" +
                "email TEXT not null primary key," +
                "profesion text not null," +
                "foreign key (email) references Usuario(email)" +
                ")");
        dataBase.execSQL("create table Contacto_Usuario(" +
                "idContactoUsuario integer not null primary key autoincrement," +
                "telefono text not null," +
                "email text not null," +
                "foreign key(email) references Usuario(email)" +
                ")");
        dataBase.execSQL("create table Integrante_de_Comite_de_Vigilancia(" +
                "idIntegrante_de_Comite_de_Vigilancia integer not null primary key autoincrement," +
                "nombres text not null," +
                "ap_paterno text not null," +
                "ap_materno text not null," +
                "idAdministracion integer not null," +
                "foreign key(idAdministracion) references Administracion(idAdministracion)" +
                ")");
        dataBase.execSQL("create table Presidente_de_Comite_de_Vigilancia(" +
                "idIntegrante_de_Comite_de_Vigilancia integer not null primary key," +
                "escolaridad_de_presidente text not null," +
                "foreign key(idIntegrante_de_Comite_de_Vigilancia) references Integrante_de_Comite_de_Vigilancia(idIntegrante_de_Comite_de_Vigilancia)" +
                ")");
        dataBase.execSQL("create table Tipo_de_Trabajador(" +
                "idTipo_de_Trabajador integer not null primary key autoincrement," +
                "tipo_de_trabajador text not null" +
                ")");
        dataBase.execSQL("insert into Tipo_de_Trabajador(tipo_de_trabajador) values('Seguridad y vigilancia'),('Limpieza'),('Mantenimiento'),('Oficina')");
        dataBase.execSQL("create table Trabajador(" +
                "idTrabajador integer not null primary key autoincrement," +
                "nombres text not null," +
                "ap_paterno text not null," +
                "ap_materno text not null," +
                "salario float not null," +
                "posee_seguro_social integer default 0," +
                "idAdministracion integer not null," +
                "idTipo_de_Trabajador integer not null," +
                "foreign key(idAdministracion) references Administracion(idAdministracion)," +
                "foreign key(idTipo_de_Trabajador) references Tipo_de_Trabajador(idTipo_de_Trabajador)" +
                ")");
        dataBase.execSQL("create table Convocatoria(" +
                "idConvocatoria INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "Asunto TEXT NOT NULL," +
                "Ubicacion_Interna TEXT," +
                "Fecha_de_Inicio LONG," +
                "firma TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "FOREIGN KEY (email) REFERENCES Usuario(email)" +
                ")");
        dataBase.execSQL("create table Punto_OdD(" +
                "idPunto_OdD INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Descripcion TEXT NOT NULL," +
                "idConvocatoria INTEGER NOT NULL," +
                "FOREIGN KEY(idConvocatoria) REFERENCES Convocatoria(idConvocatoria)" +
                ")");
        dataBase.execSQL("create table Razon_de_Ingreso(" +
                "idRazon_de_Ingreso INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "Razon_de_Ingreso TEXT NOT NULL" +
                ")");
        dataBase.execSQL("create table Concepto_de_Ingreso(" +
                "idConcepto_de_Ingreso integer not null primary key autoincrement," +
                "Concepto_de_Ingreso text not null" +
                ")");
        dataBase.execSQL("create table Ingreso(" +
                "idIngreso INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "idRazon_de_Ingreso INTEGER NOT NULL," +
                "idConcepto_de_Ingreso integer NOT NULL," +
                "monto float not null," +
                "idHabitante integer not null," +
                "departamento text," +
                "fecha text not null," +
                "email text not null," +
                "foreign key(idHabitante) references Habitante(idHabitante)," +
                "foreign key(idRazon_de_Ingreso) references Razon_de_Ingreso(idRazon_de_Ingreso)," +
                "foreign key(idConcepto_de_Ingreso) references Concepto_de_Ingreso(idConcepto_de_Ingreso)," +
                "foreign key(email) references Usuario(email)" +
                ")");
        dataBase.execSQL("create table Razon_de_Egreso(" +
                "idRazon_de_Egreso integer not null primary key autoincrement," +
                "Razon_de_Egreso text not null" +
                ")");
        dataBase.execSQL("create table Egreso(" +
                "idEgreso INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "idRazon_de_Egreso integer not null," +
                "monto float not null," +
                "favorecido text not null," +  // Debe ser un string que venga del server de un habitante registrado.
                "fecha long not null," +
                "email text not null," +
                "foreign key(idRazon_de_Egreso) references Razon_de_Egreso(idRazon_de_Egreso)," +
                "foreign key(email) references Usuario(email)" +
                ")");
    }

    /*********************
     *
     * Zona de inserts
     *
     ***********************/

    public int insertaConvocatoria(Convocatoria conv, String email){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idConvocatoria", conv.getId());
        values.put("Asunto", conv.getAsunto());
        values.put("Ubicacion_Interna", conv.getUbicacionInterna());
        values.put("Fecha_de_Inicio", conv.getFechaInicio());
        values.put("firma", conv.getFirma());
        values.put("email", email);
        db.insert("Convocatoria", "---", values);
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select last_insert_rowid() v", null);
        int id = -1;
        if(c.moveToNext()){
            id = c.getInt(0);
        }
        c.close();
        db.close();
        return id;
    }

    public void insertaPuntoOdD(PuntoOdD punto){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Descripcion", punto.getDescripcion());
        values.put("idConvocatoria", punto.getIdConvocatoria());
        db.insert("Punto_OdD", "---", values);
        db.close();
    }

    /*************************
     *
     *      Zona de selecciones
     *
     ******************************/
    public Convocatoria[] obtenerConvocatorias(String email){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from Convocatoria where email like ?", new String[]{email});
        List<Convocatoria> convocatorias = new ArrayList<>();
        while(c.moveToNext()){
            Convocatoria convocatoria = new Convocatoria(c.getInt(c.getColumnIndex("idConvocatoria")));
            convocatoria.setAsunto(c.getString(c.getColumnIndex("Asunto")));
            convocatoria.setUbicacionInterna(c.getString(c.getColumnIndex("Ubicacion_Interna")));
            convocatoria.setFechaInicio(c.getLong(c.getColumnIndex("Fecha_de_Inicio")));
            convocatoria.setFirma(c.getString(c.getColumnIndex("firma")));
            Cursor c2 = db.rawQuery("select idPunto_OdD,Descripcion from Punto_OdD where idConvocatoria = CAST(? as INTEGER)",
                    new String[]{String.valueOf(convocatoria.getId())});
            List<PuntoOdD> puntos = new ArrayList<>();
            while(c2.moveToNext()) {
                PuntoOdD punto = new PuntoOdD(c2.getInt(c2.getColumnIndex("idPunto_OdD")));
                punto.setDescripcion(c2.getString(c2.getColumnIndex("Descripcion")));
                punto.setIdConvocatoria(convocatoria.getId());
                puntos.add(punto);
            }
            c2.close();
            convocatorias.add(convocatoria);
        }
        c.close();
        db.close();
        return convocatorias.toArray(new Convocatoria[0]);
    }

    /*********************
     *
     * Zona de Revisiones
     *
     **********************/


    public boolean revisarExistenciaDeUsuarios(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from Usuario", null);
        boolean exists = c.moveToNext();
        c.close();
        return exists;
    }
}
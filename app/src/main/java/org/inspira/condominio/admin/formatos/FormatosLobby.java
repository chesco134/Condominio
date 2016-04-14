package org.inspira.condominio.admin.formatos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.adaptadores.AdaptadorDeIngresos;
import org.inspira.condominio.adaptadores.AdaptadorDeEgresos;
import org.inspira.condominio.admon.AccionesTablaCondominio;
import org.inspira.condominio.admon.AccionesTablaContable;
import org.inspira.condominio.admon.AccionesTablaHabitante;
import org.inspira.condominio.datos.AlmacenamientoInterno;
import org.inspira.condominio.datos.Condominio;
import org.inspira.condominio.datos.Egreso;
import org.inspira.condominio.datos.InformacionEgreso;
import org.inspira.condominio.datos.InformacionIngresos;
import org.inspira.condominio.datos.Ingreso;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.dialogos.ProveedorToast;
import org.inspira.condominio.fragmentos.FormatosDeEgreso;
import org.inspira.condominio.fragmentos.FormatosDeIngreso;
import org.inspira.condominio.pdf.DocumentoEgresos;
import org.inspira.condominio.pdf.DocumentoIngreso;
import org.inspira.condominio.pdf.ExportarConvocatoria;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FormatosLobby extends AppCompatActivity {

    private static final int CREAR_FORMATO = 135;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formatos_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(getResources().getString(R.string.texto_formatos_admin));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchFormatos();
            }
        });
        if(getSupportFragmentManager().findFragmentByTag("Mock") == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_lobby_main_container, new FormatosDeIngreso(), "Mock")
                    .commit();
    }

    public void generarDocumentos() {
        try {
            AlmacenamientoInterno a = new AlmacenamientoInterno(FormatosLobby.this);
            a.crearDirectorioContable();
            Calendar c = Calendar.getInstance();
            String str = obtenerMes(c.get(Calendar.MONTH));
            int year = c.get(Calendar.YEAR);
            String file = a.obtenerRutaDeAlmacenamientoContable() + "/Ingresos " + str + " " + year + ".pdf";
            String fileEgresosOrdinarios = a.obtenerRutaDeAlmacenamientoContable() + "/EgresosOrdinarios " + str + " " + year + ".pdf";
            String fileEgresosExtraordinarios = a.obtenerRutaDeAlmacenamientoContable() + "/EgresosExtraordinarios " + str + " " + year + ".pdf";
            Ingreso[] ingresos = AccionesTablaContable.obtenerIngresosDelMesOrdinarios(this);
            float totalOrdinario = 0;
            float totalOrdinarioEnBanco = 0;
            for(Ingreso ingreso : ingresos)
                if(ingreso.isExisteEnBanco())
                    totalOrdinarioEnBanco += ingreso.getMonto();
                else
                    totalOrdinario += ingreso.getMonto();
            Ingreso[] ingresosExtraordinarios = AccionesTablaContable.obtenerIngresosDelMesExtraordinarios(this);
            float totalExtraordinario = 0;
            float totalExtraordinarioEnBanco = 0;
            for(Ingreso ingreso : ingresosExtraordinarios)
                if(ingreso.isExisteEnBanco())
                    totalExtraordinarioEnBanco += ingreso.getMonto();
                else
                    totalExtraordinario += ingreso.getMonto();
            InformacionIngresos infoIngresosExtraordinarios = new InformacionIngresos(totalExtraordinarioEnBanco,totalExtraordinario);
            infoIngresosExtraordinarios.setTotalhabitantes(AccionesTablaHabitante.obtenerNumeroDeHabitantesEnTorre(this));
            infoIngresosExtraordinarios.setTotalRegulares(ingresosExtraordinarios.length);
            InformacionIngresos infoIngresos = new InformacionIngresos(totalOrdinarioEnBanco, totalOrdinario);
            infoIngresos.setTotalhabitantes(AccionesTablaHabitante.obtenerNumeroDeHabitantesEnTorre(this));
            infoIngresos.setTotalRegulares(ingresos.length);
            DocumentoIngreso doc = new DocumentoIngreso(infoIngresos, infoIngresosExtraordinarios);
            Condominio condominio = AccionesTablaCondominio.obtenerCondominio(this, ProveedorDeRecursos.obtenerIdCondominio(this));
            doc.exportarPdf(file, condominio.getNombre(), condominio.getDireccion());
            Egreso[] egresos = AccionesTablaContable.obtenerEgresosDelMesOrdinarios(this);
            List<InformacionEgreso> informacionEgresos = new ArrayList<>();
            InformacionEgreso informacionEgreso;
            for(Egreso egreso : egresos) {
                informacionEgreso = new InformacionEgreso();
                informacionEgreso.setFecha(egreso.getFecha());
                informacionEgreso.setMonto(egreso.getMonto());
                informacionEgreso.setDescripcion(AccionesTablaContable.obtenerRazonDeEgreso(this, egreso.getIdRazonDeEgreso()));
                informacionEgreso.setTipoDePago("Efectivo");
                informacionEgresos.add(informacionEgreso);
            }
            DocumentoEgresos docEgresos = new DocumentoEgresos(ProveedorDeRecursos.obtenerUsuario(this),
                    informacionEgresos.toArray(new InformacionEgreso[]{}), "Ordinarios",
                    "José A. Nochebuena 5000", "José A. Nochebuena 2000");
            docEgresos.exportarPdf(fileEgresosOrdinarios);
            Egreso[] egresosExtra = AccionesTablaContable.obtenerEgresosDelMesExtraordinarios(this);
            informacionEgresos = new ArrayList<>();
            for(Egreso egreso : egresosExtra) {
                informacionEgreso = new InformacionEgreso();
                informacionEgreso.setFecha(egreso.getFecha());
                informacionEgreso.setMonto(egreso.getMonto());
                informacionEgreso.setDescripcion(AccionesTablaContable.obtenerRazonDeEgreso(this, egreso.getIdRazonDeEgreso()));
                informacionEgreso.setTipoDePago("Efectivo");
                informacionEgresos.add(informacionEgreso);
            }
            DocumentoEgresos docEgresosExtra = new DocumentoEgresos(ProveedorDeRecursos.obtenerUsuario(this),
                    informacionEgresos.toArray(new InformacionEgreso[0]), "Extraordinarios",
                    "José A. Nochebuena", "José A. Nochebuena 2");
            docEgresosExtra.exportarPdf(fileEgresosExtraordinarios);
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    ProveedorToast.showToast(FormatosLobby.this,R.string.crear_convocatoria_archivo_creado);
                }
            });
        }catch(IOException e){
            e.printStackTrace();
            MuestraMensajeDesdeHilo.muestraMensaje(FormatosLobby.this, findViewById(R.id.formato_convocatoria_contenedor), getString(R.string.crear_convocatoria_error_pdf));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void launchFormatos() {
        startActivityForResult(new Intent(FormatosLobby.this, Formatos.class), CREAR_FORMATO);
    }

    private String obtenerMes(int month){

        String monthStr;
        switch (month){
            case 0:
                monthStr = "Enero";
                break;
            case 1:
                monthStr = "Febrero";
                break;
            case 2:
                monthStr = "Marzo";
                break;
            case 3:
                monthStr = "Abril";
                break;
            case 4:
                monthStr = "Mayo";
                break;
            case 5:
                monthStr = "Junio";
                break;
            case 6:
                monthStr = "Julio";
                break;
            case 7:
                monthStr = "Agosto";
                break;
            case 8:
                monthStr = "Septiembre";
                break;
            case 9:
                monthStr = "Octubre";
                break;
            case 10:
                monthStr = "Noviembre";
                break;
            case 11:
                monthStr = "Diciembre";
                break;
            default:
                monthStr = "";
        }
        return monthStr;
    }

}
package org.inspira.condominio.pdf;

import com.itextpdf.text.DocumentException;

import org.inspira.condominio.datos.ConceptoDeIngreso;
import org.inspira.condominio.datos.InformacionEgreso;
import org.inspira.condominio.datos.InformacionIngresos;
import org.inspira.condominio.datos.Ingreso;
import org.inspira.condominio.datos.RazonDeIngreso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcapiz on 12/04/16.
 */
public class MockContable {

    public static final String TABLITA = "pruebas/pdf/tutska.pdf";
    private static final String IMG1 = "pruebas/imgs/abbit_green_2.png";

    public void mockMainEstadoDeCuenta(){
        try {
            // TODO code application logic here
            File f = new File(TABLITA);
            f.getParentFile().mkdirs();
            DocumentoEstadoDeCuenta doc = new DocumentoEstadoDeCuenta();
            doc.setAdmin("Ayakuchi");
            doc.setCondominio("Zentauro Hill");
            doc.setIdEdoCta("Estado de cuenta prueba");
            doc.setPorcentajeSanciones((float)0.05);
            doc.setNotas("El fulano vino y se fue. Sin novedá.");
            Ingreso ingreso = new Ingreso(5);
            ingreso.setDepartamento("Adawita");
            ingreso.setFecha(new java.util.Date().getTime());
            ingreso.setMonto((float)482.5);
            ingreso.setNombre("Jorjomo Almonzo");
            ingreso.setSello("54235GFD3456");
            ConceptoDeIngreso conceptoDeIngreso = new ConceptoDeIngreso(1);
            conceptoDeIngreso.setConceptoDeIngreso("Cuota mensual");
            ingreso.setConceptoDeIngreso(conceptoDeIngreso);
            RazonDeIngreso razon = new RazonDeIngreso(1);
            razon.setRazonDeIngreso("Test razón de ingrerso");
            ingreso.setIdRazonDeIngreso(razon);
            Ingreso[] ingresos = new Ingreso[1];
            ingresos[0] = ingreso;
            doc.setAdeudos(ingresos);
            doc.setPagos(ingresos);
            doc.createPdf(TABLITA, IMG1);
            //new ITextLearning().createPdf(TABLITA);
        } catch (IOException | DocumentException ex) {
            ex.printStackTrace();
        }
        System.out.println("Done");
    }

    private void mockDocumentoIngreso(){
        try {
            // TODO code application logic here
            File f = new File(TABLITA);
            f.getParentFile().mkdirs();
            InformacionIngresos infoIngresos = new InformacionIngresos(5500, 18900);
            infoIngresos.setTotalhabitantes(560);
            infoIngresos.setTotalRegulares(420);
            InformacionIngresos infoIngresosExtra = new InformacionIngresos(500, (float)1350.8);
            infoIngresosExtra.setTotalhabitantes(560);
            infoIngresosExtra.setTotalRegulares(270);
            DocumentoIngreso doc = new DocumentoIngreso(infoIngresos, infoIngresosExtra);
            doc.exportarPdf(TABLITA, IMG1, "Zukaritas", "Calle 66306713333");
            //new ITextLearning().createPdf(TABLITA);
        } catch (IOException | DocumentException ex) {
            ex.printStackTrace();
        }
        System.out.println("Done");
    }

    private static void mockDocumentoEgresos(){
        try {
            // TODO code application logic here
            File f = new File(TABLITA);
            f.getParentFile().mkdirs();
            InformacionEgreso egreso1 = new InformacionEgreso();
            egreso1.setFecha(new java.util.Date().getTime());
            egreso1.setDescripcion("Pago a servicio de vigilancia");
            egreso1.setMonto(1100);
            egreso1.setTipoDePago("Cheque");
            InformacionEgreso egreso2 = new InformacionEgreso();
            egreso2.setFecha(new java.util.Date().getTime());
            egreso2.setDescripcion("Pago personal de jardinería");
            egreso2.setMonto((float)920.80);
            egreso2.setTipoDePago("Efectivo");
            List<InformacionEgreso> egresos = new ArrayList<>();
            egresos.add(egreso1);
            egresos.add(egreso2);
            DocumentoEgresos doc = new DocumentoEgresos("Jorge Robles Montecarlo",
                    egresos.toArray(new InformacionEgreso[0]), "Ordinarios",
                    "Julio Bértiz Monrique", "Mariana Ávila Maldonado");
            doc.exportarPdf(TABLITA, IMG1, "Zukaritas", "Calle 66306713333");
        } catch (IOException | DocumentException ex) {
            ex.printStackTrace();
        }
        System.out.println("Done");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inspira.condominio.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import itextlearning.ImageBackgroundEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.inspira.condominio.datos.Ingreso;

/**
 *
 * @author jcapiz
 */
public class DocumentoEstadoDeCuenta {

    private static final float INTERES_MORATORIO_LEGAL = 0.09f;
    private static final Font F_NORMAL = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font F_CELL_HEADER_TEXT = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE);
    
    private final Document documento;
    private String idEdoCta;
    private Ingreso[] pagos;
    private String notas;
    private Ingreso[] adeudos;
    private float porcentajeSanciones;
    private String admin;
    private String condominio;

    public DocumentoEstadoDeCuenta() {
        documento = new Document(PageSize.A4);
    }

    public void createPdf(String dest, String imgResString) throws IOException, DocumentException {
        bakeContent(dest, imgResString);
        documento.close();
    }

    private void bakeContent(String path, String imgResString) throws IOException, DocumentException {
        File outFile = new File(path);
        outFile.getParentFile().mkdirs();
        PdfWriter.getInstance(documento, new FileOutputStream(outFile));
        documento.open();
        documento.add(new Paragraph("Ciudad de México, "
                + DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(new Date()),
                new Font(FontFamily.HELVETICA, 18, Font.NORMAL, new BaseColor(0x000000))));
        Paragraph intro = new Paragraph(new Phrase("Comprobante de estado de cuenta", F_NORMAL));
        intro.setAlignment(Paragraph.ALIGN_RIGHT);
        intro.setSpacingBefore(10f);
        documento.add(intro);
        Image image = Image.getInstance(imgResString);
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(image.getScaledWidth());
        table.setLockedWidth(true);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setCellEvent(new ImageBackgroundEvent(image));
        cell.setFixedHeight(image.getScaledHeight());
        table.addCell(cell);
        documento.add(table);
        Paragraph p1 = new Paragraph("Estado de cuenta: ".concat(idEdoCta), F_NORMAL);
        p1.setSpacingBefore(5f);
        p1.setSpacingAfter(25f);
        p1.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(p1);
        documento.add(new Paragraph("Pagos", F_NORMAL));
        agregaTablaDePagos();
        Paragraph pNotas = new Paragraph("Notas: ".concat(notas), F_NORMAL);
        pNotas.setSpacingAfter(12f);
        pNotas.setSpacingAfter(2f);
        documento.add(pNotas);
        Paragraph pAdeudos = new Paragraph("Adeudos", F_NORMAL);
        pAdeudos.setSpacingBefore(25f);
        documento.add(pAdeudos);
        agregaTablaDeAdeudos();
        documento.add(new Paragraph("Recuerde que puede consultar su estado de pagos y adeudos desde la aplicación móvil o directamente con su administrador.", F_NORMAL));
        Paragraph hechoPor = new Paragraph("Elaboró:", F_NORMAL);
        hechoPor.setSpacingBefore(30f);
        hechoPor.setSpacingAfter(15f);
        documento.add(hechoPor);
        Paragraph nombreDelAdmin = new Paragraph(admin, F_NORMAL);
        nombreDelAdmin.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(nombreDelAdmin);
        Paragraph nombreDeCondominio = new Paragraph(condominio, F_NORMAL);
        nombreDeCondominio.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(nombreDeCondominio);
    }
    
    private void agregaTablaDePagos() throws DocumentException{
        PdfPTable t1 = new PdfPTable(9);
        float total = setTableHeading(t1, pagos);
        PdfPCell cTotal = new PdfPCell(new Phrase("Total", F_NORMAL));
        cTotal.setColspan(6);
        cTotal.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        t1.addCell(cTotal);
        Phrase phrase = new Phrase(String.valueOf(total).concat(" pesos"), F_NORMAL);
        PdfPCell cMontoTotal = new PdfPCell();
        cMontoTotal.setColspan(3);
        cMontoTotal.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cMontoTotal.setPhrase(phrase);
        t1.addCell(cMontoTotal);
        t1.setSpacingBefore(20);
        documento.add(t1);
    }
    
    private void agregaTablaDeAdeudos() throws DocumentException{
        PdfPTable t1 = new PdfPTable(9);
        float total = setTableHeading(t1, adeudos);
        PdfPCell cInteresMoratorioLegal = new PdfPCell(new Phrase("Interés moratorio legal"));
        cInteresMoratorioLegal.setColspan(3);
        t1.addCell(cInteresMoratorioLegal);
        PdfPCell cFechaIML = new PdfPCell(new Paragraph("---"));
        cFechaIML.setColspan(3);
        cFechaIML.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        t1.addCell(cFechaIML);
        PdfPCell cMontoIML = new PdfPCell(new Paragraph(String.format("%.2f pesos",total*INTERES_MORATORIO_LEGAL)));
        cMontoIML.setColspan(3);
        cMontoIML.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cMontoIML.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
        t1.addCell(cMontoIML);
        PdfPCell cSancionesEstablecidasPorAsamblea = new PdfPCell(new Paragraph("Sanciones establecidas por asamblea"));
        cSancionesEstablecidasPorAsamblea.setColspan(3);
        t1.addCell(cSancionesEstablecidasPorAsamblea);
        PdfPCell cFechaSEA = new PdfPCell(new Paragraph("---"));
        cFechaSEA.setColspan(3);
        cFechaSEA.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cFechaSEA.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cFechaSEA.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        t1.addCell(cFechaSEA);
        PdfPCell cMontoSEA = new PdfPCell(new Paragraph(String.format("%.2f pesos",total*porcentajeSanciones)));
        cMontoSEA.setColspan(3);
        cMontoSEA.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cMontoSEA.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        t1.addCell(cMontoSEA);
        total*=INTERES_MORATORIO_LEGAL+porcentajeSanciones;
        PdfPCell cTotal = new PdfPCell(new Phrase("Total", F_NORMAL));
        cTotal.setColspan(6);
        cTotal.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        t1.addCell(cTotal);
        PdfPCell cMontoTotal = new PdfPCell(new Phrase(String.format("%.2f pesos",total), F_NORMAL));
        cMontoTotal.setColspan(3);
        cMontoTotal.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        t1.addCell(cMontoTotal);
        t1.setSpacingBefore(20f);
        t1.setSpacingAfter(12f);
        documento.add(t1);
    }
    
    private float setTableHeading(PdfPTable t1, Ingreso[] ingresos){
        PdfPCell cConcepto = new PdfPCell(new Phrase("Concepto", F_CELL_HEADER_TEXT));
        cConcepto.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cConcepto.setColspan(3);
        cConcepto.setBackgroundColor(new BaseColor(0xff009846));
        t1.addCell(cConcepto);
        PdfPCell cFecha = new PdfPCell(new Phrase("Fecha", F_CELL_HEADER_TEXT));
        cFecha.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cFecha.setColspan(3);
        cFecha.setBackgroundColor(new BaseColor(0xff009846));
        t1.addCell(cFecha);
        PdfPCell cMonto = new PdfPCell(new Phrase("Monto", F_CELL_HEADER_TEXT));
        cMonto.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cMonto.setColspan(3);
        cMonto.setBackgroundColor(new BaseColor(0xff009846));
        t1.addCell(cMonto);
        PdfPCell concepto;
        PdfPCell monto;
        PdfPCell fecha;
        float total = 0;
        boolean isTenue = false;
        for (Ingreso ingreso : ingresos) {
            concepto = new PdfPCell();
            concepto.setColspan(3);
            concepto.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            if(isTenue){
                concepto.setPhrase(new Phrase(ingreso.getConceptoDeIngreso().getConceptoDeIngreso(), F_CELL_HEADER_TEXT));
                concepto.setBackgroundColor(new BaseColor(0xaa009846));
            }else{
                concepto.setPhrase(new Phrase(ingreso.getConceptoDeIngreso().getConceptoDeIngreso()));
            }
            t1.addCell(concepto);
            fecha = new PdfPCell();
            fecha.setColspan(3);
            fecha.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            if(isTenue){
                fecha.setBackgroundColor(new BaseColor(0xaa009846));
                fecha.setPhrase(new Phrase(DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(new Date(ingreso.getFecha())), F_CELL_HEADER_TEXT));
            }else{
                fecha.setPhrase(new Phrase(DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(new Date(ingreso.getFecha()))));
            }
            t1.addCell(fecha);
            monto = new PdfPCell();
            monto.setColspan(3);
            monto.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            if(isTenue){
                monto.setBackgroundColor(new BaseColor(0xaa009846));
                monto.setPhrase(new Phrase(String.format("%.2f pesos",ingreso.getMonto()), F_CELL_HEADER_TEXT));
            }else{
                monto.setPhrase(new Phrase(String.format("%.2f pesos",ingreso.getMonto())));
            }
            t1.addCell(monto);
            total += ingreso.getMonto();
            isTenue = !isTenue;
            System.out.println("Total: " + total);
        }
        return total;
    }

    public void setIdEdoCta(String idEdoCta) {
        this.idEdoCta = idEdoCta;
    }

    public void setPagos(Ingreso[] pagos) {
        this.pagos = pagos;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public void setAdeudos(Ingreso[] adeudos) {
        this.adeudos = adeudos;
    }

    public void setPorcentajeSanciones(float porcentajeSanciones) {
        this.porcentajeSanciones = porcentajeSanciones;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setCondominio(String condominio) {
        this.condominio = condominio;
    }
    
    
}

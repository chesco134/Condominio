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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.inspira.condominio.datos.InformacionEgreso;

/**
 *
 * @author jcapiz
 */
public class DocumentoEgresos {
    
    private static final Font F_NORMAL = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font F_CELL_HEADER_TEXT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE);
    private final Document documento;
    private final String tipoDeGasto;
    private final String admin;
    private final String presidente;
    private final String vocal1;
    private final InformacionEgreso[] egresos;
    
    public DocumentoEgresos(String admin, InformacionEgreso[] egresos, String tipoDeGasto, String presidente, String vocal1) {
        documento = new Document(PageSize.A4);
        this.admin = admin;
        this.egresos = egresos;
        this.tipoDeGasto = tipoDeGasto;
        this.presidente = presidente;
        this.vocal1 = vocal1;
    }
    
    public void exportarPdf(String destino)
            throws IOException, DocumentException {
        File file = new File(destino);
        file.getParentFile().mkdirs();
        PdfWriter.getInstance(documento, new FileOutputStream(file));
        documento.open();
        documento.add(new Paragraph("Periodo: ".concat(DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(new Date())), F_NORMAL));
        documento.add(new Paragraph(admin, F_NORMAL));
        Paragraph heading = new Paragraph("Gastos ".concat(tipoDeGasto),
                new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(0x000000)));
        heading.setSpacingBefore(16f);
        heading.setSpacingAfter(16f);
        Paragraph heading2 = new Paragraph("Vo. Bo.",
                new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(0x000000)));
        heading2.setSpacingBefore(16f);
        heading2.setSpacingAfter(32f);
        
        documento.add(heading);
        agregarTablaEgresos(egresos);
        documento.add(heading2);
        agregarZonaVistoBueno();
        documento.close();
    }
    
    private void agregarZonaVistoBueno() throws DocumentException{
        PdfPCell cell1 = new PdfPCell(new Phrase("", F_NORMAL));
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell1.setColspan(5);
        cell1.setFixedHeight(20f);
        cell1.setBorder(Rectangle.BOTTOM);
        PdfPCell cell2 = new PdfPCell(new Phrase("", F_NORMAL));
        cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell2.setColspan(1);
        cell2.setFixedHeight(20f);
        cell2.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell3 = new PdfPCell(new Phrase("", F_NORMAL));
        cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell3.setColspan(5);
        cell3.setFixedHeight(20f);
        cell3.setBorder(Rectangle.BOTTOM);
        PdfPCell cell4 = new PdfPCell(new Phrase(presidente, F_NORMAL));
        cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell4.setColspan(5);
        cell4.setFixedHeight(20f);
        cell4.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell5 = new PdfPCell(new Phrase("", F_NORMAL));
        cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell5.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell5.setColspan(1);
        cell5.setFixedHeight(20f);
        cell5.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell6 = new PdfPCell(new Phrase(vocal1, F_NORMAL));
        cell6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell6.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell6.setColspan(5);
        cell6.setFixedHeight(20f);
        cell6.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell7 = new PdfPCell(new Phrase(("(Presidente)"), F_NORMAL));
        cell7.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell7.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell7.setColspan(5);
        cell7.setFixedHeight(20f);
        cell7.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell8 = new PdfPCell(new Phrase("", F_NORMAL));
        cell8.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell8.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell8.setColspan(1);
        cell8.setFixedHeight(20f);
        cell8.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell9 = new PdfPCell(new Phrase(("(Primer vocal)"), F_NORMAL));
        cell9.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell9.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell9.setColspan(5);
        cell9.setFixedHeight(20f);
        cell9.setBorder(Rectangle.NO_BORDER);
        PdfPTable table = new PdfPTable(11);
        table.setWidthPercentage(100);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        table.addCell(cell7);
        table.addCell(cell8);
        table.addCell(cell9);
        documento.add(table);
    }
    
    private void agregarTablaEgresos(InformacionEgreso[] egresos) throws DocumentException{
        PdfPCell cell1 = new PdfPCell(new Phrase("Fecha", F_CELL_HEADER_TEXT));
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell1.setColspan(2);
        cell1.setFixedHeight(20f);
        cell1.setBackgroundColor(new BaseColor(0xff009846));
        PdfPCell cell2 = new PdfPCell(new Phrase("Descripción", F_CELL_HEADER_TEXT));
        cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell2.setColspan(6);
        cell2.setBackgroundColor(new BaseColor(0xff009846));
        cell2.setFixedHeight(20f);
        PdfPCell cell3 = new PdfPCell(new Phrase("Monto", F_CELL_HEADER_TEXT));
        cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell3.setColspan(2);
        cell3.setBackgroundColor(new BaseColor(0xff009846));
        cell3.setFixedHeight(20f);
        PdfPCell cell4 = new PdfPCell(new Phrase("Pagado con", F_CELL_HEADER_TEXT));
        cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell4.setColspan(2);
        cell4.setBackgroundColor(new BaseColor(0xff009846));
        cell4.setFixedHeight(20f);
        PdfPCell cell5 = new PdfPCell(new Phrase("Total", F_NORMAL));
        cell5.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell5.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell5.setColspan(8);
        cell5.setFixedHeight(20f);
        PdfPCell cell6 = new PdfPCell();
        cell6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell6.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell6.setColspan(4);
        cell6.setFixedHeight(20f);
        PdfPTable table = new PdfPTable(12);
        table.setWidthPercentage(100);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        float total = agregarEgresos(table, egresos);
        cell5.setBorder(Rectangle.RIGHT);
        table.addCell(cell5);
        cell6.setPhrase(new Phrase(String.format("%.2f pesos", total), F_NORMAL));
        table.addCell(cell6);
        documento.add(table);
    }
    
    private float agregarEgresos(PdfPTable table, InformacionEgreso[] egresos){
        float total = 0;
        for(InformacionEgreso egreso : egresos){
            PdfPCell cell4 = new PdfPCell(new Phrase(DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(new Date(egreso.getFecha())), F_NORMAL));
            cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell4.setColspan(2);
            cell4.setFixedHeight(20f);
            PdfPCell cell5 = new PdfPCell(new Phrase(egreso.getDescripcion(), F_NORMAL));
            cell5.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell5.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell5.setColspan(6);
            cell5.setFixedHeight(20f);
            PdfPCell cell6 = new PdfPCell(new Phrase(String.format("%.2f pesos", egreso.getMonto()), F_NORMAL));
            cell6.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cell6.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell6.setColspan(2);
            cell6.setFixedHeight(20f);
            PdfPCell cell7 = new PdfPCell(new Phrase(egreso.getTipoDePago(), F_NORMAL));
            cell7.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell7.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell7.setColspan(2);
            cell7.setFixedHeight(20f);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
            table.addCell(cell7);
            total += egreso.getMonto();
        }
        return total;
    }
    
}

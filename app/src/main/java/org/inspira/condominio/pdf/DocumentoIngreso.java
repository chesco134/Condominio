/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inspira.condominio.pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.inspira.condominio.datos.InformacionIngresos;

/**
 *
 * @author jcapiz
 */
public class DocumentoIngreso {

    private static final Font F_NORMAL = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font F_CELL_HEADER_TEXT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.WHITE);
    private final Document documento;
    private final InformacionIngresos infoIngresos;
    private final InformacionIngresos infoIngresosExtra;
    private float totalRegular;
    private float totalExtra;

    public DocumentoIngreso(InformacionIngresos infoIngresos, InformacionIngresos infoIngresosExtra) {
        documento = new Document(PageSize.A4);
        this.infoIngresos = infoIngresos;
        this.infoIngresosExtra = infoIngresosExtra;
        totalRegular = 0;
        totalExtra = 0;
    }

    public void exportarPdf(String destino, String nombreInmueble, String dir)
            throws IOException, DocumentException {
        File file = new File(destino);
        PdfWriter.getInstance(documento, new FileOutputStream(file));
        documento.open();
        Paragraph inmueble = new Paragraph(nombreInmueble,
                new Font(Font.FontFamily.HELVETICA, 26, Font.BOLD, new BaseColor(0x000000)));
        inmueble.setSpacingAfter(8f);
        documento.add(inmueble);
        documento.add(new Paragraph(dir, F_NORMAL));
        documento.add(new Paragraph("Periodo: ".concat(DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(new Date())), F_NORMAL));
        Paragraph heading = new Paragraph("Formato de ingresos ordinario",
                new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(0x000000)));
        heading.setSpacingBefore(16f);
        heading.setSpacingAfter(16f);
        Paragraph heading2 = new Paragraph("Cobranza ordinaria",
                new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(0x000000)));
        heading2.setSpacingBefore(16f);
        heading2.setSpacingAfter(16f);
        Paragraph heading3 = new Paragraph("Formato de ingresos extraordinario",
                new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(0x000000)));
        heading3.setSpacingBefore(16f);
        heading3.setSpacingAfter(16f);
        Paragraph heading4 = new Paragraph("Cobranza extraordinaria",
                new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(0x000000)));
        heading4.setSpacingBefore(16f);
        heading4.setSpacingAfter(16f);
        documento.add(heading);
        totalRegular = addTablaFormatoIngresos(infoIngresos);
        documento.add(heading2);
        addTablaCobranza(infoIngresos);
        documento.add(heading3);
        totalExtra = addTablaFormatoIngresos(infoIngresosExtra);
        documento.add(heading4);
        addTablaCobranza(infoIngresosExtra);
        Paragraph heading5 = new Paragraph(String.format("TOTAL DE INGRESOS: %.2f pesos", totalRegular + totalExtra),
                new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(0x000000)));
        heading5.setSpacingBefore(16f);
        heading5.setSpacingAfter(16f);
        documento.add(heading5);
        documento.close();
    }
    
    private float addTablaFormatoIngresos(InformacionIngresos infoIngresos) throws DocumentException{
        PdfPCell cellHeader1 = new PdfPCell(new Phrase("Ingresos Ordinarios", F_CELL_HEADER_TEXT));
        cellHeader1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cellHeader1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellHeader1.setColspan(5);
        cellHeader1.setFixedHeight(20f);
        cellHeader1.setBackgroundColor(new BaseColor(0xff009846));
        PdfPCell cellHeader2 = new PdfPCell(new Phrase("Monto", F_CELL_HEADER_TEXT));
        cellHeader2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cellHeader2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellHeader2.setColspan(5);
        cellHeader2.setBackgroundColor(new BaseColor(0xff009846));
        cellHeader2.setFixedHeight(20f);
        PdfPCell cellHeader3 = new PdfPCell(new Phrase("En cuenta bancaria", F_NORMAL));
        cellHeader3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cellHeader3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellHeader3.setColspan(5);
        cellHeader3.setFixedHeight(20f);
        PdfPCell cellHeader4 = new PdfPCell(new Phrase(String.format("%.2f pesos", infoIngresos.getMontoCuentaBancaria()), F_NORMAL));
        cellHeader4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cellHeader4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellHeader4.setColspan(5);
        cellHeader4.setFixedHeight(20f);
        PdfPCell cellHeader5 = new PdfPCell(new Phrase("En caja de administración", F_NORMAL));
        cellHeader5.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cellHeader5.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellHeader5.setColspan(5);
        cellHeader5.setFixedHeight(20f);
        PdfPCell cellHeader6 = new PdfPCell(new Phrase(String.format("%.2f pesos", infoIngresos.getMontoEnCajaDeAdministracion()), F_NORMAL));
        cellHeader6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cellHeader6.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellHeader6.setColspan(5);
        cellHeader6.setFixedHeight(20f);
        PdfPCell cellHeader7 = new PdfPCell(new Phrase("Total", F_NORMAL));
        cellHeader7.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cellHeader7.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellHeader7.setColspan(5);
        cellHeader7.setFixedHeight(20f);
        PdfPCell cellHeader8 = new PdfPCell(new Phrase(String.format("%.2f pesos", infoIngresos.getMontoCuentaBancaria() + infoIngresos.getMontoEnCajaDeAdministracion()), F_NORMAL));
        cellHeader8.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cellHeader8.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cellHeader8.setColspan(5);
        cellHeader8.setFixedHeight(20f);
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);
        table.addCell(cellHeader1);
        table.addCell(cellHeader2);
        table.addCell(cellHeader3);
        table.addCell(cellHeader4);
        table.addCell(cellHeader5);
        table.addCell(cellHeader6);
        table.addCell(cellHeader7);
        table.addCell(cellHeader8);
        table.setSpacingBefore(3f);
        documento.add(table);
        return infoIngresos.getMontoCuentaBancaria() + infoIngresos.getMontoEnCajaDeAdministracion();
    }
    
    private void addTablaCobranza(InformacionIngresos infoIngresos) throws DocumentException{
        PdfPCell cell1 = new PdfPCell(new Phrase("Tipo de condóminos", F_CELL_HEADER_TEXT));
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell1.setColspan(6);
        cell1.setFixedHeight(20f);
        cell1.setBackgroundColor(new BaseColor(0xff009846));
        PdfPCell cell2 = new PdfPCell(new Phrase("Número", F_CELL_HEADER_TEXT));
        cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell2.setColspan(2);
        cell2.setBackgroundColor(new BaseColor(0xff009846));
        cell2.setFixedHeight(20f);
        PdfPCell cell3 = new PdfPCell(new Phrase("Porcentaje", F_CELL_HEADER_TEXT));
        cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell3.setColspan(2);
        cell3.setBackgroundColor(new BaseColor(0xff009846));
        cell3.setFixedHeight(20f);
        PdfPCell cell4 = new PdfPCell(new Phrase("Condóminos que efectuaron su pago", F_NORMAL));
        cell4.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell4.setColspan(6);
        cell4.setFixedHeight(20f);
        PdfPCell cell5 = new PdfPCell(new Phrase(String.valueOf(infoIngresos.getTotalRegulares()), F_NORMAL));
        cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell5.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell5.setColspan(2);
        cell5.setFixedHeight(20f);
        PdfPCell cell6 = new PdfPCell(new Phrase(String.format("%.2f%%", ((float)infoIngresos.getTotalRegulares()/(float)infoIngresos.getTotalhabitantes())*100), F_NORMAL));
        cell6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell6.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell6.setColspan(2);
        cell6.setFixedHeight(20f);
        PdfPCell cell7 = new PdfPCell(new Phrase("Condóminos morosos", F_NORMAL));
        cell7.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell7.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell7.setColspan(6);
        cell7.setFixedHeight(20f);
        PdfPCell cell8 = new PdfPCell(new Phrase(String.valueOf(infoIngresos.getTotalhabitantes() - infoIngresos.getTotalRegulares()), F_NORMAL));
        cell8.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell8.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell8.setColspan(2);
        cell8.setFixedHeight(20f);
        PdfPCell cell9 = new PdfPCell(new Phrase(String.format("%.2f%%",(((float)infoIngresos.getTotalhabitantes() - (float)infoIngresos.getTotalRegulares())/(float)infoIngresos.getTotalhabitantes())*100), F_NORMAL));
        cell9.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell9.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell9.setColspan(2);
        cell9.setFixedHeight(20f);
        PdfPCell cell10 = new PdfPCell(new Phrase("Total", F_NORMAL));
        cell10.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell10.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell10.setColspan(6);
        cell10.setFixedHeight(20f);
        PdfPCell cell11 = new PdfPCell(new Phrase(String.valueOf(infoIngresos.getTotalhabitantes()), F_NORMAL));
        cell11.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell11.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell11.setColspan(2);
        cell11.setFixedHeight(20f);
        PdfPCell cell12 = new PdfPCell(new Phrase( "100%", F_NORMAL));
        cell12.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell12.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell12.setColspan(2);
        cell12.setFixedHeight(20f);
        PdfPTable table = new PdfPTable(10);
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
        table.addCell(cell10);
        table.addCell(cell11);
        table.addCell(cell12);
        documento.add(table);
    }

    private void addHousePicture(String imgResStr) throws BadElementException, IOException, DocumentException {
        Image image = Image.getInstance(imgResStr);
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(image.getScaledWidth());
        table.setLockedWidth(true);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        //cell.setCellEvent(new ImageBackgroundEvent(image));
        cell.setFixedHeight(image.getScaledHeight());
        table.addCell(cell);
        table.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
        documento.add(table);
    }

}

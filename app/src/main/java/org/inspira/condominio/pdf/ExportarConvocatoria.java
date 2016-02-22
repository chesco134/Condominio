package org.inspira.condominio.pdf;

import android.app.Activity;
import android.content.Context;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.inspira.condominio.R;
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.datos.EstructuraConvocatoria;
import org.inspira.condominio.datos.PuntoOdD;
import org.inspira.condominio.dialogos.ProveedorToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by David Azaraf.
 */
public class ExportarConvocatoria {

    private static final Font NORMAL = new Font(Font.FontFamily.HELVETICA, 10.0F, 0, BaseColor.BLACK);
    private static final Font NORMAL_NEGRITA = new Font(Font.FontFamily.HELVETICA, 10.0F, 1, BaseColor.BLACK);
    private static final Font CHIQUITA = new Font(Font.FontFamily.HELVETICA, 8.0F, 0, BaseColor.BLACK);
    private static final Font CHIQUITA_NEGRITA = new Font(Font.FontFamily.HELVETICA, 8.0F, 1, BaseColor.BLACK);
    private Convocatoria convocatoria;
    private EstructuraConvocatoria estructuraConvocatoria;
    private Context context;

    public ExportarConvocatoria(Context context, Convocatoria convocatoria)
    {
        this.convocatoria = convocatoria;
        this.context = context;
        estructuraConvocatoria = new EstructuraConvocatoria(context);
    }

    public void crearArchivo(File file) throws IOException {
        try {
            Document document = new Document(PageSize.A4);
            FileOutputStream fos = new FileOutputStream(file);
            PdfWriter.getInstance(document, fos);
            document.open();
            document.newPage();
            setContenido(document);
            document.close();
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private void setContenido(Document document)
            throws DocumentException
    {
        Paragraph p = new Paragraph(estructuraConvocatoria.getTitulo(), NORMAL_NEGRITA);
        p.setAlignment(1);
        document.add(p);

        p = new Paragraph(estructuraConvocatoria.getIntro(convocatoria.getAsunto())
                + estructuraConvocatoria.getOrigenDeConvocatoria(convocatoria.getCondominio())
                + estructuraConvocatoria.getUbicacion(convocatoria.getUbicacion())
                + estructuraConvocatoria.getFechaConvocatoria(new Date(convocatoria.getFechaInicio()))
                + estructuraConvocatoria.getLugar(convocatoria.getUbicacionInterna()), NORMAL);

        p.setAlignment(3);
        document.add(p);

        p = new Paragraph(estructuraConvocatoria.getPrimeraConvocatoriaTitulo(), NORMAL_NEGRITA);
        p.setAlignment(1);
        document.add(p);
        p = new Paragraph(estructuraConvocatoria.getHoraPrimera(convocatoria.formatoDeTiempo(Convocatoria.PRIMERA_CONV)), NORMAL);
        p.setAlignment(3);
        document.add(p);
        p = new Paragraph(formatoOrdenDelDia(convocatoria.getPuntos()), NORMAL);
        p.setAlignment(0);
        document.add(p);

        p = new Paragraph(estructuraConvocatoria.getSegundaConvocatoriaTitulo(), CHIQUITA_NEGRITA);
        p.setAlignment(1);
        document.add(p);
        p = new Paragraph(estructuraConvocatoria.getHoraSegunda(convocatoria.formatoDeTiempo(Convocatoria.SEGUNDA_CONV)), CHIQUITA);
        p.setAlignment(3);
        document.add(p);

        p = new Paragraph(estructuraConvocatoria.getTerceraConvocatoriaTitulo(), CHIQUITA_NEGRITA);
        p.setAlignment(1);
        document.add(p);
        p = new Paragraph(estructuraConvocatoria.getHoraTercera(convocatoria.formatoDeTiempo(Convocatoria.TERCERA_CONV)), CHIQUITA);
        p.setAlignment(3);
        document.add(p);

        p = new Paragraph(estructuraConvocatoria.getLey(), CHIQUITA);
        p.setAlignment(3);
        document.add(p);

        document.add(Chunk.NEWLINE);
        p = new Paragraph(estructuraConvocatoria.getFechaPublicacion(new Date()), NORMAL_NEGRITA);
        p.setAlignment(1);
        document.add(p);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        p = new Paragraph("ATENTAMENTE", NORMAL_NEGRITA);
        p.setAlignment(1);
        document.add(p);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        p = new Paragraph(convocatoria.getFirma(), NORMAL_NEGRITA);
        p.setAlignment(1);
        document.add(p);
    }

    private String formatoOrdenDelDia(List<PuntoOdD> puntos){
        String ordenDelDia = context.getString(R.string.estructura_de_convocatoria_primer_punto_unamovible)
                + "\n"
                + context.getString(R.string.estructura_de_convocatoria_segundo_punto_inamovible);
        for(PuntoOdD punto : puntos){
            ordenDelDia = ordenDelDia.concat("\n" + punto.getDescripcion());
        }
        return ordenDelDia;
    }
}
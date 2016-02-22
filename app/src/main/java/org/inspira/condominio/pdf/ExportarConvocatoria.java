package org.inspira.condominio.pdf;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.inspira.condominio.R;
import org.inspira.condominio.datos.EstructuraConvocatoria;
import org.inspira.condominio.dialogos.ProveedorToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by David Azaraf.
 */
public class ExportarConvocatoria {

    private static final Font NORMAL = new Font(Font.FontFamily.HELVETICA, 10.0F, 0, BaseColor.BLACK);
    private static final Font NORMAL_NEGRITA = new Font(Font.FontFamily.HELVETICA, 10.0F, 1, BaseColor.BLACK);
    private static final Font CHIQUITA = new Font(Font.FontFamily.HELVETICA, 8.0F, 0, BaseColor.BLACK);
    private static final Font CHIQUITA_NEGRITA = new Font(Font.FontFamily.HELVETICA, 8.0F, 1, BaseColor.BLACK);
    private String convocatoria;
    private String condominio;
    private String ubicacion;
    private Date fechaConvocatoria;
    private String lugarAsamblea;
    private String ordenDelDia;
    private String primeraHora;
    private String segundaHora;
    private String terceraHora;
    private Date hoy;
    private String firma;
    private EstructuraConvocatoria estructuraConvocatoria;
    private Context context;

    public ExportarConvocatoria(Context context, String convocatoria, String condominio, String ubicacion, Date fechaConvocatoria, String lugarAsamblea, String ordenDelDia, String primeraHora, String segundaHora, String terceraHora, Date hoy, String firma)
    {
        this.convocatoria = convocatoria;
        this.condominio = condominio;
        this.ubicacion = ubicacion;
        this.fechaConvocatoria = fechaConvocatoria;
        this.lugarAsamblea = lugarAsamblea;
        this.ordenDelDia = ordenDelDia;
        this.primeraHora = primeraHora;
        this.segundaHora = segundaHora;
        this.terceraHora = terceraHora;
        this.hoy = hoy;
        this.firma = firma;
        this.context = context;
        estructuraConvocatoria = new EstructuraConvocatoria(context);
    }

    public void crearArchivo(final File file) throws IOException {
        new Thread() {
            @Override public void run() {
                try {
                    Document document = new Document(PageSize.A4);
                    FileOutputStream fos = new FileOutputStream(file);
                    PdfWriter.getInstance(document, fos);
                    document.open();
                    document.newPage();
                    setContenido(document);
                    document.close();
                    ((Activity)context).runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            ProveedorToast
                                    .showToast(context, R.string.crear_convocatoria_archivo_creado);
                        }
                    });
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void setContenido(Document document)
            throws DocumentException
    {
        Paragraph p = new Paragraph(estructuraConvocatoria.getTitulo(), NORMAL_NEGRITA);
        p.setAlignment(1);
        document.add(p);

        p = new Paragraph(estructuraConvocatoria.getIntro(this.convocatoria)
                + estructuraConvocatoria.getOrigenDeConvocatoria(this.condominio)
                + estructuraConvocatoria.getUbicacion(this.ubicacion)
                + estructuraConvocatoria.getFechaConvocatoria(this.fechaConvocatoria)
                + estructuraConvocatoria.getLugar(this.lugarAsamblea), NORMAL);

        p.setAlignment(3);
        document.add(p);

        p = new Paragraph(estructuraConvocatoria.getPrimeraConvocatoriaTitulo(), NORMAL_NEGRITA);
        p.setAlignment(1);
        document.add(p);
        p = new Paragraph(estructuraConvocatoria.getHoraPrimera(this.primeraHora), NORMAL);
        p.setAlignment(3);
        document.add(p);
        ordenDelDia = context.getString(R.string.estructura_de_convocatoria_primer_punto_unamovible)
                + "\n"
                + context.getString(R.string.estructura_de_convocatoria_segundo_punto_inamovible)
                + "\n"
                + ordenDelDia;
        p = new Paragraph(ordenDelDia, NORMAL);
        p.setAlignment(0);
        document.add(p);

        p = new Paragraph(estructuraConvocatoria.getSegundaConvocatoriaTitulo(), CHIQUITA_NEGRITA);
        p.setAlignment(1);
        document.add(p);
        p = new Paragraph(estructuraConvocatoria.getHoraSegunda(this.segundaHora), CHIQUITA);
        p.setAlignment(3);
        document.add(p);

        p = new Paragraph(estructuraConvocatoria.getTerceraConvocatoriaTitulo(), CHIQUITA_NEGRITA);
        p.setAlignment(1);
        document.add(p);
        p = new Paragraph(estructuraConvocatoria.getHoraTercera(this.terceraHora), CHIQUITA);
        p.setAlignment(3);
        document.add(p);

        p = new Paragraph(estructuraConvocatoria.getLey(), CHIQUITA);
        p.setAlignment(3);
        document.add(p);

        document.add(Chunk.NEWLINE);
        p = new Paragraph(estructuraConvocatoria.getFechaPublicacion(this.hoy), NORMAL_NEGRITA);
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
        p = new Paragraph(this.firma, NORMAL_NEGRITA);
        p.setAlignment(1);
        document.add(p);
    }
}

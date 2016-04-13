package org.inspira.condominio.actividades;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.inspira.condominio.dialogos.DialogoDeLista;

/**
 * Created by jcapiz on 19/03/16.
 */
public class ActualizaTextoDesdeLista implements View.OnClickListener {

    private int stringArrayRes;
    private String titulo;
    private String[] elementos;
    private View referencedView;

    public ActualizaTextoDesdeLista(int stringArrayRes, String titulo) {
        this.stringArrayRes = stringArrayRes;
        this.titulo = titulo;
    }

    public ActualizaTextoDesdeLista(String titulo, String[] elementos) {
        this.titulo = titulo;
        this.elementos = elementos;
    }

    public void setReferencedView(View referencedView) {
        this.referencedView = referencedView;
    }

    @Override
    public void onClick(final View v){
        DialogoDeLista dlista = new DialogoDeLista();
        dlista.setAccion(new DialogoDeLista.AccionDialogoDeLista() {
            @Override
            public void objetoSeleccionado(String texto) {
                if (referencedView == null) {
                    ((TextView) v).setText(texto);
                    ((TextView) v).setTextColor(Color.BLACK);
                }else{
                    ((TextView) referencedView).setText(texto);
                    ((TextView) referencedView).setTextColor(Color.BLACK);
                }
            }
        });
        if(elementos != null)
            dlista.setElementos(elementos);
        else
            dlista.setStringArrayRes(stringArrayRes);
        dlista.setTitulo(titulo);
        dlista.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "Seleccionar elemento");
    }
}

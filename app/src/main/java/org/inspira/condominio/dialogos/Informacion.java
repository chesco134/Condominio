package org.inspira.condominio.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.inspira.condominio.R;

public class Informacion extends DialogFragment {

    private EntradaTexto.AccionDialogo accion;

    public void setAccion(EntradaTexto.AccionDialogo accion) {
        this.accion = accion;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle extras = getArguments();
        String titulo = extras.getString("titulo");
        String mensaje = extras.getString("mensaje");
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton(R.string.dialogo_entrada_texto_aceptar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accion.accionPositiva(Informacion.this);
                            }
                        })
                .setNegativeButton(R.string.dialogo_entrada_texto_cancelar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accion.accionNegativa(Informacion.this);
                            }
                        });
        return builder.create();
    }
}

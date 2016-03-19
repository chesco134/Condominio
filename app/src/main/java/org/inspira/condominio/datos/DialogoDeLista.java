package org.inspira.condominio.datos;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.inspira.condominio.R;

/**
 * Created by jcapiz on 16/03/16.
 */
public class DialogoDeLista extends DialogFragment {

    private AccionDialogoDeLista accion;
    private int stringArrayResource;

    public interface AccionDialogoDeLista{
        void objetoSeleccionado(String texto);
    }

    public void setAccion(AccionDialogoDeLista accion) {
        this.accion = accion;
    }

    public void setStringArrayResource(int stringArrayResource) {
        this.stringArrayResource = stringArrayResource;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tipo de condominio")
                .setItems(stringArrayResource, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        accion.objetoSeleccionado(getActivity().getResources().getStringArray(R.array.tipos_de_condominio)[which]);
                    }
                });
        return builder.create();
    }
}

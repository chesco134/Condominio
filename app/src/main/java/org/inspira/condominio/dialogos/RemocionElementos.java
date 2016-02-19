package org.inspira.condominio.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.inspira.condominio.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siempre on 19/02/2016.
 */
public class RemocionElementos extends DialogFragment {

    private String[] elementos;
    private EntradaTexto.AccionDialogo ad;
    private List<Integer> elementosSeleccionados;

    public Integer[] getElementosSeleccionados() {
        return elementosSeleccionados.toArray(new Integer[0]);
    }

    public void setAd(EntradaTexto.AccionDialogo ad) {
        this.ad = ad;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Bundle args;
        if(savedInstanceState == null){
            args = getArguments();
            elementosSeleccionados = new ArrayList<>();
        }else{
            args = savedInstanceState;
            elementosSeleccionados = args.getIntegerArrayList("elementos_seleccionados");
        }
        elementos = args.getStringArray("elementos");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogo_remocion_elementos_titulo)
                .setPositiveButton(R.string.dialogo_entrada_texto_aceptar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ad.accionPositiva(RemocionElementos.this);
                            }
                        })
                .setNegativeButton(R.string.dialogo_entrada_texto_cancelar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ad.accionNegativa(RemocionElementos.this);
                            }
                        })
                .setMultiChoiceItems(elementos, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            elementosSeleccionados.add(new Integer(which));
                        }else{
                            elementosSeleccionados.remove(new Integer(which));
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putStringArrayList("elementos_seleccionados",(ArrayList)elementosSeleccionados);
        outState.putStringArray("elementos",elementos);
    }

}
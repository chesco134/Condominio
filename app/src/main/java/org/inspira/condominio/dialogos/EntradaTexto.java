package org.inspira.condominio.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import org.inspira.condominio.R;

/**
 * Created by Siempre on 19/02/2016.
 */
public class EntradaTexto extends DialogFragment {

    private String mensaje;

    public interface AccionDialogo{
        void accionPositiva(DialogFragment fragment);
        void accionNegativa(DialogFragment fragment);
    }

    private AccionDialogo accionDialogo;

    public String getEntradaDeTexto() {
        return entradaDeTexto.getText().toString();
    }

    public void setEntradaDeTexto(EditText entradaDeTexto) {
        this.entradaDeTexto = entradaDeTexto;
    }

    private EditText entradaDeTexto;

    public void setAccionDialogo(AccionDialogo accionDialogo) {
        this.accionDialogo = accionDialogo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args;
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        entradaDeTexto = (EditText)inflater.inflate(R.layout.entrada_de_texto, null);
        if(savedInstanceState == null){
            args = getArguments();
        }else{
            args = savedInstanceState;
            entradaDeTexto.setText(args.getString("texto"));
        }
        mensaje = args.getString("mensaje");
        builder.setTitle(mensaje)
                .setPositiveButton(R.string.dialogo_entrada_texto_aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        accionDialogo.accionPositiva(EntradaTexto.this);
                    }
                })
                .setNegativeButton(R.string.dialogo_entrada_texto_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        accionDialogo.accionNegativa(EntradaTexto.this);
                    }
                })
                .setView(entradaDeTexto);
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("mensaje", mensaje);
        outState.putString("texto", entradaDeTexto.getText().toString());
    }

}

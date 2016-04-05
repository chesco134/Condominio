package org.inspira.condominio.actividades;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.dialogos.DialogoDeLista;

/**
 * Created by jcapiz on 30/03/16.
 */
public class ActualizaEntradaDesdeArreglo implements View.OnClickListener{

    private ColocaValorDesdeDialogo.FormatoDeMensaje fdm;
    private AccionCheckBox.ActualizacionDeCampo adc;
    private String key;
    private int arrayRes;
    private Context context;
    private String elementos[];

    public ActualizaEntradaDesdeArreglo(Context context, ColocaValorDesdeDialogo.FormatoDeMensaje fdm, int arrayRes, String key, AccionCheckBox.ActualizacionDeCampo adc) {
        this.fdm = fdm;
        this.adc = adc;
        this.key = key;
        this.arrayRes = arrayRes;
        elementos = null;
        this.context = context;
    }

    public ActualizaEntradaDesdeArreglo(Context context, ColocaValorDesdeDialogo.FormatoDeMensaje fdm, String[] elementos, String key, AccionCheckBox.ActualizacionDeCampo adc) {
        this.fdm = fdm;
        this.adc = adc;
        this.key = key;
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public void onClick(View view){
        DialogoDeLista ddl = new DialogoDeLista();
        if( elementos != null )
            ddl.setElementos(elementos);
        else
        if( arrayRes == -1)
            ddl.setStringArrayRes(R.array.tipos_de_condominio);
        else
            ddl.setStringArrayRes(arrayRes);
        String base = key.split("id")[1].replace('_', ' ');
        ddl.setTitulo(base);
        ddl.setAccion(new MiAccionDialogoDeLista(context, key, (TextView) view, fdm, adc));
        ddl.show(((AppCompatActivity) context).getSupportFragmentManager(), "Seleccion Arreglo");
    }
}

package org.inspira.condominio.actividades;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jcapiz on 18/03/16.
 */
public class Verificador {

    public static final int FLOTANTE = 1;
    public static final int ENTERO = 2;
    public static final int TEXTO = 3;

    public static boolean marcaCampo(Context context, TextView campo, int tipo){
        boolean correcto = validaCampoNumerico(campo, tipo);
        if(!correcto){
            campo.setBackgroundColor(ProveedorDeRecursos.obtenerColorDeError(context));
        }
        return correcto;
    }

    private static boolean validaCampoNumerico(TextView campoNumerico, int tipo){
        boolean veredicto = false;
        String contenido = campoNumerico.getText().toString().trim();
        if(!"".equals(contenido))
            try{
                switch (tipo) {
                    case FLOTANTE:
                        float valorFlotante = Float.parseFloat(contenido);
                        veredicto = true;
                        break;
                    case ENTERO:
                        int valorEntero = Integer.parseInt(contenido);
                        veredicto = true;
                        break;
                    case TEXTO:
                        veredicto = true;
                        break;
                }
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
        return veredicto;
    }

}

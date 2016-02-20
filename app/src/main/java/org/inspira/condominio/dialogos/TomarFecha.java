package org.inspira.condominio.dialogos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Siempre on 19/02/2016.
 */
public class TomarFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private String fecha;
    private boolean panic = false;
    private EntradaTexto.AccionDialogo accionDialogo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        fecha = null;

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        if(Calendar.getInstance().compareTo(c) <= 0){
            view.setBackgroundColor(Color.parseColor("#ff86241f"));
            fecha = (day < 10 ? "0" + day : day) + "/" + (month < 10 ? "0" + month : month)
                    + "/" + year;
            accionDialogo.accionPositiva(this);
        }else{
            view.setBackgroundColor(Color.WHITE);
            panic = true;
        }
    }

    public void setAccionDialogo(EntradaTexto.AccionDialogo accionDialogo) {
        this.accionDialogo = accionDialogo;
    }

    public boolean wreckHavoc() {
        return panic;
    }

    public String getFecha() {
        return fecha;
    }
}

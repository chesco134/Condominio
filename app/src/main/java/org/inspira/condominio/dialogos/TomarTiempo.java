package org.inspira.condominio.dialogos;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Siempre on 19/02/2016.
 */
public class TomarTiempo extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private boolean panic = false;
    private String tiempo;
    private EntradaTexto.AccionDialogo accionDialogo;

    public EntradaTexto.AccionDialogo getAccionDialogo() {
        return accionDialogo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setAccionDialogo(EntradaTexto.AccionDialogo accionDialogo) {
        this.accionDialogo = accionDialogo;
    }

    public boolean wreckHavoc() {
        return panic;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        tiempo = null;

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        int cHourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int cMinute = c.get(Calendar.MINUTE);
        if( cHourOfDay >= hourOfDay && cMinute > minute){
            panic = true;
        }else{
            tiempo = (hourOfDay < 10 ? "0"+hourOfDay : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute);
            accionDialogo.accionPositiva(this);
        }
    }

}

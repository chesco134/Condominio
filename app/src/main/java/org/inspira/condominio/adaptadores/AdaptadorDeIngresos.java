package org.inspira.condominio.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.admon.AccionesTablaContable;
import org.inspira.condominio.admon.AccionesTablaHabitante;
import org.inspira.condominio.datos.Habitante;
import org.inspira.condominio.datos.Ingreso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jcapiz on 13/04/16.
 */
public class AdaptadorDeIngresos extends BaseAdapter {

    private Context context;
    private Ingreso[] ingresos;
    private float total;

    public AdaptadorDeIngresos(Context context){
        this.context = context;
        ingresos = AccionesTablaContable.obtenerIngresosDelMes(context);
        total = 0;
        for(Ingreso ingreso : ingresos)
            total += ingreso.getMonto();
    }

    public float getTotal(){
        return total;
    }

    @Override
    public int getCount() {
        return ingresos.length;
    }

    @Override
    public Object getItem(int position) {
        return ingresos[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.elemento_ingreso, parent, false);
        TextView monto = (TextView)view.findViewById(R.id.elemento_ingreso_monto);
        TextView nombreHabitante = (TextView)view.findViewById(R.id.elemento_ingreso_habitante);
        TextView razonDeIngreso = (TextView) view.findViewById(R.id.elemento_ingreso_razon_de_ingreso);
        TextView conceptoIngreso = (TextView) view.findViewById(R.id.elemento_ingreso_concepto_de_ingreso);
        TextView fecha = (TextView) view.findViewById(R.id.elemento_ingreso_fecha);
        monto.setText(String.valueOf(ingresos[position].getMonto()));
        Habitante habitante = AccionesTablaHabitante.obtenerHabitante(context, ingresos[position].getIdHabitante());
        String nombreCompleto = habitante.getApPaterno()
                + " " + habitante.getApMaterno() + " " + habitante.getNombres();
        nombreHabitante.setText(nombreCompleto);
        razonDeIngreso.setText(AccionesTablaContable.obtenerRazonDeIngreso(context, ingresos[position].getRazonDeIngreso().getId()));
        conceptoIngreso.setText(AccionesTablaContable.obtenerConceptoDeIngreso(context, ingresos[position].getConceptoDeIngreso().getId()));
        fecha.setText(DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(new Date(ingresos[position].getFecha())));
        fecha.append(formatedHour(ingresos[position].getFecha()));
        return view;
    }

    private String formatedHour(long time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        return "\n" + (hour < 10 ? "0" + hour : hour)
                + ":" + (minute < 10 ? "0" + minute : minute);
    }
}

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
import org.inspira.condominio.datos.Egreso;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jcapiz on 13/04/16.
 */
public class AdaptadorDeEgresos extends BaseAdapter {

    private Context context;
    private Egreso[] egresos;
    private float total;

    public AdaptadorDeEgresos(Context context) {
        this.context = context;
        egresos = AccionesTablaContable.obtenerEgresosDelMes(context);
        total = 0;
        for(Egreso egreso : egresos)
            total += egreso.getMonto();
    }

    public float getTotal(){
        return total;
    }

    @Override
    public int getCount() {
        return egresos.length;
    }

    @Override
    public Object getItem(int position) {
        return egresos[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view;
        if(convertView == null)
            view = (RelativeLayout)inflater.inflate(R.layout.elemento_egreso, parent, false);
        else
            view = (RelativeLayout)convertView;
        TextView monto = (TextView)view.findViewById(R.id.elemento_egreso_monto);
        TextView nombreHabitante = (TextView)view.findViewById(R.id.elemento_egreso_habitante);
        TextView razonDeIngreso = (TextView) view.findViewById(R.id.elemento_egreso_razon_de_egreso);
        TextView fecha = (TextView) view.findViewById(R.id.elemento_egreso_fecha);
        monto.setText(String.valueOf(egresos[position].getMonto()));
        nombreHabitante.setText(egresos[position].getFavorecido());
        razonDeIngreso.setText(AccionesTablaContable.obtenerRazonDeEgreso(context, egresos[position].getIdRazonDeEgreso()));
        fecha.setText(DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(new Date(egresos[position].getFecha())));
        if(egresos[position].isEsExtraordinario()){
            fecha.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else{
            fecha.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        return view;
    }
}

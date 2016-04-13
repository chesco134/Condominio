package org.inspira.condominio.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.adaptadores.AdaptadorDeIngresos;

import java.util.Calendar;

/**
 * Created by jcapiz on 13/04/16.
 */
public class FormatosDeIngreso extends Fragment {

    private ListView ingresos;
    private TextView total;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.formato_de_ingreso_fragment, parent, false);
        ingresos = (ListView) rootView.findViewById(R.id.lista_de_ingreso_lista_ingresos);
        total = (TextView) rootView.findViewById(R.id.lista_de_ingreso_total);
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String monthStr;
        switch (month){
            case 0:
                monthStr = "Enero";
                break;
            case 1:
                monthStr = "Febrero";
                break;
            case 2:
                monthStr = "Marzo";
                break;
            case 3:
                monthStr = "Abril";
                break;
            case 4:
                monthStr = "Mayo";
                break;
            case 5:
                monthStr = "Junio";
                break;
            case 6:
                monthStr = "Julio";
                break;
            case 7:
                monthStr = "Agosto";
                break;
            case 8:
                monthStr = "Septiembre";
                break;
            case 9:
                monthStr = "Octubre";
                break;
            case 10:
                monthStr = "Noviembre";
                break;
            case 11:
                monthStr = "Diciembre";
                break;
            default:
                monthStr = "";
        }
        monthStr = monthStr.concat(" de " + year);
        assert total != null;
        total.setText(monthStr);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        AdaptadorDeIngresos adaptadorDeIngresos = new AdaptadorDeIngresos(getContext());
        ingresos.setAdapter(adaptadorDeIngresos);
        total.setText("Total: ".concat(String.format("%.2f", adaptadorDeIngresos.getTotal())).concat(" pesos"));
    }
}

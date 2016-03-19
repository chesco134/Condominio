package org.inspira.condominio.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import org.inspira.condominio.admin.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jcapiz on 15/03/16.
 */
public class MallaDeCheckBoxes extends BaseAdapter {

    private final Map<String, Boolean> marcas;
    private Context context;
    private String[] elementos;

    public MallaDeCheckBoxes(Context context, Map<String, Boolean> marcas){
        this.context = context;
        this.marcas = marcas;
        elementos = TEXTOS;
    }

    public MallaDeCheckBoxes(Context context, Map<String, Boolean> marcas, String[] elementos){
        this.context = context;
        this.marcas = marcas;
        this.elementos = elementos;
    }

    @Override
    public int getCount() {
        return elementos.length;
    }

    @Override
    public Object getItem(int position) {
        return elementos[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null){
            view = convertView;
            ((CheckBox)view).setChecked(marcas.get(((CheckBox)view).getText().toString()));
        }else {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.elemento_check_box, parent, false);
            CheckBox cbox = (CheckBox)view.findViewById(R.id.nuevo_condominio_check_box);
            cbox.setText(elementos[position]);
            cbox.setChecked(marcas.get(elementos[position]));
            cbox.setOnClickListener(new CheckBoxClicked());
        }
        return view;
    }

    private class CheckBoxClicked implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            CheckBox box = (CheckBox) v;
            marcas.put(box.getText().toString(), box.isChecked());
        }
    }

    public static final String[] TEXTOS =
            {"Sala de juntas"
            ,"GYM"
            ,"Espacio recreativo"
            ,"Espacio cultural"
            ,"Oficinas administrativas"
            ,"Alarma sísmica"
            ,"Cisterna de agua pluvial"};
}

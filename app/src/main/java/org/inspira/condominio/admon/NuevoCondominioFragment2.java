package org.inspira.condominio.admon;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.inspira.condominio.adaptadores.MallaDeCheckBoxes;
import org.inspira.condominio.admin.R;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jcapiz on 16/03/16.
 */
public class NuevoCondominioFragment2 extends Fragment {

    private static final Map<String, Boolean> marcas = new TreeMap<>();
    private AccionNCondominio2 accion;

    public interface AccionNCondominio2{
        void siguiente(Map<String, Boolean> values);

        void onResume();
    }

    public void setAccion(AccionNCondominio2 accion){
        this.accion = accion;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        if(savedInstanceState == null){
            for(String elemento : MallaDeCheckBoxes.TEXTOS)
                marcas.put(elemento, false);
        }else{
            boolean[] values = savedInstanceState.getBooleanArray("values");
            for(int i=0; i<MallaDeCheckBoxes.TEXTOS.length; i++)
                marcas.put(MallaDeCheckBoxes.TEXTOS[i], values != null && values[i]);
        }
        View rootView = inflater.inflate(R.layout.nuevo_condominio_2, parent, false);
        MallaDeCheckBoxes malla = new MallaDeCheckBoxes(getContext(), marcas);
        GridView mallaDeOpciones = (GridView) rootView.findViewById(R.id.nuevo_condominio_2_checkboxes);
        mallaDeOpciones.setAdapter(malla);
        rootView.findViewById(R.id.nuevo_condominio_2_siguiente)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accion.siguiente(marcas);
                    }
                });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        Boolean[] booleans = marcas.values().toArray(new Boolean[0]);
        boolean[] bols = new boolean[booleans.length];
        for(int i=0; i<booleans.length; i++)
            bols[i] = booleans[i];
        outState.putBooleanArray("values", bols);
    }

    @Override
    public void onResume(){
        super.onResume();
        accion.onResume();
    }
}

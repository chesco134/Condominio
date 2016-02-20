package org.inspira.condominio.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.CrearConvocatoria;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.dialogos.RemocionElementos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdenDelDia extends Fragment {

    private List<String> puntos;
    private ListView listaDePuntos;
    private ArrayAdapter<String> adapter;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.hacer_orden_del_dia,parent,false);
        if(savedInstanceState == null) {
            puntos = new ArrayList<>();
            Bundle args = getArguments();
            if(args != null)
                Collections.addAll(puntos, args.getStringArray("puntos"));
        }else
            puntos = savedInstanceState.getStringArrayList("puntos");
        assert puntos != null;
        adapter = new ArrayAdapter<>(getActivity(), R.layout.entrada_simple, puntos);
        listaDePuntos = (ListView) rootView.findViewById(R.id.hacer_orden_del_dia_lista);
        listaDePuntos.setAdapter(adapter);
        listaDePuntos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cambioDeTexto(((TextView) view).getText().toString(), position);
            }
        });
        setHasOptionsMenu(true);
        Log.d("{Orden del día}", "Soy de la sagrada orden del día y me he recreado");
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_convocatoria, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        boolean manejado = false;
        int itemId = item.getItemId();
        if(itemId == R.id.menu_convocatoria_agregar_punto){
            agregarPunto();
            manejado = true;
        }else if(itemId == R.id.menu_convocatoria_quitar_punto){
            removerPuntos();
            manejado = true;
        }else if(itemId == R.id.menu_convocatoria_hecho){
            ((CrearConvocatoria)getActivity()).creaConvocatoria();
        }
        return manejado;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putStringArrayList("puntos", (ArrayList<String>) puntos);
    }

    private void hecho(){
        if(puntos.size() > 0){
            ((CrearConvocatoria)getActivity()).creaConvocatoria();
        }else{
            ProveedorSnackBar.muestraBarraDeBocados(listaDePuntos,
                    getActivity().getString(R.string.orden_del_dia_parametros_faltantes));
        }
    }

    private void cambioDeTexto(String contenido, int posicion){
        EntradaTexto det = new EntradaTexto();
        Bundle args = new Bundle();
        args.putString("contenido", contenido);
        args.putString("mensaje",getResources().getString(R.string.dialogo_entrada_texto_modificar_texto));
        det.setArguments(args);
        det.setAccionDialogo(new AccionCambioDeTexto(posicion));
        det.show(getActivity().getSupportFragmentManager(), "Cambiar texto");
    }

    private void agregarPunto(){
        EntradaTexto et = new EntradaTexto();
        Bundle args = new Bundle();
        args.putString("contenido","");
        args.putString("mensaje", getResources().getString(R.string.dialogo_entrada_texto_agregar_texto));
        et.setArguments(args);
        et.setAccionDialogo(new AccionAgregarPunto());
        et.show(getActivity().getSupportFragmentManager(), "Agregar texto");
    }

    private void removerPuntos(){
        RemocionElementos re = new RemocionElementos();
        Bundle args = new Bundle();
        args.putStringArray("elementos", puntos.toArray(new String[0]));
        re.setArguments(args);
        re.setAd(new AccionQuitarPuntos());
        re.show(getActivity().getSupportFragmentManager(), "Remover texto");
    }

    private class AccionCambioDeTexto implements EntradaTexto.AccionDialogo{

        private int posicion;

        public AccionCambioDeTexto(int posicion){
            this.posicion = posicion;
        }

        @Override
        public void accionPositiva(DialogFragment df){
            EntradaTexto det = (EntradaTexto) df;
            String texto = det.getEntradaDeTexto();
            puntos.set(posicion, texto);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void accionNegativa(DialogFragment df){}
    }

    private class AccionAgregarPunto implements EntradaTexto.AccionDialogo{

        @Override
        public void accionPositiva(DialogFragment fragment) {
            EntradaTexto det = (EntradaTexto) fragment;
            puntos.add(det.getEntradaDeTexto());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void accionNegativa(DialogFragment fragment) {}
    }

    private class AccionQuitarPuntos implements EntradaTexto.AccionDialogo{

        @Override
        public void accionPositiva(DialogFragment fragment) {
            RemocionElementos re = (RemocionElementos) fragment;
            Integer[] elementos = re.getElementosSeleccionados();
            prepareElements(elementos);
            for(Integer elemento : elementos)
                puntos.remove(elemento.intValue());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void accionNegativa(DialogFragment fragment) {}
    }

    private void prepareElements(Integer[] elements){
        Integer hold;
        for(int i=0; i<elements.length; i++){
            for(int j=i+1; j<elements.length; j++){
                if(elements[i] < elements[j]){
                    hold = elements[i];
                    elements[i] = elements[j];
                    elements[j] = hold;
                }
            }
        }
    }

    public String[] getPuntos() {
        try{return puntos.toArray(new String[0]);}
        catch(NullPointerException ignore){ return null; }
    }

    public void setPuntos(String[] puntos) {
        try {
            Collections.addAll(this.puntos, puntos);
            adapter.notifyDataSetChanged();
        }catch(NullPointerException ignore){}
    }
}
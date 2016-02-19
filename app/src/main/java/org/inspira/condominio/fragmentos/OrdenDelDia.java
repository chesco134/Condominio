package org.inspira.condominio.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.CrearConvocatoria;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.dialogos.RemocionElementos;

import java.util.ArrayList;
import java.util.List;

public class OrdenDelDia extends Fragment {

    private List<String> puntos;
    private ListView listaDePuntos;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.hacer_orden_del_dia,parent,false);
        if(savedInstanceState == null)
            puntos = new ArrayList<>();
        else
            puntos = savedInstanceState.getStringArrayList("puntos");
        assert puntos != null;
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.entrada_simple, puntos);
        listaDePuntos = (ListView) rootView.findViewById(R.id.hacer_orden_del_dia_lista);
        listaDePuntos.setAdapter(adapter);
        listaDePuntos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cambioDeTexto(position);
            }
        });
        setHasOptionsMenu(true);
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

    private void cambioDeTexto(int posicion){
        EntradaTexto det = new EntradaTexto();
        Bundle args = new Bundle();
        args.putString("mensaje",getResources().getString(R.string.dialogo_entrada_texto_modificar_texto));
        det.setArguments(args);
        det.setAccionDialogo(new AccionCambioDeTexto(posicion));
        det.show(getActivity().getSupportFragmentManager(), "Cambio de texto");
    }

    private void agregarPunto(){
        EntradaTexto et = new EntradaTexto();
        Bundle args = new Bundle();
        args.putString("titulo", getResources().getString(R.string.dialogo_entrada_texto_agregar_texto));
        et.setArguments(args);
        et.setAccionDialogo(new AccionAgregarPunto());
        et.show(getActivity().getSupportFragmentManager(), "Agregar texto");
    }

    private void removerPuntos(){
        RemocionElementos re = new RemocionElementos();
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
        }

        @Override
        public void accionNegativa(DialogFragment df){}
    }

    private class AccionAgregarPunto implements EntradaTexto.AccionDialogo{

        @Override
        public void accionPositiva(DialogFragment fragment) {
            EntradaTexto det = (EntradaTexto) fragment;
            puntos.add(det.getEntradaDeTexto());
        }

        @Override
        public void accionNegativa(DialogFragment fragment) {}
    }

    private class AccionQuitarPuntos implements EntradaTexto.AccionDialogo{

        @Override
        public void accionPositiva(DialogFragment fragment) {
            RemocionElementos re = (RemocionElementos) fragment;
            Integer[] elementos = re.getElementosSeleccionados();
            for(Integer elemento : elementos)
                puntos.remove(elemento.intValue());
        }

        @Override
        public void accionNegativa(DialogFragment fragment) {}
    }

    public String[] getPuntos() {
        return puntos.toArray(new String[0]);
    }
}
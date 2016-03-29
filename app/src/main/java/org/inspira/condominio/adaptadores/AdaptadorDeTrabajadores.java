package org.inspira.condominio.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.admon.AccionesTablaTrabajador;
import org.inspira.condominio.datos.Trabajador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jcapiz on 28/03/16.
 */
public class AdaptadorDeTrabajadores extends BaseAdapter {

    private Context context;
    private List<Trabajador> trabajadores;

    public AdaptadorDeTrabajadores(Context context, int idTipoDeTrabajador) {
        this.context = context;
        cargarListaDeTrabajadores(idTipoDeTrabajador);
    }

    public void agregarTrabajador(Trabajador trabajador){
        trabajadores.add(trabajador);
        notifyDataSetChanged();
    }

    public Trabajador removerTrabajador(int posicion){
        Trabajador trabajador = trabajadores.remove(posicion);
        notifyDataSetChanged();
        return trabajador;
    }

    public List<Trabajador> obtenerTrabajadores(){
        return trabajadores;
    }

    @Override
    public int getCount() {
        return trabajadores.size();
    }

    @Override
    public Object getItem(int position) {
        return trabajadores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.entrada_lista_habitante, parent, false);
        Trabajador trabajador = trabajadores.get(position);
        String nombreHabitante = trabajador.getApPaterno() + " " + trabajador.getApMaterno() + " " + trabajador.getNombres();
        ((TextView) view.findViewById(R.id.entrada_lista_habitante_nombre))
                .setText(nombreHabitante);
        ((TextView) view.findViewById(R.id.entrada_lista_habitante_departamento))
                .setText(trabajador.isPoseeSeguroSocial() ? "Asegurado" : "------");
        ((ImageView) view.findViewById(R.id.entrada_lista_habitante_perfil))
                .setImageResource(trabajador.isGenero() ? R.drawable.user_coin_blk : R.drawable.woman_coin);
        return view;
    }

    private void cargarListaDeTrabajadores(int idTipoDeTrabajador){
        trabajadores = new ArrayList<>();
        Collections.addAll(trabajadores, AccionesTablaTrabajador.obtenerTrabajadores(context, idTipoDeTrabajador));
    }
}

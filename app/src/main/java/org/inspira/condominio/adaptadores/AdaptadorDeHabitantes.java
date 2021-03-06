package org.inspira.condominio.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.admon.AccionesTablaHabitante;
import org.inspira.condominio.datos.Habitante;
import org.inspira.condominio.dialogos.ProveedorSnackBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jcapiz on 25/03/16.
 */
public class AdaptadorDeHabitantes extends BaseAdapter {

    private Context context;
    private List<Habitante> habitantes;

    public AdaptadorDeHabitantes(Context context) {
        this.context = context;
        cargarListaDeHabitantes();
    }

    public void agregarHabitante(Habitante habitante){
        habitantes.add(habitante);
        notifyDataSetChanged();
    }

    public Habitante removerHabitante(int posicion){
        Habitante habitante = habitantes.remove(posicion);
        notifyDataSetChanged();
        return habitante;
    }

    @Override
    public int getCount() {
        return habitantes.size();
    }

    @Override
    public Object getItem(int position) {
        return habitantes.get(position);
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
        Habitante habitante = habitantes.get(position);
        String nombreHabitante = habitante.getApPaterno() + " " + habitante.getApMaterno() + " " + habitante.getNombres();
        ((TextView) view.findViewById(R.id.entrada_lista_habitante_nombre))
                .setText(nombreHabitante);
        ((TextView) view.findViewById(R.id.entrada_lista_habitante_departamento))
                .setText(habitante.getNombreDepartamento());
        ImageView logo = (ImageView) view.findViewById(R.id.entrada_lista_habitante_perfil);
        logo.setImageResource(habitante.isGenero() ? R.drawable.user_coin_blk :
                R.drawable.woman_coin);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProveedorSnackBar
                        .muestraBarraDeBocados(v, "Uy que me haces cosquillas! :D");
            }
        });
        return view;
    }

    private void cargarListaDeHabitantes(){
        habitantes = new ArrayList<>();
        Collections.addAll(habitantes, AccionesTablaHabitante.obtenerHabitantes(context, ProveedorDeRecursos.obtenerIdTorreActual(context)));
    }
}

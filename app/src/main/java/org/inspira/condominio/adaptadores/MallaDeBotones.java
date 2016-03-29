package org.inspira.condominio.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.admin.formatos.FormatosLobby;
import org.inspira.condominio.admin.trabajadores.ControlDeTrabajadores;

/**
 * Created by jcapiz on 27/03/16.
 */
public class MallaDeBotones extends BaseAdapter {

    private Context context;

    public MallaDeBotones(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.panel_boton, parent, false);
        if(parent.getWidth() > 0){
            rootView.setLayoutParams(new AbsListView.LayoutParams((parent.getWidth()/3) -2, (parent.getWidth()/3) - 2));
            rootView.setBackgroundResource(colores[position]);
        }
        ImageView icon = (ImageView) rootView.findViewById(R.id.panel_boton_icono);
        icon.setImageResource(iconos[position]);
        TextView panelText = (TextView) rootView.findViewById(R.id.panel_boton_texto);
        panelText.setText(etiquetas[position]);
        panelText.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Black.ttf"));
        rootView.setOnClickListener(new ButtonClickHandler(position));
        return rootView;
    }

    private class ButtonClickHandler implements View.OnClickListener{

        private final int destiny;

        public ButtonClickHandler(int destiny) {
            this.destiny = destiny;
        }

        @Override
        public void onClick(View view){
            switch(destiny){
                case 0:
                    //
                    break;
                case 1:
                    context.startActivity(new Intent(context, FormatosLobby.class));
                    break;
                case 2:
                    context.startActivity(new Intent(context, ControlDeTrabajadores.class));
                    break;
            }
        }
    }

    private static final int[] colores = {R.drawable.my_custom_panel_buton_1, R.drawable.my_custom_panel_buton_2, R.drawable.my_custom_panel_button_3};
    private static final String[] etiquetas = {"Codominio", "Contabilidad","Trabajadores"};
    private static final int[] iconos = {R.drawable.ic_domain_white_24dp, R.drawable.ic_receipt_white_24dp, R.drawable.ic_work_white_24dp};
}

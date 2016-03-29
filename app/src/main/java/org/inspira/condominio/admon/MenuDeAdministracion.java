package org.inspira.condominio.admon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import org.inspira.condominio.R;
import org.inspira.condominio.adaptadores.MallaDeBotones;

/**
 * Created by jcapiz on 27/03/16.
 */
public class MenuDeAdministracion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_administracion);
        ((GridView)findViewById(R.id.menu_administracion_malla_de_botones))
                .setAdapter(new MallaDeBotones(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

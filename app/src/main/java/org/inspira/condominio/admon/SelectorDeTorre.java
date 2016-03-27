package org.inspira.condominio.admon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.inspira.condominio.R;

/**
 * Created by jcapiz on 26/03/16.
 */
public class SelectorDeTorre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.preparacion_main_container, new FragmentoSelectorDeTorre())
                    .commit();
        }
    }

}

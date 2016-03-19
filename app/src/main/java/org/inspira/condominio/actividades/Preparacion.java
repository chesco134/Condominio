package org.inspira.condominio.actividades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.inspira.condominio.R;
import org.inspira.condominio.fragmentos.Login;
import org.inspira.condominio.fragmentos.SignUp;

/**
 * Created by jcapiz on 29/02/16.
 */
public class Preparacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        if(savedInstanceState == null)
            colocaFragmento();
    }

    private void colocaFragmento() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.preparacion_main_container, new Login())
                .commit();
    }
}

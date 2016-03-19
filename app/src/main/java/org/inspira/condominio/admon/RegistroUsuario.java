package org.inspira.condominio.admon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.inspira.condominio.R;
import org.inspira.condominio.fragmentos.SignUp;

/**
 * Created by jcapiz on 19/03/16.
 */
public class RegistroUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.preparacion_main_container, new SignUp())
                    .commit();
        }
    }
}

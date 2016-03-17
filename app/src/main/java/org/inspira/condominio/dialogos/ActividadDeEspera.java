package org.inspira.condominio.dialogos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.inspira.condominio.R;

/**
 * Created by jcapiz on 16/03/16.
 */
public class ActividadDeEspera extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_activity);

    }

    @Override
    public void onBackPressed(){

    }
}

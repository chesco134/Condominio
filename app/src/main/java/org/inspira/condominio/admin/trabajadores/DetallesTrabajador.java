package org.inspira.condominio.admin.trabajadores;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jcapiz on 4/04/16.
 */
public class DetallesTrabajador extends AppCompatActivity {

    private LinearLayout contenedorContactos;
    private int[] idContactos;
    private TextView nombreHabitante;
    private CheckBox poseeSeguro;
    private CheckBox marcaEsPropietario;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scrolling_detalles_habitante);

    }
}

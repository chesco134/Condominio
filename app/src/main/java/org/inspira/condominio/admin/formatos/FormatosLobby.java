package org.inspira.condominio.admin.formatos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.inspira.condominio.R;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.dialogos.ProveedorToast;

public class FormatosLobby extends AppCompatActivity {

    private static final int CREAR_FORMATO = 135;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formatos_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.texto_formatos_admin));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchFormatos();
            }
        });
    }

    private void launchFormatos() {
        startActivityForResult(new Intent(FormatosLobby.this, Formatos.class), CREAR_FORMATO);
    }

    private void iniciaDialogoParaConsultaDeSello(String email){
        EntradaTexto et = new EntradaTexto();
        Bundle args = new Bundle();
        args.putString("mensaje", "Por favor, ingrese su sello electrónico para continuar");
        et.setArguments(args);
        et.setAccionDialogo(new AccionSobreIngresoDeSello(email) {
        });
        et.show(getSupportFragmentManager(), "Ingresar sello");
    }

    private class AccionSobreIngresoDeSello implements EntradaTexto.AccionDialogo{

        private String email;

        public AccionSobreIngresoDeSello(String email) {
            this.email = email;
        }

        @Override
        public void accionPositiva(DialogFragment fragment) {
            EntradaTexto et = (EntradaTexto) fragment;
            String sello = et.getEntradaDeTexto();
            if (!"".equals(sello)) {
                //new CondominioBD(FormatosLobby.this).agregaSello(email, sello);
                launchFormatos();
                ProveedorToast.showToast(FormatosLobby.this, "Gracias");
            }else{
                ProveedorSnackBar
                        .muestraBarraDeBocados(findViewById(R.id.toolbar), "El campo no debe quedar vacío");
            }
        }

        @Override
        public void accionNegativa(DialogFragment fragment) {
        }
    }

}
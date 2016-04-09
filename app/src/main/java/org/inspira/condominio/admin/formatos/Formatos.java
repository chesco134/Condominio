package org.inspira.condominio.admin.formatos;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.adaptadores.MyFragmentStatePagerAdapter;
import org.inspira.condominio.admon.AccionesTablaContable;
import org.inspira.condominio.dialogos.EntradaTexto;

import java.util.LinkedList;

public class Formatos extends AppCompatActivity implements
        ActionBar.TabListener {

    private MyFragmentStatePagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formatos_admin);
        FragmentManager fm = getSupportFragmentManager();
        LinkedList<Fragment> frags = new LinkedList<>();
        frags.add(new FormatoDeIngreso());
        frags.add(new FormatoDeEgreso());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(), frags);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final ActionBar actionBar = getSupportActionBar();mViewPager
                .addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (Integer TITULO : TITULOS) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(TITULO)
                    .setTabListener(this));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
        getSupportActionBar().setTitle(TITULOS[tab.getPosition()]);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


    public static class FormatoDeIngreso extends Fragment {

        private Spinner razonesDePago;
        private Spinner conceptosDePago;
        private EditText monto;
        private EditText nombre;

        public FormatoDeIngreso() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.formato_de_ingreso, container, false);
            razonesDePago = (Spinner) rootView.findViewById(R.id.formato_de_ingreso_razon_de_pago);
            conceptosDePago = (Spinner) rootView.findViewById(R.id.formato_de_ingreso_concepto);
            monto = (EditText) rootView.findViewById(R.id.formato_de_ingreso_monto);
            nombre = (EditText) rootView.findViewById(R.id.formato_de_ingreso_nombre);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                    AccionesTablaContable.obtenerRazonesDeIngreso(getContext()));
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                    AccionesTablaContable.obtenerConceptosDeIngreso(getContext()));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            razonesDePago.setAdapter(adapter);
            conceptosDePago.setAdapter(adapter2);
            razonesDePago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedText = ((TextView) view).getText().toString();
                    if ("Otro".equals(selectedText)) {
                        iniciaRecepcionDeTexto();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            return rootView;
        }

        private void iniciaRecepcionDeTexto() {
            EntradaTexto entradaTexto = new EntradaTexto();
            Bundle args = new Bundle();
            args.putString("mensaje", "Nueva razón de ingreso");
            entradaTexto.setArguments(args);
            entradaTexto.setAccionDialogo(new EntradaTexto.AccionDialogo() {
                @Override
                public void accionPositiva(DialogFragment fragment) {

                }

                @Override
                public void accionNegativa(DialogFragment fragment) {}
            });
            entradaTexto.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "Agregar");
        }
    }

    public static class FormatoDeEgreso extends Fragment{

        private Spinner conceptosDeEgreso;
        private EditText monto;
        private EditText favorecido;

        public FormatoDeEgreso(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.formato_de_egreso, container, false);
            conceptosDeEgreso = (Spinner) rootView.findViewById(R.id.formato_de_egreso_razon_de_pago);
            monto = (EditText) rootView.findViewById(R.id.formato_de_egreso_monto);
            favorecido = (EditText) rootView.findViewById(R.id.formato_de_egreso_nombre);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.razones_de_egreso, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            conceptosDeEgreso.setAdapter(adapter);
            return rootView;
        }
    }

    private static final Integer[] TITULOS = {
            R.string.formato_de_ingreso_header,
            R.string.formato_de_egreso_header
    };
}

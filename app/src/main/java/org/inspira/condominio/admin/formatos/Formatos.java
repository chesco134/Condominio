package org.inspira.condominio.admin.formatos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.ActualizaTextoDesdeLista;
import org.inspira.condominio.actividades.CompruebaCamposJSON;
import org.inspira.condominio.actividades.MuestraMensajeDesdeHilo;
import org.inspira.condominio.actividades.ProveedorDeRecursos;
import org.inspira.condominio.adaptadores.MyFragmentStatePagerAdapter;
import org.inspira.condominio.admon.AccionesTablaContable;
import org.inspira.condominio.admon.AccionesTablaHabitante;
import org.inspira.condominio.datos.ConceptoDeIngreso;
import org.inspira.condominio.datos.Habitante;
import org.inspira.condominio.datos.Ingreso;
import org.inspira.condominio.datos.RazonDeEgreso;
import org.inspira.condominio.datos.RazonDeIngreso;
import org.inspira.condominio.dialogos.DialogoDeLista;
import org.inspira.condominio.dialogos.EntradaTexto;
import org.inspira.condominio.dialogos.ProveedorSnackBar;
import org.inspira.condominio.networking.ContactoConServidor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

        private TextView razonDePago;
        private TextView conceptoDePago;
        private TextView nombre;
        private SwitchCompat esExtraordinario;
        private EditText monto;
        private List<String> nombres;
        private Habitante[] habitantes;

        public FormatoDeIngreso() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.formato_de_ingreso, container, false);
            razonDePago = (TextView) rootView.findViewById(R.id.formato_de_ingreso_razon_de_pago);
            conceptoDePago = (TextView) rootView.findViewById(R.id.formato_de_ingreso_concepto);
            nombre = (TextView) rootView.findViewById(R.id.formato_de_ingreso_nombre);
            esExtraordinario = (SwitchCompat) rootView.findViewById(R.id.formato_de_ingreso_es_extraordinario);
            monto = (EditText) rootView.findViewById(R.id.formato_de_ingreso_monto);
            Button confirmar = (Button) rootView.findViewById(R.id.formato_de_ingreso_confirmar);
            confirmar.setOnClickListener(new ValidacionDeCampos(getContext()));
            return rootView;
        }

        @Override
        public void onResume(){
            super.onResume();
            colocarElementos();
        }

        private void colocarElementos() {
            RelativeLayout contenedorRazonesDePago = (RelativeLayout) getView().findViewById(R.id.formato_de_ingreso_contenedor_razon_de_ingreso);
            String[] razonesDeIngreso = AccionesTablaContable.obtenerRazonesDeIngreso(getContext()).toArray(new String[0]);
            contenedorRazonesDePago.setOnClickListener(new MiDialogoDeLista(getContext(), "Razones de ingreso", razonesDeIngreso, "Razon_de_Ingreso", razonDePago));
            RelativeLayout contenedorConceptoDePago = (RelativeLayout) getView().findViewById(R.id.formato_de_ingreso_contenedor_concepto_de_ingreso);
            String[] conceptosDeIngreso = AccionesTablaContable.obtenerConceptosDeIngreso(getContext()).toArray(new String[0]);
            contenedorConceptoDePago.setOnClickListener(new MiDialogoDeLista(getContext(), "Conceptos de ingreso", conceptosDeIngreso, "Concepto_de_Ingreso", conceptoDePago));
            RelativeLayout contenedorNombre = (RelativeLayout) getView().findViewById(R.id.formato_de_ingreso_contenedor_nombre);
            habitantes = AccionesTablaHabitante.obtenerHabitantes(getContext());
            nombres = new ArrayList<>();
            for(Habitante habitante : habitantes)
                nombres.add(habitante.getApPaterno() + " " + habitante.getApMaterno() + " " + habitante.getNombres());
            ActualizaTextoDesdeLista actualizaTextoDesdeLista3 = new ActualizaTextoDesdeLista("Habitantes", nombres.toArray(new String[0]));
            actualizaTextoDesdeLista3.setReferencedView(nombre);
            contenedorNombre.setOnClickListener(actualizaTextoDesdeLista3);
        }

        private class ValidacionDeCampos implements View.OnClickListener,
            ContactoConServidor.AccionesDeValidacionConServidor{

            private Context context;
            private Ingreso ingreso;
            private View view;

            public ValidacionDeCampos(Context context) {
                this.context = context;
                this.view = view;
            }

            @Override
            public void onClick(View v){
                iniciaValidacionDeCampos();
            }

            private void iniciaValidacionDeCampos(){
                RazonDeIngreso razonDeIngreso = generaRazonDeIngreso();
            }

            private RazonDeIngreso generaRazonDeIngreso(){
                RazonDeIngreso razonDeIngreso = null;
                String razon = razonDePago.getText().toString();
                String concepto = conceptoDePago.getText().toString();
                String nombre = FormatoDeIngreso.this.nombre.getText().toString();
                String monto = FormatoDeIngreso.this.monto.getText().toString();
                if(!"Razón de pago".equals(razon) && !"Concepto".equals(concepto) && !"Nombre".equals(nombre)){
                    iniciaValidacionDeCamposRemota(razon, concepto, nombre, Float.parseFloat(monto));
                }
                return razonDeIngreso;
            }

            private void iniciaValidacionDeCamposRemota(String razon, String concepto, String nombre, float monto) {
                String cuerpoDeMensaje = armarCuerpoDeMensaje(razon, concepto, nombre, monto);
                ContactoConServidor contactoConServidor = new ContactoConServidor(this, cuerpoDeMensaje);
                contactoConServidor.start();
            }

            private String armarCuerpoDeMensaje(String razon, String concepto, String nombre, float monto) {
                String cuerpoDeMensaje = null;
                Habitante habitante = habitantes[nombres.indexOf(nombre)];
                ConceptoDeIngreso conceptoDeIngreso = new ConceptoDeIngreso(AccionesTablaContable.obtenerIdConceptoDeIngreso(context, concepto));
                conceptoDeIngreso.setConceptoDeIngreso(concepto);
                RazonDeIngreso razonDeIngreso = new RazonDeIngreso(AccionesTablaContable.obtenerIdRazonDeIngreso(context, razon));
                razonDeIngreso.setRazonDeIngreso(razon);
                ingreso = new Ingreso();
                ingreso.setIdHabitante(habitante.getId());
                ingreso.setConceptoDeIngreso(conceptoDeIngreso);
                ingreso.setRazonDeIngreso(razonDeIngreso);
                ingreso.setDepartamento(habitante.getNombreDepartamento());
                ingreso.setFecha(new Date().getTime());
                ingreso.setMonto(monto);
                ingreso.setExtraordinario(esExtraordinario.isChecked());
                ingreso.setEmail(ProveedorDeRecursos.obtenerEmail(context));
                try{
                    JSONObject json = new JSONObject();
                    json.put("action", ProveedorDeRecursos.REGISTRO_DE_INGRESO);
                    json.put("idRazon_de_Ingreso", ingreso.getRazonDeIngreso());
                    json.put("idConcepto_de_Ingreso", ingreso.getConceptoDeIngreso());
                    json.put("idHabitante", ingreso.getIdHabitante());
                    json.put("departamento", ingreso.getDepartamento());
                    json.put("fecha", ingreso.getFecha());
                    json.put("es_extraordinario", ingreso.isExtraordinario() ? 1 : 0);
                    json.put("email", ingreso.getEmail());
                    cuerpoDeMensaje = json.toString();
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return cuerpoDeMensaje;
            }

            @Override
            public void resultadoSatisfactorio(Thread t) {
                String response = ((ContactoConServidor)t).getResponse();
                if(CompruebaCamposJSON.validaContenido(response)){
                    int id = (int) CompruebaCamposJSON.obtenerCampos(response).get("id");
                    ingreso.setId(id);
                    AccionesTablaContable.agregarIngreso(context, ingreso);
                    MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, view, "Hecho");
                }else{
                    MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, view, "Servicio por el momento no disponible");
                }
            }

            @Override
            public void problemasDeConexion(Thread t) {
                MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, view, "Servicio momentáneamente inalcanzable");
            }
        }
    }

    public static class FormatoDeEgreso extends Fragment{

        private TextView conceptosDeEgreso;
        private TextView monto;
        private TextView favorecido;

        public FormatoDeEgreso(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.formato_de_egreso, container, false);
            conceptosDeEgreso = (TextView) rootView.findViewById(R.id.formato_de_egreso_razon_de_egreso);
            monto = (TextView) rootView.findViewById(R.id.formato_de_egreso_monto);
            favorecido = (TextView) rootView.findViewById(R.id.formato_de_egreso_favorecido);
            return rootView;
        }

        @Override
        public void onResume(){
            super.onResume();
            colocarElementos();
        }

        private void colocarElementos() {
            RelativeLayout contenedorRazonesDeEgreso = (RelativeLayout) getView().findViewById(R.id.formato_de_egreso_contenedor_razon_de_egreso);
            String[] razonesDeEgreso = AccionesTablaContable.obtenerRazonesDeEgreso(getContext()).toArray(new String[0]);
        }
    }

    private static final Integer[] TITULOS = {
            R.string.formato_de_ingreso_header,
            R.string.formato_de_egreso_header
    };

    private static class MiDialogoDeLista implements View.OnClickListener,
            DialogoDeLista.AccionDialogoDeLista,
            EntradaTexto.AccionDialogo,
            ContactoConServidor.AccionesDeValidacionConServidor{

        private Context context;
        private String titulo;
        private String[] elementos;
        private String tabla;
        private TextView etiquetaObjetivo;
        private String valor;

        public MiDialogoDeLista(Context context, String titulo, String[] elementos, String tabla, TextView etiquetaObjetivo) {
            this.context = context;
            this.titulo = titulo;
            this.elementos = elementos;
            this.tabla = tabla;
            this.etiquetaObjetivo = etiquetaObjetivo;
        }

        @Override
        public void onClick(View v) {
            mostrarDialogoDeLista();
        }

        @Override
        public void objetoSeleccionado(String texto) {
            if("Otro".equals(texto)){
                iniciaDialogoDeEntradaDeTexto();
            }else{
                colocarTexto(texto);
            }
        }

        @Override
        public void accionPositiva(DialogFragment fragment) {
            String respuesta = ((EntradaTexto) fragment).getEntradaDeTexto();
            if(!"".equals(respuesta)){
                valor = respuesta;
                iniciarValidacionDeTexto(respuesta);
            }
        }

        @Override
        public void accionNegativa(DialogFragment fragment) {}

        @Override
        public void resultadoSatisfactorio(Thread t) {
            String respuesta = ((ContactoConServidor)t).getResponse();
            if(CompruebaCamposJSON.validaContenido(respuesta)){
                Map<String, Object> campos = CompruebaCamposJSON.obtenerCampos(respuesta);
                int id = (Integer)campos.get("id");
                if("Razon_de_Ingreso".equals(tabla)){
                    RazonDeIngreso razonDeIngreso = new RazonDeIngreso(id);
                    razonDeIngreso.setRazonDeIngreso(valor);
                    AccionesTablaContable.agregarRazonDeIngreso(context, razonDeIngreso);
                }else if("Concepto_de_Ingreso".equals(tabla)){
                    ConceptoDeIngreso conceptoDeIngreso = new ConceptoDeIngreso(id);
                    conceptoDeIngreso.setConceptoDeIngreso(valor);
                    AccionesTablaContable.agregarConceptoDeIngreso(context, conceptoDeIngreso);
                }else if("Razon_de_Egreso".equals(tabla)){
                    RazonDeEgreso razonDeEgreso= new RazonDeEgreso(id);
                    razonDeEgreso.setRazonDeEgreso(valor);
                    AccionesTablaContable.agregarRazonDeEgreso(context, razonDeEgreso);
                }
                colocarTexto(valor);
                despliegueDeNotificacion("Hecho");
            }
        }

        @Override
        public void problemasDeConexion(Thread t) {
            despliegueDeNotificacion("Servicio temporalmente no disponible");
        }

        private String armarCuerpoDeMensaje(String textoCandidato){
            String cuerpoDeMensaje = null;
            try{
                JSONObject json = new JSONObject();
                json.put("table", tabla);
                json.put("value", textoCandidato);
                json.put("action", ProveedorDeRecursos.REGISTRO_DE_RAZON_O_CONCEPTO);
                cuerpoDeMensaje = json.toString();
            }catch(JSONException e){
                e.printStackTrace();
            }
            return cuerpoDeMensaje;
        }

        private void despliegueDeNotificacion(String mensaje){
            DespliegueDeNotificacion despliegueDeNotificacion = new DespliegueDeNotificacion(etiquetaObjetivo, mensaje);
            ((AppCompatActivity)context).runOnUiThread(despliegueDeNotificacion);
        }

        private void iniciarValidacionDeTexto(String textoCandidato){
            String cuerpoDeMensaje = armarCuerpoDeMensaje(textoCandidato);
            ContactoConServidor contactoConServidor = new ContactoConServidor(this, cuerpoDeMensaje);
            contactoConServidor.start();
        }

        private void colocarTexto(String texto){
            ActualizacionDeTextoObjetivo actualizar = new ActualizacionDeTextoObjetivo(texto);
            ((AppCompatActivity)context).runOnUiThread(actualizar);
        }

        private void iniciaDialogoDeEntradaDeTexto(){
            EntradaTexto entradaTexto = new EntradaTexto();
            Bundle args = new Bundle();
            args.putString("mensaje", "Nueva opción");
            entradaTexto.setArguments(args);
            entradaTexto.setAccionDialogo(this);
            entradaTexto.show(((AppCompatActivity) context).getSupportFragmentManager(), "Definir");
        }

        private void mostrarDialogoDeLista(){
            DialogoDeLista dialogoDeLista = new DialogoDeLista();
            dialogoDeLista.setTitulo(titulo);
            dialogoDeLista.setElementos(elementos);
            dialogoDeLista.setAccion(this);
            dialogoDeLista.show(((AppCompatActivity) context).getSupportFragmentManager(), "Selección");
        }

        private class ActualizacionDeTextoObjetivo implements Runnable{

            private String texto;

            private ActualizacionDeTextoObjetivo(String texto) {
                this.texto = texto;
            }

            @Override
            public void run(){
                etiquetaObjetivo.setText(texto);
            }
        }

        private class DespliegueDeNotificacion implements Runnable{

            private View view;
            private String mensaje;

            public DespliegueDeNotificacion(View view, String mensaje) {
                this.view = view;
                this.mensaje = mensaje;
            }

            @Override
            public void run(){
                ProveedorSnackBar.muestraBarraDeBocados(view, mensaje);
            }
        }
    }
}
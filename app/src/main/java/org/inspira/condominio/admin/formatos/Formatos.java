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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import org.inspira.condominio.datos.Egreso;
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
        private EditText monto;
        private SwitchCompat existeEnBanco;
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
            monto = (EditText) rootView.findViewById(R.id.formato_de_ingreso_monto);
            existeEnBanco = (SwitchCompat) rootView.findViewById(R.id.formato_de_ingreso_en_banco);
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
            }

            @Override
            public void onClick(View v){
                this.view = v;
                iniciaValidacionDeCampos();
            }

            private void iniciaValidacionDeCampos(){
                generaRazonDeIngreso();
            }

            private void generaRazonDeIngreso(){
                String razon = razonDePago.getText().toString();
                String concepto = conceptoDePago.getText().toString();
                String nombre = FormatoDeIngreso.this.nombre.getText().toString();
                String monto = FormatoDeIngreso.this.monto.getText().toString();
                if(!"Razón de pago".equals(razon) && !"Concepto".equals(concepto) && !"Nombre".equals(nombre)){
                    iniciaValidacionDeCamposRemota(razon, concepto, nombre, Float.parseFloat(monto));
                }
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
                ingreso.setExisteEnBanco(existeEnBanco.isChecked());
                ingreso.setEmail(ProveedorDeRecursos.obtenerEmail(context));
                try{
                    JSONObject json = new JSONObject();
                    json.put("action", ProveedorDeRecursos.REGISTRO_DE_INGRESO);
                    json.put("idRazon_de_Ingreso", ingreso.getRazonDeIngreso().getId());
                    json.put("idConcepto_de_Ingreso", ingreso.getConceptoDeIngreso().getId());
                    json.put("monto", ingreso.getMonto());
                    json.put("idHabitante", ingreso.getIdHabitante());
                    json.put("departamento", ingreso.getDepartamento());
                    json.put("fecha", ingreso.getFecha());
                    json.put("existe_en_banco", ingreso.isExisteEnBanco());
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
                    MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity) context, view, "Hecho");
                    getActivity().finish();
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

        private TextView razonDeEgreso;
        private TextView monto;
        private TextView favorecido;
        private SwitchCompat esExtraordinario;

        public FormatoDeEgreso(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.formato_de_egreso, container, false);
            razonDeEgreso = (TextView) rootView.findViewById(R.id.formato_de_egreso_razon_de_egreso);
            monto = (TextView) rootView.findViewById(R.id.formato_de_egreso_monto);
            favorecido = (TextView) rootView.findViewById(R.id.formato_de_egreso_favorecido);
            esExtraordinario = (SwitchCompat) rootView.findViewById(R.id.formato_de_egreso_es_extraordinario);
            Button confirmar = (Button) rootView.findViewById(R.id.formato_de_egreso_confirmar);
            confirmar.setOnClickListener(new ValidacionDeCampos(getContext()));
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
            contenedorRazonesDeEgreso.setOnClickListener(new MiDialogoDeLista(getContext(), "Razones de egreso", razonesDeEgreso, "Razon_de_Egreso", razonDeEgreso));
            RelativeLayout contenedorMonto = (RelativeLayout) getView().findViewById(R.id.formato_de_egreso_contenedor_monto);
            contenedorMonto.setOnClickListener(new ActualizarCampoMonto(monto, getContext()));
            RelativeLayout contenedorFavorecido = (RelativeLayout) getView().findViewById(R.id.formato_de_egreso_contenedor_favorecido);
            contenedorFavorecido.setOnClickListener(new ActualizarCampoFavorecido(getContext(), favorecido));
        }

        private class ActualizarCampoFavorecido implements View.OnClickListener, EntradaTexto.AccionDialogo {

            private Context context;
            private TextView referencedView;

            public ActualizarCampoFavorecido(Context context, TextView referencedView) {
                this.context = context;
                this.referencedView = referencedView;
            }

            @Override
            public void onClick(View v) {
                iniciarDialogo();
            }

            private void iniciarDialogo(){
                EntradaTexto et = new EntradaTexto();
                et.setTipoDeEntradaDeTexto(EditorInfo.TYPE_CLASS_TEXT);
                Bundle args = new Bundle();
                args.putString("mensaje", "A favor de");
                args.putString("contenido", referencedView.getText().toString());
                et.setArguments(args);
                et.setAccionDialogo(this);
                et.show(((AppCompatActivity)context).getSupportFragmentManager(), "Obtener favorecido");
            }

            @Override
            public void accionPositiva(DialogFragment fragment) {
                String texto = ((EntradaTexto)fragment).getEntradaDeTexto();
                if(!"".equals(texto))
                    referencedView.setText(texto);
            }

            @Override
            public void accionNegativa(DialogFragment fragment) {}
        }

        private class ActualizarCampoMonto implements View.OnClickListener, EntradaTexto.AccionDialogo {

            private TextView referencedView;
            private Context context;

            public ActualizarCampoMonto(TextView referencedView, Context context) {
                this.referencedView = referencedView;
                this.context = context;
            }

            @Override
            public void onClick(View view){
                iniciarDialogoDeMonto();
            }

            private void iniciarDialogoDeMonto(){
                EntradaTexto et = new EntradaTexto();
                et.setTipoDeEntradaDeTexto(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                Bundle args = new Bundle();
                args.putString("mensaje", "Defina el monto");
                args.putString("contenido", referencedView.getText().toString());
                et.setArguments(args);
                et.setAccionDialogo(this);
                et.show(((AppCompatActivity)context).getSupportFragmentManager(), "Obtener monto");
            }

            @Override
            public void accionPositiva(DialogFragment fragment) {
                String texto = ((EntradaTexto)fragment).getEntradaDeTexto();
                if(!"".equals(texto))
                    try{
                        if(Float.parseFloat(texto) > 0)
                            referencedView.setText(texto);
                        else
                            ProveedorSnackBar.muestraBarraDeBocados(referencedView, "¿Se trata de una deuda?");
                    } catch (NumberFormatException e){
                        e.printStackTrace();
                        ProveedorSnackBar.muestraBarraDeBocados(referencedView, "Debe ser un número");
                    }
            }

            @Override
            public void accionNegativa(DialogFragment fragment) {}
        }

        private class ValidacionDeCampos implements View.OnClickListener,
                ContactoConServidor.AccionesDeValidacionConServidor{

            private Context context;
            private Egreso egreso;
            private View view;

            public ValidacionDeCampos(Context context) {
                this.context = context;
            }

            @Override
            public void onClick(View v){
                this.view = v;
                iniciaValidacionDeCampos();
            }

            private void iniciaValidacionDeCampos(){
                generaRazonDeIngreso();
            }

            private void generaRazonDeIngreso(){
                String razon = razonDeEgreso.getText().toString();
                String favorecido = FormatoDeEgreso.this.favorecido.getText().toString();
                String monto = FormatoDeEgreso.this.monto.getText().toString();
                if(!"Razón de pago".equals(razon) && !"Favorecido".equals(favorecido)){
                    iniciaValidacionDeCamposRemota(razon, favorecido, Float.parseFloat(monto));
                }
            }

            private void iniciaValidacionDeCamposRemota(String razon, String favorecido, float monto) {
                String cuerpoDeMensaje = armarCuerpoDeMensaje(razon, favorecido, monto);
                ContactoConServidor contactoConServidor = new ContactoConServidor(this, cuerpoDeMensaje);
                contactoConServidor.start();
            }

            private String armarCuerpoDeMensaje(String razon, String favorecido, float monto) {
                String cuerpoDeMensaje = null;
                egreso = new Egreso();
                egreso.setIdRazonDeEgreso(AccionesTablaContable.obtenerIdRazonDeEgreso(context, razon));
                egreso.setMonto(monto);
                egreso.setFecha(new Date().getTime());
                egreso.setFavorecido(favorecido);
                egreso.setEsExtraordinario(esExtraordinario.isChecked());
                egreso.setEmail(ProveedorDeRecursos.obtenerEmail(context));
                try{
                    JSONObject json = new JSONObject();
                    json.put("action", ProveedorDeRecursos.REGISTRO_DE_EGRESO);
                    json.put("idRazon_de_Egreso", egreso.getIdRazonDeEgreso());
                    json.put("monto", egreso.getMonto());
                    json.put("fecha", egreso.getFecha());
                    json.put("favorecido", egreso.getFavorecido());
                    json.put("es_extraordinario", egreso.isEsExtraordinario() ? 1 : 0);
                    json.put("email", egreso.getEmail());
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
                    egreso.setId(id);
                    AccionesTablaContable.agregarEgreso(context, egreso);
                    MuestraMensajeDesdeHilo.muestraMensaje((AppCompatActivity)context, view, "Hecho");
                    getActivity().finish();
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
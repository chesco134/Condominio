package org.inspira.condominio.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.inspira.condominio.R;
import org.inspira.condominio.actividades.Configuraciones;
import org.inspira.condominio.actividades.Lobby;
import org.inspira.condominio.actividades.Preparacion;
import org.inspira.condominio.actividades.SplashScreen;
import org.inspira.condominio.admin.formatos.Formatos;
import org.inspira.condominio.admin.formatos.FormatosLobby;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.dialogos.ProveedorSnackBar;

public class CentralPoint extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SERVER_URL = "http://votacionesipn.com/condominios/";
    private boolean isFirstTime;
    private boolean secondTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_point);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProveedorSnackBar.muestraBarraDeBocados(findViewById(R.id.hello_world),
                        getString(R.string.crear_convocatoria_sitio_en_construccion));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        isFirstTime = savedInstanceState == null;
        secondTime = true;
    }

    @Override
    protected void onPostResume(){
        super.onPostResume();
        if(isFirstTime) {
            isFirstTime = false;
            launchSplash();
        }else
            revisarDatosDeUsuario();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(navigationView.getHeaderCount()-1);
        ((TextView)hView.findViewById(R.id.under_pp)).setText(
                getSharedPreferences(getClass().getName(), Context.MODE_PRIVATE)
                        .getString("usuario", "NaN")
        );
        ((TextView)hView.findViewById(R.id.textView)).setText(
                getSharedPreferences(getClass().getName(), Context.MODE_PRIVATE)
                        .getString("email", "NaN")
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.central_point, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        if(itemId == R.id.action_add_person){
            launchRegitraNuevoHabitante();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchRegitraNuevoHabitante() {
        ProveedorSnackBar
                .muestraBarraDeBocados(findViewById(R.id.hello_world), "Sitio en construcción");
    }

    private void launchSplash() {
        startActivity(new Intent(this, SplashScreen.class));
    }

    private void revisarDatosDeUsuario() {
        CondominioBD db = new CondominioBD(this);
        if(!db.revisarExistenciaDeUsuarios())
            iniciaRegistro();
    }

    private void iniciaRegistro() {
        startActivity(new Intent(this, Preparacion.class));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_convocatorias) {
            launchConvocatorias();
        } else if (id == R.id.nav_administracion) {
            launchAdministracion();
        } else if (id == R.id.nav_accidentes) {
            launchAccidentes();
        } else if (id == R.id.nav_configuracion) {
            launchConfiguracion();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchConvocatorias(){
        Intent i = new Intent(this, Lobby.class);
        startActivity(i);
    }

    private void launchAdministracion(){
        startActivity(new Intent(this, FormatosLobby.class));
    }

    private void launchAccidentes(){
        ProveedorSnackBar.muestraBarraDeBocados(findViewById(R.id.hello_world),
                getString(R.string.crear_convocatoria_sitio_en_construccion));
    }

    private void launchConfiguracion(){
        startActivity(new Intent(this, Configuraciones.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(secondTime){
                ProveedorSnackBar
                        .muestraBarraDeBocados(findViewById(R.id.toolbar), "Presione una vez más para salir");
                secondTime = false;
            }else{
                super.onBackPressed();
            }
        }
    }
}
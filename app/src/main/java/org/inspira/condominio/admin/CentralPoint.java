package org.inspira.condominio.admin;

import org.inspira.condominio.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.inspira.condominio.actividades.Lobby;
import org.inspira.condominio.actividades.Preparacion;
import org.inspira.condominio.datos.CondominioBD;
import org.inspira.condominio.dialogos.ProveedorSnackBar;

import java.net.URL;

public class CentralPoint extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SERVER_URL = "http://votacionesipn.com/condominios/";

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

        TextView helloMessage = (TextView) findViewById(R.id.hello_world);
        helloMessage
                .setTypeface(Typeface.createFromAsset(getAssets(),"Roboto-Black.ttf"));
        helloMessage.setText("Aqui aparecerán las notificaciones al admin.");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.central_point, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume(){
        super.onPostResume();
        revisarDatosDeUsuario();
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
        ProveedorSnackBar.muestraBarraDeBocados(findViewById(R.id.hello_world),
                getString(R.string.crear_convocatoria_sitio_en_construccion));
    }

    private void launchAccidentes(){
        ProveedorSnackBar.muestraBarraDeBocados(findViewById(R.id.hello_world),
                getString(R.string.crear_convocatoria_sitio_en_construccion));
    }

    private void launchConfiguracion(){
        ProveedorSnackBar.muestraBarraDeBocados(findViewById(R.id.hello_world),
                getString(R.string.crear_convocatoria_sitio_en_construccion));
    }
}
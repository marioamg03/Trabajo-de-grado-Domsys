package bitsandpixels.domsys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import bitsandpixels.domsys.auto_home.AutoHomeActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , Luces.OnFragmentInteractionListener, TempHumed.OnFragmentInteractionListener,Garaje.OnFragmentInteractionListener,
Gas.OnFragmentInteractionListener,Seguridad.OnFragmentInteractionListener,Aspersores.OnFragmentInteractionListener,Ventilacion.OnFragmentInteractionListener,
NivelTanque.OnFragmentInteractionListener{

    private FirebaseAuth pAutenticacion;
    private FirebaseAuth.AuthStateListener pAutenticacionOyente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // EJECUTO EL AUTENTICADOR DEL USUARIO
        pAutenticacion = FirebaseAuth.getInstance();
        pAutenticacionOyente = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };


        //Ejecuto la primera vista de la app
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Contenedor,new Luces()).commit();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.autohome);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainIntent = new Intent(MainActivity.this, AutoHomeActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // El elemento de la barra de acción de la manija hace clic aquí. La barra de acción
        // gestiona automáticamente los clics en el botón Inicio / Arriba, tanto tiempo
        // como se especifica una actividad principal en AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cerrar_sesión) {
            pAutenticacion.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Boolean fragmentoSeleccionado = false;

        if (id == R.id.nav_luces) {
            fragment = new Luces();
            fragmentoSeleccionado = true;
        } else if (id == R.id.nav_aspersores) {
            fragment = new Aspersores();
            fragmentoSeleccionado = true;
        } else if (id == R.id.nav_seguridad) {
            fragment = new Seguridad();
            fragmentoSeleccionado = true;
        } else if (id == R.id.nav_ventilacion) {
            fragment = new Ventilacion();
            fragmentoSeleccionado = true;
        } else if (id == R.id.nav_temphum) {
            fragment = new TempHumed();
            fragmentoSeleccionado = true;
        } else if (id == R.id.nav_nivelTanque) {
            fragment = new NivelTanque();
            fragmentoSeleccionado = true;
        } else if (id == R.id.nav_aperGaraje) {
            fragment = new Garaje();
            fragmentoSeleccionado = true;
        } else if (id == R.id.nav_gas) {
            fragment = new Gas();
            fragmentoSeleccionado = true;
    }

        if (fragmentoSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor,fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        pAutenticacion.addAuthStateListener(pAutenticacionOyente);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (pAutenticacionOyente != null) {
            pAutenticacion.removeAuthStateListener(pAutenticacionOyente);
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}

package org.inspira.condominio.actividades;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.inspira.condominio.R;
import org.inspira.condominio.fragmentos.Login;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //Perform...
                finish();
            }
        };
        // Tiempo que dura el splashscreen
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }

    @Override
    public void onBackPressed(){
    }

}

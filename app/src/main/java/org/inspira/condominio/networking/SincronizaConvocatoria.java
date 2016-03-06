package org.inspira.condominio.networking;

import android.app.Activity;
import android.content.Context;

import org.inspira.condominio.admin.CentralPoint;
import org.inspira.condominio.datos.Convocatoria;
import org.inspira.condominio.datos.PuntoOdD;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jcapiz on 5/03/16.
 */
public class SincronizaConvocatoria extends Thread {

    private Convocatoria convocatoria;
    private Context context;
    private AccionesSyncConvocatoria acciones;

    public void setAcciones(AccionesSyncConvocatoria acciones) {
        this.acciones = acciones;
    }

    public SincronizaConvocatoria(Context context, Convocatoria convocatoria){
        this.context = context;
        this.convocatoria = convocatoria;
    }

    public interface AccionesSyncConvocatoria{
        void validacionCorrecta(int idConvocatoria);
        void validacionIncorrecta();
    }

    @Override
    public void run(){
        try{
            JSONObject json = new JSONObject();
            json.put("action", 3);
            json.put("Asunto", convocatoria.getAsunto());
            json.put("Condominio", convocatoria.getCondominio());
            json.put("Ubicacion", convocatoria.getUbicacion());
            json.put("Ubicacion_Interna", convocatoria.getUbicacionInterna());
            json.put("Firma", convocatoria.getFirma());
            json.put("Fecha_de_Inicio", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(convocatoria.getFechaInicio()));
            json.put("email", context.getSharedPreferences(CentralPoint.class.getName(), Context.MODE_PRIVATE).getString("email", "NaN"));
            List<PuntoOdD> puntos = convocatoria.getPuntos();
            JSONArray puntosOdD = new JSONArray();
            for(PuntoOdD puntoActual : puntos)
                puntosOdD.put(puntoActual.getDescripcion());
            json.put("puntos", puntosOdD);
            HttpURLConnection con = (HttpURLConnection) new URL(CentralPoint.SERVER_URL).openConnection();
            con.setDoOutput(true);
            DataOutputStream salida = new DataOutputStream(con.getOutputStream());
            salida.write(json.toString().getBytes());
            salida.flush();
            int length;
            byte[] chunk = new byte[64];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataInputStream entrada = new DataInputStream(con.getInputStream());
            while((length = entrada.read(chunk)) != -1)
                baos.write(chunk, 0, length);
            json = new JSONObject(URLDecoder.decode(baos.toString(), "utf8"));
            baos.close();
            if(json.getBoolean("content")){
                acciones.validacionCorrecta(json.getInt("idConvocatoria"));
            }else{
                acciones.validacionIncorrecta();
            }
            con.disconnect();
            salida.close();
            entrada.close();
        }catch(IOException | JSONException e){
            e.printStackTrace();
            acciones.validacionIncorrecta();
        }
    }
}

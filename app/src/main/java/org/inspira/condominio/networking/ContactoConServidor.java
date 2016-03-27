package org.inspira.condominio.networking;

import android.util.Log;

import org.inspira.condominio.admin.CentralPoint;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by jcapiz on 16/03/16.
 */
public class ContactoConServidor extends Thread {

    private AccionesDeValidacionConServidor acciones;
    private String content;
    private String response;

    public ContactoConServidor(AccionesDeValidacionConServidor acciones, String content) {
        this.acciones = acciones;
        this.content = content;
    }

     public interface AccionesDeValidacionConServidor{
         void resultadoSatisfactorio(Thread t);
         void problemasDeConexion(Thread t);
     }

    @Override
    public void run(){
        try{
            Log.d("Server", "We are to send ("+content.getBytes().length+" bytes): " + content);
            HttpURLConnection con = (HttpURLConnection) new URL(CentralPoint.SERVER_URL).openConnection();
            con.setDoOutput(true);
            DataOutputStream salida = new DataOutputStream(con.getOutputStream());
            salida.write(content.getBytes());
            salida.flush();
            int length;
            byte[] chunk = new byte[64];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataInputStream entrada = new DataInputStream(con.getInputStream());
            while((length = entrada.read(chunk)) != -1)
                baos.write(chunk,0,length);
            response = URLDecoder.decode(baos.toString(), "utf8").trim();
            Log.d("ContactoServer", "Server dijo (" + baos.size() + " bytes): " + response);
            baos.close();
            con.disconnect();
            entrada.close();
            salida.close();
            acciones.resultadoSatisfactorio(this);
        }catch(IOException e){
            e.printStackTrace();
            acciones.problemasDeConexion(this);
        }
    }

    public String getResponse(){
        return response;
    }
}

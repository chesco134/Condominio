package org.inspira.condominio.networking;

import android.os.AsyncTask;
import android.util.Log;

import org.inspira.condominio.admin.CentralPoint;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by Viridiana R on 17/02/2016.
 */
public class LoginConnection extends AsyncTask<String,String,JSONObject> {

    OnConnectionAction actions;

    public LoginConnection(OnConnectionAction actions){
        this.actions = actions;
    }

    public interface OnConnectionAction{
        void validationSucceded(JSONObject json);
        void validationError();
    }

    @Override
    protected JSONObject doInBackground(String... params){
        JSONObject json = null;
        String user = params[0];
        String password = params[1];
        try {
            URL url = new URL(CentralPoint.SERVER_URL); // la url del ws
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setDoOutput(true);//to upload data
            //urlCon.setRequestMethod("POST");
            //to send
            json = new JSONObject();
            json.put("action", 1);
            json.put("user", user);
            json.put("password", password);
            DataOutputStream salida = new DataOutputStream(urlCon.getOutputStream());
            salida.write(json.toString().getBytes());
            salida.flush();

            //to receive
            DataInputStream entrada = new DataInputStream(urlCon.getInputStream());
            int length;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] chunk = new byte[64];
            while( (length = entrada.read(chunk)) != -1)
                baos.write(chunk,0,length);
            Log.d("Logger", URLDecoder.decode(baos.toString(), "utf8"));
            json = new JSONObject(baos.toString());
            baos.close();
        }catch(JSONException | IOException e){
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        try {
            if (result.getBoolean("content")) {
                actions.validationSucceded(result);
            } else {
                actions.validationError();
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}

package org.inspira.condominio.admon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jcapiz on 16/03/16.
 */
public class CompruebaCamposJSON {

    public static boolean validaContenido(String jsonstr){
        boolean esCorrecto = false;
        try{
            JSONObject json = new JSONObject(jsonstr);
            esCorrecto = json.getBoolean("content");
        }catch(JSONException e){
            e.printStackTrace();
        }
        return esCorrecto;
    }

    public static Map<String, Object> obtenerCampos(String jsonstr){
        Map<String, Object> elementos = null;
        try{
            JSONObject json = new JSONObject(jsonstr);
            elementos = new TreeMap<>();
            Iterator<String> iter = json.keys();
            String key;
            while(iter.hasNext()) {
                key = iter.next();
                elementos.put(key, json.get(key));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return elementos;
    }
}

package unicen.inmobiliaria;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


public class ConsultaServidor extends AsyncTask<ContentValues, Void, StringBuffer> {


    private static final String URL = "http://inmo360.esy.es/php/";
    private static final String MYADMIN = "mysql.hostinger.com.ar";
    private static final String BD = "u330485941_inmo";
    private static final String USER = "u330485941_jose";
    private static final String PASS = "kVTA!!GEAV3v";
    private static final long HORAS = 18000000; //5hr = 18000000miliseg
    private final String nombreCache;

    private AsyncRespuesta padre;
    private ProgressDialog ruedita;
    private Context context;
    private StringBuffer cache;

    public ConsultaServidor(AsyncRespuesta padre, Context context, String nombreCache) {
        this.context = context;
        this.padre = padre;
        this.nombreCache = nombreCache.replaceAll(" ", "").toLowerCase();
        ruedita = new ProgressDialog(context);
    }

    @Override
    protected StringBuffer doInBackground(ContentValues... params) {
        ContentValues lista = params[0];
        if (cache.length() == 0)
            try {
                URL url = new URL(URL + lista.getAsString("url"));
                lista.remove("url");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                lista.put("MYADMIN", MYADMIN);
                lista.put("BD", BD);
                lista.put("USER", USER);
                lista.put("PASS", PASS);
                writer.write(CodificarPost(lista));//PRoBANDO
                writer.flush();
                writer.close();
                os.close();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    while ((line = br.readLine()) != null)
                        cache.append(line);
                } else {
                    cache = new StringBuffer("error");
                }
                connection.disconnect();
                if (cache.toString().contains("Warning") || cache.toString().contains("error")) {
                    cache.delete(0, cache.length());
                }
                cachear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return (cache);
    }


    private String CodificarPost(ContentValues params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.valueSet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ruedita.setMessage("Consultando Servidor");
        ruedita.show();
        this.testCacheado();
    }

    protected void onPostExecute(StringBuffer response) {
        super.onPostExecute(response);
        padre.respuestaServer(response);
        ruedita.dismiss();
    }

    private void cachear() {
        if (this.cache.length() != 0) {
            File cacheFile = new File(this.context.getCacheDir(), nombreCache + ".tmp");
            try {
                FileOutputStream outputStream = new FileOutputStream(cacheFile);
                outputStream.write(cache.toString().getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void testCacheado() {
        cache = new StringBuffer();
        try {
            File cacheFile = new File(this.context.getCacheDir(), nombreCache + ".tmp");
            if ((System.currentTimeMillis() - cacheFile.lastModified()) < HORAS) { //5hr = 18000000miliseg
                BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(cacheFile)));
                String line;
                while ((line = input.readLine()) != null) {
                    cache.append(line);
                }
                input.close();
            } else
                cacheFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

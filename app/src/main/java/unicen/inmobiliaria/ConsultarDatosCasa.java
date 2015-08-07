package unicen.inmobiliaria;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ConsultarDatosCasa extends AsyncRespuesta {

    private final static String PHPCASA = "ListarMarker.php?";
    private static String DIRECCION;
    private Context context;

    public ConsultarDatosCasa(String ciudad, String direccion, Context context) {
        this.context = context;
        ContentValues request = new ContentValues();
        request.put("url", PHPCASA);
        request.put("Ciudad", ciudad);
        ConsultaServidor consulta = new ConsultaServidor(this, context, ciudad);
        consulta.execute(request, null, null);
        DIRECCION = direccion;
    }

    @Override
    protected void respuestaServer(StringBuffer s) {
        if (s.length() == 0)
            Toast.makeText(context, "Ocurrio un problema", Toast.LENGTH_LONG).show();
        else {
            JSONObject json;
            String tempJson = "";
            while ((s.length() != 0) && (tempJson.isEmpty()))
                try {
                    json = toJson(s);
                    if (json.getString("Dirección").equals(DIRECCION))
                        tempJson = json.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            Intent broadcastIntent = new Intent("Cargar Datos de Casa");
            broadcastIntent.setAction("Cargar Datos de Casa");
            broadcastIntent.putExtra("jsonCasa", tempJson);
            context.sendBroadcast(broadcastIntent);
        }
    }
}

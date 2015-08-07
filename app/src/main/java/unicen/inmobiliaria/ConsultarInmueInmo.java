package unicen.inmobiliaria;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

public class ConsultarInmueInmo extends AsyncRespuesta {

    private final static String PHPINMO = "ListarInmoInmue.php?";
    private Context context;

    public ConsultarInmueInmo(String id_inmo, String direccion, Context context) {
        ContentValues request = new ContentValues();
        request.put("url", PHPINMO);
        request.put("ID_inmueble", id_inmo);
        this.context = context;
        ConsultaServidor consulta = new ConsultaServidor(this, context, "inmobs_" + direccion);
        consulta.execute(request, null, null);
    }

    @Override
    protected void respuestaServer(StringBuffer s) {
        if (s.length() == 0)
            Toast.makeText(context, "Ocurrio un problema", Toast.LENGTH_LONG).show();
        else {
            ArrayList<String> jsonInmo = new ArrayList<>();
            while (s.length() != 0)
                try {
                    jsonInmo.add(toJson(s).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            Intent broadcastIntent = new Intent("Cargar Datos de Inmobiliaria");
            broadcastIntent.setAction("Cargar Datos de Inmobiliaria");
            broadcastIntent.putExtra("jsonInmo", jsonInmo);
            context.sendBroadcast(broadcastIntent);
        }
    }
}

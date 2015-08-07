package unicen.inmobiliaria;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

public class listarCiudades extends AsyncRespuesta {

    private final static String PHP = "ListarTodasCiudad.php";
    private Context context;

    public listarCiudades(Context context) {
        this.context = context;
        ContentValues request = new ContentValues();
        request.put("url", PHP);
        ConsultaServidor consulta = new ConsultaServidor(this, context, "ListarTodasCiudad");
        consulta.execute(request, null, null);
    }

    @Override

    public void respuestaServer(StringBuffer s) {
        if (s.length() == 0)
            Toast.makeText(this.context, "Problemas con el servidor.\n¿Está conectado a internet?", Toast.LENGTH_LONG).show();
        else {
            ArrayList<String> aux = new ArrayList<>();
            while (s.length() != 0)
                try {
                    aux.add(toJson(s).getString("Nombre"));
                } catch (JSONException e) {
                    Toast.makeText(this.context, "Problemas con el servidor.\nPuede haber datos erroneos", Toast.LENGTH_SHORT).show();
                }
            Intent broadcastIntent = new Intent("Cargar SpinnerCiudad");
            broadcastIntent.setAction("Cargar SpinnerCiudad");
            broadcastIntent.putExtra("Lista", aux);
            context.sendBroadcast(broadcastIntent);
        }
    }

}

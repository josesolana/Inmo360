package unicen.inmobiliaria;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AsyncRespuesta implements GoogleMap.OnInfoWindowClickListener {

    private final static String PHP = "ListarMarker.php?";
    private final static String ERROR = "ERROR";
    private static String CIUDAD;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        CIUDAD = getIntent().getStringExtra("Ciudad");
        LatLng posCity = new AdressesLatLng().get(CIUDAD, this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posCity, 15));
        //--------------------------Mapa Inicializado---------------------------//
        ContentValues request = new ContentValues();//ACA cargo los parametros
        request.put("url", PHP);
        request.put("Ciudad", CIUDAD);
        ConsultaServidor consulta = new ConsultaServidor(this, this, CIUDAD);
        consulta.execute(request, null, null);
        mMap.setOnInfoWindowClickListener(MapsActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(ERROR));
    }

    @Override
    public void respuestaServer(StringBuffer s) {
        if (s.length() == 0)
            Toast.makeText(this, "No existen propiedades.\nBusque en otra localidad", Toast.LENGTH_LONG).show();
        else {
            mMap.clear();
            while (s.length() != 0)
                try {
                    crearMarker(toJson(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            Toast.makeText(this, "Toque las Burbujas\npara una\nVISTA 360°", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (!marker.getTitle().equals(ERROR)) {
            Intent i = new Intent(MapsActivity.this, DescripcionVivienda.class);
            i.putExtra("Título", marker.getTitle());
            i.putExtra("Ciudad", CIUDAD);
            startActivity(i);
        }
    }

    private void crearMarker(JSONObject json_obj) throws JSONException {
        mMap.addMarker(new MarkerOptions()
                .position(new AdressesLatLng().get((json_obj.getString("Dirección") + ", " + CIUDAD), this))
                .title(json_obj.getString("Dirección"))
                .draggable(false)
                .alpha(0.7f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.homemarker)))
                .setSnippet("Precio: $" + json_obj.get("Precio"));
    }
}

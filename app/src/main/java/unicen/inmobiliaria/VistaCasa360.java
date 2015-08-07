package unicen.inmobiliaria;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class VistaCasa360 extends AsyncRespuesta implements OnStreetViewPanoramaReadyCallback {

    //  private final static String PHP = "ConsultarPanoId.php";
    private final static String PHP = "ListarMarker.php?";

    private String panoID;
    private ImageView star;
    private boolean isStarry;
    private String DIRECCION;
    private String CIUDAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_casa360);
        DIRECCION = getIntent().getStringExtra("Título");
        CIUDAD = getIntent().getStringExtra("Ciudad");
        ContentValues request = new ContentValues();
        request.put("url", PHP);
        request.put("Ciudad", CIUDAD);
        ConsultaServidor consulta = new ConsultaServidor(this, this, CIUDAD);
        consulta.execute(request, null, null);
        star = (ImageView) findViewById(R.id.img_star);
        star.setVisibility(View.INVISIBLE);
    }

    @Override
    public void respuestaServer(StringBuffer s) {
        if (s.length() == 0) {
            panoID = "9s5GqIXp7J0AAAQZWNfvtA"; //Iguazu
            Toast.makeText(this, "Ocurrio un problema\nLe mostramos las Cataratas de Iguazú", Toast.LENGTH_LONG).show();
        } else {
            JSONObject json;
            boolean encontrado = false;
            while ((s.length() != 0) && (!encontrado)) {
                try {
                    json = toJson(s);
                    if (json.getString("Dirección").equals(DIRECCION)) {
                        panoID = json.getString("PanoId");
                        encontrado = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            star.setVisibility(View.VISIBLE);
            if ((Inmo360.BD.getDireccion(DIRECCION)).isEmpty()) {
                star.setImageResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                isStarry = false;
            } else {
                star.setImageResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
                isStarry = true;
            }
        }
        StreetViewPanoramaFragment SVPF = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.vistacasa360);
        SVPF.getStreetViewPanoramaAsync(this);
    }

    public void setFavorito(View v) {
        if (!isStarry) {
            Inmo360.BD.saveQuoteRow(DIRECCION, CIUDAD);
            star.setImageResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
        } else {
            Inmo360.BD.deleteQuoteRow(DIRECCION);
            star.setImageResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
        }
        isStarry = !isStarry;
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(this.panoID);
    }
}
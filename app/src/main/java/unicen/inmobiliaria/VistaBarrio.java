package unicen.inmobiliaria;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class VistaBarrio extends FragmentActivity implements OnStreetViewPanoramaReadyCallback {

    private LatLng panoId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String DIRECCION = getIntent().getStringExtra("Título");
        String CIUDAD = getIntent().getStringExtra("Ciudad");
        setContentView(R.layout.activity_vista_barrio);
        StreetViewPanoramaFragment SVPF = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.vistabarrio);
        panoId = new AdressesLatLng().get(DIRECCION + ", " + CIUDAD, this);
        SVPF.getStreetViewPanoramaAsync(VistaBarrio.this);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(this.panoId);
    }
}
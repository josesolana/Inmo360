package unicen.inmobiliaria;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class AdressesLatLng {


    public LatLng get(String address, Context padre) {
        double lat = 0.0, lng = 0.0;
        Geocoder geoCoder = new Geocoder(padre, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                lat = addresses.get(0).getLatitude();
                lng = addresses.get(0).getLongitude();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LatLng(lat, lng);
    }


}

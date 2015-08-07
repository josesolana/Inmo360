package unicen.inmobiliaria;

import android.support.v4.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AsyncRespuesta extends FragmentActivity {
    protected abstract void respuestaServer(StringBuffer s);

    protected JSONObject toJson(StringBuffer s) throws JSONException {
        JSONObject json_obj = new JSONObject(s.toString());
        s.replace(0, s.length(), s.substring(s.indexOf("}") + 1));
        return json_obj;
    }
}
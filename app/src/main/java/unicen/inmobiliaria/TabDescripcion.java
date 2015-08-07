package unicen.inmobiliaria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TabDescripcion extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static String DIRECCION;
    private ArrayList<JSONObject> jsonInmo;
    private BroadcastReceiver DatosCasa = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ListView listview = (ListView) findViewById(R.id.lv_Descripcion);
            ArrayList<StringBuffer> list = new ArrayList<>();
            try {
                JSONObject jsonCasa = new JSONObject(intent.getStringExtra("jsonCasa"));
                list.add(new StringBuffer(jsonCasa.getString("Dirección") + ", " + jsonCasa.getString("Nombre")));

                for (int i = 0; i < jsonCasa.names().length(); i++)
                    if ((!jsonCasa.getString(jsonCasa.names().getString(i)).equals("null")) && (DATOS.Datos_Inmueble.contains(jsonCasa.names().getString(i))))
                        list.add(new StringBuffer(jsonCasa.names().getString(i) + ": " + jsonCasa.getString(jsonCasa.names().getString(i))));

                new ConsultarInmueInmo(jsonCasa.getString("ID_inmueble"), DIRECCION, TabDescripcion.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StableArrayAdapter adapter = new StableArrayAdapter(TabDescripcion.this, android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);
        }
    };
    private BroadcastReceiver DatosInmo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> listInmo = new ArrayList<>();
            JSONObject jsontemp;
            ArrayList<String> arrayJsonString = intent.getStringArrayListExtra("jsonInmo");
            jsonInmo.clear();
            for (String jsonString : arrayJsonString)
                try {
                    jsontemp = new JSONObject(jsonString);
                    jsonInmo.add(jsontemp);
                    listInmo.add(jsontemp.getString("Nombre"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            if (!listInmo.isEmpty()) {
                Spinner spinner = (Spinner) findViewById(R.id.spin_inmo);
                spinner.setPromptId(R.string.txt_prompt_inmo);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(TabDescripcion.this, android.R.layout.simple_spinner_dropdown_item, listInmo);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(TabDescripcion.this);
                spinner.setSelection(0, true);
            } else
                Toast.makeText(TabDescripcion.this, "Ocurrio un problema ", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_descripcion);
        DIRECCION = getIntent().getStringExtra("Título");
        String CIUDAD = getIntent().getStringExtra("Ciudad");
        this.registerReceiver(DatosCasa, new IntentFilter("Cargar Datos de Casa"));
        new ConsultarDatosCasa(CIUDAD, DIRECCION, TabDescripcion.this);
        this.registerReceiver(DatosInmo, new IntentFilter("Cargar Datos de Inmobiliaria"));
        jsonInmo = new ArrayList<>();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String inmobiliaria = parent.getItemAtPosition(position).toString();
        String titulo;
        String valor;
        new ConsultarLogoInmo((ImageView) findViewById(R.id.img_logo_inmo), TabDescripcion.this, inmobiliaria).execute();
        ArrayList<StringBuffer> list = new ArrayList<>();
        ListView listview = (ListView) findViewById(R.id.lv_inmo);
        for (JSONObject json : jsonInmo)
            try {
                if (json.getString("Nombre").equals(inmobiliaria)) {
                    for (int i = 0; i < json.names().length(); i++) {
                        titulo = json.names().getString(i);
                        valor = json.getString(titulo);
                        if ((!valor.equals("null")) && (DATOS.Datos_Inmobiliaria.contains(titulo)))
                            list.add(new StringBuffer(titulo + ": " + valor));
                    }
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void onStop() {
        super.onStop();
        if (DatosCasa != null) {
            unregisterReceiver(DatosCasa);
            DatosCasa = null;
        }
        if (DatosInmo != null) {
            unregisterReceiver(DatosInmo);
            DatosInmo = null;
        }
    }

    //-------------------------------------------------------------------------//

    @Override
    protected void onPause() {
        super.onPause();
        if (DatosCasa != null) {
            unregisterReceiver(DatosCasa);
            DatosCasa = null;
        }
        if (DatosInmo != null) {
            unregisterReceiver(DatosInmo);
            DatosInmo = null;
        }
    }

    public static final class DATOS {
        public static ArrayList<String> Datos_Inmueble;
        public static ArrayList<String> Datos_Inmobiliaria;

        static {
            ArrayList<String> tmp_Inmueble = new ArrayList<>();
            ArrayList<String> tmp_Inmobiliaria = new ArrayList<>();

            tmp_Inmueble.add("Precio");
            tmp_Inmueble.add("Ambientes");
            tmp_Inmueble.add("Cocina");
            tmp_Inmueble.add("Baños");
            tmp_Inmueble.add("Habitaciones");

            tmp_Inmobiliaria.add("Nombre");
            tmp_Inmobiliaria.add("Telefono");
            tmp_Inmobiliaria.add("PaginaWeb");
            tmp_Inmobiliaria.add("InfoExtra");

            Datos_Inmueble = tmp_Inmueble;
            Datos_Inmobiliaria = tmp_Inmobiliaria;
        }

    }

    private class StableArrayAdapter extends ArrayAdapter<StringBuffer> {

        HashMap<StringBuffer, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<StringBuffer> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            StringBuffer item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
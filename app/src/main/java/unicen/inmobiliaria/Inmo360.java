/**
 * hostinger.com.ar *******   inmo360.esy.es + BDPass: kVTA!!GEAV3v
 */

//C:\Users\José\AppData\Local\Android\Sdk\platform-tools\adb.exe connect 192.168.1.64:5555


package unicen.inmobiliaria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class Inmo360 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static FormatoBD BD;
    private String stCity;
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> myParam = intent.getExtras().getStringArrayList("Lista");
            if (myParam != null) {
                Spinner spinner = (Spinner) findViewById(R.id.spin_ciudad);
                spinner.setPromptId(R.string.txt_prompt_ciudad);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Inmo360.this, android.R.layout.simple_spinner_dropdown_item, myParam);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(
                        new NothingSelectedSpinnerAdapter(
                                adapter,
                                R.layout.activity_nothing_selected_spinner_adapter,
                                Inmo360.this));
                spinner.setOnItemSelectedListener(Inmo360.this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(myReceiver, new IntentFilter("Cargar SpinnerCiudad"));
        new listarCiudades(this);
        setContentView(R.layout.activity_inmo360);
        BD = new FormatoBD(this);
        this.stCity = "";
    }


    protected void onStop() {
        super.onStop();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
    }


    public void escanearCodigo(View v) {
        Intent i = new Intent(Inmo360.this, QR.class);
        startActivity(i);
    }

    public void entrarFavorito(View v) {
        Intent i = new Intent(Inmo360.this, Favoritos.class);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            this.stCity = parent.getItemAtPosition(position).toString();
        }
    }

    public void mapasAct(View v) {
        if (!this.stCity.isEmpty()) {
            Intent i = new Intent(Inmo360.this, MapsActivity.class);
            i.putExtra("Ciudad", this.stCity);
            startActivity(i);
        } else
            Toast.makeText(this, R.string.txt_dialog_ciudad, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

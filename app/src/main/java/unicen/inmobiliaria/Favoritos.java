package unicen.inmobiliaria;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Favoritos extends AppCompatActivity {

    private ListView listview;

    private void actualizarLista() {
        ArrayList<String> listado = Inmo360.BD.getAll();
        if (listado.isEmpty()) {
            Toast.makeText(this, "No hay Favoritos", Toast.LENGTH_SHORT).show();
            finish();
        }
        StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, listado);
        listview.setAdapter(adapter);
    }

    protected void onResume() {
        this.actualizarLista();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        listview = (ListView) findViewById(R.id.lv_fav);
        this.actualizarLista();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(750).alpha(0.25f)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(Favoritos.this, DescripcionVivienda.class);
                                i.putExtra("Título", item.substring(0, item.indexOf(",")).trim());
                                i.putExtra("Ciudad", item.substring(item.indexOf(",") + 1).trim());
                                startActivity(i);
                                view.animate().setDuration(250).alpha(0);
                                view.animate().alpha(1);

                            }
                        });
            }

        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }//todo notificationbar con subscripcion a modificaciones de los fav
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
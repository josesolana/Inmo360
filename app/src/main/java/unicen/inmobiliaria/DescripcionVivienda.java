package unicen.inmobiliaria;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.widget.TabHost;


public class DescripcionVivienda extends TabActivity {

    private TabActivity tabactivity;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String DIRECCION = getIntent().getStringExtra("Título");
        final String CIUDAD = getIntent().getStringExtra("Ciudad");
        String pestaña = getIntent().getAction();
        tabactivity = this;

        setContentView(R.layout.activity_descripcion_vivienda);
        Resources res = getResources();

        final TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);

        /*  Vista Casa 360  */
        Intent i = new Intent(this, VistaCasa360.class);
        i.putExtra("Título", DIRECCION);
        i.putExtra("Ciudad", CIUDAD);
        i.setAction("NO");
        TabHost.TabSpec spec = tabs.newTabSpec("Vista360");
        spec.setIndicator("", res.getDrawable(android.R.drawable.ic_dialog_map, getTheme()));
        spec.setContent(i);
        tabs.addTab(spec);

        /*  Tabdescripción  */
        i = new Intent(this, TabDescripcion.class);
        i.putExtra("Título", DIRECCION);
        i.putExtra("Ciudad", CIUDAD);
        spec = tabs.newTabSpec("Descripcion");
        spec.setContent(i);
        spec.setIndicator("", res.getDrawable(android.R.drawable.ic_dialog_info, getTheme()));
        tabs.addTab(spec);

      /*  VistaBarrio  */
        i = new Intent(this, VistaBarrio.class);
        i.putExtra("Título", DIRECCION);
        i.putExtra("Ciudad", CIUDAD);
        spec = tabs.newTabSpec("Barrio");
        spec.setContent(i);
        spec.setIndicator("", res.getDrawable(android.R.drawable.ic_dialog_alert, getTheme()));
        tabs.addTab(spec);

        if (pestaña == null) {
            tabs.setCurrentTabByTag("Vista360");
            getIntent().putExtra("ultimo", "Vista360");
        } else
            tabs.setCurrentTabByTag(pestaña);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                if ((!(getIntent().getStringExtra("ultimo").equals(tabId))))
                    if (tabId.equals("Vista360") || tabId.equals("Barrio")) {
                        getIntent().setAction(tabId);
                        getIntent().putExtra("ultimo", tabId);
                        tabactivity.recreate();
                    }
            }
        });
    }

}

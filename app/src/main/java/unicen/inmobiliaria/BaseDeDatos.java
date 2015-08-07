package unicen.inmobiliaria;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BaseDeDatos extends SQLiteOpenHelper {

    private final static String DBNOMBRE = "Inmo360.db";
    private final static int VER = 1;

    public BaseDeDatos(Context context) {
        super(context, DBNOMBRE, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FormatoBD.CREAR_TABLAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

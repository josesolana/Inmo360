package unicen.inmobiliaria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class FormatoBD {

    //Metainformación de la base de datos
    public static final String TABLAFAVORITOS = "Favoritos";
    public static final String STRING = "text";
    public static final String INT = "integer";
    public static final String IMG = "blob";
    //Script de Creación de la tabla Quotes
    public static final String CREAR_TABLAS = "create table " + TABLAFAVORITOS + "(" +
            Columnas.ID_FAVORITOS + " " + INT + " primary key autoincrement, " +
            Columnas.DIRECCION + " " + STRING + " not null unique, " +
            Columnas.CIUDAD + " " + STRING + " not null, " +
            Columnas.FOTO + " " + IMG + ")";
    private SQLiteDatabase database;

    public FormatoBD(Context context) {
        BaseDeDatos openHelper = new BaseDeDatos(context);
        database = openHelper.getWritableDatabase();
    }

    public void saveQuoteRow(String direccion, String ciudad) {
        ContentValues values = new ContentValues();
        values.put(FormatoBD.Columnas.DIRECCION, direccion);
        values.put(Columnas.CIUDAD, ciudad);
        database.insert(TABLAFAVORITOS, null, values);
    }

    public void deleteQuoteRow(String dir/*, String ciudad*/) {
        String selection = Columnas.DIRECCION + " = ?";
        String[] selectionArgs = {dir/*, ciudad */};
        database.delete(TABLAFAVORITOS, selection, selectionArgs);
    }

    public ArrayList<String> getAll() {
        Cursor cursor = database.rawQuery("select * from " + TABLAFAVORITOS, null);
        ArrayList<String> mArrayList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(cursor.getString(cursor.getColumnIndex(Columnas.DIRECCION)) + ", " + cursor.getString(cursor.getColumnIndex(Columnas.CIUDAD)));
        }
        cursor.close();
        return mArrayList;
    }

    public ArrayList<String> getDireccion(String dir) {
        Cursor cursor = database.rawQuery("select * from " + TABLAFAVORITOS + " where " + Columnas.DIRECCION + " like \'" + dir + "\'", null);
        ArrayList<String> mArrayList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(cursor.getString(cursor.getColumnIndex(Columnas.DIRECCION)));
        }
        cursor.close();
        return mArrayList;
    }

    public static class Columnas {
        public static final String ID_FAVORITOS = BaseColumns._ID;
        public static final String DIRECCION = "direccion";
        public static final String CIUDAD = "ciudad";
        public static final String FOTO = "foto";
    }
}
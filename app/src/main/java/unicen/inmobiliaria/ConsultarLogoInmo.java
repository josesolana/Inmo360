package unicen.inmobiliaria;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConsultarLogoInmo extends AsyncTask<Void, Void, Bitmap> {

    private final static String URL = "http://inmo360.esy.es/php/";
    private ImageView bmImagen;
    private String path;
    private String nombreFoto;
    private Bitmap logo;

    public ConsultarLogoInmo(ImageView bmImagen, Context context, String nombre) {
        this.bmImagen = bmImagen;
        nombreFoto = nombre + ".png";
        path = Environment.getExternalStorageDirectory().toString() + "/Inmo360/";
        logo = null;
        final File dir = new File(path);
        if (!dir.isDirectory())
            if (!dir.mkdirs())
                path = context.getCacheDir().getAbsolutePath() + "/Inmo360/";

    }

    protected Bitmap doInBackground(Void... params) {
        if (logo == null)
            try {
                InputStream in = new java.net.URL(URL + nombreFoto).openStream();
                logo = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return logo;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.testCacheado();
    }

    @Override
    protected void onPostExecute(Bitmap resultado) {
        super.onPostExecute(resultado);
        cachear();
        bmImagen.setImageBitmap(resultado);
    }

    private void cachear() {
        if (logo != null) {
            File cacheFile = new File(path, nombreFoto);
            try {
                FileOutputStream outputStream = new FileOutputStream(cacheFile);
                logo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void testCacheado() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        logo = BitmapFactory.decodeFile(path + nombreFoto, options);
    }

}

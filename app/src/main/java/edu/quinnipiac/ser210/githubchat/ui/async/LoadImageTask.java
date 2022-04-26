package edu.quinnipiac.ser210.githubchat.ui.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Thomas Kwashnak
 */
public class LoadImageTask extends AsyncTask<String,Void,Bitmap> {

    private Listener listener;

    public LoadImageTask(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = null;
       try {
           InputStream in = new URL(strings[0]).openStream();
           bitmap = BitmapFactory.decodeStream(in);
           in.close();

       } catch (IOException e) {
           e.printStackTrace();
       }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(listener != null && bitmap != null) {
            listener.onLoadImage(bitmap);
        }
    }

    interface Listener {
        void onLoadImage(Bitmap bitmap);
    }
}

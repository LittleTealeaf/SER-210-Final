package edu.quinnipiac.ser210.githubchat.ui.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

import edu.quinnipiac.ser210.githubchat.threads.ThreadWrapper;

public class ImageLoader {

    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;

        try {
            InputStream in = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static int loadImage(String url, OnImageLoaded listener) {
        return ThreadWrapper.startThread(() -> loadBitmap(url),listener::onImageLoaded);
    }

    public static int loadImage(String url, OnImageLoaded listener, int channel) {
        return ThreadWrapper.startThread(() -> loadBitmap(url),listener::onImageLoaded,channel);
    }

}

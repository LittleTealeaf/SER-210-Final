package edu.quinnipiac.ser210.githubchat.ui.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.InputStream;
import java.net.URL;

import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;

/**
 * @author Thomas Kwashnak
 * Handles image loading, including caching images by url for future use
 */
public class ImageLoader {

    private static final LruCache<String, Bitmap> cache;

    static {
        cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024) / 8) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public static int loadImage(String url, OnLoadBitmap listener) {
        return ThreadManager.startThread(() -> loadBitmap(url), listener::onLoadBitmap);
    }

    public static Bitmap loadBitmap(String url) {

        Bitmap bitmap = cache.get(url);
        if (bitmap != null) {
            return bitmap;
        }

        try {
            InputStream in = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        cache.put(url, bitmap);

        return bitmap;
    }

    public static int loadImage(String url, OnLoadBitmap listener, int channel) {
        return ThreadManager.startThread(() -> loadBitmap(url), listener::onLoadBitmap, channel);
    }

    public interface OnLoadBitmap {

        void onLoadBitmap(Bitmap bitmap, int channel);
    }
}

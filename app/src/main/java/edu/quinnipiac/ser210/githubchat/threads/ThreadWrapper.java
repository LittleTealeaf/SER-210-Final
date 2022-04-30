package edu.quinnipiac.ser210.githubchat.threads;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadWrapper {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static int lastRegisteredChannel;

    static {
        lastRegisteredChannel = Integer.MIN_VALUE;
    }

    public static <T> int startThread(Task<T> function, Callback<T> notifier) {
        return startThread(function, notifier, registerChannel());
    }

    public static <T> int startThread(Task<T> function, Callback<T> notifier, int channel) {
        executorService.execute(() -> {
            T item = function.execute();
            handler.post(() -> notifier.notify(item, channel));
        });
        return channel;
    }

    public synchronized static int registerChannel() {
        return lastRegisteredChannel++;
    }
}
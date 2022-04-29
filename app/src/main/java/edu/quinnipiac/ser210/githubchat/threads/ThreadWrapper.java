package edu.quinnipiac.ser210.githubchat.threads;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadWrapper {

    private static int lastRegisteredChannel;

    static {
        lastRegisteredChannel = 1;
    }

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public synchronized static int registerChannel() {
        return lastRegisteredChannel++;
    }

    public static <T> int startThread(Task<T> function, Callback<T> notifier) {
        return startThread(function,notifier,registerChannel());
    }

    public static <T> int startThread(Task<T> function, Callback<T> notifier, int channel) {
        executorService.execute(() -> {
            T item = function.execute();
            handler.post(() -> notifier.notify(item,channel));
        });
        return channel;
    }
}

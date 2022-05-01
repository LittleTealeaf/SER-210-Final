package edu.quinnipiac.ser210.githubchat.threads;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {

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
        run(() -> {
            T item = function.execute();
            schedule(() -> notifier.notify(item, channel));
        });
        return channel;
    }

    /**
     * Starts a runnable thread
     * @param runnable Script to run
     */
    public static void run(Runnable runnable) {
        executorService.execute(runnable);
    }

    /**
     * Schedules a runnable to execute on the main thread
     * @param runnable Script to run
     */
    public static void schedule(Runnable runnable) {
        handler.post(runnable);
    }

    public synchronized static int registerChannel() {
        return lastRegisteredChannel++;
    }
}

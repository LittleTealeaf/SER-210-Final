package edu.quinnipiac.ser210.githubchat.threads;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {

    /**
     * The null channel, or the channel id that will never be registered by {@link #registerChannel()}
     */
    public static final int NULL_CHANNEL = 0;
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static int lastRegisteredChannel;

    static {
        lastRegisteredChannel = Integer.MIN_VALUE;
    }

    /**
     * Begins a new thread with a given task function and a callback receiver. The receiver will be notified on a unique registered channel while on the main thread.
     * @param function The function to execute asynchronously
     * @param notifier The object to notify when the task is completed
     * @param <T> The type of object that is being returned from the task
     * @return The channel that the notifier will be registered on. The channel will be automatically generated
     */
    public static <T> int startThread(Task<T> function, Callback<T> notifier) {
        return startThread(function, notifier, registerChannel());
    }

    /**
     * Begins a new thread with a given task function, a callback receiver, and a predetermined channel. The receiver will be notified on the provided channel on the main
     * thread.
     * @param function The function to execute asynchronously
     * @param notifier The object to notify on the main thread when the task is completed
     * @param channel The channel that the notifier should be notified on
     * @param <T> The type of object that is being returned from the task
     * @return THe channel that the notifier will be registered on. This is the same as the channel passed into the thread
     */
    public static <T> int startThread(Task<T> function, Callback<T> notifier, int channel) {
        run(() -> {
            //This first runs the function, which is called on its own thread
            T item = function.execute();

            //This will schedule notifying the notifier on the main thread
            schedule(() -> notifier.notify(item, channel));
        });
        return channel;
    }

    public static int startThread(SimpleTask task, SimpleCallback notifier) {
        return startThread(task,notifier,registerChannel());
    }

    public static int startThread(SimpleTask task, SimpleCallback notifier, int channel) {
        run(() -> {
            task.execute();
            schedule(() -> notifier.notify(channel));
        });
        return channel;
    }

    /**
     * Increments and returns a unique channel id.
     * @return A unique channel id
     */
    public synchronized static int registerChannel() {
        //Increment channel registry
        lastRegisteredChannel = lastRegisteredChannel + 1;

        //Skip channel if it's the null channel
        if(lastRegisteredChannel == NULL_CHANNEL) {
            lastRegisteredChannel = lastRegisteredChannel + 1;
        }

        return lastRegisteredChannel;
    }

    /**
     * Starts a runnable thread
     *
     * @param runnable Script to run
     */
    public static void run(Runnable runnable) {
        //Executes the runnable on a new thread
        executorService.execute(runnable);
    }

    /**
     * Schedules a runnable to execute on the main thread
     *
     * @param runnable Script to run
     */
    public static void schedule(Runnable runnable) {
        //Schedules runnable on the handler
        handler.post(runnable);
    }

    /**
     * Schedules a runnable to execute on the main thread after a given time
     * @param runnable runnable script to run
     * @param time milliseconds delay before the runnable should be run
     */
    public static void scheduleDelayed(Runnable runnable, long time) {
        handler.postDelayed(runnable, time);
    }
}

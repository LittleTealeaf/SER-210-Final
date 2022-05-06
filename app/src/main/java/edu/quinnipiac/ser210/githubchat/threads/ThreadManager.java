package edu.quinnipiac.ser210.githubchat.threads;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>Manages anything and everything threaddy! This class doesn't just launch threads, but also manages tasks, notifying listeners, and so on.</p>
 * <p>Firstly, lets get over the thread part. There are two main types of tasks that this class can execute: async and scheduled. </p>
 * <ul>
 *     <li><b>Async Tasks</b> are tasks that need to be executed on a different thread. These are the tasks that are not connected to the UI in any way. The primary use
 *     of async tasks is for fetching API results, or dealing with the Database</li>
 *     <li><b>Scheduled Tasks</b> however are tasks that are required to be run on the main ui thread. The primary use of scheduled tasks is for notifying listeners that
 *     a given task is complete, letting the attached objects update the ui with the provided item</li>
 * </ul>
 * <p>These two types can be directly used with the {@link #run(Runnable)} and {@link #schedule(Runnable)} methods. {@link #run(Runnable)} runs tasks an
 * {@link ExecutorService} (with a cached thread pool, see {@link Executors#newCachedThreadPool()}), while {@link #schedule(Runnable)} uses a {@link Handler} in order to
 * schedule tasks to execute on the main thread.</p>
 * <p>Now, lets talk channels. In this app, a "channel" is basically a unique id for a given asynchronous task. Any class or object can request a new channel by using the
 * {@link #registerChannel()} method. This returns a unique ID that can be used to prepare an object to properly deal with a return value from a task. Each listener for a
 * particular task not only receives the returned object, but also the channel that the task was operated on. One benefit of this method is that it is possible to use the
 * same listener for multiple tasks, while able to differentiate which task was executed. Additionally, if a task is currently running, but no longer needed, one simply
 * needs to reset the compared channel value to {@link #NULL_CHANNEL}, essentially voiding that running task.
 * </p>
 * <p>
 *     <b>TLDR:</b> Process goes like this: Task needs to be run, a task is created using {@link #startThread(Task, Callback)}. First, it creates the thread and then
 *     returns the channel that the listener should listen to. On the alternate thread, it computes the task. Once the task is done and the value is retrieved, it switches
 *     back to the main thread using {@link #schedule(Runnable)} and executes the listener, passing both the returned value and the channel. The listener then should check
 *     if the channel matches what it received, and execute whatever tasks it needed to be run .
 * </p>
 * @author Thomas Kwashnak
 */
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

    public static int startThread(Runnable task, SimpleCallback notifier) {
        return startThread(task,notifier,registerChannel());
    }

    public static int startThread(Runnable task, SimpleCallback notifier, int channel) {
        run(() -> {
            task.run();
            schedule(() -> notifier.notify(channel));
        });
        return channel;
    }

    public static void startThread(Runnable task, Runnable post) {
        run(() -> {
            task.run();
            schedule(post);
        });
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

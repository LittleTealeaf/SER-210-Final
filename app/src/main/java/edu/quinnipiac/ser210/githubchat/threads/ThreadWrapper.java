package edu.quinnipiac.ser210.githubchat.threads;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadWrapper {

    private static int lastRegisteredChannel = 1;

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static int registerChannel() {
        return lastRegisteredChannel++;
    }

}

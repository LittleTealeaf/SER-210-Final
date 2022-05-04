package edu.quinnipiac.ser210.githubchat.threads;

/**
 * A simple interface that manages a callback with a passed parameter from an async task
 * @param <T> The type of object being returned
 * @author Thomas Kwashnak
 */
public interface Callback<T> {

    /**
     * Notifies the receiving object about the completion of an async task, providing the item and channel
     * @param item The item to pass through
     * @param channel The channel, as registered in the Thread Manager
     */
    void notify(T item, int channel);
}

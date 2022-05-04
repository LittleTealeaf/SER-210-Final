package edu.quinnipiac.ser210.githubchat.threads;

/**
 * Represents a task to be run asynchronously with a return type
 * @param <T> The object type returned
 * @author Thomas Kwashnak
 */
public interface Task<T> {

    T execute();
}

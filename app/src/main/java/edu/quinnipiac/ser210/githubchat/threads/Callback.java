package edu.quinnipiac.ser210.githubchat.threads;

public interface Callback<T> {

    void notify(T item, int channel);
}

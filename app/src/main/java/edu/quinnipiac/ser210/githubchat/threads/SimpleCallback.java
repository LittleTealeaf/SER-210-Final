package edu.quinnipiac.ser210.githubchat.threads;

/**
 * An extension of Callback, which does not pass an item through
 * @author Thomas Kwashnak
 */
public interface SimpleCallback {

    void notify(int channel);
}

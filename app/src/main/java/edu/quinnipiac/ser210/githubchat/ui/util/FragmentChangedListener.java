package edu.quinnipiac.ser210.githubchat.ui.util;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * Basically an interface for the MainActivity so fragments can tell the activity when they are created. This was done because I couldn't find a simple easy way for the
 * navigation controller to tell the MainActivity when it's changed (while passing in the new fragment object)
 * @author Thomas Kwashnak
 */
public interface FragmentChangedListener {

    static void notifyContext(Context context, Fragment fragment) {
        if (context instanceof FragmentChangedListener) {
            ((FragmentChangedListener) context).onFragmentLoaded(fragment);
        }
    }

    void onFragmentLoaded(Fragment fragment);
}

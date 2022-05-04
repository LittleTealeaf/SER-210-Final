package edu.quinnipiac.ser210.githubchat.ui.util;

import android.content.Context;

import androidx.fragment.app.Fragment;

public interface FragmentChangedListener {

    static void notifyContext(Context context, Fragment fragment) {
        if (context instanceof FragmentChangedListener) {
            ((FragmentChangedListener) context).onFragmentLoaded(fragment);
        }
    }

    void onFragmentLoaded(Fragment fragment);
}

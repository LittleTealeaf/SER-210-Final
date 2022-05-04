package edu.quinnipiac.ser210.githubchat.ui.util;

import android.content.Context;

import androidx.fragment.app.Fragment;

public interface FragmentChangedListener {

    void onFragmentLoaded(Fragment fragment);

    static void notifyContext(Context context, Fragment fragment) {
        if(context instanceof FragmentChangedListener) {
            ((FragmentChangedListener) context).onFragmentLoaded(fragment);
        }
    }
}

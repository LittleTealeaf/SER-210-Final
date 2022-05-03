package edu.quinnipiac.ser210.githubchat.ui.util;

import androidx.appcompat.widget.Toolbar;

public interface ToolbarHolder {

    static Toolbar from(Object object) {
        if (object instanceof ToolbarHolder) {
            return ((ToolbarHolder) object).getToolbar();
        } else {
            return null;
        }
    }

    Toolbar getToolbar();
}

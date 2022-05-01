package edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces;

import androidx.appcompat.widget.Toolbar;

public interface ToolbarHolder {
    Toolbar getToolbar();

    static Toolbar from(Object object) {
        if(object instanceof ToolbarHolder) {
            return ((ToolbarHolder) object).getToolbar();
        } else {
            return null;
        }
    }
}

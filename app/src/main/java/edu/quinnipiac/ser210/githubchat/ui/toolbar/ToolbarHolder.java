package edu.quinnipiac.ser210.githubchat.ui.toolbar;

import androidx.appcompat.widget.Toolbar;

/**
 * Exposes the Toolbar
 * @author Thomas Kwashnak
 */
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

package edu.quinnipiac.ser210.githubchat.ui.toolbar;

/**
 * Collection of interfaces that indicate different actions/buttons on the toolbar. By implementing one of these interfaces, the Main Activity will automatically display
 * and hook up that Toolbar Item to the fragment's implementation
 * @author Thomas Kwashnak
 */
public class ToolbarAction {

    public interface Share {

        void onShare();
    }

    public interface Info {

        void onInfo();
    }

    public interface Github {

        void onGithub();
    }
}

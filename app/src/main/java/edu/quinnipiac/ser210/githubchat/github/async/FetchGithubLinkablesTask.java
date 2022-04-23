package edu.quinnipiac.ser210.githubchat.github.async;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.Linkable;

public class FetchGithubLinkablesTask {

    public interface Listener {
        void onFetchLinkables(List<Linkable> linkables);
    }
}

package edu.quinnipiac.ser210.githubchat.github.async;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.RepoItem;

public class FetchGithubRepoItemTask {

    private final Listener listener;

    public FetchGithubRepoItemTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper, Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onFetchRepoItems(List<RepoItem> repoItems);
    }
}

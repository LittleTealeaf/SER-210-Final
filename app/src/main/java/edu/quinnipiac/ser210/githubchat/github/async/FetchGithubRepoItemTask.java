package edu.quinnipiac.ser210.githubchat.github.async;

import java.util.LinkedList;
import java.util.List;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubIssue;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubPull;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.RepoItem;

/**
 * @author Thomas Kwashnak
 * An AsyncTask-like that diverges and fetches both Pull Requests and Issues from a provided repository
 */
public class FetchGithubRepoItemTask implements FetchGithubPullsTask.Listener, FetchGithubIssuesTask.Listener {

    private final Listener listener;

    private final GithubWrapper githubWrapper;
    private final DatabaseHelper databaseHelper;

    private final List<RepoItem> repoItems = new LinkedList<>();

    private boolean isFirst;


    public FetchGithubRepoItemTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper, Listener listener) {
        this.listener = listener;
        this.databaseHelper = databaseHelper;
        this.githubWrapper = githubWrapper;
    }

    public void execute(String... strings) {
        new FetchGithubPullsTask(githubWrapper, databaseHelper, this).execute(strings);
        new FetchGithubIssuesTask(githubWrapper, databaseHelper, this).execute(strings);
    }

    @Override
    public void onFetchGithubIssues(List<GithubIssue> githubIssueList) {
        addRepoItems(new LinkedList<>(githubIssueList));
    }

    @Override
    public void onFetchGithubPulls(List<GithubPull> pullList) {
        addRepoItems(new LinkedList<>(pullList));
    }

    private synchronized void addRepoItems(List<RepoItem> items) {
        repoItems.addAll(items);
        if(isFirst) {
            listener.onFetchRepoItems(repoItems);
        } else {
            isFirst = true;
        }
    }

    public interface Listener {

        void onFetchRepoItems(List<RepoItem> repoItems);
    }
}

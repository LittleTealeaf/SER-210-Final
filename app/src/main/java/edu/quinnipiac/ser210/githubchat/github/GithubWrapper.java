package edu.quinnipiac.ser210.githubchat.github;

import android.app.Activity;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubRepoItemTask;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubRepoListTask;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubRepoTask;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubTask;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubUserTask;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;

/**
 * @author Thomas Kwashnak
 */
public class GithubWrapper {

    public static String TOKEN = "githubToken";

    private String githubToken;
    private DatabaseHelper databaseHelper;

    public GithubWrapper(DatabaseHelper databaseHelper, String githubToken) {
        this.githubToken = githubToken;
        this.databaseHelper = databaseHelper;
    }

    public void setGithubToken(String githubToken) {
        this.githubToken = githubToken;
    }

    public String getGithubToken() {
        return githubToken;
    }

    public void fetchGithubUser(String username, FetchGithubUserTask.Listener listener) {
        new FetchGithubUserTask(this,databaseHelper,listener).execute(username);
    }

    public void fetchGithubRepo(String fullName, FetchGithubRepoTask.Listener listener) {
        new FetchGithubRepoTask(this,databaseHelper,listener).execute(fullName);
    }

    public void fetchGithubRepoList(String username, FetchGithubRepoListTask.Listener listener) {
        new FetchGithubRepoListTask(this,databaseHelper,listener).execute(username);
    }

    public void fetchGithubRepoItems(String fullName, FetchGithubRepoItemTask.Listener listener) {
        new FetchGithubRepoItemTask(this,databaseHelper,listener).execute(fullName);
    }

    public void fetchCurrentUser(FetchGithubUserTask.Listener listener) {
        new FetchGithubUserTask(this,databaseHelper,listener).execute();
    }

    public void fetchCurrentUserRepos(FetchGithubRepoListTask.Listener listener) {
        new FetchGithubRepoListTask(this,databaseHelper,listener).execute();
    }

    public static GithubWrapper fromObject(Object object) {
        if(object instanceof Holder) {
            return ((Holder) object).getGithubWrapper();
        } else {
            return null;
        }
    }


    public interface Holder {
        GithubWrapper getGithubWrapper();
    }
}

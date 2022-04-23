package edu.quinnipiac.ser210.githubchat.github;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubTask;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubUserTask;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;

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




    public interface Holder {
        GithubWrapper getGithubWrapper();
    }
}

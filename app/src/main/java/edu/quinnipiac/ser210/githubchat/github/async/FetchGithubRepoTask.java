package edu.quinnipiac.ser210.githubchat.github.async;

import org.json.JSONObject;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;

/**
 * @author Thomas Kwashnak
 * An AsyncTask that fetches a Repository from its full name
 */
public class FetchGithubRepoTask extends FetchGithubTask {

    private final Listener listener;

    public FetchGithubRepoTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper, Listener listener) {
        super(githubWrapper, databaseHelper);
        this.listener = listener;
    }

    @Override
    protected String getURL(String[] strings) {
        return "https://api.github.com/repos/" + strings[0];
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            listener.onFetchGithubRepo(new GithubRepo(new JSONObject(s)));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public interface Listener {
        void onFetchGithubRepo(GithubRepo repo);
    }
}

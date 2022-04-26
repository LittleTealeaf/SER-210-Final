package edu.quinnipiac.ser210.githubchat.github.async;

import org.json.JSONArray;

import java.util.LinkedList;
import java.util.List;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubPull;

/**
 * @author Thomas Kwashnak
 * An AsyncTask that handles fetching all pull requests from a given repository
 */
public class FetchGithubPullsTask extends FetchGithubListTask {

    private final Listener listener;

    public FetchGithubPullsTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper, Listener listener) {
        super(githubWrapper, databaseHelper);
        this.listener = listener;
    }

    @Override
    protected String getURL(String[] strings) {
        return "https://api.github.com/repos/" + strings[0] + "/pulls";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONArray array = new JSONArray(s);
            List<GithubPull> pulls = new LinkedList<>();
            for(int i = 0; i < array.length(); i++) {
                pulls.add(new GithubPull(array.getJSONObject(i)));
            }
            listener.onFetchGithubPulls(pulls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface Listener {

        void onFetchGithubPulls(List<GithubPull> pullList);
    }
}

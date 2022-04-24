package edu.quinnipiac.ser210.githubchat.github.async;


import org.json.JSONObject;

import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;

public class FetchGithubUserTask extends FetchGithubTask {

    private Listener listener;

    public FetchGithubUserTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper, Listener listener) {
        super(githubWrapper,databaseHelper);
        this.listener = listener;

    }



    @Override
    protected String createURL(String[] strings) {
        return "https://api.github.com/users/" + strings[0];
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            listener.onFetchGithubUser(new GithubUser(new JSONObject(s)));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public interface Listener {
        void onFetchGithubUser(GithubUser user);
    }
}

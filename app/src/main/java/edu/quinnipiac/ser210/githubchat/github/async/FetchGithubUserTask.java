package edu.quinnipiac.ser210.githubchat.github.async;


import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;

public class FetchGithubUserTask extends FetchGithubTask {

    private Listener listener;

    public FetchGithubUserTask(GithubWrapper githubWrapper, Listener listener) {
        super(githubWrapper);
        this.listener = listener;

    }



    @Override
    protected String createURL(String[] strings) {
        return "https://api.github.com/users/" + strings[0];
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println(s);
    }

    public interface Listener {
        void onFetchGithubUser(GithubUser user);
    }
}

package edu.quinnipiac.ser210.githubchat.github.async;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.RepoItem;

public class FetchGithubRepoListTask extends FetchGithubTask {



    private final Listener listener;
    private final List<GithubRepo> repoList;
    private final int page;

    public FetchGithubRepoListTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper, Listener listener) {
        super(githubWrapper,databaseHelper);
        this.listener = listener;
        this.repoList = new LinkedList<>();
        this.page = 0;
    }

    private FetchGithubRepoListTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper, Listener listener, List<GithubRepo> repoList,
                                    int page) {
        super(githubWrapper,databaseHelper);
        this.listener = listener;
        this.repoList = repoList;
        this.page = page;
    }

    @Override
    protected void addHeaders(HttpsURLConnection connection) {
        super.addHeaders(connection);
        connection.addRequestProperty("per_page","100");
        connection.addRequestProperty("page",Integer.toString(page));
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONArray items = new JSONArray(s);
            for(int i = 0; i < items.length(); i++) {
                repoList.add(new GithubRepo(items.getJSONObject(i)));
            }
            if(items.length() < 100) {
                listener.onFetchGithubRepoList(repoList);
            } else {
                new FetchGithubRepoListTask(githubWrapper,databaseHelper,listener,repoList,page + 1);
            }
        } catch(Exception e) {

        }
    }

    @Override
    protected String createURL(String[] strings) {
        return null;
    }

    interface Listener {
        void onFetchGithubRepoList(List<GithubRepo> repoList);
    }
}

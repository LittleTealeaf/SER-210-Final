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

public class FetchGithubRepoListTask extends FetchGithubListTask {

    private final Listener listener;

    public FetchGithubRepoListTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper, Listener listener) {
        super(githubWrapper, databaseHelper);
        this.listener = listener;
    }

    @Override
    protected String createURL(String[] strings) {
        return "https://api.github.com/users/" + strings[0] + "/repos";
    }

    @Override
    protected void onPostExecute(String s) {

        try {
            JSONArray jsonArray = new JSONArray(s);
            List<GithubRepo> repoList = new LinkedList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                repoList.add(new GithubRepo(jsonArray.getJSONObject(i)));
            }
            listener.onFetchGithubRepoList(repoList);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public interface Listener {
        void onFetchGithubRepoList(List<GithubRepo> repoList);
    }
}

package edu.quinnipiac.ser210.githubchat.github.async;

import org.json.JSONArray;

import java.util.LinkedList;
import java.util.List;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubIssue;

public class FetchGithubIssuesTask extends FetchGithubListTask {

    private final Listener listener;

    public FetchGithubIssuesTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper, Listener listener) {
        super(githubWrapper, databaseHelper);
        this.listener = listener;
    }

    @Override
    protected String createURL(String[] strings) {
        return "https://api.github.com/repos/" + strings[0] + "/issues";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONArray array = new JSONArray(s);
            List<GithubIssue> issues = new LinkedList<>();
            for(int i = 0; i < array.length(); i++) {
                issues.add(new GithubIssue(array.getJSONObject(i)));
            }
            listener.onFetchGithubIssues(issues);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    interface Listener {
        void onFetchGithubIssues(List<GithubIssue> githubIssueList);
    }
}

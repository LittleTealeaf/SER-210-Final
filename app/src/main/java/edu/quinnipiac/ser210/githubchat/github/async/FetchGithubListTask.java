package edu.quinnipiac.ser210.githubchat.github.async;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;

public abstract class FetchGithubListTask extends FetchGithubTask {

    private int page = 1;

    public FetchGithubListTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper) {
        super(githubWrapper, databaseHelper);
    }

    @Override
    protected void addHeaders(HttpsURLConnection connection) {
        super.addHeaders(connection);
        connection.addRequestProperty("per_page","100");
        connection.addRequestProperty("page",Integer.toString(page));
    }

    @Override
    protected String getURLKey(String... strings) {
        return super.getURLKey(strings) + " " + page;
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONArray items = new JSONArray();

        JSONArray fetch = null;
        try {
            while((fetch = new JSONArray(super.doInBackground(strings))).length() > 0) {
                page++;
                for(int i = 0; i < fetch.length(); i++) {
                    items.put(fetch.getJSONObject(i));
                }
                if(fetch.length() < 100) {
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return items.toString();
    }
}

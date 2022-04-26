package edu.quinnipiac.ser210.githubchat.github.async;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;

/**
 * @author Thomas Kwashnak
 * An abstract AsyncTask that handles fetching multiple pages from some endpoint
 */
public abstract class FetchGithubListTask extends FetchGithubTask {

    private static final int per_page = 100;

    private int page = 1;

    public FetchGithubListTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper) {
        super(githubWrapper, databaseHelper);
    }

    @Override
    public Map<String, String> buildHeaders() {
        return new HashMap<String,String>() {{
           put("page",Integer.toString(page));
           put("per_page",Integer.toString(per_page));
        }};
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
                if(fetch.length() < per_page) {
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return items.toString();
    }
}

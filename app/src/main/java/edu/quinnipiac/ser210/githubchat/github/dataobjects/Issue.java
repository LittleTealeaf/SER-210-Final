package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

public class Issue implements RepoItem {

    private final int number;
    private final String title;
    private final String url;
    private final boolean closed;
    private final GithubUser user;

    public Issue(JSONObject jsonObject) throws JSONException {
        number = jsonObject.getInt("number");
        title = jsonObject.getString("title");
        url = jsonObject.getString("html_url");
        closed = jsonObject.getString("state").equals("closed");
        user = new GithubUser(jsonObject.getJSONObject("user"));
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public GithubUser getUser() {
        return user;
    }
}

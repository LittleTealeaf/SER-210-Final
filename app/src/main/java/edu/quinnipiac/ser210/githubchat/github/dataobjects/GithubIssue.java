package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

public class GithubIssue implements GithubAttachable {

    private int number;
    private String title;
    private String url;
    private boolean closed;
    private GithubUser githubUser;

    public GithubIssue(JSONObject jsonObject) throws JSONException {
        number = jsonObject.getInt("number");
        title = jsonObject.getString("title");
        url = jsonObject.getString("html_url");
        closed = jsonObject.getString("state").equals("closed");
        githubUser = new GithubUser(jsonObject.getJSONObject("user"));
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
    public GithubUser getGithubUser() {
        return githubUser;
    }
}

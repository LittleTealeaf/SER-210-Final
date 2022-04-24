package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

public class GithubUser {

    private String login;
    private String avatar_url;

    public GithubUser(JSONObject object) throws JSONException {
        login = object.getString("login");
        avatar_url = object.getString("avatar_url");
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }
}

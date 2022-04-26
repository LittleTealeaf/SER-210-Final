package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Thomas Kwashnak
 * Represents a Github User fetched from the Github REST API
 */
public class GithubUser {

    private final String login;
    private final String avatar_url;

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

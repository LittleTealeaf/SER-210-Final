package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Thomas Kwashnak
 */
public class GithubUser {

    private final String login;
    private final String avatarUrl;

    public GithubUser(JSONObject object) throws JSONException {
        login = object.getString("login");
        avatarUrl = object.getString("avatar_url");
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}

package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import static edu.quinnipiac.ser210.githubchat.util.JsonUtil.tryGetString;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Thomas Kwashnak
 */
public class GithubUser {

    private final String login;
    private final String name;
    private final String email;
    private final String avatarUrl;
    private final String url;

    public GithubUser(JSONObject object) throws JSONException {
        login = object.getString("login");
        email = tryGetString(object, "email");
        name = tryGetString(object, "name");
        url = object.getString("html_url");
        avatarUrl = object.getString("avatar_url");
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}

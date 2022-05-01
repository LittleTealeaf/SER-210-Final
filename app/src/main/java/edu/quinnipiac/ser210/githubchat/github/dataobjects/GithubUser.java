package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

import edu.quinnipiac.ser210.githubchat.util.JsonUtil;

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
        login = JsonUtil.tryGetString(object, "login");
        email = JsonUtil.tryGetString(object, "email");
//        email = object.has("email") ? object.getString("email") : null;
        name = JsonUtil.tryGetString(object, "name");
//        name = object.has("name") ? object.getString("name") : null;
        url = JsonUtil.tryGetString(object, "html_url");
//        url = object.getString("html_url");
        avatarUrl = JsonUtil.tryGetString(object, "avatar_url");
//        avatarUrl = object.getString("avatar_url");

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

package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Thomas Kwashnak
 */
public class GithubRepo {

    private final String name;
    private final String fullName;
    private final GithubUser owner;
    private final String url;

    public GithubRepo(JSONObject object) throws JSONException {
        name = object.getString("name");
        fullName = object.getString("full_name");
        owner = new GithubUser(object.getJSONObject("owner"));
        url = object.getString("html_url");
    }

    public GithubRepo(String name, String fullName, GithubUser owner, String url) {
        this.name = name;
        this.fullName = fullName;
        this.owner = owner;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public GithubUser getOwner() {
        return owner;
    }

    public String getUrl() {
        return url;
    }
}

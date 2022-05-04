package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import static edu.quinnipiac.ser210.githubchat.util.JsonUtil.tryOrDefault;

import org.json.JSONException;
import org.json.JSONObject;

import edu.quinnipiac.ser210.githubchat.util.JsonUtil;

/**
 * @author Thomas Kwashnak
 */
public class GithubRepo {

    private final String name;
    private final String fullName;
    private final String description;
    private final GithubUser owner;
    private final String url;
    private final String website;

    public GithubRepo(JSONObject object) throws JSONException {
        name = object.getString("name");
        fullName = object.getString("full_name");
        owner = tryOrDefault(() -> new GithubUser(object.getJSONObject("owner")), null);
        description = tryOrDefault(() -> object.getString("description"), "");
        url = object.getString("html_url");
        website = tryOrDefault(() -> JsonUtil.tryGetString(object,"homepage"), null);
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

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

}

package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRepository;

/**
 * @author Thomas Kwashnak
 * Represents a Github Repository fetched from the Github REST API
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

    public ChatRepository toChatRepository() {
        ChatRepository repository = new ChatRepository();
        repository.setFullName(fullName);
        repository.setFavorite(false);
        return repository;
    }
}

package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.util.JsonUtil;

/**
 * @author Thomas Kwashnak
 */
public class GithubPull implements GithubAttachable {

    private final int number;
    private final String title;
    private final String url;
    private final String state;
    private final boolean isDraft;
    private final boolean isMerged;
    private final GithubUser githubUser;

    public GithubPull(JSONObject jsonObject) throws JSONException {
        number = jsonObject.getInt("number");
        title = jsonObject.getString("title");
        url = jsonObject.getString("html_url");
        state = JsonUtil.tryGetString(jsonObject, "state");
        isMerged = jsonObject.getBoolean("merged");
        isDraft = jsonObject.getBoolean("draft");
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
    public GithubUser getGithubUser() {
        return githubUser;
    }

    @Override
    public int getStatusDrawable() {
        if(state.equals("open")) {
            if(isDraft) {
                return R.drawable.ic_git_pull_request_draft_24;
            } else {
                return R.drawable.ic_git_pull_request_24;
            }
        } else {
            if(isMerged) {
                return R.drawable.ic_git_merge_24;
            } else {
                return R.drawable.ic_git_pull_request_closed_24;
            }
        }
    }
}

package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import static edu.quinnipiac.ser210.githubchat.util.JsonUtil.tryOrDefault;

import org.json.JSONException;
import org.json.JSONObject;

import edu.quinnipiac.ser210.githubchat.R;

/**
 * @author Thomas Kwashnak
 */
public class GithubIssue implements GithubAttachment {

    private final int number;
    private final String title;
    private final String url;
    private final boolean draft;
    private final String state;
    private final GithubUser githubUser;

    public GithubIssue(JSONObject jsonObject) throws JSONException {
        number = jsonObject.getInt("number");
        title = tryOrDefault(() -> jsonObject.getString("title"),"");
        url = jsonObject.getString("html_url");
        state = tryOrDefault(() -> jsonObject.getString("state"),"closed");
        githubUser = tryOrDefault(() -> new GithubUser(jsonObject.getJSONObject("user")),null);
        draft = tryOrDefault(() -> jsonObject.getBoolean("draft"),false);
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
            if(draft) {
                return R.drawable.ic_issue_draft_24;
            } else {
                return R.drawable.ic_issue_opened_24;
            }
        } else {
            return R.drawable.ic_issue_closed_24;
        }
    }
}

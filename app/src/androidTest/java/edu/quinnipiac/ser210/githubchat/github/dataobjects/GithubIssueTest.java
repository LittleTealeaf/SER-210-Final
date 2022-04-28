package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GithubIssueTest {

    private static final String json = "{ \"url\": \"https://api.github.com/repos/LittleTealeaf/paceManager/issues/351\", \"repository_url\": " +
            "\"https://api.github.com/repos/LittleTealeaf/paceManager\", \"labels_url\": \"https://api.github" +
            ".com/repos/LittleTealeaf/paceManager/issues/351/labels{/name}\", \"comments_url\": \"https://api.github" +
            ".com/repos/LittleTealeaf/paceManager/issues/351/comments\", \"events_url\": \"https://api.github" +
            ".com/repos/LittleTealeaf/paceManager/issues/351/events\", \"html_url\": \"https://github.com/LittleTealeaf/paceManager/issues/351\", " +
            "\"id\": 1200295805, \"node_id\": \"I_kwDODMjujs5Hiw99\", \"number\": 351, \"title\": \"Combine CI Actions\", \"user\": { \"login\": " +
            "\"LittleTealeaf\", \"id\": 35083315, \"node_id\": \"MDQ6VXNlcjM1MDgzMzE1\", \"avatar_url\": \"https://avatars.githubusercontent" +
            ".com/u/35083315?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LittleTealeaf\", \"html_url\": \"https://github" +
            ".com/LittleTealeaf\", \"followers_url\": \"https://api.github.com/users/LittleTealeaf/followers\", \"following_url\": \"https://api" +
            ".github.com/users/LittleTealeaf/following{/other_user}\", \"gists_url\": \"https://api.github" +
            ".com/users/LittleTealeaf/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LittleTealeaf/starred{/owner}{/repo}\", " +
            "\"subscriptions_url\": \"https://api.github.com/users/LittleTealeaf/subscriptions\", \"organizations_url\": \"https://api.github" +
            ".com/users/LittleTealeaf/orgs\", \"repos_url\": \"https://api.github.com/users/LittleTealeaf/repos\", \"events_url\": \"https://api" +
            ".github.com/users/LittleTealeaf/events{/privacy}\", \"received_events_url\": \"https://api.github" +
            ".com/users/LittleTealeaf/received_events\", \"type\": \"User\", \"site_admin\": false }, \"labels\": [ ], \"state\": \"open\", " +
            "\"locked\": false, \"assignee\": null, \"assignees\": [ ], \"milestone\": null, \"comments\": 0, \"created_at\": " +
            "\"2022-04-11T17:28:21Z\", \"updated_at\": \"2022-04-11T17:28:21Z\", \"closed_at\": null, \"author_association\": \"OWNER\", " +
            "\"active_lock_reason\": null, \"body\": null, \"reactions\": { \"url\": \"https://api.github" +
            ".com/repos/LittleTealeaf/paceManager/issues/351/reactions\", \"total_count\": 0, \"+1\": 0, \"-1\": 0, \"laugh\": 0, \"hooray\": 0, " +
            "\"confused\": 0, \"heart\": 0, \"rocket\": 0, \"eyes\": 0 }, \"timeline_url\": \"https://api.github" +
            ".com/repos/LittleTealeaf/paceManager/issues/351/timeline\", \"performed_via_github_app\": null }";

    GithubIssue githubIssue;


    @Before
    public void setUp() throws Exception {
        githubIssue = new GithubIssue(new JSONObject(json));
    }

    @Test
    public void getNumber() {
        assertEquals(351,githubIssue.getNumber());
    }

    @Test
    public void getTitle() {
        assertEquals("Combine CI Actions",githubIssue.getTitle());
    }

    @Test
    public void getURL() {
        assertEquals("https://github.com/LittleTealeaf/paceManager/issues/351",githubIssue.getURL());
    }

    @Test
    public void isClosed() {
        assertFalse(githubIssue.isClosed());
    }

    @Test
    public void getGithubUser() {
        assertEquals("LittleTealeaf",githubIssue.getGithubUser().getLogin());
    }
}
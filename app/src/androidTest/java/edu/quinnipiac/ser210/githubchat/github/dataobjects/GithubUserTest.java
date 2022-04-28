package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GithubUserTest {

    private GithubUser githubUser;

    private static final String jsonString = "{ \"login\": \"LittleTealeaf\", \"id\": 35083315, \"node_id\": \"MDQ6VXNlcjM1MDgzMzE1\", " +
            "\"avatar_url\": \"https://avatars.githubusercontent.com/u/35083315?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github" +
            ".com/users/LittleTealeaf\", \"html_url\": \"https://github.com/LittleTealeaf\", \"followers_url\": \"https://api.github" +
            ".com/users/LittleTealeaf/followers\", \"following_url\": \"https://api.github.com/users/LittleTealeaf/following{/other_user}\", " +
            "\"gists_url\": \"https://api.github.com/users/LittleTealeaf/gists{/gist_id}\", \"starred_url\": \"https://api.github" +
            ".com/users/LittleTealeaf/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github" +
            ".com/users/LittleTealeaf/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LittleTealeaf/orgs\", \"repos_url\": " +
            "\"https://api.github.com/users/LittleTealeaf/repos\", \"events_url\": \"https://api.github.com/users/LittleTealeaf/events{/privacy}\"," +
            " \"received_events_url\": \"https://api.github.com/users/LittleTealeaf/received_events\", \"type\": \"User\", \"site_admin\": false, " +
            "\"name\": \"Thomas Kwashnak\", \"company\": null, \"blog\": \"\", \"location\": \"Connecticut\", \"email\": null, \"hireable\": true, " +
            "\"bio\": \"Student at Quinnipiac University. Bachelors of Arts Computer Science and Data Science Double Major with a minor in " +
            "economics\", \"twitter_username\": \"LittleTeeaaa\", \"public_repos\": 81, \"public_gists\": 1, \"followers\": 8, \"following\": 19, " +
            "\"created_at\": \"2018-01-03T23:38:21Z\", \"updated_at\": \"2022-04-27T21:38:54Z\" }";

    @Before
    public void setUp() throws Exception {
        githubUser = new GithubUser(new JSONObject(jsonString));
    }

    @Test
    public void getLogin() {
        assertEquals("LittleTealeaf",githubUser.getLogin());
    }

    @Test
    public void getAvatarUrl() {
        assertEquals("https://avatars.githubusercontent.com/u/35083315?v=4",githubUser.getAvatarUrl());
    }
}
package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GithubRepoTest {

    private static final String repoJson = "{ \"id\": 214494862, \"node_id\": \"MDEwOlJlcG9zaXRvcnkyMTQ0OTQ4NjI=\", \"name\": \"paceManager\", " +
                                           "\"full_name\": \"LittleTealeaf/paceManager\", \"private\": false, \"owner\": { \"login\": " +
                                           "\"LittleTealeaf\", \"id\": 35083315, " +
                                           "\"node_id\": \"MDQ6VXNlcjM1MDgzMzE1\", \"avatar_url\": \"https://avatars.githubusercontent" +
                                           ".com/u/35083315?v=4\", \"gravatar_id\": " +
                                           "\"\", \"url\": \"https://api.github.com/users/LittleTealeaf\", \"html_url\": \"https://github" +
                                           ".com/LittleTealeaf\", \"followers_url\": " +
                                           "\"https://api.github.com/users/LittleTealeaf/followers\", \"following_url\": \"https://api.github" +
                                           ".com/users/LittleTealeaf/following{/other_user}\", \"gists_url\": \"https://api.github" +
                                           ".com/users/LittleTealeaf/gists{/gist_id}\", " +
                                           "\"starred_url\": \"https://api.github.com/users/LittleTealeaf/starred{/owner}{/repo}\", " +
                                           "\"subscriptions_url\": \"https://api.github" +
                                           ".com/users/LittleTealeaf/subscriptions\", \"organizations_url\": \"https://api.github" +
                                           ".com/users/LittleTealeaf/orgs\", \"repos_url\": " +
                                           "\"https://api.github.com/users/LittleTealeaf/repos\", \"events_url\": \"https://api.github" +
                                           ".com/users/LittleTealeaf/events{/privacy}\"," +
                                           " \"received_events_url\": \"https://api.github.com/users/LittleTealeaf/received_events\", \"type\": " +
                                           "\"User\", \"site_admin\": false }," +
                                           " \"html_url\": \"https://github.com/LittleTealeaf/paceManager\", \"description\": \"Pace time " +
                                           "calculator to rank teams based on a " +
                                           "provided goal time\", \"fork\": false, \"url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager\", \"forks_url\": " +
                                           "\"https://api.github.com/repos/LittleTealeaf/paceManager/forks\", \"keys_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/keys{/key_id}\", \"collaborators_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/collaborators{/collaborator}\", \"teams_url\": \"https://api" +
                                           ".github" + ".com/repos/LittleTealeaf/paceManager/teams\", \"hooks_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/hooks\", " + "\"issue_events_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/issues/events{/number}\", \"events_url\": \"https://api" +
                                           ".github.com/repos/LittleTealeaf/paceManager/events\", \"assignees_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/assignees{/user}\", \"branches_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/branches{/branch}\", \"tags_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/tags\", \"blobs_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/git/blobs{/sha}\", \"git_tags_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/git/tags{/sha}\", \"git_refs_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/git/refs{/sha}\", \"trees_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/git/trees{/sha}\", \"statuses_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/statuses/{sha}\", \"languages_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/languages\", \"stargazers_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/stargazers\", \"contributors_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/contributors\", \"subscribers_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/subscribers\", \"subscription_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/subscription\", \"commits_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/commits{/sha}\", \"git_commits_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/git/commits{/sha}\", \"comments_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/comments{/number}\", \"issue_comment_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/issues/comments{/number}\", \"contents_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/contents/{+path}\", \"compare_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/compare/{base}...{head}\", \"merges_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/merges\", \"archive_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/{archive_format}{/ref}\", \"downloads_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/downloads\", \"issues_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/issues{/number}\", \"pulls_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/pulls{/number}\", \"milestones_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/milestones{/number}\", \"notifications_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/notifications{?since,all,participating}\", \"labels_url\": " +
                                           "\"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/labels{/name}\", \"releases_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/releases{/id}\", \"deployments_url\": \"https://api.github" +
                                           ".com/repos/LittleTealeaf/paceManager/deployments\", \"created_at\": \"2019-10-11T17:32:59Z\", " +
                                           "\"updated_at\": " +
                                           "\"2022-03-10T19:03:15Z\", \"pushed_at\": \"2022-04-26T14:29:19Z\", \"git_url\": \"git://github" +
                                           ".com/LittleTealeaf/paceManager.git\", " +
                                           "\"ssh_url\": \"git@github.com:LittleTealeaf/paceManager.git\", \"clone_url\": \"https://github" +
                                           ".com/LittleTealeaf/paceManager.git\", " +
                                           "\"svn_url\": \"https://github.com/LittleTealeaf/paceManager\", \"homepage\": \"https://littletealeaf" +
                                           ".github.io/paceManager/\", " +
                                           "\"size\": 73014, \"stargazers_count\": 4, \"watchers_count\": 4, \"language\": \"Java\", " +
                                           "\"has_issues\": true, \"has_projects\": true," +
                                           " \"has_downloads\": true, \"has_wiki\": true, \"has_pages\": true, \"forks_count\": 0, \"mirror_url\": " +
                                           "null, \"archived\": false, " +
                                           "\"disabled\": false, \"open_issues_count\": 5, \"license\": { \"key\": \"apache-2.0\", \"name\": " +
                                           "\"Apache License 2.0\", \"spdx_id\": " +
                                           "\"Apache-2.0\", \"url\": \"https://api.github.com/licenses/apache-2.0\", \"node_id\": " +
                                           "\"MDc6TGljZW5zZTI=\" }, \"allow_forking\": true," +
                                           " \"is_template\": false, \"topics\": [ \"java\" ], \"visibility\": \"public\", \"forks\": 0, " +
                                           "\"open_issues\": 5, \"watchers\": 4, " +
                                           "\"default_branch\": \"main\", \"temp_clone_token\": null, \"network_count\": 0, \"subscribers_count\": " +
                                           "0 }";

    GithubRepo githubRepo;

    @Before
    public void setUp() throws Exception {
        githubRepo = new GithubRepo(new JSONObject(repoJson));
    }

    @Test
    public void getName() {
        assertEquals("paceManager", githubRepo.getName());
    }

    @Test
    public void getFullName() {
        assertEquals("LittleTealeaf/paceManager", githubRepo.getFullName());
    }

    @Test
    public void getOwner() {
        assertEquals("LittleTealeaf", githubRepo.getOwner().getLogin());
    }

    @Test
    public void getUrl() {
        assertEquals("https://github.com/LittleTealeaf/paceManager", githubRepo.getUrl());
    }
}
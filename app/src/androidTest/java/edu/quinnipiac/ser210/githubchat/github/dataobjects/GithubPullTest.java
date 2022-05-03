package edu.quinnipiac.ser210.githubchat.github.dataobjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class GithubPullTest {

    private static final String json = "{ \"url\": \"https://api.github.com/repos/LittleTealeaf/SER-210-Final/pulls/87\", \"id\": 922277497, " +
                                       "\"node_id\": \"PR_kwDOHHUjYc42-NZ5\", \"html_url\": \"https://github" + ".com/LittleTealeaf/SER-210-Final/pull/87\", \"diff_url\": " +
                                       "\"https://github.com/LittleTealeaf/SER-210-Final/pull/87.diff\", \"patch_url\": \"https://github" +
                                       ".com/LittleTealeaf/SER-210-Final/pull/87.patch\", \"issue_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/issues/87\", \"number\": 87, \"state\": \"open\", \"locked\": false," + " \"title\": \"Update UML\"," +
                                       " \"user\": { \"login\": \"LittleTealeaf\", \"id\": 35083315, \"node_id\": \"MDQ6VXNlcjM1MDgzMzE1\", " + "\"avatar_url\": \"https://avatars" +
                                       ".githubusercontent.com/u/35083315?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github" + ".com/users/LittleTealeaf\", \"html_url\":" +
                                       " \"https://github.com/LittleTealeaf\", \"followers_url\": \"https://api.github" + ".com/users/LittleTealeaf/followers\", \"following_url\":" +
                                       " \"https://api.github.com/users/LittleTealeaf/following{/other_user}\", \"gists_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/gists{/gist_id}\", \"starred_url\": \"https://api.github" + ".com/users/LittleTealeaf/starred{/owner}{/repo}\", " +
                                       "\"subscriptions_url\": \"https://api.github.com/users/LittleTealeaf/subscriptions\", \"organizations_url\":" + " \"https://api.github" +
                                       ".com/users/LittleTealeaf/orgs\", \"repos_url\": \"https://api.github.com/users/LittleTealeaf/repos\", " + "\"events_url\": \"https://api" +
                                       ".github.com/users/LittleTealeaf/events{/privacy}\", \"received_events_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/received_events\", \"type\": \"User\", \"site_admin\": false }, \"body\": null, " + "\"created_at\": " +
                                       "\"2022-04-28T19:59:25Z\", \"updated_at\": \"2022-04-28T19:59:25Z\", \"closed_at\": null, \"merged_at\": " + "null, \"merge_commit_sha\": " +
                                       "\"9c87ce84f89ab00f585ff5ac04878f7405092d74\", \"assignee\": null, \"assignees\": [ ], " + "\"requested_reviewers\": [ ], " +
                                       "\"requested_teams\": [ ], \"labels\": [ { \"id\": 3996890115, \"node_id\": \"LA_kwDOHHUjYc7uO7QD\", " + "\"url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/labels/documentation\", \"name\": \"documentation\", \"color\": " + "\"0075ca\", \"default\": true, " +
                                       "\"description\": \"Improvements or additions to documentation\" } ], \"milestone\": { \"url\": " + "\"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/milestones/3\", \"html_url\": \"https://github" + ".com/LittleTealeaf/SER-210-Final/milestone/3\", " +
                                       "\"labels_url\": \"https://api.github.com/repos/LittleTealeaf/SER-210-Final/milestones/3/labels\", \"id\": " + "7876244, \"node_id\": " +
                                       "\"MI_kwDOHHUjYc4AeC6U\", \"number\": 3, \"title\": \"Iteration 2\", \"description\": \"\", \"creator\": { " + "\"login\": " +
                                       "\"LittleTealeaf\", \"id\": 35083315, \"node_id\": \"MDQ6VXNlcjM1MDgzMzE1\", \"avatar_url\": " + "\"https://avatars.githubusercontent" +
                                       ".com/u/35083315?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LittleTealeaf\", " + "\"html_url\": \"https://github" +
                                       ".com/LittleTealeaf\", \"followers_url\": \"https://api.github.com/users/LittleTealeaf/followers\", " + "\"following_url\": \"https://api" +
                                       ".github.com/users/LittleTealeaf/following{/other_user}\", \"gists_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/gists{/gist_id}\", \"starred_url\": \"https://api.github" + ".com/users/LittleTealeaf/starred{/owner}{/repo}\", " +
                                       "\"subscriptions_url\": \"https://api.github.com/users/LittleTealeaf/subscriptions\", \"organizations_url\":" + " \"https://api.github" +
                                       ".com/users/LittleTealeaf/orgs\", \"repos_url\": \"https://api.github.com/users/LittleTealeaf/repos\", " + "\"events_url\": \"https://api" +
                                       ".github.com/users/LittleTealeaf/events{/privacy}\", \"received_events_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/received_events\", \"type\": \"User\", \"site_admin\": false }, \"open_issues\": " + "1, \"closed_issues\": 12, " +
                                       "\"state\": \"open\", \"created_at\": \"2022-04-13T19:37:24Z\", \"updated_at\": \"2022-04-28T19:59:25Z\", " + "\"due_on\": " +
                                       "\"2022-04-28T07:00:00Z\", \"closed_at\": null }, \"draft\": false, \"commits_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/pulls/87/commits\", \"review_comments_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/pulls/87/comments\", \"review_comment_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/pulls/comments{/number}\", \"comments_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/issues/87/comments\", \"statuses_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/statuses/d2ea86b18aa09d9c4751988a2c2f87b191f540e9\", \"head\": { " + "\"label\": " +
                                       "\"LittleTealeaf:update-uml-week-2\", \"ref\": \"update-uml-week-2\", \"sha\": " + "\"d2ea86b18aa09d9c4751988a2c2f87b191f540e9\", \"user\": " +
                                       "{ \"login\": \"LittleTealeaf\", \"id\": 35083315, \"node_id\": \"MDQ6VXNlcjM1MDgzMzE1\", \"avatar_url\": " + "\"https://avatars" +
                                       ".githubusercontent.com/u/35083315?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github" + ".com/users/LittleTealeaf\", \"html_url\":" +
                                       " \"https://github.com/LittleTealeaf\", \"followers_url\": \"https://api.github" + ".com/users/LittleTealeaf/followers\", \"following_url\":" +
                                       " \"https://api.github.com/users/LittleTealeaf/following{/other_user}\", \"gists_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/gists{/gist_id}\", \"starred_url\": \"https://api.github" + ".com/users/LittleTealeaf/starred{/owner}{/repo}\", " +
                                       "\"subscriptions_url\": \"https://api.github.com/users/LittleTealeaf/subscriptions\", \"organizations_url\":" + " \"https://api.github" +
                                       ".com/users/LittleTealeaf/orgs\", \"repos_url\": \"https://api.github.com/users/LittleTealeaf/repos\", " + "\"events_url\": \"https://api" +
                                       ".github.com/users/LittleTealeaf/events{/privacy}\", \"received_events_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/received_events\", \"type\": \"User\", \"site_admin\": false }, \"repo\": { " + "\"id\": 477438817, \"node_id\": " +
                                       "\"R_kgDOHHUjYQ\", \"name\": \"SER-210-Final\", \"full_name\": \"LittleTealeaf/SER-210-Final\", \"private\":" + " false, \"owner\": { " +
                                       "\"login\": \"LittleTealeaf\", \"id\": 35083315, \"node_id\": \"MDQ6VXNlcjM1MDgzMzE1\", \"avatar_url\": " + "\"https://avatars" +
                                       ".githubusercontent.com/u/35083315?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github" + ".com/users/LittleTealeaf\", \"html_url\":" +
                                       " \"https://github.com/LittleTealeaf\", \"followers_url\": \"https://api.github" + ".com/users/LittleTealeaf/followers\", \"following_url\":" +
                                       " \"https://api.github.com/users/LittleTealeaf/following{/other_user}\", \"gists_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/gists{/gist_id}\", \"starred_url\": \"https://api.github" + ".com/users/LittleTealeaf/starred{/owner}{/repo}\", " +
                                       "\"subscriptions_url\": \"https://api.github.com/users/LittleTealeaf/subscriptions\", \"organizations_url\":" + " \"https://api.github" +
                                       ".com/users/LittleTealeaf/orgs\", \"repos_url\": \"https://api.github.com/users/LittleTealeaf/repos\", " + "\"events_url\": \"https://api" +
                                       ".github.com/users/LittleTealeaf/events{/privacy}\", \"received_events_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/received_events\", \"type\": \"User\", \"site_admin\": false }, \"html_url\": " + "\"https://github" +
                                       ".com/LittleTealeaf/SER-210-Final\", \"description\": \"Github Chat Android App\", \"fork\": false, \"url\":" + " \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final\", \"forks_url\": \"https://api.github" + ".com/repos/LittleTealeaf/SER-210-Final/forks\", " +
                                       "\"keys_url\": \"https://api.github.com/repos/LittleTealeaf/SER-210-Final/keys{/key_id}\", " + "\"collaborators_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/collaborators{/collaborator}\", \"teams_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/teams\", \"hooks_url\": \"https://api.github" + ".com/repos/LittleTealeaf/SER-210-Final/hooks\", " +
                                       "\"issue_events_url\": \"https://api.github.com/repos/LittleTealeaf/SER-210-Final/issues/events{/number}\", " + "\"events_url\": " +
                                       "\"https://api.github.com/repos/LittleTealeaf/SER-210-Final/events\", \"assignees_url\": \"https://api" + ".github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/assignees{/user}\", \"branches_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/branches{/branch}\", \"tags_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/tags\", \"blobs_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/git/blobs{/sha}\", \"git_tags_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/git/tags{/sha}\", \"git_refs_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/git/refs{/sha}\", \"trees_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/git/trees{/sha}\", \"statuses_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/statuses/{sha}\", \"languages_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/languages\", \"stargazers_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/stargazers\", \"contributors_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/contributors\", \"subscribers_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/subscribers\", \"subscription_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/subscription\", \"commits_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/commits{/sha}\", \"git_commits_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/git/commits{/sha}\", \"comments_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/comments{/number}\", \"issue_comment_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/issues/comments{/number}\", \"contents_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/contents/{+path}\", \"compare_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/compare/{base}...{head}\", \"merges_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/merges\", \"archive_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/{archive_format}{/ref}\", \"downloads_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/downloads\", \"issues_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/issues{/number}\", \"pulls_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/pulls{/number}\", \"milestones_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/milestones{/number}\", \"notifications_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/notifications{?since,all,participating}\", \"labels_url\": " + "\"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/labels{/name}\", \"releases_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/releases{/id}\", \"deployments_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/deployments\", \"created_at\": \"2022-04-03T19:04:57Z\", " + "\"updated_at\": " +
                                       "\"2022-04-26T17:31:23Z\", \"pushed_at\": \"2022-04-28T19:59:25Z\", \"git_url\": \"git://github" + ".com/LittleTealeaf/SER-210-Final.git\", " +
                                       "\"ssh_url\": \"git@github.com:LittleTealeaf/SER-210-Final.git\", \"clone_url\": \"https://github" + ".com/LittleTealeaf/SER-210-Final" +
                                       ".git\", \"svn_url\": \"https://github.com/LittleTealeaf/SER-210-Final\", \"homepage\": \"\", \"size\": " + "1433, \"stargazers_count\": 0, " +
                                       "\"watchers_count\": 0, \"language\": \"Java\", \"has_issues\": true, \"has_projects\": true, " + "\"has_downloads\": true, \"has_wiki\": " +
                                       "true, \"has_pages\": false, \"forks_count\": 0, \"mirror_url\": null, \"archived\": false, \"disabled\": " + "false, \"open_issues_count\":" +
                                       " 30, \"license\": null, \"allow_forking\": true, \"is_template\": false, \"topics\": [ ], \"visibility\": " + "\"public\", \"forks\": 0, " +
                                       "\"open_issues\": 30, \"watchers\": 0, \"default_branch\": \"main\" } }, \"base\": { \"label\": " + "\"LittleTealeaf:main\", \"ref\": " +
                                       "\"main\", \"sha\": \"51d8d443506386ab551d17018aabdc078a5b103a\", \"user\": { \"login\": \"LittleTealeaf\", " + "\"id\": 35083315, " +
                                       "\"node_id\": \"MDQ6VXNlcjM1MDgzMzE1\", \"avatar_url\": \"https://avatars.githubusercontent" + ".com/u/35083315?v=4\", \"gravatar_id\": " +
                                       "\"\", \"url\": \"https://api.github.com/users/LittleTealeaf\", \"html_url\": \"https://github" + ".com/LittleTealeaf\", \"followers_url\": " +
                                       "\"https://api.github.com/users/LittleTealeaf/followers\", \"following_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/following{/other_user}\", \"gists_url\": \"https://api.github" + ".com/users/LittleTealeaf/gists{/gist_id}\", " +
                                       "\"starred_url\": \"https://api.github.com/users/LittleTealeaf/starred{/owner}{/repo}\", " + "\"subscriptions_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/subscriptions\", \"organizations_url\": \"https://api.github" + ".com/users/LittleTealeaf/orgs\", \"repos_url\": " +
                                       "\"https://api.github.com/users/LittleTealeaf/repos\", \"events_url\": \"https://api.github" + ".com/users/LittleTealeaf/events{/privacy}\"," +
                                       " \"received_events_url\": \"https://api.github.com/users/LittleTealeaf/received_events\", \"type\": " + "\"User\", \"site_admin\": false }," +
                                       " \"repo\": { \"id\": 477438817, \"node_id\": \"R_kgDOHHUjYQ\", \"name\": \"SER-210-Final\", \"full_name\": " +
                                       "\"LittleTealeaf/SER-210-Final\", \"private\": false, \"owner\": { \"login\": \"LittleTealeaf\", \"id\": " + "35083315, \"node_id\": " +
                                       "\"MDQ6VXNlcjM1MDgzMzE1\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/35083315?v=4\", " + "\"gravatar_id\": \"\", \"url\": " +
                                       "\"https://api.github.com/users/LittleTealeaf\", \"html_url\": \"https://github.com/LittleTealeaf\", " + "\"followers_url\": \"https://api" +
                                       ".github.com/users/LittleTealeaf/followers\", \"following_url\": \"https://api.github" + ".com/users/LittleTealeaf/following{/other_user}\"," +
                                       " \"gists_url\": \"https://api.github.com/users/LittleTealeaf/gists{/gist_id}\", \"starred_url\": " + "\"https://api.github" +
                                       ".com/users/LittleTealeaf/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github" +
                                       ".com/users/LittleTealeaf/subscriptions\", \"organizations_url\": \"https://api.github" + ".com/users/LittleTealeaf/orgs\", \"repos_url\": " +
                                       "\"https://api.github.com/users/LittleTealeaf/repos\", \"events_url\": \"https://api.github" + ".com/users/LittleTealeaf/events{/privacy}\"," +
                                       " \"received_events_url\": \"https://api.github.com/users/LittleTealeaf/received_events\", \"type\": " + "\"User\", \"site_admin\": false }," +
                                       " \"html_url\": \"https://github.com/LittleTealeaf/SER-210-Final\", \"description\": \"Github Chat Android " + "App\", \"fork\": false, " +
                                       "\"url\": \"https://api.github.com/repos/LittleTealeaf/SER-210-Final\", \"forks_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/forks\", \"keys_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/keys{/key_id}\", \"collaborators_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/collaborators{/collaborator}\", \"teams_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/teams\", \"hooks_url\": \"https://api.github" + ".com/repos/LittleTealeaf/SER-210-Final/hooks\", " +
                                       "\"issue_events_url\": \"https://api.github.com/repos/LittleTealeaf/SER-210-Final/issues/events{/number}\", " + "\"events_url\": " +
                                       "\"https://api.github.com/repos/LittleTealeaf/SER-210-Final/events\", \"assignees_url\": \"https://api" + ".github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/assignees{/user}\", \"branches_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/branches{/branch}\", \"tags_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/tags\", \"blobs_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/git/blobs{/sha}\", \"git_tags_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/git/tags{/sha}\", \"git_refs_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/git/refs{/sha}\", \"trees_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/git/trees{/sha}\", \"statuses_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/statuses/{sha}\", \"languages_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/languages\", \"stargazers_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/stargazers\", \"contributors_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/contributors\", \"subscribers_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/subscribers\", \"subscription_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/subscription\", \"commits_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/commits{/sha}\", \"git_commits_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/git/commits{/sha}\", \"comments_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/comments{/number}\", \"issue_comment_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/issues/comments{/number}\", \"contents_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/contents/{+path}\", \"compare_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/compare/{base}...{head}\", \"merges_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/merges\", \"archive_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/{archive_format}{/ref}\", \"downloads_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/downloads\", \"issues_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/issues{/number}\", \"pulls_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/pulls{/number}\", \"milestones_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/milestones{/number}\", \"notifications_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/notifications{?since,all,participating}\", \"labels_url\": " + "\"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/labels{/name}\", \"releases_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/releases{/id}\", \"deployments_url\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/deployments\", \"created_at\": \"2022-04-03T19:04:57Z\", " + "\"updated_at\": " +
                                       "\"2022-04-26T17:31:23Z\", \"pushed_at\": \"2022-04-28T19:59:25Z\", \"git_url\": \"git://github" + ".com/LittleTealeaf/SER-210-Final.git\", " +
                                       "\"ssh_url\": \"git@github.com:LittleTealeaf/SER-210-Final.git\", \"clone_url\": \"https://github" + ".com/LittleTealeaf/SER-210-Final" +
                                       ".git\", \"svn_url\": \"https://github.com/LittleTealeaf/SER-210-Final\", \"homepage\": \"\", \"size\": " + "1433, \"stargazers_count\": 0, " +
                                       "\"watchers_count\": 0, \"language\": \"Java\", \"has_issues\": true, \"has_projects\": true, " + "\"has_downloads\": true, \"has_wiki\": " +
                                       "true, \"has_pages\": false, \"forks_count\": 0, \"mirror_url\": null, \"archived\": false, \"disabled\": " + "false, \"open_issues_count\":" +
                                       " 30, \"license\": null, \"allow_forking\": true, \"is_template\": false, \"topics\": [ ], \"visibility\": " + "\"public\", \"forks\": 0, " +
                                       "\"open_issues\": 30, \"watchers\": 0, \"default_branch\": \"main\" } }, \"_links\": { \"self\": { \"href\":" + " \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/pulls/87\" }, \"html\": { \"href\": \"https://github" + ".com/LittleTealeaf/SER-210-Final/pull/87\" " +
                                       "}, \"issue\": { \"href\": \"https://api.github.com/repos/LittleTealeaf/SER-210-Final/issues/87\" }, " + "\"comments\": { \"href\": " +
                                       "\"https://api.github.com/repos/LittleTealeaf/SER-210-Final/issues/87/comments\" }, \"review_comments\": { " + "\"href\": \"https://api" +
                                       ".github.com/repos/LittleTealeaf/SER-210-Final/pulls/87/comments\" }, \"review_comment\": { \"href\": " + "\"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/pulls/comments{/number}\" }, \"commits\": { \"href\": \"https://api" + ".github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/pulls/87/commits\" }, \"statuses\": { \"href\": \"https://api.github" +
                                       ".com/repos/LittleTealeaf/SER-210-Final/statuses/d2ea86b18aa09d9c4751988a2c2f87b191f540e9\" } }, " + "\"author_association\": \"OWNER\", " +
                                       "\"auto_merge\": null, \"active_lock_reason\": null }";

    GithubPull githubPull;

    @Before
    public void setUp() throws Exception {
        githubPull = new GithubPull(new JSONObject(json));
    }

    @Test
    public void getNumber() {
        assertEquals(87, githubPull.getNumber());
    }

    @Test
    public void getTitle() {
        assertEquals("Update UML", githubPull.getTitle());
    }

    @Test
    public void getURL() {
        assertEquals("https://github.com/LittleTealeaf/SER-210-Final/pull/87", githubPull.getURL());
    }

    @Test
    public void isClosed() {
        assertFalse(githubPull.isClosed());
    }

    @Test
    public void getGithubUser() {
        assertEquals("LittleTealeaf", githubPull.getGithubUser().getLogin());
    }
}
package edu.quinnipiac.ser210.githubchat.github.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubIssue;

/**
 * @author Thomas Kwashnak
 */
@Deprecated
public interface OnFetchGithubIssues extends GithubWrapper.OnFetchGithubIssueList {

    void onFetchGithubIssueList(List<GithubIssue> githubIssues, int channel);
}

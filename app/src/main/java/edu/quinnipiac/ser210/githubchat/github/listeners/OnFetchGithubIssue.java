package edu.quinnipiac.ser210.githubchat.github.listeners;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubIssue;

@Deprecated
public interface OnFetchGithubIssue {

    void onFetchGithubIssue(GithubIssue issue, int channel);
}

package edu.quinnipiac.ser210.githubchat.github.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubIssue;

public interface OnFetchGithubIssues {
    void onFetchGithubIssues(List<GithubIssue> githubIssues, int channel);
}
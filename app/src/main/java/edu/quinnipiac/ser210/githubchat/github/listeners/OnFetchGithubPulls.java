package edu.quinnipiac.ser210.githubchat.github.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubPull;

public interface OnFetchGithubPulls {
    void onFetchGithubPulls(List<GithubPull> githubPulls, int channel);
}
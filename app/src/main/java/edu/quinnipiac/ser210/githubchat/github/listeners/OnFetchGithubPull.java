package edu.quinnipiac.ser210.githubchat.github.listeners;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubPull;

public interface OnFetchGithubPull {
    void onFetchGithubPull(GithubPull pull, int channel);
}
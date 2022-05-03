package edu.quinnipiac.ser210.githubchat.github.listeners;

import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubPull;

@Deprecated
public interface OnFetchGithubPull extends GithubWrapper.OnFetchGithubPull {

    void onFetchGithubPull(GithubPull pull, int channel);
}

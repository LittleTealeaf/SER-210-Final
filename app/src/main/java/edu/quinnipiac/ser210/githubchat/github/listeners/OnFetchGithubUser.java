package edu.quinnipiac.ser210.githubchat.github.listeners;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;

public interface OnFetchGithubUser {
    void onFetchGithubUser(GithubUser githubUser, int channel);
}

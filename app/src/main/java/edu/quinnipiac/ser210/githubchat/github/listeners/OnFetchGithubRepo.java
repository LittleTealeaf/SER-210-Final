package edu.quinnipiac.ser210.githubchat.github.listeners;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;

public interface OnFetchGithubRepo {
    void onFetchGithubRepo(GithubRepo repo, int channel);
}
package edu.quinnipiac.ser210.githubchat.github.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;

public interface OnFetchGithubRepoList {
    void onFetchGithubRepoList(List<GithubRepo> githubRepos, int channel);
}
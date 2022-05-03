package edu.quinnipiac.ser210.githubchat.database.listeners;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;

@Deprecated
public interface OnFetchGithubCache {

    void onFetchGithubCache(GithubCache githubCache, int channel);
}

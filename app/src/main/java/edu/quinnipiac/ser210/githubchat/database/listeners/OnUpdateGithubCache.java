package edu.quinnipiac.ser210.githubchat.database.listeners;

import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;

@Deprecated
public interface OnUpdateGithubCache extends DatabaseWrapper.OnUpdateGithubCache {

    void onUpdateGithubCache(GithubCache cache, int channel);
}

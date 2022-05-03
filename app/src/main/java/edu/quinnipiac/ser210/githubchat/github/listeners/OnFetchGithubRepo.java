package edu.quinnipiac.ser210.githubchat.github.listeners;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;

/**
 * @author Thomas Kwashnak
 */
@Deprecated
public interface OnFetchGithubRepo {

    void onFetchGithubRepo(GithubRepo repo, int channel);
}

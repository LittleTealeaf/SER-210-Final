package edu.quinnipiac.ser210.githubchat.github.listeners;

import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;

/**
 * @author Thomas Kwashnak
 */
@Deprecated
public interface OnFetchGithubUser extends GithubWrapper.OnFetchGithubUser {

    void onFetchGithubUser(GithubUser githubUser, int channel);
}

package edu.quinnipiac.ser210.githubchat.github.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubPull;

/**
 * @author Thomas Kwashnak
 */
@Deprecated
public interface OnFetchGithubPulls extends GithubWrapper.OnFetchGithubPullList {

    void onFetchGithubPullList(List<GithubPull> githubPulls, int channel);
}

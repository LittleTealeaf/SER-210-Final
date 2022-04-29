package edu.quinnipiac.ser210.githubchat.github.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachable;

/**
 * @author Thomas Kwashnak
 */
public interface OnFetchGithubAttachbleList {
    void onFetchMessageAttachableList(List<GithubAttachable> attachableList, int channel);
}

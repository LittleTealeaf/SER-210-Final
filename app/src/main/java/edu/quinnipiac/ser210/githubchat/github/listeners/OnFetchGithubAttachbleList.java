package edu.quinnipiac.ser210.githubchat.github.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachment;

/**
 * @author Thomas Kwashnak
 */
public interface OnFetchGithubAttachbleList {

    void onFetchMessageAttachableList(List<GithubAttachment> attachableList, int channel);
}

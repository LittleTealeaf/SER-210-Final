package edu.quinnipiac.ser210.githubchat.github.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachment;

/**
 * @author Thomas Kwashnak
 */
@Deprecated
public interface OnFetchGithubAttachbleList extends GithubWrapper.OnFetchGithubAttachmentList {

    void onFetchGithubAttachmentList(List<GithubAttachment> attachableList, int channel);
}

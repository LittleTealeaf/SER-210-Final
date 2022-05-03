package edu.quinnipiac.ser210.githubchat.github.listeners;

import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachment;

@Deprecated
public interface OnFetchGithubAttachable extends GithubWrapper.OnFetchGithubAttachment {

    void onFetchGithubAttachment(GithubAttachment attachable, int channel);
}

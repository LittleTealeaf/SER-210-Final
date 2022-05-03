package edu.quinnipiac.ser210.githubchat.github.listeners;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachment;

@Deprecated
public interface OnFetchGithubAttachable {

    void onFetchGithubAttachable(GithubAttachment attachable, int channel);
}

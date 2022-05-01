package edu.quinnipiac.ser210.githubchat.github.listeners;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachable;

public interface OnFetchGithubAttachable {

    void onFetchGithubAttachable(GithubAttachable attachable, int channel);
}

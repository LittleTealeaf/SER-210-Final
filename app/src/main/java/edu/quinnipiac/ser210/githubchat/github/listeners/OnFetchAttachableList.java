package edu.quinnipiac.ser210.githubchat.github.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachable;

public interface OnFetchAttachableList {
    void onFetchMessageAttachableList(List<GithubAttachable> attachableList, int channel);
}

package edu.quinnipiac.ser210.githubchat.github.interfaces;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachable;

public interface GithubAttachableHolder {
    List<GithubAttachable> getAttachableList();
    GithubAttachable getAttachable(int number);
}

package edu.quinnipiac.ser210.githubchat.github.dataobjects;

public interface GithubAttachable {
    int getNumber();

    String getTitle();

    String getURL();

    boolean isClosed();

    GithubUser getGithubUser();
}

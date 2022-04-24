package edu.quinnipiac.ser210.githubchat.github.dataobjects;

public interface RepoItem {

    int getNumber();

    String getTitle();

    String getURL();

    boolean isClosed();

    GithubUser getUser();
}

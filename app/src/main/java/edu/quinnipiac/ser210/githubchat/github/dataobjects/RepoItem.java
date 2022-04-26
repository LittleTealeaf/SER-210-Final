package edu.quinnipiac.ser210.githubchat.github.dataobjects;

/**
 * Represents common required methods between {@link GithubIssue} and {@link GithubPull}
 * @author Thomas Kwashnak
 */
public interface RepoItem {

    int getNumber();

    String getTitle();

    String getURL();

    boolean isClosed();

    GithubUser getUser();
}

package edu.quinnipiac.ser210.githubchat.github.dataobjects;

/**
 * @author Thomas Kwashnak
 */
public interface GithubAttachable {

    int getNumber();

    String getTitle();

    String getURL();

    boolean isClosed();

    GithubUser getGithubUser();
}

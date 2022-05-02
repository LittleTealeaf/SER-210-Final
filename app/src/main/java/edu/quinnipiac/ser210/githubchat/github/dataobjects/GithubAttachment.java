package edu.quinnipiac.ser210.githubchat.github.dataobjects;

/**
 * @author Thomas Kwashnak
 */
public interface GithubAttachment {

    int getNumber();

    String getTitle();

    String getURL();

    GithubUser getGithubUser();

    int getStatusDrawable();
}
